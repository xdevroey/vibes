/*
 * #%L
 * vibes-dsl
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package be.unamur.transitionsystem.dsl;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.dsl.exception.ActionDefinitionException;
import be.unamur.transitionsystem.dsl.exception.StateDefinitionException;
import be.unamur.transitionsystem.dsl.exception.TransitionDefinitionException;

/**
 * This abstract class allows to define transition systems (TSs) by
 * extending it and implementing the define() method. The states, actions and
 * transitions may be defined in the body of the define() method by calling
 * state(String stateName), action(String actionName) and from(String stateName)
 * method of this class.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public abstract class TransitionSystemDefinition {

    private boolean defined = false;
    protected TransitionSystem ts;

    /**
     * Creates a new definition of the TransitionSystem using the given ts as a
     * basis. This constructor should be used by subclasses in order to have the
     * desired Transition system kind (LTS, FTS or Usage Model).
     *
     * @param ts The TS on which states, actions and transitions will be defined
     * in the define() method.
     */
    protected TransitionSystemDefinition(TransitionSystem ts) {
        this.ts = ts;
    }

    /**
     * Create a new TS defintion.
     */
    public TransitionSystemDefinition() {
        this(new LabelledTransitionSystem());
    }

    /**
     * Allows sub-classes to define the initial state of the TS.
     *
     * @param stateName The initial state of the TS under definition.
     */
    protected void initial(String stateName) {
        State s = this.ts.addState(stateName);
        this.ts.setInitialState(s);
    }

    /**
     * Allows sub-classes to add a transition starting from the state with the
     * given name to the TS.
     *
     * @param stateName The origin state of the transition to add to the TS
     * under definition.
     * @return The definition object used to set the other parameters of the
     * transition.
     */
    protected TransitionDefinition from(String stateName) {
        TransitionDefinition def = new TransitionDefinition(stateName, this);
        return def;
    }

    /**
     * Allows sub-classes to add states to the TS.
     *
     * @param stateName The name of the state to add to the TS under
     * definition.
     * @return The definition object used to set the other parameters of the
     * state.
     */
    protected StateDefinition state(String stateName) {
        StateDefinition def = new StateDefinition(stateName);
        return def;
    }

    /**
     * Allows sub-classes to add actions to the TS.
     *
     * @param actionName The name of the action to add to the TS under
     * definition.
     * @return The definition object used to set the other parameters of the
     * action.
     */
    protected ActionDefinition action(String actionName) {
        ActionDefinition def = new ActionDefinition(actionName);
        return def;
    }

    /**
     * The method that has to be implemented by sub-classes in order to define
     * the TS. This method will contain call to state(String), action(String)
     * and from(String) methods.
     *
     * @throws TransitionDefinitionException If there is an error in the
     * definition of a transition
     * @throws StateDefinitionException If there is an error in the definition
     * of a state.
     * @throws ActionDefinitionException If there is an error in the definition
     * of an action.
     */
    protected abstract void define();

    /**
     * Used by TransitionDefinition returned by from(String) method.
     *
     * @param def The object returned by from(String) method.
     */
    void notifyTransitionDefinitionComplete(TransitionDefinition def) {
        if (def.getToStateName() == null) {
            throw new TransitionDefinitionException("Transition has to end in a state!");
        }
        State from = getTransitionSystem().addState(def.getFromStateName());
        State to = getTransitionSystem().addState(def.getToStateName());
        Action act = getTransitionSystem().addAction(def.getActionName());
        getTransitionSystem().addTransition(from, to, act);
    }

    /**
     * Used by StateDefinition returned by state(String) method.
     *
     * @param def The object returned by state(String) method.
     */
    void notifyStateDefinitionComplete(StateDefinition definition) {
        getTransitionSystem().addState(definition.getStateName());
    }

    /**
     * Used by ActionDefinition returned by action(String) method.
     *
     * @param def The object returned by action(String) method.
     */
    void notifyActionDefinitionComplete(ActionDefinition definition) {
        getTransitionSystem().addAction(definition.getActionName());
    }

    /**
     * Returns the transition system defined in the define() method of this
     * class.
     *
     * @return The transition system dined in the class.
     * @throws TransitionDefinitionException If there is an error in the
     * definition of a transition
     * @throws StateDefinitionException If there is an error in the definition
     * of a state.
     * @throws ActionDefinitionException If there is an error in the definition
     * of an action.
     */
    public TransitionSystem getTransitionSystem() {
        if (!defined) {
            defined = true;
            define();
        }
        return this.ts;
    }

}
