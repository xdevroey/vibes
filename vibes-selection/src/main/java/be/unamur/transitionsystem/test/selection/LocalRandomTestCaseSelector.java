package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.annotation.DistanceFromInitialStateAnnotator;
import be.unamur.transitionsystem.annotation.DistanceToInitialStateAnnotator;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Select random test cases passing by the given localStates. If no local state
 * is provided (localStates is empty), all the states of the transition system
 * are considered as local.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class LocalRandomTestCaseSelector<T extends TransitionSystem> extends RandomTestCaseSelector<T> {

    private static final Logger LOG = LoggerFactory.getLogger(LocalRandomTestCaseSelector.class);

    private State[] localStates;

    private Random random;

    public LocalRandomTestCaseSelector(T transitionSystem, int maxNbrTry, int maxLength, TestCaseValidator validator, TestCaseFactory factory, Set<State> localStates) {
        super(transitionSystem, maxNbrTry, maxLength, validator, factory);
        checkNotNull(localStates, "Parameter localStates may not be null!");
        this.localStates = localStates.stream().filter(s -> transitionSystem.getState(s.getName()) != null).toArray(State[]::new);
        // If there is no local states, consider all states as local.
        if (this.localStates.length == 0) {
            this.localStates = Iterators.toArray(localStates.iterator(), State.class);
        }
        if (!transitionSystem.getProperty(DistanceFromInitialStateAnnotator.PROP_TS_IS_ANNOTATED_DFROM, false)) {
            DistanceFromInitialStateAnnotator.getInstance().annotate(transitionSystem);
        }
        if (!transitionSystem.getProperty(DistanceToInitialStateAnnotator.PROP_TS_IS_ANNOTATED_DTO, false)) {
            DistanceToInitialStateAnnotator.getInstance().annotate(transitionSystem);
        }
    }

    public LocalRandomTestCaseSelector(T transitionSystem, int maxNbrTry, int maxLength, TestCaseValidator validator, Set<State> localStates) {
        this(transitionSystem, maxNbrTry, maxLength, validator, LtsMutableTestCase.FACTORY, localStates);
    }

    public LocalRandomTestCaseSelector(T transitionSystem, int maxNbrTry, int maxLength, Set<State> localStates) {
        this(transitionSystem, maxNbrTry, maxLength, AlwaysTrueValidator.TRUE_VALIDATOR, localStates);
    }

    public LocalRandomTestCaseSelector(T transitionSystem, Set<State> localStates) {
        this(transitionSystem, DEFAULT_MAX_NUMBER_TRY, DEFAULT_MAX_LENGTH, localStates);
    }

    @Override
    public TestCase select() throws TestCaseSelectionException {
        checkState(this.localStates.length > 0, "No states considered for generation!");
        initRandom();
        return selectTestCase();
    }

    @Override
    public List<TestCase> select(int nbr) throws TestCaseSelectionException {
        checkState(this.localStates.length > 0, "No states considered for generation!");
        initRandom();
        List<TestCase> lst = new ArrayList(nbr);
        for (int i = 0; i < nbr; i++) {
            lst.add(selectTestCase());
        }
        return lst;
    }

    @Override
    public void select(int nbr, TestCaseWrapUp wrapUp) throws TestCaseSelectionException {
        checkState(this.localStates.length > 0, "No states considered for generation!");
        initRandom();
        for (int i = 0; i < nbr; i++) {
            TestCase testcase = selectTestCase();
            try {
                wrapUp.wrapUp(testcase);
            } catch (TestCaseException ex) {
                LOG.error("Error while wrapping up test case {}!", testcase, ex);
                throw new TestCaseSelectionException("Error while wrapping up test case!", ex);
            }
        }
    }

    private void initRandom() {
        if (getRandomSeed() == null) {
            random = new Random();
        } else {
            random = new Random(getRandomSeed());
        }
    }

    private TestCase selectTestCase() throws TestCaseSelectionException {
        State start = localStates[random.nextInt(localStates.length)];
        Deque<Transition> deque = Queues.newArrayDeque();
        // Compute prefix
        State state = start;
        while (!state.equals(getTransitionSystem().getInitialState())) {
            Transition tr = getIncomingTransition(state);
            deque.addFirst(tr);
            state = tr.getFrom();
        }
        // Compute suffix
        state = start;
        while (!state.equals(getTransitionSystem().getInitialState())) {
            Transition tr = getOutgoingTransition(state);
            deque.addLast(tr);
            state = tr.getTo();
        }
        TestCase testcase = getFactory().buildTestCase();
        try {
            for (Transition tr : deque) {
                testcase.enqueue(tr);
            }
        } catch (TestCaseException ex) {
            LOG.error("Exception while generating local random test case!", ex);
            throw new TestCaseSelectionException("Exception while generating local random test case!", ex);
        }
        return testcase;
    }

    private Transition getIncomingTransition(State state) throws TestCaseSelectionException {
        if (state.incomingSize() <= 0) {
            throw new TestCaseSelectionException("Found an inaccessible state, cannot go back to initial state from here!");
        }
        int dfrom = state.getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1);
        Iterator<Transition> it = state.incomingTransitions();
        List<Transition> previous = Lists.newArrayList();
        while (it.hasNext()) {
            Transition candidate = it.next();
            int dfromPrevious = candidate.getFrom().getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1);
            checkState(dfromPrevious >= 0, "State %s has not been annotated using distance from initial state!", candidate.getFrom());
            if (dfromPrevious <= dfrom) {
                previous.add(candidate);
            }
        }
        if (previous.size() <= 0) {
            throw new TestCaseSelectionException("No previous state with lesser dfrom value found from state " + state + "!");
        }
        return previous.get(random.nextInt(previous.size()));
    }

    private Transition getOutgoingTransition(State state) throws TestCaseSelectionException {
        if (state.outgoingSize() <= 0) {
            throw new TestCaseSelectionException("Found a dead end , cannot go back to initial state from here!");
        }
        int dto = state.getProperty(DistanceToInitialStateAnnotator.PROP_STATE_DTO, -1);
        Iterator<Transition> it = state.outgoingTransitions();
        List<Transition> nexts = Lists.newArrayList();
        while (it.hasNext()) {
            Transition candidate = it.next();
            int dtoNext = candidate.getTo().getProperty(DistanceToInitialStateAnnotator.PROP_STATE_DTO, -1);
            checkState(dtoNext >= 0, "State %s has not been annotated using distance from initial state!", candidate.getFrom());
            if (dtoNext <= dto) {
                nexts.add(candidate);
            }
        }
        if (nexts.size() <= 0) {
            throw new TestCaseSelectionException("No next state with lesser dfrom value found from state " + state + "!");
        }
        return nexts.get(random.nextInt(nexts.size()));
    }

}
