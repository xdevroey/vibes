package be.unamur.transitionsystem.test.execution;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import be.unamur.transitionsystem.test.execution.exception.IllegalFiredTransition;
import com.google.common.collect.Queues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import static com.google.common.base.Preconditions.*;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;

/**
 * This class is used to execute a transition system.
 *
 * @param <T> The type of transition system to execute
 */
public abstract class TransitionSystemExecutor<T extends TransitionSystem> {

    private static final Logger logger = LoggerFactory
            .getLogger(TransitionSystemExecutor.class);

    private T ts;
    private State currentState;
    private Deque<Transition> executedTransitions;

    /**
     * Creates a new transition system executor.
     *
     * @param ts The transition system to execute.
     */
    public TransitionSystemExecutor(T ts) {
        checkNotNull(ts);
        checkNotNull(ts.getInitialState(), "Initial state must be set for ts!");
        this.ts = ts;
        executedTransitions = Queues.newArrayDeque();
        setCurrentState(ts.getInitialState());
    }

    /**
     * Check that the representation invariant is respected and launch an
     * IllegalStateException if not.
     */
    private void checkInv() {
        checkState((executedTransitions.isEmpty() && currentState.equals(ts.getInitialState()))
                || getCurrentState().equals(executedTransitions.getLast().getTo()),
                "Representation invariant violated in class TransitionSystemExecutor!");
    }

    /**
     * Returns the transition system executed.
     *
     * @return The transition system executed.
     */
    protected T getTs() {
        return ts;
    }

    /**
     * Returns the current state of the transition system.
     *
     * @return The current state of the transition system.
     */
    public State getCurrentState() {
        return currentState;
    }

    /**
     * Sets the current state of the transition system.
     *
     * @param state The current state of the transition system. Has to be the
     * last state (getTo()) of this.executedTransitions. May not be null.
     */
    protected void setCurrentState(State state) {
        checkNotNull(state);
        this.currentState = state;
        checkInv();
    }

    /**
     * Checks if the transition system in its current state may fire the given
     * transition (without executing it).
     *
     * @param tr The transition to execute. May not be null.
     * @return True if the given transition may be executed by the transition
     * system.
     */
    public boolean mayFire(Transition tr) {
        checkNotNull(tr);
        return tr.getFrom().equals(getCurrentState());
    }

    /**
     * Lists the transitions that may be fired by this transition system in its
     * current state using the given action.
     *
     * @param act The action to execute by the transition system in its current
     * state. May not be null.
     * @return The list of transitions that may be fired by the transition
     * system in its current state using the given action.
     */
    public List<Transition> listNextTransitions(Action act) {
        List<Transition> candidates = Lists.newArrayList();
        Iterator<Transition> it = getCurrentState().outgoingTransitions();
        while (it.hasNext()) {
            Transition t = it.next();
            if (t.getAction().equals(act) && mayFire(t)) {
                candidates.add(t);
            }
        }
        return candidates;
    }

    /**
     * Returns the list of transitions that may be fired by the transition
     * system in its current state.
     *
     * @return The list of transitions that may be fired by the transition
     * system in its current state.
     */
    public List<Transition> listNextTransitions() {
        return Lists.newArrayList(getCurrentState().outgoingTransitions());
    }

    /**
     * Fire the given transition and set the transition system in its new state.
     *
     * @param tr The transition to fire for the transition system. Method
     * mayFire(tr) has to return true or launch an IllegalFiredTransition
     * otherwise.
     * @throws IllegalFiredTransition If the given transition may not be fired
     * by the transition system in its current state.
     */
    public void fireTransition(Transition tr) throws IllegalFiredTransition {
        if (!mayFire(tr)) {
            throw new IllegalFiredTransition("Transition " + tr + " cannot be fired in the current state "
                    + getCurrentState() + "!");
        }
        executedTransitions.addLast(tr);
        setCurrentState(tr.getTo());
        logger.debug("Transition {} fired, now in state {}", tr, getCurrentState());
    }

    /**
     * Fire the transitions of the given iterable object.
     *
     * @param it The iterable object containing transitions.
     * @throws IllegalFiredTransition If the given object does not contain a
     * valid sequence of transitions that may be fired by the transition system
     * in its current state.
     */
    public void fireTransitions(Iterable<Transition> it) throws IllegalFiredTransition {
        for (Transition tr : it) {
            fireTransition(tr);
        }
    }

    /**
     * Fire the given transitions in the given order.
     *
     * @param transitions The transitions to fire.
     * @throws IllegalFiredTransition If the given object does not contain a
     * valid sequence of transitions that may be fired by the transition system
     * in its current state.
     */
    public void fireTransitions(Transition... transitions) throws IllegalFiredTransition {
        for (Transition tr : transitions) {
            fireTransition(tr);
        }
    }

    /**
     * Sets the transition system in its previous state. May not be backtracked
     * beyond initial state.
     *
     * @return The backtracked transition.
     */
    public Transition backtrack() {
        checkState(executedTransitions.size() > 0,
                "Cannot backtrack beyond initial state!");
        Transition last = executedTransitions.removeLast();
        setCurrentState(executedTransitions.getLast().getFrom());
        checkInv();
        return last;
    }

    /**
     * Returns a fresh list of executed transitions.
     *
     * @return A fresh list of executed transitions.
     */
    public List<Transition> getExecutedTransitions() {
        return Lists.newArrayList(this.executedTransitions);
    }

    /**
     * Resets the internal state to the initial state of the transition system.
     */
    public void reset() {
        executedTransitions.clear();
        setCurrentState(ts.getInitialState());
    }

}
