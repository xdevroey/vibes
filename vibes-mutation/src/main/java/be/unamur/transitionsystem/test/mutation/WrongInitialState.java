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
import static be.unamur.fts.fexpression.FExpression.*;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.mutation.exception.MutationException;

/**
 * Changes the initial state to another state of the system.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class WrongInitialState extends MutationOperator {

    public static final String WIS_ACTION_NAME = "__WIS_ACTION";
    public static final String WIS_SYMBOL = "WIS";

    private State newInitialState;
    private StateSelectionStrategy strategy;

    public WrongInitialState(TransitionSystem ts, StateSelectionStrategy strategy) {
        super(ts);
        this.strategy = strategy;
    }

    public WrongInitialState(TransitionSystem ts) {
        this(ts, null);
    }

    @Override
    public void setStateSelectionStrategy(StateSelectionStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * @throws MutationException If the state returned by the
     * {@link StateSelectionStrategy} is the initial state of the
     * {@link LabelledTransitionSystem}.
     */
    @Override
    public void apply() throws MutationException {
        assert (strategy != null);
        clearModifiedStates();
        String key = getSymbol() + "_";
        this.newInitialState = strategy.selectState(this, getTransitionSystem());
        if (this.newInitialState == null) {
            throw new MutationException("No new initial state selected!");
        }
        if (this.newInitialState.equals(getTransitionSystem().getInitialState())) {
            throw new MutationException("Initial State won't change!");
        }
        setMutationUniqueKey(key + this.newInitialState.getName());
        addModifiedState(getTransitionSystem().getInitialState());
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
        State oldInit = fts.getInitialState();
        String name = getMutationUniqueKey();
        while (fts.getState(name) != null) {
            name = name + "_XDE";
        }
        State artificialInit = fts.addState(name);
        fts.setInitialState(artificialInit);
        fts.addTransition(artificialInit, oldInit, fts.addAction(WIS_ACTION_NAME),
                featureExpr(getFeatureId()).not());
        fts.addTransition(artificialInit, fts.getState(this.newInitialState.getName()),
                fts.addAction(WIS_ACTION_NAME), featureExpr(getFeatureId()));
        return fts;
    }

    @Override
    public TransitionSystem result() {
        if (getMutationUniqueKey() == null) {
            throw new IllegalStateException("Call apply() method first!");
        }
        TransitionSystem result = getTransitionSystem().copy();
        result.setInitialState(this.newInitialState);
        return result;
    }

    public State getNewInitialState() {
        return this.newInitialState;
    }

    @Override
    public String getSymbol() {
        return WIS_SYMBOL;
    }

}
