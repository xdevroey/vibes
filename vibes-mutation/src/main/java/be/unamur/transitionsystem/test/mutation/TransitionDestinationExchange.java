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

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.test.mutation.exception.MutationException;

/**
 * Redirects a transition to another state according to the provided selection
 * strategies.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class TransitionDestinationExchange extends MutationOperator {

    private static final Logger logger = LoggerFactory.getLogger(TransitionDestinationExchange.class);

    private Transition transition;
    private State newDest;

    private TransitionSelectionStrategy trSelectStrat;
    private StateSelectionStrategy stateSelectStrat;

    public TransitionDestinationExchange(TransitionSystem ts,
            TransitionSelectionStrategy trSelectStrat,
            StateSelectionStrategy stateSelectStrat) {
        super(ts);
        this.trSelectStrat = trSelectStrat;
        this.stateSelectStrat = stateSelectStrat;
    }

    public TransitionDestinationExchange(TransitionSystem ts) {
        this(ts, null, null);
    }

    @Override
    public void setTransitionSelectionStrategy(TransitionSelectionStrategy strategy) {
        this.trSelectStrat = strategy;
    }

    @Override
    public void setStateSelectionStrategy(StateSelectionStrategy strategy) {
        this.stateSelectStrat = strategy;
    }

    @Override
    public void apply() throws MutationException {
        assert (this.trSelectStrat != null && this.stateSelectStrat != null);
        clearModifiedStates();
        this.transition = trSelectStrat.selectTransition(this, getTransitionSystem());
        if (this.transition == null) {
            throw new MutationException("No transition selected!");
        }
        this.newDest = stateSelectStrat.selectState(this, getTransitionSystem());
        if (this.newDest == null) {
            throw new MutationException("No new destination state selected!");
        }
        setMutationUniqueKey(getSymbol() + "_" + transition.getFrom().getName() + "_to_"
                + transition.getTo().getName() + "_act_"
                + transition.getAction().getName() + "_redirected_to_"
                + newDest.getName());
        addModifiedState(transition.getFrom());
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
        logger.debug("FTS to use in transposition is {}", fts);
        logger.debug("Transition to search is {}", transition);
        FeaturedTransition oldTransition = null;
        Iterator<Transition> outTransitionsIt = fts.getState(transition.getFrom().getName()).outgoingTransitions();
        while (outTransitionsIt.hasNext() && oldTransition == null) {
            oldTransition = (FeaturedTransition) outTransitionsIt.next();
            if (!transition.equals(oldTransition)) {
                oldTransition = null;
            }
        }
        logger.debug("Transition to remove for transposing is {}", oldTransition);
        assert (oldTransition != null);
        fts.removeTransition(oldTransition);
        if (oldTransition.getFeatureExpression() != null
                && !oldTransition.getFeatureExpression().isTrue()) {
            fts.addTransition(oldTransition.getFrom(), oldTransition.getTo(),
                    oldTransition.getAction(),
                    featureExpr(getFeatureId()).not().and(oldTransition.getFeatureExpression()));
            fts.addTransition(
                    oldTransition.getFrom(),
                    fts.getState(newDest.getName()),
                    oldTransition.getAction(),
                    featureExpr(getFeatureId()).and(oldTransition.getFeatureExpression()));
        } else {
            fts.addTransition(oldTransition.getFrom(), oldTransition.getTo(),
                    oldTransition.getAction(), featureExpr(getFeatureId()).not());
            fts.addTransition(oldTransition.getFrom(), fts.getState(newDest.getName()),
                    oldTransition.getAction(), featureExpr(getFeatureId()));
        }
        return fts;
    }

    @Override
    public TransitionSystem result() {
        if (getMutationUniqueKey() == null) {
            throw new IllegalStateException("Call apply() method first!");
        }
        TransitionSystem result = getTransitionSystem().copy();
        result.removeTransition(transition);
        State from = result.getState(transition.getFrom().getName());
        State to = result.getState(newDest.getName());
        Action act = result.getAction(transition.getAction().getName());
        result.addTransition(from, to, act);
        return result;
    }

    @Override
    public String getSymbol() {
        return "TDE";
    }

    public Transition getTransition() {
        return transition;
    }

    public State getNewDest() {
        return newDest;
    }

}
