package be.unamur.transitionsystem.test.execution;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.solver.ConstraintIdentifier;
import be.unamur.fts.solver.SolverFacade;
import be.unamur.fts.solver.exception.ConstraintSolvingException;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.execution.exception.ExecutionRuntimeException;

/**
 * Creates a new featured transition system executor.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class FeaturedTransitionSystemExecutor extends TransitionSystemExecutor<FeaturedTransitionSystem> {

    private SolverFacade solver;

    /**
     * Creates a new transition system executor.
     *
     * @param ts The transition system to execute.
     * @param solver
     */
    public FeaturedTransitionSystemExecutor(FeaturedTransitionSystem ts, SolverFacade solver) {
        super(ts);
        this.solver = solver;
    }

    @Override
    public boolean mayFire(Transition tr) {
        boolean may = super.mayFire(tr);
        if (may) {
            try {
                may = checkFeatureExpression();
            } catch (ConstraintSolvingException e) {
                throw new ExecutionRuntimeException("Exception while using solver!", e);
            }
        }
        return may;
    }

    private boolean checkFeatureExpression() throws ConstraintSolvingException {
        FExpression expr = FExpression.trueValue();
        for (Transition tr : getExecutedTransitions()) {
            FeaturedTransition t = (FeaturedTransition) tr;
            expr.andWith(t.getFeatureExpression());
            expr = expr.applySimplification();
        }
        ConstraintIdentifier id = solver.addConstraint(expr);
        boolean ok = solver.isSatisfiable();
        solver.removeConstraint(id);
        return ok;
    }
}
