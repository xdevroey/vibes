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

import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.fexpression.exception.ParserException;
import be.unamur.fts.fexpression.ParserUtil;

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
            TransitionSystemDefinition def) {
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
