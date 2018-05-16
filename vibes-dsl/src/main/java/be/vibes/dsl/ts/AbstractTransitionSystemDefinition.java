package be.vibes.dsl.ts;

import be.vibes.dsl.exception.ActionDefinitionException;
import be.vibes.dsl.exception.StateDefinitionException;
import be.vibes.dsl.exception.TransitionDefinitionException;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.TransitionSystemFactory;

/**
 * This abstract class allows to define transition systems (TSs) by extending it
 * and implementing the define() method. The states, actions and transitions may
 * be defined in the body of the define() method by calling state(String
 * stateName), action(String actionName) and from(String stateName) method of
 * this class.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public abstract class AbstractTransitionSystemDefinition {

    private boolean defined = false;
    protected TransitionSystemFactory factory;
    protected TransitionSystem ts;

    /**
     * Create a new TS definition.
     */
    public AbstractTransitionSystemDefinition() {
    }

    /**
     * Allows sub-classes to define the initial state of the TS. Should 
     * always be called first in the define method.
     *
     * @param stateName The initial state of the TS under definition.
     */
    protected void initial(String stateName) {
        this.factory = new TransitionSystemFactory(stateName);
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
     * @param stateName The name of the state to add to the TS under definition.
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
        if (def.getTargetStateName() == null) {
            throw new TransitionDefinitionException("Transition has to end in a state!");
        }
        factory.addState(def.getSourceStateName()); 
        factory.addState(def.getTargetStateName()); 
        factory.addAction(def.getActionName());
        factory.addTransition(def.getSourceStateName(), def.getActionName(), def.getTargetStateName());
    }

    /**
     * Used by StateDefinition returned by state(String) method.
     *
     * @param def The object returned by state(String) method.
     */
    void notifyStateDefinitionComplete(StateDefinition def) {
        factory.addState(def.getStateName()); 
    }

    /**
     * Used by ActionDefinition returned by action(String) method.
     *
     * @param def The object returned by action(String) method.
     */
    void notifyActionDefinitionComplete(ActionDefinition def) {
        factory.addState(def.getActionName());
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
            ts = factory.build();
        }
        return ts;
    }

}
