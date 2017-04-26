package be.unamur.transitionsystem.test.mutation;

/*
 * #%L
 * vibes-mutation
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
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.mutation.exception.MutationException;

/**
 * Removes the action of a transition.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class ActionMissing extends ActionExchange {

    public ActionMissing(TransitionSystem ts, TransitionSelectionStrategy strategy) {
        super(ts, strategy, (MutationOperator operator, TransitionSystem ts1) -> ts1.addAction(Action.NO_ACTION_NAME));
    }

    public ActionMissing(TransitionSystem ts) {
        this(ts, null);
    }

    @Override
    public void setActionSelectionStrategy(ActionSelectionStrategy strategy) {
    }

    @Override
    public void apply() throws MutationException {
        super.apply();
        setMutationUniqueKey(getSymbol() + "_" + getTransition().getFrom().getName() + "_to_"
                + getTransition().getTo().getName() + "_removes_"
                + getTransition().getAction().getName());
    }

    @Override
    public String getSymbol() {
        return "AMI";
    }

}
