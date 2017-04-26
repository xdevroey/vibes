package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.annotation.DistanceFromInitialStateAnnotator;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.annotation.DistanceToInitialStateAnnotator;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class LocalRandomTestCaseGenerator extends RandomTestCaseGenerator {

    private static final Logger logger = LoggerFactory.getLogger(LocalRandomTestCaseGenerator.class);

    public static final String PROP_TS_ISANNOTATED = LocalRandomTestCaseGenerator.class.getName().concat(".isannotated");

    private List<State> localStates;

    /**
     *
     * @param testCaseFactory
     * @param validator
     * @param wrapUp
     * @param localStates For every random test cases geenrated, at least one
     * state of this list will be included in the random path followed by the
     * generator. The considered state is randomly selected from this list. An
     * empty list is equivalent to call a full random generation.
     */
    public LocalRandomTestCaseGenerator(TestCaseFactory testCaseFactory, TestCaseValidator validator, TestCaseWrapUp wrapUp,
            List<State> localStates) {
        super(testCaseFactory, validator, wrapUp);
        this.localStates = localStates;
    }

    public List<State> getLocalStates() {
        return localStates;
    }

    public void setLocalStates(List<State> localStates) {
        this.localStates = localStates;
    }

    /**
     * Generates a random test case localized to this local states list or null
     * if it failed to generate a valid test case.
     *
     * @param ts The transition system from which test cases are generated. If
     * the transition system has been modified since the last call to
     * generateTestCase, its PROP_TS_ISANNOTATED property must be set to false.
     * @param validator The test case validator to use.
     * @param maxLength The maximal length of the test case.
     * @return A random test case local to this local states.
     * @throws TestCaseSelectionException If the generator fails to reach the
     * initial state from one of this local states.
     */
    @Override
    public TestCase generateTestCase(TransitionSystem ts, TestCaseValidator validator, int maxLength) throws TestCaseSelectionException {
        if (localStates.isEmpty()) {
            return super.generateTestCase(ts, validator, maxLength);
        } 
        // Annotate LTS if it has not already been done.
        if (!ts.getProperty(PROP_TS_ISANNOTATED, false)) {
            annotate(ts);
        }
        // Build localStates for the given TS to avoid generation from wrong TS.
        List<State> consideredStates = Lists.newArrayList();
        Iterator<State> states = ts.states();
        while (states.hasNext()) {
            State s = states.next();
            if (localStates.contains(s)) {
                consideredStates.add(s);
            }
        }
        try {
            // Randomly select a state for which the test case will have to pass by
            State state = consideredStates.get(getRandom().nextInt(consideredStates.size()));
            state = ts.getState(state.getName()); // be sure to select state in the given TS
            logger.debug("State {} selected", state);
            // Get a random prefix for that state
            RandomPrefixVisitor visistor = new RandomPrefixVisitor(ts.getInitialState(), getRandom(), getTestCaseFactory(), validator);
            RandomSuffixVisitor suffixVisitor = new RandomSuffixVisitor(getRandom(), getTestCaseFactory(), validator);
            visistor.setMaxLength(maxLength);
            TestCase testCase = getTestCaseFactory().buildTestCase();
            if (state.accept(visistor)) {
                for (Transition t : visistor.getTransitions()) {
                    testCase.enqueue(t);
                    suffixVisitor.addAlreadyFiredTransition(t);
                }
            } else {
                throw new TestCaseSelectionException("Unnable to reach initial state from state " + state + "!");
            }
            // Complete with a random suffix starting from that state
            suffixVisitor.setInitialState(ts.getInitialState());
            suffixVisitor.setTestCase(testCase);
            suffixVisitor.setMaxLength(maxLength);
            state.accept(suffixVisitor);
            return suffixVisitor.getTestCase();
        } catch (TestCaseException ex) {
            throw new TestCaseSelectionException("Error while generating test case!", ex);
        } catch (VisitException ex) {
            throw new TestCaseSelectionException("Error while generating test case!", ex);
        }
    }

    private void annotate(TransitionSystem ts) {
        DistanceFromInitialStateAnnotator.getInstance().annotate(ts);
        DistanceToInitialStateAnnotator.getInstance().annotate(ts);
        ts.setProperty(PROP_TS_ISANNOTATED, true);
    }

}
