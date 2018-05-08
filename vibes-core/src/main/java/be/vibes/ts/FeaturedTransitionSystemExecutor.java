package be.vibes.ts;

import be.vibes.fexpression.FExpression;
import be.vibes.solver.ConstraintIdentifier;
import be.vibes.solver.FeatureModel;
import be.vibes.solver.SolverFatalErrorException;
import be.vibes.solver.exception.ConstraintNotFoundException;
import be.vibes.solver.exception.ConstraintSolvingException;
import be.vibes.solver.exception.SolverInitializationException;
import be.vibes.ts.exception.TransitionSystenExecutionException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class FeaturedTransitionSystemExecutor extends TransitionSystemExecutor {

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
        ConstraintIdentifier currentExecId = addConstraint(getFexpression(current));
        for (Transition tr : candidates) {
            ConstraintIdentifier trId = addConstraint(getTransitionSystem().getFExpression(tr));
            try {
                if (fm.isSatisfiable()) {
                    result.add(tr);
                }
            } catch (ConstraintSolvingException ex) {
                LOG.debug("Solver could not feature expression {} of transition {} ", trId.getConstraint(), tr, ex);
                throw new TransitionSystenExecutionException("Exception happend while solving feature expression " + trId.getConstraint() + " of transition " + tr + "!", ex);
            } finally {
                removeConstraint(currentExecId);
            }
        }
        return result;
    }

    private ConstraintIdentifier addConstraint(FExpression expr) throws TransitionSystenExecutionException {
        try {
            return fm.addConstraint(expr);
        } catch (SolverInitializationException ex) {
            LOG.debug("Could not add constraint {} to Feature Model solver!", expr, ex);
            throw new TransitionSystenExecutionException("Could add constraint " + expr + " to Feature Model solver!", ex);
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
        } catch (ConstraintNotFoundException ex) {
            LOG.debug("Solver encountered a fatal error while removing constraint {}, will reset!", id.getConstraint(), ex);
        } finally {
            try {
                fm.reset();
            } catch (SolverInitializationException ex) {
                LOG.error("Could not reset feature model!", ex);
                throw new TransitionSystenExecutionException("Could not reset feature model!", ex);
            }
        }
    }

    private FExpression getFexpression(Execution current) {
        FExpression expr = FExpression.trueValue();
        for (Transition tr : current) {
            expr.andWith(getTransitionSystem().getFExpression(tr));
        }
        return expr;
    }

    @Override
    protected FeaturedTransitionSystem getTransitionSystem() {
        return (FeaturedTransitionSystem) super.getTransitionSystem();
    }

}
