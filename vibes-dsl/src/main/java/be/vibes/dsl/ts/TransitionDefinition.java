package be.vibes.dsl.ts;

import be.vibes.ts.Action;

/**
 * This class is used to define transitions in the define() method of a
 * LabelledTransitionSystemDefinition instance.
 *
 * @see LabelledTransitionSystemDefinition
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class TransitionDefinition {

    protected AbstractTransitionSystemDefinition context;
    protected String actionName = Action.EPSILON_ACTION;
    protected String fromStateName;
    protected String toStateName;

    TransitionDefinition(String fromStateName, AbstractTransitionSystemDefinition def) {
        this.fromStateName = fromStateName;
        this.context = def;
    }

    /**
     * Sets the action of this transition.
     *
     * @param actionName The action executed when the transition is fired.
     * @return This.s
     */
    public TransitionDefinition action(String actionName) {
        this.actionName = actionName;
        return this;
    }

    /**
     * Sets the destination of this transition.
     *
     * @param stateName The destination state of this transition.
     */
    public void to(String stateName) {
        this.toStateName = stateName;
        this.context.notifyTransitionDefinitionComplete(this);
    }

    String getActionName() {
        return actionName;
    }

    String getSourceStateName() {
        return fromStateName;
    }

    String getTargetStateName() {
        return toStateName;
    }

}
