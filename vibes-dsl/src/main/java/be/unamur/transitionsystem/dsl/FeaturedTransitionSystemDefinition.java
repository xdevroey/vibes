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

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.dsl.exception.TransitionDefinitionException;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;

/**
 * This abstract class allows to define featured transition systems (FTS) by extending
 * it and implementing the define() method.
 *
 * @see TransitionSystemDefinition
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public abstract class FeaturedTransitionSystemDefinition extends
        TransitionSystemDefinition {

    /**
     * Creates a new FTS definition corresponding to the declaration made in
     * this.define().
     */
    public FeaturedTransitionSystemDefinition() {
        super(new FeaturedTransitionSystem());
    }

    @Override
    void notifyTransitionDefinitionComplete(TransitionDefinition definition) {
        if (definition.getToStateName() == null) {
            throw new TransitionDefinitionException("Transition has to end in a state!");
        }
        FeaturedTransitionDefinition def = (FeaturedTransitionDefinition) definition;
        State from = getTransitionSystem().addState(def.getFromStateName());
        State to = getTransitionSystem().addState(def.getToStateName());
        Action act = getTransitionSystem().addAction(def.getActionName());
        getTransitionSystem().addTransition(from, to, act, def.getFExpression());
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
