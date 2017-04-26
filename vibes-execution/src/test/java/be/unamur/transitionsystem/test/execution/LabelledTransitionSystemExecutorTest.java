package be.unamur.transitionsystem.test.execution;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.test.execution.exception.IllegalFiredTransition;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.define;
import static org.junit.Assert.*;

/*
 * Created by xde on 8/06/15.
 */
public class LabelledTransitionSystemExecutorTest {

    private static final Logger logger = LoggerFactory.getLogger(LabelledTransitionSystemExecutorTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    };

    @Test
    public void testSimpleExecution() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        LabelledTransitionSystemExecutor exec = new LabelledTransitionSystemExecutor(lts);
        assertEquals("Wrong initial state!", lts.getInitialState(), exec.getCurrentState());
        List<Transition> nexts = exec.listNextTransitions();
        assertEquals(1, nexts.size());
        assertTrue(exec.mayFire(nexts.get(0)));
        exec.fireTransition(nexts.get(0));
        assertEquals("Wrong state!", lts.getState("s1"), exec.getCurrentState());

        nexts = exec.listNextTransitions(lts.getAction("act"));
        assertEquals(2, nexts.size());
        assertTrue(exec.mayFire(nexts.get(0)));
        assertTrue(exec.mayFire(nexts.get(1)));
        exec.fireTransition(nexts.get(1));
        assertEquals("Wrong state!", nexts.get(1).getTo(), exec.getCurrentState());

        exec.reset();
        assertEquals("Reset not done!", lts.getInitialState(), exec.getCurrentState());
    }

    @Test(expected = IllegalFiredTransition.class)
    public void testFailedExecution() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        LabelledTransitionSystemExecutor exec = new LabelledTransitionSystemExecutor(lts);
        assertEquals("Wrong initial state!", lts.getInitialState(), exec.getCurrentState());
        List<Transition> nexts = exec.listNextTransitions();
        assertEquals(1, nexts.size());
        assertTrue(exec.mayFire(nexts.get(0)));
        exec.fireTransition(nexts.get(0));
        assertEquals("Wrong state!", lts.getState("s1"), exec.getCurrentState());

        exec.fireTransition(lts.getState("s2").outgoingTransitions().next());
        fail();
    }

}
