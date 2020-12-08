package be.vibes.selection.ec;

import be.vibes.selection.ec.objective.Objective;
import be.vibes.selection.exception.TestCaseSelectionException;
import be.vibes.selection.random.RandomSelection;
import be.vibes.ts.TestCase;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.exception.TransitionSystenExecutionException;
import be.vibes.ts.execution.Execution;
import be.vibes.ts.execution.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uma.jmetal.problem.Problem;

import java.util.ArrayList;
import java.util.List;

public class TestSuiteGeneration<T extends TransitionSystem> implements Problem<TestSuiteSolution> {

    private static final Logger LOG = LoggerFactory.getLogger(TestSuiteGeneration.class);

    private final RandomSelection<T> selection;
    private final Executor<T> executor;
    private final List<Objective> objectives;
    private final int testSuiteSize;
    private final String name;

    public TestSuiteGeneration(int testSuiteSize, RandomSelection<T> selection, Executor<T> executor) {
        this(testSuiteSize, selection, executor, "test suite generation");
    }

    public TestSuiteGeneration(int testSuiteSize, RandomSelection<T> selection, Executor<T> executor, String name) {
        this.testSuiteSize = testSuiteSize;
        this.selection = selection;
        this.executor = executor;
        this.name = name;
        this.objectives = new ArrayList<>();
    }

    @Override
    public int getNumberOfVariables() {
        return this.testSuiteSize;
    }

    @Override
    public int getNumberOfObjectives() {
        return this.objectives.size();
    }

    @Override
    public int getNumberOfConstraints() {
        return 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void evaluate(TestSuiteSolution solution) {
        List<Execution> executions = new ArrayList<>();
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            TestCase testCase = solution.getVariable(i);
            try {
                this.executor.execute(testCase);
                this.executor.getCurrentExecutions().forEachRemaining((exec) -> {
                    executions.add(exec);
                });
                this.executor.reset();
            } catch (TransitionSystenExecutionException e) {
                LOG.error("Error while executing the generated tests!", e);
            }
        }
        for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
            solution.setObjective(i, this.objectives.get(i).evaluate(executions));
        }
    }

    @Override
    public TestSuiteSolution createSolution() {
        TestSuiteSolution solution = new TestSuiteSolution(getNumberOfVariables(), getNumberOfObjectives());
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            try {
                solution.setVariable(i, selection.select());
            } catch (TestCaseSelectionException e) {
                LOG.error("Error while selecting test case during solution initialization!", e);
                throw new IllegalStateException("Exception while initializing solution: could not select test case!", e);
            }
        }
        return solution;
    }

    public void addObjective(Objective objective){
        this.objectives.add(objective);
    }
}
