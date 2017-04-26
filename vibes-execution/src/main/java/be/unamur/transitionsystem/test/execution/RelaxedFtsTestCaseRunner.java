package be.unamur.transitionsystem.test.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.fts.solver.SolverFacade;
import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.selection.fts.FtsTestCaseValidator;

public class RelaxedFtsTestCaseRunner implements
        TestCaseRunner<FeaturedTransitionSystem> {

    private static final Logger logger = LoggerFactory
            .getLogger(RelaxedFtsTestCaseRunner.class);

    private FtsTestCaseValidator validator;

    public RelaxedFtsTestCaseRunner(SolverFacade solver) {
        validator = new FtsTestCaseValidator(solver);
    }

    @Override
    public ExecutionTree<FeaturedTransition> run(FeaturedTransitionSystem ts, TestCase testCase) {
        ExecutionTree<FeaturedTransition> execTree = new ExecutionTree();
        RelaxedFtsTestCaseVisitor visitor = new RelaxedFtsTestCaseVisitor(testCase,
                execTree.getRoot(), validator);
        try {
            ts.getInitialState().accept(visitor);
        } catch (VisitException e) {
            logger.error("Error while executing test case!", e);
            throw new IllegalStateException(
                    "Error while executing test case, this should not happen but ...", e);
        }
        return execTree;
    }

}
