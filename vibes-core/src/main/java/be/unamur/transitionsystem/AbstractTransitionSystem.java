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
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Represents an abstract transtition system with states, actions, transitions,
 * and one initial state. Sub classes will implement this abstract class and
 * specify the type of states, actions and transitions supported. Building the
 * transition system will be done using the add and remove methods.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 * @param <S> The type of states supported by the transition system.
 * @param <T> The type of transitions supported by the transition system.
 * @param <A> The type of actions supported by the transition system.
 */
public abstract class AbstractTransitionSystem<S extends State, T extends Transition, A extends Action>
        extends TransitionSystem {

    protected TransitionSystemElementFactory<S, T, A> factory;

    private S initialState;
    private Map<String, S> states = Maps.newHashMap();
    private Map<String, A> actions = Maps.newHashMap();

    /**
     * Creates a new abstract transition system using the provided elements
     * factory.
     *
     * @param factory The factory used to build elements of this transition
     * system.
     */
    protected AbstractTransitionSystem(TransitionSystemElementFactory<S, T, A> factory) {
        checkNotNull(factory);
        this.factory = factory;
        addAction(Action.NO_ACTION_NAME);
    }

    /**
     * Creates a new copy of the given abstract transition system.
     *
     * @param original The original transition system to copy.
     */
    protected AbstractTransitionSystem(AbstractTransitionSystem<S, T, A> original) {
        this(original.factory);
        importActions(original.actions.values().iterator());
        importStates(original.states.values().iterator());
        importTransitions(original.states.values().iterator());
        this.initialState = getState(original.getInitialState().getName());
    }

    /**
     * Imports the given actions into this.
     *
     * @param actions The actions to import.
     */
    protected void importActions(Iterator<A> actions) {
        while (actions.hasNext()) {
            addAction(actions.next().getName());
        }
    }

    /**
     * Imports the given states into this.
     *
     * @param states The states to import.
     */
    protected void importStates(Iterator<S> states) {
        S state;
        while (states.hasNext()) {
            state = states.next();
            this.addState(state.getName());
        }
    }

    /**
     * Imports the given transitions into this.
     *
     * @param states The satates from which transitions are imported.
     */
    protected void importTransitions(Iterator<S> states) {
        S state;
        @SuppressWarnings("rawtypes")
        Iterator itTrans;
        Transition trans;
        while (states.hasNext()) {
            state = states.next();
            itTrans = state.outgoingTransitions();
            while (itTrans.hasNext()) {
                trans = (Transition) itTrans.next();
                addTransition(getState(trans.getFrom().getName()), getState(trans.getTo()
                        .getName()), getAction(trans.getAction().getName()));
            }
        }
    }

    /**
     * Sets the elements factory. Pre: this.factory is not null.
     *
     * @param factory The factory used to build the elements of this transition
     * system.
     */
    protected void setFactory(TransitionSystemElementFactory<S, T, A> factory) {
        checkNotNull(factory);
        this.factory = factory;
    }

    @Override
    public S getState(String name) {
        return this.states.get(name);
    }

    @Override
    public A getAction(String name) {
        return this.actions.get(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setInitialState(State initialState) {
        checkNotNull(initialState);
        this.initialState = (S) initialState;
    }

    @Override
    public S getInitialState() {
        return this.initialState;
    }

    @Override
    public int numberOfStates() {
        return this.states.size();
    }

    @Override
    public int numberOfActions() {
        return this.actions.size();
    }

    // Adding elements
    @Override
    public S addState(String name) {
        checkNotNull(name);
        S state = this.states.get(name);
        if (state == null) {
            state = this.factory.buildState();
            state.setName(name);
            this.states.put(name, state);
        }
        return state;
    }

    @Override
    public T addTransition(State from, State to, Action action) {
        checkNotNull(from);
        checkNotNull(to);
        checkNotNull(action);
        checkArgument(this.states.containsKey(from.getName()));
        checkArgument(this.states.containsKey(to.getName()));
        checkArgument(this.actions.containsKey(action.getName()));
        T tr = this.factory.buildTransition(getState(from.getName()),
                getState(to.getName()), getAction(action.getName()));
        from.addOutgoing(tr);
        to.addIncoming(tr);
        return tr;
    }

    @Override
    public A addAction(String name) {
        checkNotNull(name);
        A act = this.actions.get(name);
        if (act == null) {
            act = this.factory.buildAction();
            act.setName(name);
            this.actions.put(name, act);
        }
        return act;
    }

    // Removing elements
    @Override
    @SuppressWarnings("unchecked")
    public void removeState(State state) {
        checkNotNull(state);
        if (this.initialState.equals(state)) {
            this.initialState = null;
        }
        List<T> lst = Lists.newArrayList();
        lst.addAll((List<? extends T>) state.getIncoming());
        lst.addAll((List<? extends T>) state.getOutgoing());
        for (T tr : lst) {
            removeTransition(tr);
        }
        this.states.remove(state.getName());
    }

    @Override
    public void removeTransition(Transition tr) {
        Transition toRemove = this.getState(tr.getFrom().getName()).getOutTransition(tr);
        if (toRemove != null) { // If the transition has not been already removed (happens for loop transitions)
            toRemove.getFrom().removeOutgoing(tr);
            toRemove.getTo().removeIncoming(tr);
        }
    }

    // Iterator over elements
    @Override
    public Iterator<S> states() {
        return this.states.values().iterator();
    }

    @Override
    public Iterator<A> actions() {
        return this.actions.values().iterator();
    }

    @Override
    public abstract TransitionSystem copy();

    @Override
    public String toString() {
        return "AbstractTransitionSystem [initialState=" + initialState.getName()
                + ", states=" + states + ", actions=" + actions + "]";
    }

    @Override
    public boolean contains(State s) {
        State found = this.states.get(s.getName());
        return found != null && found.equals(s);
    }

}
