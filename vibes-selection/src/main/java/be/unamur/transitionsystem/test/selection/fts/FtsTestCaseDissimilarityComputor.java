package be.unamur.transitionsystem.test.selection.fts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.*;
import be.unamur.transitionsystem.test.FtsTestCase;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.selection.JaccardDissimilarityComputor;
import be.unamur.transitionsystem.test.selection.TestCaseDissimilarityComputor;
import be.unamur.transitionsystem.test.selection.exception.DissimilarityComputationException;
import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.solver.ConstraintIdentifier;
import be.unamur.fts.solver.SolverFacade;
import be.unamur.fts.solver.SolverFatalErrorException;
import be.unamur.fts.solver.exception.ConstraintNotFoundException;
import be.unamur.fts.solver.exception.ConstraintSolvingException;
import be.unamur.fts.solver.exception.SolverInitializationException;
import be.unamur.transitionsystem.test.selection.SequenceBasedDissimilarityComputor;
import be.unamur.transitionsystem.test.selection.SetBasedDissimilarityComputor;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;

public class FtsTestCaseDissimilarityComputor extends TestCaseDissimilarityComputor {

    private static final Logger logger = LoggerFactory
            .getLogger(FtsTestCaseDissimilarityComputor.class);

    private TestCaseDissimilarityComputor testCaseDissimilarity;
    private SolverFacade solver;
    private BinaryOperator<Double> combineOperator;

    public FtsTestCaseDissimilarityComputor(SolverFacade solver) {
        this(solver, new JaccardDissimilarityComputor());
    }
    
    public FtsTestCaseDissimilarityComputor(SolverFacade solver, TestCaseDissimilarityComputor computor) {
        this(solver, computor, (Double x, Double y) -> x*y);
    }
    
    public FtsTestCaseDissimilarityComputor(SolverFacade solver, TestCaseDissimilarityComputor computor, BinaryOperator<Double> combineOperator) {
        this.solver = solver;
        this.testCaseDissimilarity = computor;
        this.combineOperator = combineOperator;
    }
    
    public FtsTestCaseDissimilarityComputor(SolverFacade solver, SetBasedDissimilarityComputor<? extends Set> computor) {
        this(solver, TestCaseDissimilarityComputor.toTestCaseDissimilarityComputor(computor));
    }
    
    public FtsTestCaseDissimilarityComputor(SolverFacade solver, SequenceBasedDissimilarityComputor<? extends List> computor) {
        this(solver, TestCaseDissimilarityComputor.toTestCaseDissimilarityComputor(computor));
    }
    
    @Override
    public double dissimilarity(TestCase o1, TestCase o2)
            throws DissimilarityComputationException {
        checkArgument(o1 instanceof FtsTestCase);
        checkArgument(o2 instanceof FtsTestCase);
        double dissimilarity = testCaseDissimilarity.dissimilarity(o1, o2);
        checkState(dissimilarity >= 0,
                "Dissimilarity computed using %s has to be >=0 but was %s!",
                testCaseDissimilarity.getClass().getName(), new Double(dissimilarity));
        FtsTestCase tc1 = (FtsTestCase) o1;
        FtsTestCase tc2 = (FtsTestCase) o2;
        // Compute similarity
        double dissimilarity2 = productsDissimilarity(tc1, tc2);
        checkState(dissimilarity2 >= 0,
                "Products dissimilarity has to be >=0 but was %s!", new Double(
                        dissimilarity2));
        dissimilarity = combineOperator.apply(dissimilarity, dissimilarity2);
        return dissimilarity;
    }

    private double productsDissimilarity(FtsTestCase o1, FtsTestCase o2) throws DissimilarityComputationException {
        try {
            FExpression intersect;
            if (o1.getProductConstraint() == null || o1.getProductConstraint().isTrue()) {
                intersect = o2.getProductConstraint() == null ? FExpression.trueValue() : o2.getProductConstraint();
            } else if (o2.getProductConstraint() == null) {
                intersect = o1.getProductConstraint();
            } else {
                intersect = o1.getProductConstraint().and(o2.getProductConstraint()).applySimplification();
            }

            FExpression union;
            if (o1.getProductConstraint() == null || o1.getProductConstraint().isTrue()) {
                union = o2.getProductConstraint() == null ? FExpression.trueValue() : o2.getProductConstraint();
            } else if (o2.getProductConstraint() == null) {
                union = o1.getProductConstraint();
            } else {
                union = o1.getProductConstraint().or(o2.getProductConstraint()).applySimplification();
            }

            ConstraintIdentifier id = solver.addConstraint(intersect);
            double intersectionCount = solver.getNumberOfSolutions();
            solver.removeConstraint(id);

            id = solver.addConstraint(union);
            double unionCount = solver.getNumberOfSolutions();
            solver.removeConstraint(id);

            checkState(unionCount >= intersectionCount, "Union (%s) has to be higher or equal to intersection (%s) !", unionCount, intersectionCount);

            return 1 - (intersectionCount / unionCount);
        } catch (SolverInitializationException e) {
            logger.error("Error while coputing product sets for dissimilarity!", e);
            throw new DissimilarityComputationException("Solver error to compute products", e);
        } catch (SolverFatalErrorException e) {
            logger.error("Error while coputing product sets for dissimilarity!", e);
            throw new DissimilarityComputationException("Solver error to compute products", e);
        } catch (ConstraintNotFoundException e) {
            logger.error("Error while coputing product sets for dissimilarity!", e);
            throw new DissimilarityComputationException("Solver error to compute products", e);
        } catch (ConstraintSolvingException e) {
            logger.error("Error while coputing product sets for dissimilarity!", e);
            throw new DissimilarityComputationException("Solver error to compute products", e);
        }
    }
}
