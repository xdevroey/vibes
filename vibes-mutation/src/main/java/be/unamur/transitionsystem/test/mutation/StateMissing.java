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
import java.util.Iterator;

import com.google.common.collect.Lists;

import static be.unamur.fts.fexpression.FExpression.*;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.test.mutation.exception.MutationException;

/**
 * Removes a random state and its transitions from the transition system
 * provided at the construction of the operator.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class StateMissing extends MutationOperator {

    private State missingState;
    private StateSelectionStrategy strategy;

    public StateMissing(TransitionSystem ts, StateSelectionStrategy strategy) {
        super(ts);
        this.strategy = strategy;
    }

    public StateMissing(TransitionSystem ts) {
        this(ts, null);
    }

    @Override
    public void setStateSelectionStrategy(StateSelectionStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Apply the mutation oeprator.
     *
     * @throws MutationException If the state returned by the
     * {@link StateSelectionStrategy} is the initial state of the
     * {@link LabelledTransitionSystem}.
     */
    @Override
    public void apply() throws MutationException {
        assert (this.strategy != null);
        clearModifiedStates();
        String key = getSymbol() + "_";
        this.missingState = strategy.selectState(this, getTransitionSystem());
        if (this.missingState == null) {
            throw new MutationException("No state selected!");
        }
        if (this.missingState.equals(getTransitionSystem().getInitialState())) {
            throw new MutationException(
                    "Initial State may not be removed from the transition system, see WrongStartState mutation operator!");
        }
        setMutationUniqueKey(key + this.missingState.getName());
        for (Iterator<Transition> iterator = missingState.incomingTransitions(); iterator.hasNext();) {
            Transition t = iterator.next();
            addModifiedState(t.getFrom());
        }
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
        // Mark all the incoming transitions to have a not FExpression with getMutationUniqueKey() as feature
        State state = fts.getState(this.missingState.getName());
        @SuppressWarnings("rawtypes")
        Iterator incomings = Lists.newArrayList(state.incomingTransitions()).iterator(); // Trick to avoid concurent modification
        FeaturedTransition trans;
        while (incomings.hasNext()) {
            trans = (FeaturedTransition) incomings.next();
            fts.removeTransition(trans);
            if (trans.getFeatureExpression() != null
                    && !trans.getFeatureExpression().isTrue()) {
                fts.addTransition(trans.getFrom(), trans.getTo(), trans.getAction(),
                        featureExpr(getFeatureId()).not().and(trans.getFeatureExpression()).applySimplification());
            } else {
                fts.addTransition(trans.getFrom(), trans.getTo(), trans.getAction(),
                        featureExpr(getFeatureId()).not());
            }
        }
        return fts;
    }

    @Override
    public TransitionSystem result() {
        if (getMutationUniqueKey() == null) {
            throw new IllegalStateException("Call apply() method first!");
        }
        TransitionSystem result = getTransitionSystem().copy();
        result.removeState(this.missingState);
        return result;
    }

    /**
     * Returns the state in the original transition system that has been removed
     * in the result trantision system.
     *
     * @return The missing state.
     */
    public State getMissingState() {
        return missingState;
    }

    @Override
    public String getSymbol() {
        return "SMI";
    }

}
