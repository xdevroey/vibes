package be.vibes.ts.coverage;

import be.vibes.ts.TestCase;
import be.vibes.ts.TestSet;
import be.vibes.ts.exception.CoverageComputationException;
import be.vibes.ts.execution.Execution;

import java.util.Iterator;

/**
 * A coverage criteria gives, for a test or a set of tests, a value between 0 (denoting no coverage)
 * and 1 (denoting full coverage) denoting the coverage percentage.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public interface CoverageCriteria {

    /**
     * Returns the coverage for the given test case as a value between 0 (denoting no coverage)
     * and 1 (denoting full coverage).
     *
     * @param test The test to evaluate.
     * @return A value between 0 and 1.
     * @throws CoverageComputationException If an error occurs during the computation.
     */
    public double coverage(TestCase test) throws CoverageComputationException;

    /**
     * Returns the coverage for the given set of test cases as a value between 0 (denoting no coverage)
     * and 1 (denoting full coverage).
     *
     * @param suite The set of tests to evaluate.
     * @return A value between 0 and 1.
     * @throws CoverageComputationException If an error occurs during the computation.
     */
    public double coverage(TestSet suite) throws CoverageComputationException;

    /**
     * Returns the coverage for the given execution as a value between 0 (denoting no coverage)
     * and 1 (denoting full coverage). The execution must have been performed on the transition system for which
     * the coverage criteria is defined.
     *
     * @param execution The execution to evaluate.
     * @return A value between 0 and 1.
     * @throws CoverageComputationException If an error occurs during the computation.
     */
    public double coverage(Execution execution) throws CoverageComputationException;

    /**
     * Returns the coverage for the given executions as a value between 0 (denoting no coverage)
     * and 1 (denoting full coverage). The executions must have been performed on the transition system for which
     * the coverage criteria is defined.
     *
     * @param execution The execution to evaluate.
     * @return A value between 0 and 1.
     * @throws CoverageComputationException If an error occurs during the computation.
     */
    public double coverage(Iterator<Execution> execution) throws CoverageComputationException;

}
