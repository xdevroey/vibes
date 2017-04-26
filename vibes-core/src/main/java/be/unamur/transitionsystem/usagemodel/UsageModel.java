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
import java.util.Iterator;

import be.unamur.transitionsystem.AbstractTransitionSystem;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;

import static com.google.common.base.Preconditions.*;

/**
 * A class representing usage models. A usage model is a TS where each
 * transition is labelled with an action and a probability to be executed.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class UsageModel extends
        AbstractTransitionSystem<State, UsageModelTransition, Action> {

    /**
     * Creates a new usage model.
     */
    public UsageModel() {
        super(new UsageModelElementFactory());
    }

    /**
     * Creates a new usage model from the given original one.
     * @param original
     */
    public UsageModel(UsageModel original) {
        super(original);
    }

    /**
     * Add a transition with the given probability to the model.
     *
     * @param from The source state. Pre: this.getState(from.getName()) != null
     * @param to The destination state. Pre: this.getState(to.getName()) != null
     * @param action The aciton of the transition. Pre:
     * this.getAction(action.getName()) != null
     * @param probability The probability of the transition. Pre: probability
     * has to be between 0 and 1.
     * @return The added transition.
     */
    public UsageModelTransition addTransition(State from, State to, Action action,
            double probability) {
        checkArgument(0 <= probability && probability <= 1);
        ((UsageModelElementFactory) factory).setDefaultValue(probability);
        UsageModelTransition tr = super.addTransition(from, to, action);
        ((UsageModelElementFactory) factory).setDefaultValue(UsageModelElementFactory.DEFAULT_PROBA);
        return tr;
    }

    @Override
    public UsageModelTransition addTransition(State from, State to, Action action) {
        return addTransition(from, to, action, 1.0);
    }

    @Override
    protected void importTransitions(Iterator<State> states) {
        State state;
        Iterator<Transition> itTrans;
        UsageModelTransition trans;
        while (states.hasNext()) {
            state = states.next();
            itTrans = state.outgoingTransitions();
            while (itTrans.hasNext()) {
                trans = (UsageModelTransition) itTrans.next();
                addTransition(getState(trans.getFrom().getName()), getState(trans.getTo()
                        .getName()), getAction(trans.getAction().getName()),
                        trans.getProbability());
            }
        }
    }

    @Override
    public TransitionSystem copy() {
        return new UsageModel(this);
    }

}
