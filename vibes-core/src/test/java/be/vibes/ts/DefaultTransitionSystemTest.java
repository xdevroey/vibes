package be.vibes.ts;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTransitionSystemTest {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTransitionSystemTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOG.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };
	
    // States testing
    @Test
    public void testAddState() {
        DefaultTransitionSystem ts = new DefaultTransitionSystem("initial");
        State s = ts.addState("initial");
        assertThat(s, notNullValue());
        assertThat("State name invalid", s.getName(), equalTo("initial"));
        assertThat("New state should not have incoming transitions", Iterators.size(ts.getIncoming(s)), equalTo(0));
        assertThat("New state should not have outgoing transitions", Iterators.size(ts.getOutgoing(s)), equalTo(0));
        State s2 = ts.getState("initial");
        assertThat(s, sameInstance(s2));
    }

    @Test
    public void testAddStateExisting() {
        DefaultTransitionSystem ts = new DefaultTransitionSystem("initial");
        State s = ts.addState("initial");
        assertThat(s, notNullValue());
        State s2 = ts.addState("initial");
        assertThat(s, sameInstance(s2));
    }

    @Test
    public void testSetIntiialState() {
        DefaultTransitionSystem ts = new DefaultTransitionSystem("initial");
        State s = ts.addState("initial");
        assertThat(s, notNullValue());
        State s2 = ts.getInitialState();
        assertThat(s, sameInstance(s2));
    }

    // Actions testing 
    @Test
    public void testAddAction() {
        DefaultTransitionSystem ts = new DefaultTransitionSystem("initial");
        Action act = ts.addAction("act");
        assertThat(act, notNullValue());
        Action act2 = ts.getAction("act");
        assertThat(act, sameInstance(act2));
    }

    @Test
    public void testAddActionExisting() {
        DefaultTransitionSystem ts = new DefaultTransitionSystem("initial");
        Action act = ts.addAction("act");
        assertThat(act, notNullValue());
        Action act2 = ts.addAction("act");
        assertThat(act, sameInstance(act2));
    }

    // Transitions testing
    @Test
    public void testAddTranstition() {
        DefaultTransitionSystem ts = new DefaultTransitionSystem("s0");
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        Action act = ts.addAction("ts");
        Transition t1 = ts.addTransition(s0, act, s1);
        assertThat(t1.getAction(), equalTo(act));
        assertThat(t1.getSource(), equalTo(s0));
        assertThat(t1.getTarget(), equalTo(s1));
        
        assertThat(ts.getIncomingCount(s0), equalTo(0));
        assertThat(ts.getOutgoingCount(s0), equalTo(1));
        
        assertThat(ts.getIncomingCount(s1), equalTo(1));
        assertThat(ts.getOutgoingCount(s1), equalTo(0));
        
        assertThat(Lists.newArrayList(ts.getOutgoing(s0)), contains(t1));
        assertThat(Lists.newArrayList(ts.getIncoming(s1)), contains(t1));
    }

    @Test
    public void testAddTranstitionExisting() {
        DefaultTransitionSystem ts = new DefaultTransitionSystem("s0");
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        Action act = ts.addAction("ts");
        Transition t1 = ts.addTransition(s0, act, s1);
        assertThat(Lists.newArrayList(ts.getOutgoing(s0)), contains(t1));
        assertThat(Lists.newArrayList(ts.getIncoming(s1)), contains(t1));
        Transition t2 = ts.addTransition(s0, act, s1);
        assertThat(ts.getOutgoingCount(s0), equalTo(1));
        assertThat(ts.getIncomingCount(s1), equalTo(1));
        assertThat(Lists.newArrayList(ts.getOutgoing(s0)), contains(t2));
        assertThat(Lists.newArrayList(ts.getIncoming(s1)), contains(t2));
    }

    @Test
    public void testAddTranstitionMultiples() {
        DefaultTransitionSystem ts = new DefaultTransitionSystem("s0");
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        Action act = ts.addAction("ts");
        Transition t1 = ts.addTransition(s0, act, s1);
        Transition t2 = ts.addTransition(s1, act, s1);
        Transition t3 = ts.addTransition(s1, act, s0);

        assertThat(ts.getIncomingCount(s0), equalTo(1));
        assertThat(ts.getOutgoingCount(s0), equalTo(1));
        
        assertThat(Lists.newArrayList(ts.getIncoming(s0)), contains(t3));
        assertThat(Lists.newArrayList(ts.getOutgoing(s0)), contains(t1));

        assertThat(ts.getIncomingCount(s1), equalTo(2));
        assertThat(ts.getOutgoingCount(s1), equalTo(2));
        
        assertThat(Lists.newArrayList(ts.getIncoming(s1)), containsInAnyOrder(t1, t2));
        assertThat(Lists.newArrayList(ts.getOutgoing(s1)), containsInAnyOrder(t2, t3));
    }

}
