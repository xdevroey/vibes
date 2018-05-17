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
package be.vibes.dsl.ts;

import be.vibes.fexpression.FExpression;
import be.vibes.fexpression.ParserUtil;
import be.vibes.fexpression.exception.ParserException;


/**
 * This class is used to define featured transitions in the define() method of a
 * FeaturedTransitionSystemDefinition instance.
 *
 * @see FeaturedTransitionSystemDefinition
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class FeaturedTransitionDefinition extends TransitionDefinition {

    private FExpression fexpr = FExpression.trueValue();

    FeaturedTransitionDefinition(String fromStateName,
            AbstractTransitionSystemDefinition def) {
        super(fromStateName, def);
    }

    /**
     * Sets the feature expression of this transition.
     *
     * @param expression The feature expression. It has to respect the syntax
     * defined in be.unamur.info.vibes-fexpression module.
     * @return This.
     * @throws IllegalArgumentException If the given feature expression contains
     * syntax errors.
     */
    public FeaturedTransitionDefinition fexpr(String expression) {
        try {
            this.fexpr = ParserUtil.getInstance().parse(expression);
        } catch (ParserException e) {
            throw new IllegalArgumentException("Could not parse feature expression!", e);
        }
        return this;
    }

    /**
     * Sets the feature expression of this transition.
     *
     * @param expression The feature expression.
     * @return This.
     */
    public FeaturedTransitionDefinition fexpr(FExpression expression) {
        this.fexpr = expression;
        return this;
    }

    @Override
    public FeaturedTransitionDefinition action(String actionName) {
        return (FeaturedTransitionDefinition) super.action(actionName);
    }

    FExpression getFExpression() {
        return fexpr;
    }

}
