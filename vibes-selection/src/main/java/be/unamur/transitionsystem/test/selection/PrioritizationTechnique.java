package be.unamur.transitionsystem.test.selection;

import java.util.List;

import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.selection.exception.DissimilarityComputationException;

public interface PrioritizationTechnique {

    /**
     * Returns a prioritized (fresh list) of the given list of test-cases.
     *
     * @param testCases The list of test cases to prioritize.
     * @return A prioritized (fresh list) of the given list of test-cases.
     * @throws DissimilarityComputationException
     */
    public List<TestCase> prioritize(List<TestCase> testCases) throws DissimilarityComputationException;

    /**
     * Returns the fitness value for the last prioritized test-cases.
     * @return The fitness value for the last prioritized test-cases.
     */
    public double getFitness();

}
