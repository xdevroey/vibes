package be.unamur.transitionsystem.test.execution;

import static be.unamur.transitionsystem.dsl.FeaturedTransitionSystemBuilder.*;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.fts.solver.Sat4JSolverFacade;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.ExecutionNode;
import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.TestCase;

@SuppressWarnings("deprecation")
public class RelaxedFtsTestCaseRunnerTest {

    private static final Logger logger = LoggerFactory.getLogger(RelaxedFtsTestCaseRunnerTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };
 
	
	@Test
    public void testSimpleTestCase() throws Exception {
        FeaturedTransitionSystem fts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();

        TestCase tc = LtsMutableTestCase.FACTORY.buildTestCase();
        tc = tc.enqueue(new Action("act1"))
                .enqueue(new Action("act2"))
                .enqueue(new Action("act0"));

        File dimacsModel = new File(RelaxedFtsTestCaseRunner.class.getClassLoader().getResource("vending-machine.dimacs").toURI());
        File featureMapping = new File(RelaxedFtsTestCaseRunner.class.getClassLoader().getResource("vending-machine.map").toURI());
        assertTrue("Test file not found: vending-machine.dimacs!", dimacsModel.exists());
        assertTrue("Test file not found: vending-machine.map!", featureMapping.exists());
        Sat4JSolverFacade solver = new Sat4JSolverFacade(dimacsModel, featureMapping);
        RelaxedFtsTestCaseRunner runner = new RelaxedFtsTestCaseRunner(solver);
        ExecutionTree<FeaturedTransition> exec = runner.run(fts,tc);
        logger.debug("Execution tree : {}", exec);
        assertNotNull(exec);
        ExecutionNode<FeaturedTransition> node = exec.getRoot().sons().next();
        for (String exp : new String[]{"act1", "act2", "act0"}) {
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
    public void testMultiPathOnePossibleTestCase() throws Exception {
        FeaturedTransitionSystem fts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").fexpression("!FreeDrinks").to("s2")
                .from("s1").action("act").fexpression("FreeDrinks").to("s3")
                .from("s1").action("act").to("s3")
                .from("s3").action("act0").fexpression("!FreeDrinks").to("s0")
                .from("s2").action("act0").to("s0")
                .build();

        TestCase tc = LtsMutableTestCase.FACTORY.buildTestCase();
        tc = tc.enqueue(new Action("act1"))
                .enqueue(new Action("act"))
                .enqueue(new Action("act0"));

        File dimacsModel = new File(StrictFtsTestCaseRunnerTest.class.getClassLoader().getResource("vending-machine.dimacs").toURI());
        File featureMapping = new File(StrictFtsTestCaseRunnerTest.class.getClassLoader().getResource("vending-machine.map").toURI());
        assertTrue("Test file not found: vending-machine.dimacs!", dimacsModel.exists());
        assertTrue("Test file not found: vending-machine.map!", featureMapping.exists());
        Sat4JSolverFacade solver = new Sat4JSolverFacade(dimacsModel, featureMapping);
        RelaxedFtsTestCaseRunner runner = new RelaxedFtsTestCaseRunner(solver);
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
    public void testPathWithNoAction() throws Exception {
        FeaturedTransitionSystem fts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("actErr").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").to("s3")
                .from("s3").action("act").to("s2")
                .from("s2").action("act0").to("s0")
                .build();

        TestCase tc = LtsMutableTestCase.FACTORY.buildTestCase();
        tc = tc.enqueue(new Action("act1"))
                .enqueue(new Action("act"))
                .enqueue(new Action("act0"));

        File dimacsModel = new File(StrictFtsTestCaseRunnerTest.class.getClassLoader().getResource("vending-machine.dimacs").toURI());
        File featureMapping = new File(StrictFtsTestCaseRunnerTest.class.getClassLoader().getResource("vending-machine.map").toURI());
        assertTrue("Test file not found: vending-machine.dimacs!", dimacsModel.exists());
        assertTrue("Test file not found: vending-machine.map!", featureMapping.exists());
        Sat4JSolverFacade solver = new Sat4JSolverFacade(dimacsModel, featureMapping);
        RelaxedFtsTestCaseRunner runner = new RelaxedFtsTestCaseRunner(solver);
        ExecutionTree<FeaturedTransition> exec = runner.run(fts,tc);
        logger.debug("Execution tree : {}", exec);
        assertNotNull(exec);
        ExecutionNode<FeaturedTransition> node = exec.getRoot().sons().next();
        for (String exp : new String[]{"act1", "NO_ACTION", "act", "act0"}) {
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

}
