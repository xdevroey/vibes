package be.vibes.selection.ec.operator;

import be.vibes.selection.ec.TestSuiteSolution;
import be.vibes.selection.exception.TestCaseSelectionException;
import be.vibes.selection.random.RandomSelection;
import be.vibes.ts.TransitionSystem;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * Randomly replaces one test case in the test suite by a randomly generated one.
 *
 * @param <T> The type of transition system from which test cases are selected.
 * @author Xavier Devroey
 */
public class RandomSingleMutation<T extends TransitionSystem> implements MutationOperator<TestSuiteSolution> {

    private static final Logger LOG = LoggerFactory.getLogger(RandomSingleMutation.class);

    private final RandomSelection<T> selection;
    private final double probability;

    public RandomSingleMutation(RandomSelection<T> selection, double probability) {
        Preconditions.checkArgument(probability >= 0, "Probability cannot be negative");
        this.selection = selection;
        this.probability = probability;
    }

    @Override
    public double getMutationProbability() {
        return this.probability;
    }

    /**
     * Applies the mutation on the given solution with a given probability.
     *
     * @param solution The data to process. Cannot be null.
     * @throws NullPointerException If solution is null.
     */
    @Override
    public TestSuiteSolution execute(TestSuiteSolution solution) {
        Preconditions.checkNotNull(solution);
        doMutation(this.probability, solution);
        return solution;
    }

    private void doMutation(double probability, TestSuiteSolution solution) {
        if (JMetalRandom.getInstance().nextDouble() <= probability) {
            int index = JMetalRandom.getInstance().nextInt(0, solution.getNumberOfVariables() - 1);
            try {
                solution.setVariable(index, this.selection.select());
            } catch (TestCaseSelectionException e) {
                LOG.error("Was unable to select a random test while performing ReplaceOne mutation!", e);
            }
        }
    }

}
