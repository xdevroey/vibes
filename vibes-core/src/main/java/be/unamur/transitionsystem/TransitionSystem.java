package be.unamur.transitionsystem;

/*
 * #%L vibes-core %% Copyright (C) 2014 PReCISE, University of Namur %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. #L%
 */
import java.util.Iterator;

/**
 * Abstract data type representing a Transition system.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public abstract class TransitionSystem extends TransitionSystemElement {

    /**
     * Returns the state corresponding to the given name.
     *
     * @param name The name of the state to retrieve.
     * @return The state corresponding to the given name or null if the state
     * does not exists.
     */
    public abstract State getState(String name);

    /**
     * Returns the action corresponding to the given name.
     *
     * @param name The name of the action to retrieve.
     * @return The action corresponding to the given name or null if the action
     * does not exists.
     */
    public abstract Action getAction(String name);

    /**
     * Sets the initial state of the TS to the given state.
     *
     * @param initialState The new initial state. Pre: initialState not null.
     */
    public abstract void setInitialState(State initialState);

    /**
     * Returns the initial state of the TS.
     *
     * @return
     */
    public abstract State getInitialState();

    /**
     * Add a state to the TS with the given name and returns it. If the state
     * already exists in the TS, the call is equivalent to this.getState(name).
     *
     * @param name The name of the new state. Pre: name not null
     * @return Creates a new state with the given name or returns
     * this.getState(name) if the states already exists.
     */
    public abstract State addState(String name);

    /**
     * Creates a new transition in the TS with the given source, destination,
     * and action. The states and the action have to be part of the transition
     * system.
     *
     * @param from The source state. Pre: this.getState(from.getName()) not null
     * @param to The destination state. Pre: this.getState(to.getName()) not
     * null
     * @param action The aciton of the transition. Pre:
     * this.getAction(action.getName()) not null
     * @return
     */
    public abstract Transition addTransition(State from, State to, Action action);

    /**
     * Add an action to the TS and returns it or returns this.getAction(name) if
     * the action already exists for the given TS.
     *
     * @param name The name of the action to add. Pre: name != null.
     * @return The new action or this.getAction(name) if the action alredy
     * exists.
     */
    public abstract Action addAction(String name);

    /**
     * Removes the given state from the TS and all its incoming/outgoing
     * transitions.
     *
     * @param state The state to remove. Pre: state not null and
     * this.getState(state.getName()) not null
     */
    public abstract void removeState(State state);

    /**
     * Removes the given transition from TS
     *
     * @param tr The transition to remove. Pre: tr not null and
     * this.getState(tr.getFrom().getName()) not null
     */
    public abstract void removeTransition(Transition tr);

    /**
     * Returns an iterator over the states of the TS.
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public abstract Iterator states();

    /**
     * Returns an iterator over the actions of the TS.
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public abstract Iterator actions();

    /**
     * Returns a copy of the TS.
     *
     * @return
     */
    public abstract TransitionSystem copy();

    /**
     * Returns the number of states of the TS.
     *
     * @return
     */
    public abstract int numberOfStates();

    /**
     * Returns the number of actions of the TS.
     *
     * @return
     */
    public abstract int numberOfActions();

    /**
     * Returns true if the given state is part of this TS.
     *
     * @param s The state to lookup.
     * @return True if s is one of this TS states.
     */
    public abstract boolean contains(State s);

}
