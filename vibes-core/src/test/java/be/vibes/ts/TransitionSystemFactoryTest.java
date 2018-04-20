
package be.vibes.ts;

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
public class TransitionSystemFactoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(TransitionSystemFactoryTest.class);

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
    public void testNew() {
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        TransitionSystem ts = factory.build();
        assertThat(ts.getStatesCount(), equalTo(1));
        assertThat(ts.getState("s0"), notNullValue());
        assertThat(ts.getTransitionsCount(), equalTo(0));
        assertThat(ts.getActionsCount(), equalTo(0));
        assertThat(ts.getPropositionsCount(), equalTo(0));
    }

    @Test
    public void testAddState() {
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addState("s1");
        TransitionSystem ts = factory.build();
        assertThat(ts.getStatesCount(), equalTo(2));
        assertThat(ts.getState("s0"), notNullValue());
        assertThat(ts.getState("s0").getName(), equalTo("s0"));
        assertThat(ts.getState("s1"), notNullValue());
        assertThat(ts.getState("s1").getName(), equalTo("s1"));
        assertThat(ts.getTransitionsCount(), equalTo(0));
        assertThat(ts.getActionsCount(), equalTo(0));
        assertThat(ts.getPropositionsCount(), equalTo(0));
    }

    @Test
    public void testLabelState() {
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addState("s1");
        factory.addProposition("p1");
        factory.labelState("s1", "p1");
        TransitionSystem ts = factory.build();
        assertThat(ts.getStatesCount(), equalTo(2));
        assertThat(ts.getState("s0"), notNullValue());
        assertThat(ts.getState("s0").getName(), equalTo("s0"));
        State s1 = ts.getState("s1");
        assertThat(s1, notNullValue());
        assertThat(s1.getName(), equalTo("s1"));
        assertThat(ts.getTransitionsCount(), equalTo(0));
        assertThat(ts.getActionsCount(), equalTo(0));
        // Check propositions
        assertThat(ts.getPropositionsCount(), equalTo(1));
        AtomicProposition p1 = ts.getAtomicProposition("p1");
        assertThat(ts.getLabel(s1), equalTo(p1));
    }

    @Test
    public void testAddAction() {
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addAction("a1");
        TransitionSystem ts = factory.build();
        assertThat(ts.getStatesCount(), equalTo(1));
        assertThat(ts.getState("s0"), notNullValue());
        assertThat(ts.getState("s0").getName(), equalTo("s0"));
        assertThat(ts.getTransitionsCount(), equalTo(0));
        assertThat(ts.getActionsCount(), equalTo(1));
        assertThat(ts.getAction("a1"), notNullValue());
        assertThat(ts.getPropositionsCount(), equalTo(0));
    }

    @Test
    public void testAddTransition() {
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addState("s1");
        factory.addAction("a1");
        factory.addTransition("s0", "a1", "s1");
        TransitionSystem ts = factory.build();
        assertThat(ts.getStatesCount(), equalTo(2));
        assertThat(ts.getState("s0"), notNullValue());
        assertThat(ts.getState("s0").getName(), equalTo("s0"));
        assertThat(ts.getState("s1"), notNullValue());
        assertThat(ts.getState("s1").getName(), equalTo("s1"));
        assertThat(ts.getTransitionsCount(), equalTo(1));
        assertThat(ts.getTransitions(ts.getState("s0"), ts.getAction("a1"), ts.getState("s1")), notNullValue());
        assertThat(ts.getActionsCount(), equalTo(1));
        assertThat(ts.getAction("a1"), notNullValue());
        assertThat(ts.getAction("a1").getName(), equalTo("a1"));
        assertThat(ts.getPropositionsCount(), equalTo(0));
    }

    

}