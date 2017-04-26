package be.unamur.transitionsystem.test.execution;

import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.define;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.ExecutionNode;
import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.TestCase;

@SuppressWarnings("deprecation")
public class RelaxedUnfinishedTestCaseVisitorTest {

    private static final Logger logger = LoggerFactory.getLogger(RelaxedUnfinishedTestCaseVisitorTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	@Test
    public void testSimpleTestCase() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
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

        ExecutionTree<Transition> exec = new ExecutionTree<Transition>();
        RelaxedUnfinishedTestCaseVisitor visitor = new RelaxedUnfinishedTestCaseVisitor(tc, exec.getRoot());
        assertTrue(lts.getInitialState().accept(visitor));
        logger.debug("Execution tree : {}", exec);
        assertNotNull(exec);
        ExecutionNode<Transition> node = exec.getRoot().sons().next();
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
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();

        TestCase tc = LtsMutableTestCase.FACTORY.buildTestCase();
        tc = tc.enqueue(new Action("act1"))
                .enqueue(new Action("act"))
                .enqueue(new Action("act0"));

        ExecutionTree<Transition> exec = new ExecutionTree<Transition>();
        RelaxedUnfinishedTestCaseVisitor visitor = new RelaxedUnfinishedTestCaseVisitor(tc, exec.getRoot());
        assertTrue(lts.getInitialState().accept(visitor));
        logger.debug("Execution tree : {}", exec);
        assertNotNull(exec);
        ExecutionNode<Transition> node = exec.getRoot().sons().next();
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
        LabelledTransitionSystem lts = define().init("s0")
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

        ExecutionTree<Transition> exec = new ExecutionTree<Transition>();
        RelaxedUnfinishedTestCaseVisitor visitor = new RelaxedUnfinishedTestCaseVisitor(tc, exec.getRoot());
        assertTrue(lts.getInitialState().accept(visitor));
        logger.debug("Execution tree : {}", exec);
        assertNotNull(exec);
        ExecutionNode<Transition> node = exec.getRoot().sons().next();
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

    @Test
    public void testPathWithNoActionAndSmallCycle() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("actErr").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").to("s3")
                .from("s3").to("s1")
                .from("s3").action("act").to("s2")
                .from("s2").action("act0").to("s0")
                .build();

        TestCase tc = LtsMutableTestCase.FACTORY.buildTestCase();
        tc = tc.enqueue(new Action("act1"))
                .enqueue(new Action("act"))
                .enqueue(new Action("act0"));

        ExecutionTree<Transition> exec = new ExecutionTree<Transition>();
        RelaxedUnfinishedTestCaseVisitor visitor = new RelaxedUnfinishedTestCaseVisitor(tc, exec.getRoot());
        assertTrue(lts.getInitialState().accept(visitor));
        logger.debug("Execution tree : {}", exec);
        assertNotNull(exec);
        ExecutionNode<Transition> node = exec.getRoot().sons().next();
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

    @Test
    public void testPathWithNoActionAndLargeCycle() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("actErr").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").to("s3")
                .from("s3").to("s4")
                .from("s4").to("s5")
                .from("s5").to("s1")
                .from("s3").action("act").to("s2")
                .from("s2").action("act0").to("s0")
                .build();

        TestCase tc = LtsMutableTestCase.FACTORY.buildTestCase();
        tc = tc.enqueue(new Action("act1"))
                .enqueue(new Action("act"))
                .enqueue(new Action("act0"));

        ExecutionTree<Transition> exec = new ExecutionTree<Transition>();
        RelaxedUnfinishedTestCaseVisitor visitor = new RelaxedUnfinishedTestCaseVisitor(tc, exec.getRoot());
        assertTrue(lts.getInitialState().accept(visitor));
        logger.debug("Execution tree : {}", exec);
        assertNotNull(exec);
        ExecutionNode<Transition> node = exec.getRoot().sons().next();
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

    @Test
    public void testPathWithNoActionAndLargeCycleNoEnd() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("actErr").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").to("s3")
                .from("s3").to("s4")
                .from("s4").to("s5")
                .from("s5").to("s3")
                .from("s3").action("act").to("s2")
                .from("s2").action("act0").to("s0")
                .build();

        TestCase tc = LtsMutableTestCase.FACTORY.buildTestCase();
        tc = tc.enqueue(new Action("act1"))
                .enqueue(new Action("act"))
                .enqueue(new Action("act0"));

        ExecutionTree<Transition> exec = new ExecutionTree<Transition>();
        RelaxedUnfinishedTestCaseVisitor visitor = new RelaxedUnfinishedTestCaseVisitor(tc, exec.getRoot());
        assertTrue(lts.getInitialState().accept(visitor));
        logger.debug("Execution tree : {}", exec);
        assertNotNull(exec);
        ExecutionNode<Transition> node = exec.getRoot().sons().next();
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
