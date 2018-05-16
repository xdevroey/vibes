package be.vibes.dsl.ts;

import be.vibes.dsl.exception.TransitionDefinitionException;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.FeaturedTransitionSystemFactory;

/**
 * This abstract class allows to define featured transition systems (FTS) by extending
 * it and implementing the define() method.
 *
 * @see AbstractTransitionSystemDefinition
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public abstract class FeaturedTransitionSystemDefinition extends
        AbstractTransitionSystemDefinition {

    /**
     * Creates a new FTS definition corresponding to the declaration made in
     * this.define().
     */
    public FeaturedTransitionSystemDefinition() {
        super();
    }

    @Override
    protected void initial(String stateName) {
        factory = new FeaturedTransitionSystemFactory(stateName);
    }

    @Override
    void notifyTransitionDefinitionComplete(TransitionDefinition definition) {
        if (definition.getTargetStateName() == null) {
            throw new TransitionDefinitionException("Transition has to end in a state!");
        }
        FeaturedTransitionSystemFactory fac = (FeaturedTransitionSystemFactory) factory;
        FeaturedTransitionDefinition def = (FeaturedTransitionDefinition) definition;
        fac.addState(def.getSourceStateName()); 
        fac.addState(def.getTargetStateName()); 
        fac.addAction(def.getActionName());
        fac.addTransition(def.getSourceStateName(), def.getActionName(), def.getFExpression(), def.getTargetStateName());
    }

    @Override
    public FeaturedTransitionSystem getTransitionSystem() {
        return (FeaturedTransitionSystem) super.getTransitionSystem();
    }

    @Override
    protected FeaturedTransitionDefinition from(String stateName) {
        FeaturedTransitionDefinition def = new FeaturedTransitionDefinition(stateName, this);
        return def;
    }
    
}
