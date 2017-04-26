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
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;

@Deprecated
public class TransitionSystemBuilderFrom {

    private TransitionSystem ts;
    private String fromStateName;
    private TransitionSystemBuilder builder;
    private String actionName = Action.NO_ACTION_NAME;

    TransitionSystemBuilderFrom(TransitionSystemBuilder builder, TransitionSystem ts, String name) {
        this.builder = builder;
        this.ts = ts;
        this.fromStateName = name;
    }

    public TransitionSystemBuilderFrom action(String name) {
        this.actionName = name;
        return this;
    }

    public TransitionSystemBuilder to(String stateName) {
        Action action = ts.addAction(actionName);
        State from = ts.addState(fromStateName);
        State to = ts.addState(stateName);
        ts.addTransition(from, to, action);
        return builder;
    }

}
