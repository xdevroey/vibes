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
import be.unamur.transitionsystem.usagemodel.UsageModel;

@Deprecated
public class UsageModelTransitionBuilder {

    private double proba = 1.0;
    private String fromName;
    private UsageModelBuilder def;
    private String actionName;
    private UsageModel ts;

    UsageModelTransitionBuilder(UsageModelBuilder usageModelDefinition,
            UsageModel ts, String stateName) {
        this.def = usageModelDefinition;
        this.ts = ts;
        this.fromName = stateName;
    }

    public UsageModelTransitionBuilder proba(double value) {
        this.proba = value;
        return this;
    }

    public UsageModelTransitionBuilder action(String actionName) {
        this.actionName = actionName;
        return this;
    }

    double getProbability() {
        return proba;
    }

    public UsageModelBuilder to(String stateName) {
        Action action = ts.addAction(actionName);
        State from = ts.addState(fromName);
        State to = ts.addState(stateName);
        ts.addTransition(from, to, action, proba);
        return def;
    }

}
