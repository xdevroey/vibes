
package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.VisitorWithReturn;
import be.unamur.transitionsystem.annotation.DistanceFromInitialStateAnnotator;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
class RandomPrefixVisitor implements VisitorWithReturn<Boolean> {
    
    private static final Logger logger = LoggerFactory.getLogger(RandomPrefixVisitor.class);
    
    private State initialState;
    private State start;
    private Random random;
    private int maxLength = Integer.MAX_VALUE;
    private TestCaseValidator validator;
    private TestCaseFactory testCaseFactory;
    private Deque<Transition> transitions;

    public RandomPrefixVisitor(State initialState, Random random, TestCaseFactory testCaseFactory, TestCaseValidator validator) {
        this.initialState = initialState;
        this.random = random;
        this.testCaseFactory = testCaseFactory;
        this.validator = validator;
        this.transitions = Queues.newArrayDeque();
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public Deque<Transition> getTransitions() {
        return transitions;
    }
    
    @Override
    public Boolean visit(State state) throws VisitException {
        checkState(transitions.size() == 0 || transitions.getFirst().getFrom().equals(state));
        if(start == null){
            start = state;
        } else if(initialState.equals(state)){
            return true;
        } else if(transitions.size() > maxLength){
            return false;
        }
        List<Transition> previous = getPreviousPossibleTransitions(state);
        for(Transition t : previous){
            transitions.addFirst(t);
            if(t.getFrom().accept(this)){
                return true;
            } else {
                transitions.removeFirst();
            }
        }
        return false;
    }

    private List<Transition> getPreviousPossibleTransitions(State state) {
        int dfrom = state.getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1);
        // Get dfrom value
        checkState(dfrom >= 0, "State %s has not been annotated using distance from initial state!", state);
        List<Transition> previous = Lists.newArrayList();
        for (Iterator<Transition> iterator = state.incomingTransitions(); iterator.hasNext();) {
            Transition candidate = iterator.next();
            // Get dfrom previous state value
            int dfromPrevious = candidate.getFrom().getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1);
            checkState(dfromPrevious >= 0, "State %s has not been annotated using distance from initial state!", candidate.getFrom());
            // Only consider transitions that have not been fired yet
            if(dfromPrevious <= dfrom && ! transitions.contains(candidate) && isValidTestCase(candidate)){
                previous.add(candidate);
            }
        }
        Collections.shuffle(previous, random);
        return previous;
    }

    private boolean isValidTestCase(Transition candidate) {
        if(validator instanceof AlwaysTrueValidator){
            return true;
        }
        TestCase tc = testCaseFactory.buildTestCase();
        try {
            tc.enqueue(candidate);
            for(Transition t : transitions){
                tc.enqueue(t);
            }
            return validator.isValid(tc);
        } catch (TestCaseException ex) {
            logger.error("Exception while validating test case, will return false!", ex);
        }
        return false;
    }
    
    
}
