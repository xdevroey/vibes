package be.vibes.ts.execution;

/*-
 * #%L
 * VIBeS: core
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import be.vibes.fexpression.FExpression;
import be.vibes.solver.ConstraintIdentifier;
import be.vibes.solver.FeatureModel;
import be.vibes.solver.SolverFatalErrorException;
import be.vibes.solver.exception.ConstraintNotFoundException;
import be.vibes.solver.exception.ConstraintSolvingException;
import be.vibes.solver.exception.SolverInitializationException;
import be.vibes.ts.Action;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.Transition;
import be.vibes.ts.exception.TransitionSystenExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class FeaturedTransitionSystemExecutor extends Executor<FeaturedTransitionSystem> {

    private static final Logger LOG = LoggerFactory.getLogger(FeaturedTransitionSystemExecutor.class);

    private final FeatureModel fm;

    public FeaturedTransitionSystemExecutor(FeaturedTransitionSystem fts, FeatureModel fm) {
        super(fts);
        this.fm = fm;
    }

    @Override
    protected List<Transition> getNextTransitions(Execution current, Action action) throws TransitionSystenExecutionException {
        List<Transition> candidates = super.getNextTransitions(current, action);
        List<Transition> result = new ArrayList<>();
        // Filter transition to ones with compatible feature expression
        ConstraintIdentifier currentExecId = addConstraint(getFexpression(current)); // Current execution is assumed to be valid.
        for (Transition tr : candidates) {
            ConstraintIdentifier trId = addConstraint(getTransitionSystem().getFExpression(tr));
            try {
                if (trId != null && fm.isSatisfiable()) {
                    result.add(tr);
                    removeConstraint(trId);
                }
            } catch (ConstraintSolvingException ex) {
                LOG.debug("Solver could not feature expression {} of transition {} ", trId.getConstraint(), tr, ex);
                throw new TransitionSystenExecutionException("Exception happened while solving feature expression " + trId.getConstraint() + " of transition " + tr + "!", ex);
            }
        }
        removeConstraint(currentExecId);
        return result;
    }

    /**
     * Returns the id of the constraint or null if the constraint provokes an unsat problem.
     *
     * @param expr The expression to add
     * @return The id of the constraint or null if the constraint provokes an unsat problem.
     * @throws TransitionSystenExecutionException If the solver encountered a fatal error that cannot be recovered from.
     */
    private ConstraintIdentifier addConstraint(FExpression expr) throws TransitionSystenExecutionException {
        try {
            return fm.addConstraint(expr);
        } catch (SolverInitializationException ex) {
            LOG.debug("Could not add constraint {} to Feature Model solver!", expr, ex);
            return null;
        } catch (SolverFatalErrorException ex) {
            LOG.debug("Solver encountered a fatal error while adding constraint {}!", expr, ex);
            throw new TransitionSystenExecutionException("Solver encountered a fatal error while adding constraint " + expr + "!", ex);
        }
    }

    private void removeConstraint(ConstraintIdentifier id) throws TransitionSystenExecutionException {
        try {
            fm.removeConstraint(id);
        } catch (SolverFatalErrorException ex) {
            LOG.debug("Could not remove constraint {} to Feature Model solver, will reset!", id.getConstraint(), ex);
            try {
                fm.reset();
            } catch (SolverInitializationException ex2) {
                LOG.error("Could not reset feature model!", ex2);
                throw new TransitionSystenExecutionException("Could not reset feature model!", ex2);
            }
        } catch (ConstraintNotFoundException ex) {
            LOG.debug("Solver encountered a fatal error while removing constraint {}, will reset!", id.getConstraint(), ex);
            try {
                fm.reset();
            } catch (SolverInitializationException ex2) {
                LOG.error("Could not reset feature model!", ex2);
                throw new TransitionSystenExecutionException("Could not reset feature model!", ex2);
            }
        }
    }

    public FExpression getFexpression(Execution current) {
        FExpression expr = FExpression.trueValue();
        if (current != null) { // If this is not the initial state, collect feature expressions
            for (Transition tr : current) {
                expr.andWith(getTransitionSystem().getFExpression(tr));
            }
        }
        return expr;
    }

    @Override
    protected FeaturedTransitionSystem getTransitionSystem() {
        return (FeaturedTransitionSystem) super.getTransitionSystem();
    }

    @Override
    public void reset() throws TransitionSystenExecutionException {
        super.reset();
        try {
            this.fm.reset();
        } catch (SolverInitializationException e) {
            LOG.error("Exception while resetting the feature model!", e);
            throw new TransitionSystenExecutionException("Exception while resetting the feature model!", e);
        }
    }
}
