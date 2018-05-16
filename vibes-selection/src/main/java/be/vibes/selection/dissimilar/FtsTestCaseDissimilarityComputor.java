package be.vibes.selection.dissimilar;

import be.vibes.fexpression.FExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.*;
import be.vibes.selection.exception.DissimilarityComputationException;
import be.vibes.solver.ConstraintIdentifier;
import be.vibes.solver.FeatureModel;
import be.vibes.solver.SolverFatalErrorException;
import be.vibes.solver.exception.ConstraintNotFoundException;
import be.vibes.solver.exception.ConstraintSolvingException;
import be.vibes.solver.exception.SolverInitializationException;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.FeaturedTransitionSystemExecutor;
import be.vibes.ts.TestCase;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;

public class FtsTestCaseDissimilarityComputor extends TestCaseDissimilarityComputor {

    private static final Logger LOG = LoggerFactory
            .getLogger(FtsTestCaseDissimilarityComputor.class);

    private final TestCaseDissimilarityComputor testCaseDissimilarity;
    private FeatureModel fm;
    private final BinaryOperator<Double> combineOperator;
    private FeaturedTransitionSystem fts;

    public FtsTestCaseDissimilarityComputor(FeatureModel solver, FeaturedTransitionSystem fts) {
        this(solver, new JaccardDissimilarityComputor(), fts);
    }

    public FtsTestCaseDissimilarityComputor(FeatureModel solver, TestCaseDissimilarityComputor computor, FeaturedTransitionSystem fts) {
        this(solver, computor, (Double x, Double y) -> x * y, fts);
    }

    public FtsTestCaseDissimilarityComputor(FeatureModel solver, TestCaseDissimilarityComputor computor, BinaryOperator<Double> combineOperator, FeaturedTransitionSystem fts) {
        this.fm = solver;
        this.testCaseDissimilarity = computor;
        this.combineOperator = combineOperator;
        this.fts = fts;
    }

    public FtsTestCaseDissimilarityComputor(FeatureModel solver, SetBasedDissimilarityComputor<? extends Set> computor, FeaturedTransitionSystem fts) {
        this(solver, TestCaseDissimilarityComputor.toTestCaseDissimilarityComputor(computor), fts);
    }

    public FtsTestCaseDissimilarityComputor(FeatureModel solver, SequenceBasedDissimilarityComputor<? extends List> computor, FeaturedTransitionSystem fts) {
        this(solver, TestCaseDissimilarityComputor.toTestCaseDissimilarityComputor(computor), fts);
    }

    public void setFm(FeatureModel fm) {
        this.fm = fm;
    }

    public void setFts(FeaturedTransitionSystem fts) {
        this.fts = fts;
    }
    
    @Override
    public double dissimilarity(TestCase o1, TestCase o2) throws DissimilarityComputationException {
        double dissimilarity = testCaseDissimilarity.dissimilarity(o1, o2);
        checkState(dissimilarity >= 0, "Dissimilarity computed using %s has to be >=0 but was %s!", testCaseDissimilarity.getClass().getName(), dissimilarity);
        LOG.trace("Test case dissimilarity is {}", dissimilarity);
        // Compute similarity
        double dissimilarity2 = productsDissimilarity(o1, o2);
        checkState(dissimilarity2 >= 0, "Products dissimilarity has to be >=0 but was %s!", dissimilarity2);
        LOG.trace("Products dissimilarity is {}", dissimilarity2);
        dissimilarity = combineOperator.apply(dissimilarity, dissimilarity2);
        LOG.trace("Combined dissimilarity is {}", dissimilarity);
        return dissimilarity;
    }

    private double productsDissimilarity(TestCase o1, TestCase o2) throws DissimilarityComputationException {
        try {
            FExpression intersect;
            FExpression o1Expr = getProductConstraint(o1);
            FExpression o2Expr = getProductConstraint(o2);
            if (o1Expr == null || o1Expr.isTrue()) {
                intersect = o2Expr == null ? FExpression.trueValue() : o2Expr;
            } else if (o2Expr == null) {
                intersect = o1Expr;
            } else {
                intersect = o1Expr.and(o2Expr).applySimplification();
            }

            FExpression union;
            if (o1Expr == null || o1Expr.isTrue()) {
                union = o2Expr == null ? FExpression.trueValue() : o2Expr;
            } else if (o2Expr == null) {
                union = o1Expr;
            } else {
                union = o1Expr.or(o2Expr).applySimplification();
            }

            ConstraintIdentifier id = fm.addConstraint(intersect);
            double intersectionCount = fm.getNumberOfSolutions();
            fm.removeConstraint(id);

            id = fm.addConstraint(union);
            double unionCount = fm.getNumberOfSolutions();
            fm.removeConstraint(id);

            checkState(unionCount >= intersectionCount, "Union (%s) has to be higher or equal to intersection (%s) !", unionCount, intersectionCount);

            return 1 - (intersectionCount / unionCount);
        } catch (SolverInitializationException e) {
            LOG.error("Error while coputing product sets for dissimilarity!", e);
            throw new DissimilarityComputationException("Solver error to compute products", e);
        } catch (SolverFatalErrorException | ConstraintNotFoundException e) {
            LOG.error("Error while coputing product sets for dissimilarity!", e);
            throw new DissimilarityComputationException("Solver error to compute products", e);
        } catch (ConstraintSolvingException e) {
            LOG.error("Error while coputing product sets for dissimilarity!", e);
            throw new DissimilarityComputationException("Solver error to compute products", e);
        }
    }

    private FExpression getProductConstraint(TestCase tc) {
        FeaturedTransitionSystemExecutor exec = new FeaturedTransitionSystemExecutor(fts, fm);
        return exec.getFexpression(tc);
    }
}
