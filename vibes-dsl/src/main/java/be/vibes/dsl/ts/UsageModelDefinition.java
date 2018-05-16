package be.vibes.dsl.ts;

import be.vibes.dsl.exception.TransitionDefinitionException;
import be.vibes.ts.UsageModel;
import be.vibes.ts.UsageModelFactory;

/**
 * This abstract class allows to define usage models by extending it and
 * implementing the define() method.
 *
 * @see AbstractTransitionSystemDefinition
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public abstract class UsageModelDefinition extends AbstractTransitionSystemDefinition {

    /**
     * Creates a new usage model definition corresponding to the declaration
     * made in this.define().
     */
    public UsageModelDefinition() {
        super();
    }

    @Override
    protected void initial(String stateName) {
        this.factory = new UsageModelFactory(stateName);
    }
    
    @Override
    protected UsageModelTransitionDefinition from(String stateName) {
        UsageModelTransitionDefinition def = new UsageModelTransitionDefinition(stateName, this);
        return def;
    }

    @Override
    void notifyTransitionDefinitionComplete(TransitionDefinition definition) {
        if (definition.getTargetStateName() == null) {
            throw new TransitionDefinitionException("Transition has to end in a state!");
        }
        UsageModelFactory fac = (UsageModelFactory) this.factory;
        UsageModelTransitionDefinition def = (UsageModelTransitionDefinition) definition;
        fac.addState(def.getSourceStateName()); 
        fac.addState(def.getTargetStateName()); 
        fac.addAction(def.getActionName());
        fac.addTransition(def.getSourceStateName(), def.getActionName(), def.getProbability(), def.getTargetStateName());
    }

    @Override
    public UsageModel getTransitionSystem() {
        return (UsageModel) super.getTransitionSystem();
    }

}
