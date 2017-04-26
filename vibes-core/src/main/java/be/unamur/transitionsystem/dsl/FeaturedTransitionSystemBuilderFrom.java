package be.unamur.transitionsystem.dsl;

/*
 * #%L
 * vibes-core
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.fts.fexpression.exception.ParserException;
import be.unamur.fts.fexpression.ParserUtil;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;

@Deprecated
public class FeaturedTransitionSystemBuilderFrom {

    private static final Logger logger = LoggerFactory.getLogger(FeaturedTransitionSystemBuilderFrom.class);

    protected String expression;
    protected TransitionSystem ts;
    protected String fromStateName;
    protected FeaturedTransitionSystemBuilder builder;
    protected String actionName = Action.NO_ACTION_NAME;

    FeaturedTransitionSystemBuilderFrom(FeaturedTransitionSystemBuilder featuredTransitionSystemBuilder, TransitionSystem ts, String stateName) {
        this.builder = featuredTransitionSystemBuilder;
        this.ts = ts;
        this.fromStateName = stateName;
    }

    public FeaturedTransitionSystemBuilderFrom action(String name) {
        this.actionName = name;
        return this;
    }

    public FeaturedTransitionSystemBuilderFrom fexpression(String expression) {
        this.expression = expression;
        return this;
    }

    public FeaturedTransitionSystemBuilder to(String stateName) {
        Action action = ts.addAction(actionName);
        State from = ts.addState(fromStateName);
        State to = ts.addState(stateName);
        if (expression != null) {
            try {
                ((FeaturedTransitionSystem) ts).addTransition(from, to, action, ParserUtil.getInstance().parse(expression));
            } catch (ParserException e) {
                logger.error("Wrong feature expression '{}'!", expression, e);
                throw new IllegalArgumentException("Could not parse feature expression " + expression, e);
            }
        } else {
            ts.addTransition(from, to, action);
        }
        return builder;
    }

}
