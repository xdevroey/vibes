package be.vibes.selection.dissimilar;

import java.util.List;

import be.vibes.selection.exception.DissimilarityComputationException;
import be.vibes.ts.TestCase;

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
