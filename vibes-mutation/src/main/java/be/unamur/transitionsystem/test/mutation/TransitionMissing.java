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
import static com.google.common.base.Preconditions.*;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.test.mutation.exception.MutationException;

public class TransitionMissing extends MutationOperator {

    private static final Logger logger = LoggerFactory.getLogger(TransitionMissing.class);

    private TransitionSelectionStrategy strategy;
    private Transition transition;

    public TransitionMissing(TransitionSystem ts, TransitionSelectionStrategy strategy) {
        super(ts);
        this.strategy = strategy;
    }

    public TransitionMissing(TransitionSystem ts) {
        this(ts, null);
    }

    @Override
    public void setTransitionSelectionStrategy(TransitionSelectionStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void apply() throws MutationException {
        assert (this.strategy != null);
        clearModifiedStates();
        this.transition = this.strategy.selectTransition(this, getTransitionSystem());
        if (this.transition == null) {
            throw new MutationException("No transition selected!");
        }
        setMutationUniqueKey(getSymbol() + "_" + getTransition().getFrom().getName() + "_to_"
                + getTransition().getTo().getName() + "_act_"
                + getTransition().getAction().getName());
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
        FeaturedTransition tr = null;
        Iterator<Transition> outTransitionsIt = fts.getState(transition.getFrom().getName()).outgoingTransitions();
        while (outTransitionsIt.hasNext() && tr == null) {
            tr = (FeaturedTransition) outTransitionsIt.next();
            if (!transition.equals(tr)) {
                tr = null;
            }
        }
        checkNotNull(tr);
        logger.debug("Transition to remove for transposing is {}", tr);
        fts.removeTransition(tr);
        if (tr.getFeatureExpression() != null
                && !tr.getFeatureExpression().isTrue()) {
            fts.addTransition(
                    tr.getFrom(),
                    tr.getTo(),
                    tr.getAction(),
                    featureExpr(getFeatureId()).not().and(tr.getFeatureExpression()));
        } else {
            fts.addTransition(tr.getFrom(), tr.getTo(), tr.getAction(),
                    featureExpr(getFeatureId()).not());
        }
        return fts;
    }

    @Override
    public TransitionSystem result() {
        if (getMutationUniqueKey() == null) {
            throw new IllegalStateException("Call apply() method first!");
        }
        TransitionSystem result = getTransitionSystem().copy();
        result.removeTransition(getTransition());
        return result;
    }

    @Override
    public String getSymbol() {
        return "TMI";
    }

    /**
     * Returns the transition missing in the system.
     *
     * @return The missing transition.
     */
    public Transition getTransition() {
        return transition;
    }

}
