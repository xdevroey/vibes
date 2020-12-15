package be.vibes.selection.ec.objective;

import be.vibes.fexpression.FExpression;
import be.vibes.solver.FeatureModel;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.execution.Execution;

import java.util.List;

/**
 * This objective seeks to increase the diversity (i.e., reduce similarity) of the products required to execute the different test,
 * when selecting tests from a Featured Transition System. More than one product are requires if
 * two or more tests are incompatible (for instance, if the tests executes transitions with incompatible
 * feature expressions). Increasing diversity increases the number of products required to execute the
 * test suite. The product dissimilarity of a test suite is the average pairwise dissimilarity of the
 * products required to execute the tests.
 *
 * @author Xavier Devroey
 */
public class ProductsDissimilarity implements Objective {

    private final FeaturedTransitionSystem fts;
    private final FeatureModel fm;

    public ProductsDissimilarity(FeaturedTransitionSystem fts, FeatureModel fm) {
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
            o1Expr = ProductsSimilarity.getProductConstraint(this.fts, executions.get(i));
            for (int j = i + 1; j < executions.size(); j++) {
                o2Expr = ProductsSimilarity.getProductConstraint(this.fts, executions.get(j));
                similarity = similarity + ProductsSimilarity.similarity(this.fm, o1Expr, o2Expr);
                count++;
            }
        }
        return count > 0 ? similarity / count : 0.0;
    }
}
