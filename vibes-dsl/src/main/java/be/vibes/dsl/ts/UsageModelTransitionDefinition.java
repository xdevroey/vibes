package be.vibes.dsl.ts;

/*
 * #%L
 * vibes-dsl
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
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

/**
 * This class is used to define transtisions in the define() method of a
 * UsageModelDefinition instance.
 *
 * @see UsageModelDefinition
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class UsageModelTransitionDefinition extends TransitionDefinition {

    private double proba = 1.0;

    public UsageModelTransitionDefinition(String fromStateName,
            AbstractTransitionSystemDefinition def) {
        super(fromStateName, def);
    }

    public UsageModelTransitionDefinition proba(double value) {
        this.proba = value;
        return this;
    }

    @Override
    public UsageModelTransitionDefinition action(String actionName) {
        return (UsageModelTransitionDefinition) super.action(actionName);
    }

    double getProbability() {
        return proba;
    }

}
