package be.vibes.ts.coverage;

import be.vibes.ts.TestCase;
import be.vibes.ts.TestSet;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.TransitionSystemFactory;
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
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class ActionCoverageTest {

    private static final Logger LOG = LoggerFactory.getLogger(ActionCoverageTest.class);

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
    public void testCoverageTest() throws Exception{
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addStates("s1", "s2", "s3");
        factory.addActions("a1", "a2", "a0", "a3");
        factory.addTransition("s0", "a1", "s1");
        factory.addTransition("s1", "a2", "s2");
        factory.addTransition("s1", "a3", "s0");
        factory.addTransition("s2", "a2", "s3");
        factory.addTransition("s3", "a0", "s0");
        TransitionSystem ts = factory.build();
        // Build test
        TestCase test = new TestCase("foo");
        test.enqueue(ts.getTransitions("s0", "a1", "s1").next());
        test.enqueue(ts.getTransitions("s1", "a2", "s2").next());
        test.enqueue(ts.getTransitions("s2", "a2", "s3").next());
        test.enqueue(ts.getTransitions("s3", "a0", "s0").next());
        ActionCoverage coverage = new ActionCoverage(ts);
        double result = coverage.coverage(test);
        assertThat(result, closeTo(3.0/4.0, 0.00001));
    }

    @Test
    public void testFullCoverageTest() throws Exception{
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addStates("s1", "s2", "s3");
        factory.addActions("a1", "a2", "a0");
        factory.addTransition("s0", "a1", "s1");
        factory.addTransition("s1", "a2", "s2");
        factory.addTransition("s1", "a0", "s0");
        factory.addTransition("s2", "a2", "s3");
        factory.addTransition("s3", "a0", "s0");
        TransitionSystem ts = factory.build();
        // Build test
        TestCase test = new TestCase("foo");
        test.enqueue(ts.getTransitions("s0", "a1", "s1").next());
        test.enqueue(ts.getTransitions("s1", "a2", "s2").next());
        test.enqueue(ts.getTransitions("s2", "a2", "s3").next());
        test.enqueue(ts.getTransitions("s3", "a0", "s0").next());
        ActionCoverage coverage = new ActionCoverage(ts);
        double result = coverage.coverage(test);
        assertThat(result, closeTo(1.0, 0.00001));
    }

    @Test
    public void testCoverageEmptyTest() throws Exception{
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addStates("s1", "s2", "s3");
        factory.addActions("a1", "a2", "a0");
        factory.addTransition("s0", "a1", "s1");
        factory.addTransition("s1", "a2", "s2");
        factory.addTransition("s1", "a0", "s0");
        factory.addTransition("s2", "a2", "s3");
        factory.addTransition("s3", "a0", "s0");
        TransitionSystem ts = factory.build();
        // Build test
        TestCase test = new TestCase("foo");
        ActionCoverage coverage = new ActionCoverage(ts);
        double result = coverage.coverage(test);
        assertThat(result, closeTo(0.0, 0.00001));
    }

    @Test
    public void testCoverageTestSet() throws Exception{
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addStates("s1", "s2", "s3");
        factory.addActions("a1", "a2", "a0", "a3");
        factory.addTransition("s0", "a1", "s1");
        factory.addTransition("s1", "a2", "s2");
        factory.addTransition("s1", "a3", "s0");
        factory.addTransition("s2", "a2", "s3");
        factory.addTransition("s2", "a0", "s0");
        factory.addTransition("s3", "a0", "s0");
        TransitionSystem ts = factory.build();
        // Build test
        TestSet testSet = new TestSet();
        TestCase test = new TestCase("foo");
        test.enqueue(ts.getTransitions("s0", "a1", "s1").next());
        test.enqueue(ts.getTransitions("s1", "a2", "s2").next());
        test.enqueue(ts.getTransitions("s2", "a2", "s3").next());
        test.enqueue(ts.getTransitions("s3", "a0", "s0").next());
        testSet.add(test);
        test = new TestCase("foo2");
        test.enqueue(ts.getTransitions("s0", "a1", "s1").next());
        test.enqueue(ts.getTransitions("s1", "a2", "s2").next());
        test.enqueue(ts.getTransitions("s2", "a2", "s3").next());
        test.enqueue(ts.getTransitions("s3", "a0", "s0").next());
        testSet.add(test);
        test = new TestCase("foo3");
        test.enqueue(ts.getTransitions("s0", "a1", "s1").next());
        test.enqueue(ts.getTransitions("s1", "a2", "s2").next());
        test.enqueue(ts.getTransitions("s2", "a0", "s0").next());
        testSet.add(test);
        ActionCoverage coverage = new ActionCoverage(ts);
        double result = coverage.coverage(testSet);
        assertThat(result, closeTo(3.0/4.0, 0.00001));
    }

    @Test
    public void testCoverageEmptyTestSet() throws Exception{
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addStates("s1", "s2", "s3");
        factory.addActions("a1", "a2", "a0", "a3");
        factory.addTransition("s0", "a1", "s1");
        factory.addTransition("s1", "a2", "s2");
        factory.addTransition("s1", "a3", "s0");
        factory.addTransition("s2", "a2", "s3");
        factory.addTransition("s2", "a2", "s0");
        factory.addTransition("s3", "a0", "s0");
        TransitionSystem ts = factory.build();
        // Build test
        TestSet testSet = new TestSet();
        ActionCoverage coverage = new ActionCoverage(ts);
        double result = coverage.coverage(testSet);
        assertThat(result, closeTo(0.0, 0.00001));
    }


}