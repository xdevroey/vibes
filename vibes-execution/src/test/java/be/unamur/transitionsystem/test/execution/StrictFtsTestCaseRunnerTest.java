package be.unamur.transitionsystem.test.execution;

import static org.junit.Assert.*;
import static be.unamur.transitionsystem.dsl.FeaturedTransitionSystemBuilder.*;

import java.io.File;
import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.fexpression.DimacsModel;
import be.unamur.fts.solver.BDDSolverFacade;
import be.unamur.fts.solver.ConstraintIdentifier;
import be.unamur.fts.solver.SolverFacade;
import be.unamur.fts.solver.SolverFatalErrorException;
import be.unamur.fts.solver.exception.ConstraintNotFoundException;
import be.unamur.fts.solver.exception.ConstraintSolvingException;
import be.unamur.fts.solver.exception.SolverInitializationException;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.ExecutionNode;
import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.FtsMutableTestCase;
import be.unamur.transitionsystem.test.TestCase;

@SuppressWarnings("deprecation")
public class StrictFtsTestCaseRunnerTest {

    private static final Logger logger = LoggerFactory
            .getLogger(StrictFtsTestCaseRunnerTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	@Test
    public void testSimple() throws Exception {
        FeaturedTransitionSystem fts = define().init("s0").from("s0").action("act1")
                .to("s1").from("s1").action("act").to("s2").from("s1").action("act2")
                .to("s2").from("s1").action("act").to("s3").from("s3").action("act2")
                .to("s2").from("s2").action("act0").to("s0").build();

        TestCase tc = FtsMutableTestCase.FACTORY.buildTestCase();
        tc = tc.enqueue(new Action("act1")).enqueue(new Action("act"))
                .enqueue(new Action("act0"));

        StrictFtsTestCaseRunner runner = new StrictFtsTestCaseRunner(new SolverFacade() {
            @Override
            public void reset() throws SolverInitializationException {
            }

            @Override
            public void removeConstraint(ConstraintIdentifier id)
                    throws SolverFatalErrorException, ConstraintNotFoundException {
            }

            @Override
            public boolean isSatisfiable() throws ConstraintSolvingException {
                return true;
            }

            @SuppressWarnings("rawtypes")
            @Override
            public Iterator getSolutions() throws ConstraintSolvingException {
                return null;
            }

            @Override
            public ConstraintIdentifier addConstraint(FExpression constraint)
                    throws SolverInitializationException, SolverFatalErrorException {
                return null;
            }

            @Override
            public double getNumberOfSolutions() throws ConstraintSolvingException {
                return 0;
            }
        });
        ExecutionTree<FeaturedTransition> exec = runner.run(fts,tc);
        logger.debug("Execution tree : {}", exec);
        assertNotNull(exec);
        ExecutionNode<FeaturedTransition> node = exec.getRoot().sons().next();
        for (String exp : new String[]{"act1", "act", "act0"}) {
            logger.debug("Execution node : {}", node);
            assertNotNull(node);
            assertEquals(exp, node.getValue().getAction().getName());
            if (node.sons().hasNext()) {
                assertEquals("Should have 1 son", 1, node.numberOfSons());
                node = node.sons().next();
            }
        }
        assertFalse(node.sons().hasNext());
    }

    @Test
    public void testWithFExpressions() throws Exception {
        FeaturedTransitionSystem fts = define().init("s0")
                .from("s0").action("act1").fexpression("FreeDrinks").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act0").fexpression("!FreeDrinks").to("s0")
                .from("s2").action("act0").to("s0")
                .build();

        TestCase tc = FtsMutableTestCase.FACTORY.buildTestCase();
        tc = tc.enqueue(new Action("act1")).enqueue(new Action("act")).enqueue(new Action("act0"));

        File dimacsModel = new File(StrictFtsTestCaseRunnerTest.class.getClassLoader().getResource("svm.splot.dimacs").toURI());
        assertTrue("Test file not found: vending-machine.dimacs!", dimacsModel.exists());
        DimacsModel dimacs = DimacsModel.createFromDimacsFile(dimacsModel);
        SolverFacade solver = new BDDSolverFacade(dimacs);
        assertTrue("Model should be SAT!", solver.isSatisfiable());

        StrictFtsTestCaseRunner runner = new StrictFtsTestCaseRunner(solver);
        ExecutionTree<FeaturedTransition> exec = runner.run(fts,tc);
        logger.debug("Execution tree : {}", exec);
        assertNotNull(exec);
        ExecutionNode<FeaturedTransition> node = exec.getRoot().sons().next();
        for (String exp : new String[]{"act1", "act", "act0"}) {
            logger.debug("Execution node : {}", node);
            assertNotNull(node);
            assertEquals(exp, node.getValue().getAction().getName());
            if (node.sons().hasNext()) {
                assertEquals("Should have 1 son", 1, node.numberOfSons());
                node = node.sons().next();
            }
        }
        assertFalse(node.sons().hasNext());
    }

    @Test
    public void testWithFExpressionsExclusive() throws Exception {
        FeaturedTransitionSystem fts = define().init("s0")
                .from("s0").action("act1").fexpression("FreeDrinks").to("s1")
                .from("s0").action("act1").fexpression("!FreeDrinks").to("s3")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act").to("s3")
                .from("s3").action("act0").fexpression("!FreeDrinks").to("s0")
                .from("s2").action("act0").fexpression("FreeDrinks").to("s0")
                .build();

        TestCase tc = FtsMutableTestCase.FACTORY.buildTestCase();
        tc = tc.enqueue(new Action("act1")).enqueue(new Action("act")).enqueue(new Action("act0"));

        File dimacsModel = new File(StrictFtsTestCaseRunnerTest.class.getClassLoader().getResource("svm.splot.dimacs").toURI());
        assertTrue("Test file not found: vending-machine.dimacs!", dimacsModel.exists());
        DimacsModel dimacs = DimacsModel.createFromDimacsFile(dimacsModel);
        SolverFacade solver = new BDDSolverFacade(dimacs);
        assertTrue("Model should be SAT!", solver.isSatisfiable());

        StrictFtsTestCaseRunner runner = new StrictFtsTestCaseRunner(solver);
        ExecutionTree<FeaturedTransition> exec = runner.run(fts,tc);
        logger.debug("Execution tree : {}", exec);
        assertNotNull(exec);
        assertEquals("Wrong number of sons in root node!", 2, exec.getRoot().numberOfSons());
        Iterator<ExecutionNode<FeaturedTransition>> it = exec.getRoot().sons();
        ExecutionNode<FeaturedTransition> node = it.next();
        for (String exp : new String[]{"act1", "act", "act0"}) {
            logger.debug("Execution node : {}", node);
            assertNotNull(node);
            assertEquals(exp, node.getValue().getAction().getName());
            if (node.sons().hasNext()) {
                assertEquals("Should have 1 son", 1, node.numberOfSons());
                node = node.sons().next();
            }
        }
        node = it.next();
        for (String exp : new String[]{"act1", "act", "act0"}) {
            logger.debug("Execution node : {}", node);
            assertNotNull(node);
            assertEquals(exp, node.getValue().getAction().getName());
            if (node.sons().hasNext()) {
                assertEquals("Should have 1 son", 1, node.numberOfSons());
                node = node.sons().next();
            }
        }
        assertFalse(it.hasNext());
    }

}
