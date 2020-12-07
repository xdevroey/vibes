package be.vibes.selection.ec;

import be.vibes.ts.TestCase;
import org.uma.jmetal.solution.AbstractSolution;

import java.util.HashMap;

public class TestSuiteSolution extends AbstractSolution<TestCase> {

    /**
     * Constructor
     *
     * @param numberOfTestCases Number of test cases in the final test suite.
     * @param numberOfObjectives
     */
    public TestSuiteSolution(int numberOfTestCases, int numberOfObjectives) {
        super(numberOfTestCases, numberOfObjectives);
    }

    /**
     * Copy constructor
     * @param solution The solution to copy.
     */
    public TestSuiteSolution(TestSuiteSolution solution) {
        this(solution.getNumberOfVariables(), solution.getNumberOfObjectives());
        for (int i = 0; i < getNumberOfObjectives(); i++) {
            setObjective(i, solution.getObjective(i));
        }
        for (int i = 0; i < getNumberOfVariables(); i++) {
            setVariable(i, solution.getVariable(i));
        }
        for (int i = 0; i < getNumberOfConstraints(); i++) {
            setConstraint(i, solution.getConstraint(i));
        }
        this.attributes = new HashMap<Object, Object>(solution.attributes);
    }

    @Override
    public TestSuiteSolution copy() {
        return new TestSuiteSolution(this);
    }
}
