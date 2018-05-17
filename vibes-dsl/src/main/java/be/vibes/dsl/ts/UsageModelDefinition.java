package be.vibes.dsl.ts;

/*-
 * #%L
 * VIBeS: dsl
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
