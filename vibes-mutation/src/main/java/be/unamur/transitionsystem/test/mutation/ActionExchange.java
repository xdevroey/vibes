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
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import be.unamur.fts.fexpression.Feature;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.test.mutation.exception.CounterExampleFoundException;
import be.unamur.transitionsystem.test.mutation.exception.MutationException;

import static com.google.common.base.Preconditions.*;

/**
 * Exchange an action on a transition.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class ActionExchange extends MutationOperator {

    private static final Logger logger = LoggerFactory.getLogger(ActionExchange.class);

    private Transition transition;
    private Action newAction;

    private TransitionSelectionStrategy trSelectStrategy;
    private ActionSelectionStrategy actSelectStrategy;

    public ActionExchange(TransitionSystem ts,
            TransitionSelectionStrategy trSelectStrategy,
            ActionSelectionStrategy actSelectStrategy) {
        super(ts);
        this.actSelectStrategy = actSelectStrategy;
        this.trSelectStrategy = trSelectStrategy;
    }

    public ActionExchange(TransitionSystem ts) {
        this(ts, null, null);
    }

    @Override
    public void setActionSelectionStrategy(ActionSelectionStrategy strategy) {
        this.actSelectStrategy = strategy;
    }

    @Override
    public void setTransitionSelectionStrategy(TransitionSelectionStrategy strategy) {
        this.trSelectStrategy = strategy;
    }

    @Override
    public void apply() throws MutationException {
        assert (this.actSelectStrategy != null);
        assert (this.trSelectStrategy != null);
        clearModifiedStates();
        String key = getSymbol() + "_";
        this.transition = trSelectStrategy.selectTransition(this, getTransitionSystem());
        if (this.transition == null) {
            throw new MutationException("No transition selected (TS not connected?)!");
        }
        this.newAction = actSelectStrategy.selectAction(this, getTransitionSystem());
        if (this.newAction == null) {
            throw new MutationException("No action selected!");
        }
        setMutationUniqueKey(key + transition.getFrom().getName() + "_to_"
                + transition.getTo().getName() + "_replaces_"
                + transition.getAction().getName() + "_by_" + newAction.getName());
        addModifiedState(transition.getFrom());
    }

    @Override
    public FeaturedTransitionSystem transpose(FeaturedTransitionSystem ftsToModify)
            throws MutationException, CounterExampleFoundException {
        if (getMutationUniqueKey() == null) {
            throw new IllegalStateException("Call apply() method first!");
        }
        FeaturedTransitionSystem fts = ftsToModify;
        if (fts == null) {
            fts = new FeaturedTransitionSystem(getTransitionSystem());
        }
        logger.debug("FTS to use in transposition is {}", fts);
        FeaturedTransition trToRemove = null;
        Iterator<Transition> outTransitionsIt = fts.getState(transition.getFrom().getName()).outgoingTransitions();
        List<FeaturedTransition> toRemove = Lists.newArrayList();
        while (outTransitionsIt.hasNext()) {
            trToRemove = (FeaturedTransition) outTransitionsIt.next();
            if (transition.equals(trToRemove)) {
                toRemove.add(trToRemove);
            }
        }
        logger.debug("Transitions to remove for transposing is {}", toRemove);
        // Remove Transitions
        for (Iterator<FeaturedTransition> iterator = toRemove.iterator(); iterator.hasNext();) {
            trToRemove = iterator.next();
            fts.removeTransition(trToRemove);
            if (trToRemove.getFeatureExpression() != null
                    && !trToRemove.getFeatureExpression().isTrue()) {
                fts.addTransition(
                        trToRemove.getFrom(),
                        trToRemove.getTo(),
                        trToRemove.getAction(),
                        FExpression.featureExpr(getFeatureId()).not().and(trToRemove.getFeatureExpression()).applySimplification());
                checkState(fts.getAction(newAction.getName()) != null, "New action does not figure in transposed FTS actions!");
                fts.addTransition(trToRemove.getFrom(), trToRemove.getTo(), fts.getAction(newAction.getName()),
                        FExpression.featureExpr(getFeatureId()).and(trToRemove.getFeatureExpression()).applySimplification());
            } else {
                fts.addTransition(trToRemove.getFrom(), trToRemove.getTo(), trToRemove.getAction(),
                        FExpression.featureExpr(getFeatureId()).not().applySimplification());
                fts.addTransition(trToRemove.getFrom(), trToRemove.getTo(), fts.getAction(newAction.getName()), FExpression.featureExpr(getFeatureId()));
            }
        }
        return fts;
    }

    @Override
    public TransitionSystem result() {
        if (getMutationUniqueKey() == null) {
            throw new IllegalStateException("Call apply() method first!");
        }
        TransitionSystem result = this.getTransitionSystem().copy();
        result.removeTransition(transition);
        State from = result.getState(transition.getFrom().getName());
        State to = result.getState(transition.getTo().getName());
        result.addTransition(from, to, result.getAction(newAction.getName()));
        return result;
    }

    public Action getNewAction() {
        return newAction;
    }

    public Transition getTransition() {
        return transition;
    }

    @Override
    public String getSymbol() {
        return "AEX";
    }

}
