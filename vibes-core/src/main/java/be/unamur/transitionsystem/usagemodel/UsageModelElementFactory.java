package be.unamur.transitionsystem.usagemodel;

/*
 * #%L vibes-core %% Copyright (C) 2014 PReCISE, University of Namur %%
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. #L%
 */
import be.unamur.transitionsystem.TransitionSystemElementFactory;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;

/**
 * A factory implementation for {@link UsageModel} elements.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class UsageModelElementFactory implements
        TransitionSystemElementFactory<State, UsageModelTransition, Action> {

    public static final double DEFAULT_PROBA = 1.0;

    private double defaultValue = DEFAULT_PROBA;

    public UsageModelElementFactory() {
    }

    @Override
    public UsageModelTransition buildTransition() {
        UsageModelTransition tr = new UsageModelTransition();
        tr.setProbability(defaultValue);
        return tr;
    }

    @Override
    public UsageModelTransition buildTransition(State from, State to, Action action) {
        UsageModelTransition tr = new UsageModelTransition(from, to, action);
        tr.setProbability(defaultValue);
        return tr;
    }

    @Override
    public State buildState() {
        return new State();
    }

    @Override
    public State buildState(String name) {
        return new State(name);
    }

    @Override
    public Action buildAction() {
        return new Action();
    }

    @Override
    public Action buildAction(String name) {
        return new Action(name);
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    void setDefaultValue(double defaultValue) {
        this.defaultValue = defaultValue;
    }

}
