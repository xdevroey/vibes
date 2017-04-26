package be.unamur.transitionsystem.dsl;

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
import be.unamur.transitionsystem.Action;

/**
 * This class is used to define transtisions in the define() method of a
 * LabelledTransitionSystemDefinition instance.
 *
 * @see LabelledTransitionSystemDefinition
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class TransitionDefinition {

    protected TransitionSystemDefinition context;
    protected String actionName = Action.NO_ACTION_NAME;
    protected String fromStateName;
    protected String toStateName;

    TransitionDefinition(String fromStateName, TransitionSystemDefinition def) {
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

    String getFromStateName() {
        return fromStateName;
    }

    String getToStateName() {
        return toStateName;
    }

}
