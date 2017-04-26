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
import be.unamur.fts.fexpression.FExpression;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.mutation.exception.MutationException;

/**
 * Add a transition between two given states with a given action according to
 * the provided strategies.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class TransitionAdd extends MutationOperator {

    private State from;
    private State to;
    private Action action;

    private StateSelectionStrategy stateSelectStrat;
    private ActionSelectionStrategy actSelectStrat;

    public TransitionAdd(TransitionSystem ts, StateSelectionStrategy stateSelectStrat,
            ActionSelectionStrategy actSelectStrat) {
        super(ts);
        this.stateSelectStrat = stateSelectStrat;
        this.actSelectStrat = actSelectStrat;
    }

    public TransitionAdd(TransitionSystem ts) {
        this(ts, null, null);
    }

    @Override
    public void setStateSelectionStrategy(StateSelectionStrategy strategy) {
        this.stateSelectStrat = strategy;
    }

    @Override
    public void setActionSelectionStrategy(ActionSelectionStrategy strategy) {
        this.actSelectStrat = strategy;
    }

    @Override
    public void apply() throws MutationException {
        assert (this.stateSelectStrat != null && this.actSelectStrat != null);
        clearModifiedStates();
        this.from = stateSelectStrat.selectState(this, getTransitionSystem());
        if (this.from == null) {
            throw new MutationException("No from state selected!");
        }
        this.to = stateSelectStrat.selectState(this, getTransitionSystem());
        if (this.to == null) {
            throw new MutationException("No to state selected!");
        }
        this.action = actSelectStrat.selectAction(this, getTransitionSystem());
        if (this.action == null) {
            throw new MutationException("No action selected!");
        }
        setMutationUniqueKey(getSymbol() + "_" + from.getName() + "_to_" + to.getName()
                + "_act_" + action.getName());
        addModifiedState(from);
    }

    @Override
    public FeaturedTransitionSystem transpose(FeaturedTransitionSystem ftsToModify)
            throws MutationException {
        if (getMutationUniqueKey() == null) {
            throw new IllegalStateException("Call apply() method first!");
        }
        FeaturedTransitionSystem fts = ftsToModify;
        if (fts == null) {
            fts = new FeaturedTransitionSystem(getTransitionSystem());
        }
        fts.addTransition(fts.getState(from.getName()), fts.getState(to.getName()), fts.getAction(action.getName()), FExpression.featureExpr(getFeatureId()));
        return fts;
    }

    @Override
    public TransitionSystem result() {
        if (getMutationUniqueKey() == null) {
            throw new IllegalStateException("Call apply() method first!");
        }
        TransitionSystem result = getTransitionSystem().copy();
        result.addTransition(result.getState(from.getName()), result.getState(to.getName()), result.getAction(action.getName()));
        return result;
    }

    @Override
    public String getSymbol() {
        return "TAD";
    }

    public State getFrom() {
        return from;
    }

    public State getTo() {
        return to;
    }

    public Action getAction() {
        return action;
    }

}
