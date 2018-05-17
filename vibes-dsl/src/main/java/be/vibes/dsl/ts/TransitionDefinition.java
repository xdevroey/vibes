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

import be.vibes.ts.Action;

/**
 * This class is used to define transitions in the define() method of a
 * LabelledTransitionSystemDefinition instance.
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
