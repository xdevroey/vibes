package be.vibes.selection.random;

import be.vibes.selection.exception.TestCaseSelectionException;
import be.vibes.ts.DistanceFromInitialStateAnnotator;
import be.vibes.ts.DistanceToInitialStateAnnotator;
import be.vibes.ts.State;
import be.vibes.ts.TestCase;
import be.vibes.ts.Transition;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.exception.TransitionSystenExecutionException;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
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
public class LocalRandomTestCaseSelector extends RandomTestCaseSelector {

    private static final Logger LOG = LoggerFactory.getLogger(LocalRandomTestCaseSelector.class);

    private State[] localStates;

    public LocalRandomTestCaseSelector(TransitionSystem transitionSystem, int maxNbrTry, int maxLength, Set<State> localStates) {
        super(transitionSystem, maxNbrTry, maxLength);
        checkNotNull(localStates, "Parameter localStates may not be null!");
        this.localStates = localStates.stream().filter(s -> transitionSystem.getState(s.getName()) != null).toArray(State[]::new);
        // If there is no local states, consider all states as local.
        if (this.localStates.length == 0) {
            this.localStates = Iterators.toArray(localStates.iterator(), State.class);
        }
        DistanceFromInitialStateAnnotator dfrom = DistanceFromInitialStateAnnotator.getInstance();
        if (!dfrom.isAnnotated(transitionSystem)) {
            dfrom.annotate(transitionSystem);
        }
        DistanceToInitialStateAnnotator dto = DistanceToInitialStateAnnotator.getInstance();
        if (!dto.isAnnotated(transitionSystem)) {
            dto.annotate(transitionSystem);
        }
    }

    public LocalRandomTestCaseSelector(TransitionSystem transitionSystem, Set<State> localStates) {
        this(transitionSystem, DEFAULT_MAX_NUMBER_TRY, DEFAULT_MAX_LENGTH, localStates);
    }

    @Override
    public TestCase select() throws TestCaseSelectionException {
        checkState(this.localStates.length > 0, "No states considered for generation!");
        return selectTestCase();
    }

    @Override
    public List<TestCase> select(int nbr) throws TestCaseSelectionException {
        checkState(this.localStates.length > 0, "No states considered for generation!");
        List<TestCase> lst = new ArrayList(nbr);
        for (int i = 0; i < nbr; i++) {
            lst.add(selectTestCase());
        }
        return lst;
    }

    private TestCase selectTestCase() throws TestCaseSelectionException {
        State start = localStates[random.nextInt(localStates.length)];
        Deque<Transition> deque = Queues.newArrayDeque();
        // Compute prefix
        State state = start;
        while (!state.equals(getTransitionSystem().getInitialState())) {
            Transition tr = getIncomingTransition(state);
            deque.addFirst(tr);
            state = tr.getSource();
        }
        // Compute suffix
        state = start;
        while (!state.equals(getTransitionSystem().getInitialState())) {
            Transition tr = getOutgoingTransition(state);
            deque.addLast(tr);
            state = tr.getTarget();
        }
        TestCase testcase = new TestCase("localrandom" + (this.id++));
        try {
            for (Transition tr : deque) {
                testcase.enqueue(tr);
            }
        } catch (TransitionSystenExecutionException ex) {
            LOG.error("Exception while generating local random test case!", ex);
            throw new TestCaseSelectionException("Exception while generating local random test case!", ex);
        }
        return testcase;
    }

    private Transition getIncomingTransition(State state) throws TestCaseSelectionException {
        if (getTransitionSystem().getIncomingCount(state) <= 0) {
            throw new TestCaseSelectionException("Found an unconnected state, cannot go back to initial state from here!");
        }
        int dfrom = state.getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1);
        Iterator<Transition> it = getTransitionSystem().getIncoming(state);
        List<Transition> previous = Lists.newArrayList();
        while (it.hasNext()) {
            Transition candidate = it.next();
            int dfromPrevious = candidate.getSource().getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1);
            checkState(dfromPrevious >= 0, "State %s has not been annotated using distance from initial state!", candidate.getSource());
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
        if (getTransitionSystem().getOutgoingCount(state) <= 0) {
            throw new TestCaseSelectionException("Found a sink state, cannot go back to initial state from here!");
        }
        int dto = state.getProperty(DistanceToInitialStateAnnotator.PROP_STATE_DTO, -1);
        Iterator<Transition> it = getTransitionSystem().getOutgoing(state);
        List<Transition> nexts = Lists.newArrayList();
        while (it.hasNext()) {
            Transition candidate = it.next();
            int dtoNext = candidate.getTarget().getProperty(DistanceToInitialStateAnnotator.PROP_STATE_DTO, -1);
            checkState(dtoNext >= 0, "State %s has not been annotated using distance to initial state!", candidate.getTarget());
            if (dtoNext <= dto) {
                nexts.add(candidate);
            }
        }
        if (nexts.size() <= 0) {
            throw new TestCaseSelectionException("No next state with lesser dto value found from state " + state + "!");
        }
        return nexts.get(random.nextInt(nexts.size()));
    }

}
