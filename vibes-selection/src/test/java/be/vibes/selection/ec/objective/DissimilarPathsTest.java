package be.vibes.selection.ec.objective;

import be.vibes.selection.dissimilar.HammingDissimilarity;
import be.vibes.selection.dissimilar.JaccardDissimilarity;
import be.vibes.selection.dissimilar.LevenshteinDissimilarity;
import be.vibes.ts.State;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.execution.Execution;
import com.google.common.collect.Sets;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DissimilarPathsTest {

    private static final Logger LOG = LoggerFactory.getLogger(DissimilarPathsTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOG.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    };

    @Test
    public void testEvaluateEmptyExecutions() {
        DissimilarPaths diss = new DissimilarPaths(new LevenshteinDissimilarity<>());
        List<Execution> executions = new ArrayList<>();
        double objValue = diss.evaluate(executions);
        assertThat(objValue, closeTo(0.0, 0.00001));
    }

    @Test
    public void testEvaluateOneExecution() throws Exception {
        TransitionSystem ts = TestUtils.getTransitionSystem();
        // Build executions
        List<Execution> executions = new ArrayList<>();
        Execution exec = new Execution()
                .enqueue(ts.getTransitions("s0", "a1", "s1").next())
                .enqueue(ts.getTransitions("s1", "a2", "s2").next())
                .enqueue(ts.getTransitions("s2", "a2", "s3").next())
                .enqueue(ts.getTransitions("s3", "a0", "s0").next());
        executions.add(exec);
        DissimilarPaths diss = new DissimilarPaths(new LevenshteinDissimilarity<>());
        double objValue = diss.evaluate(executions);
        assertThat(objValue, closeTo(0.0, 0.00001));
    }

    @Test
    public void testEvaluateSameExecutions() throws Exception {
        TransitionSystem ts = TestUtils.getTransitionSystem();
        // Build executions
        List<Execution> executions = new ArrayList<>();
        Execution exec = new Execution()
                .enqueue(ts.getTransitions("s0", "a1", "s1").next())
                .enqueue(ts.getTransitions("s1", "a2", "s2").next())
                .enqueue(ts.getTransitions("s2", "a2", "s3").next())
                .enqueue(ts.getTransitions("s3", "a0", "s0").next());
        executions.add(exec);
        exec = new Execution()
                .enqueue(ts.getTransitions("s0", "a1", "s1").next())
                .enqueue(ts.getTransitions("s1", "a2", "s2").next())
                .enqueue(ts.getTransitions("s2", "a2", "s3").next())
                .enqueue(ts.getTransitions("s3", "a0", "s0").next());
        executions.add(exec);
        DissimilarPaths diss = new DissimilarPaths(new LevenshteinDissimilarity<>());
        double objValue = diss.evaluate(executions);
        assertThat(objValue, closeTo(1.0, 0.00001));
    }

    @Test
    public void testEvaluateDifferentExecutions() throws Exception {
        TransitionSystem ts = TestUtils.getTransitionSystem();
        // Build executions
        List<Execution> executions = new ArrayList<>();
        Execution exec = new Execution()
                .enqueue(ts.getTransitions("s0", "a1", "s1").next())
                .enqueue(ts.getTransitions("s1", "a2", "s2").next())
                .enqueue(ts.getTransitions("s2", "a2", "s3").next())
                .enqueue(ts.getTransitions("s3", "a0", "s0").next());
        executions.add(exec);
        exec = new Execution()
                .enqueue(ts.getTransitions("s0", "a1", "s1").next())
                .enqueue(ts.getTransitions("s1", "a2", "s2").next())
                .enqueue(ts.getTransitions("s2", "a2", "s3").next())
                .enqueue(ts.getTransitions("s3", "a0", "s0").next());
        executions.add(exec);
        exec = new Execution()
                .enqueue(ts.getTransitions("s0", "a1", "s1").next())
                .enqueue(ts.getTransitions("s1", "a4", "s3").next())
                .enqueue(ts.getTransitions("s3", "a0", "s0").next());
        executions.add(exec);
        DissimilarPaths diss = new DissimilarPaths(new LevenshteinDissimilarity<>());
        double objValue = diss.evaluate(executions);
        double expected = (1.0 + 0.5 + 0.5) / 3.0;
        assertThat(objValue, closeTo(expected, 0.00001));
    }

}