package be.vibes.selection.ec.objective;

import be.vibes.fexpression.FExpression;
import be.vibes.solver.ConstraintIdentifier;
import be.vibes.solver.FeatureModel;
import be.vibes.solver.exception.ConstraintSolvingException;
import be.vibes.solver.exception.SolverInitializationException;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.Transition;
import be.vibes.ts.execution.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * This objective seeks to increase the similarity of the products required to execute the different test,
 * when selecting tests from a Featured Transition System. More than one product are requires if
 * two or more tests are incompatible (for instance, if the tests executes transitions with incompatible
 * feature expressions). Increasing similarity reduces the number of products required to execute the
 * test suite. The product similarity of a test suite is the average pairwise similarity of the products required to
 * execute the tests.
 *
 * @author Xavier Devroey
 */
public class ProductsSimilarity implements Objective {

    private static final Logger LOG = LoggerFactory.getLogger(ProductsSimilarity.class);

    private final FeaturedTransitionSystem fts;
    private final FeatureModel fm;

    public ProductsSimilarity(FeaturedTransitionSystem fts, FeatureModel fm) {
        this.fts = fts;
        this.fm = fm;
    }

    @Override
    public double evaluate(List<Execution> executions) {
        FExpression o1Expr;
        FExpression o2Expr;
        double similarity = 0.0;
        int count = 0;
        for (int i = 0; i < executions.size() - 1; i++) {
            o1Expr = getProductConstraint(this.fts, executions.get(i));
            for (int j = i + 1; j < executions.size(); j++) {
                o2Expr = getProductConstraint(this.fts, executions.get(j));
                similarity = similarity + similarity(this.fm, o1Expr, o2Expr);
                count++;
            }
        }
        return count > 0 ? similarity / count : 0.0;
    }

    static FExpression getProductConstraint(FeaturedTransitionSystem fts, Execution execution) {
        FExpression expr = FExpression.trueValue();
        if (execution != null) {
            for (Transition tr : execution) {
                expr.andWith(fts.getFExpression(tr));
            }
        }
        return expr;
    }

    static double similarity(FeatureModel fm, FExpression o1Expr, FExpression o2Expr) {
        FExpression intersect;
        FExpression union;
        if (o1Expr == null || o1Expr.isTrue()) {
            intersect = o2Expr == null ? FExpression.trueValue() : o2Expr;
        } else if (o2Expr == null) {
            intersect = o1Expr;
        } else {
            intersect = o1Expr.and(o2Expr).applySimplification();
        }

        if (o1Expr == null || o1Expr.isTrue()) {
            union = o2Expr == null ? FExpression.trueValue() : o2Expr;
        } else if (o2Expr == null) {
            union = o1Expr;
        } else {
            union = o1Expr.or(o2Expr).applySimplification();
        }

        ConstraintIdentifier id = null;
        double intersectionCount = 0.0;
        try {
            id = fm.addConstraint(intersect);
            intersectionCount = fm.getNumberOfSolutions();
            fm.removeConstraint(id);
        } catch (SolverInitializationException e) {
            LOG.debug("Could not add feature expression {}! Incompatible test cases? Intersection count will be 0", intersect);
        } catch (ConstraintSolvingException e) {
            LOG.error("Could not finish computing the number of intersecting products for feature expression {} due to solver error!", intersect, e);
        }

        double unionCount = 0.0;
        try {
        id = fm.addConstraint(union);
        unionCount = fm.getNumberOfSolutions();
        fm.removeConstraint(id);
        } catch (SolverInitializationException e) {
            LOG.error("Could not add feature expression {}! Is one of the tests not executable by any product? Union count will be 0", union);
        } catch (ConstraintSolvingException e) {
            LOG.error("Could not finish computing the number of intersecting products for feature expression {} due to solver error!", union, e);
        }
        checkState(unionCount >= intersectionCount, "Union (%s) has to be higher or equal to intersection (%s) !", unionCount, intersectionCount);
        return unionCount > 0.0 ? (intersectionCount / unionCount) : 0.0;
    }

}
