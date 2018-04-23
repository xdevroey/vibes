package be.vibes.ts;

import be.vibes.ts.exception.TransitionSystenExecutionException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TransitionSystemExecutorTest {

    private static final Logger LOG = LoggerFactory.getLogger(TransitionSystemExecutorTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOG.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

    @Test
    public void testCanExecute() throws Exception {
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addStates("s1", "s2", "s3");
        factory.addActions("a1", "a2", "a0");
        factory.addTransition("s0", "a1", "s1");
        factory.addTransition("s1", "a2", "s2");
        factory.addTransition("s1", "a0", "s0");
        factory.addTransition("s2", "a2", "s3");
        factory.addTransition("s3", "a0", "s0");
        TransitionSystem ts = factory.build();
        TransitionSystemExecutor exec = new TransitionSystemExecutor(ts);
        assertThat(exec.canExecute("a0"), equalTo(false));
        assertThat(exec.canExecute("a1"), equalTo(true));
        assertThat(exec.canExecute("a2"), equalTo(false));
        exec.execute("a1");
        assertThat(exec.canExecute("a0"), equalTo(true));
        assertThat(exec.canExecute("a1"), equalTo(false));
        assertThat(exec.canExecute("a2"), equalTo(true));
        exec.execute("a2");
        assertThat(exec.canExecute("a0"), equalTo(false));
        assertThat(exec.canExecute("a1"), equalTo(false));
        assertThat(exec.canExecute("a2"), equalTo(true));
    }

    @Test
    public void testExecute() throws Exception {
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addStates("s1", "s2", "s3");
        factory.addActions("a1", "a2", "a0");
        factory.addTransition("s0", "a1", "s1");
        factory.addTransition("s1", "a2", "s2");
        factory.addTransition("s1", "a2", "s0");
        factory.addTransition("s2", "a2", "s3");
        factory.addTransition("s3", "a0", "s0");
        TransitionSystem ts = factory.build();
        TransitionSystemExecutor exec = new TransitionSystemExecutor(ts);
        List<Execution> current = Lists.newArrayList(exec.getCurrentExecutions());
        assertThat(current, empty());
        exec.execute("a1");
        current = Lists.newArrayList(exec.getCurrentExecutions());
        assertThat(current, hasSize(1));
        assertThat(current.get(0).getSize(), equalTo(1));
        assertThat(current.get(0).getFirst().getSource(), equalTo(ts.getState("s0")));
        assertThat(current.get(0).getFirst().getTarget(), equalTo(ts.getState("s1")));
        assertThat(current.get(0).getFirst().getAction(), equalTo(ts.getAction("a1")));
        exec.execute("a2");
        current = Lists.newArrayList(exec.getCurrentExecutions());
        assertThat(current, hasSize(2));
        assertThat(current.get(0).getFirst(), equalTo(current.get(1).getFirst()));
        assertThat(Sets.newHashSet(current.get(0).getLast(), current.get(1).getLast()),
                containsInAnyOrder(ts.getTransitions(ts.getState("s1"), ts.getAction("a2"), ts.getState("s0")).next(),
                        ts.getTransitions(ts.getState("s1"), ts.getAction("a2"), ts.getState("s2")).next()));
    }

    @Test(expected = TransitionSystenExecutionException.class)
    public void testExecuteWithNoInputEnabled() throws Exception {
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addStates("s1", "s2", "s3");
        factory.addActions("a1", "a2", "a0");
        factory.addTransition("s0", "a1", "s1");
        factory.addTransition("s1", "a2", "s2");
        factory.addTransition("s1", "a0", "s0");
        factory.addTransition("s2", "a2", "s3");
        factory.addTransition("s3", "a0", "s0");
        TransitionSystem ts = factory.build();
        TransitionSystemExecutor exec = new TransitionSystemExecutor(ts);
        exec.execute("a1");
        assertThat(exec.canExecute("a1"), equalTo(false));
        exec.execute("a1");
    }
    

}
