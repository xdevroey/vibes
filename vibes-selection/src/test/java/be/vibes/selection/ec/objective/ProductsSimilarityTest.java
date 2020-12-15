package be.vibes.selection.ec.objective;

import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.execution.Execution;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static be.vibes.selection.ec.objective.TestUtils.getFeatureModel;
import static be.vibes.selection.ec.objective.TestUtils.getFeaturedTransitionSystem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;

public class ProductsSimilarityTest {

    private static final Logger LOG = LoggerFactory.getLogger(ProductsSimilarityTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOG.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    };

    @Test
    public void ProductsSimilarity() {
        ProductsSimilarity diss = new ProductsSimilarity(getFeaturedTransitionSystem(), getFeatureModel());
        List<Execution> executions = new ArrayList<>();
        double objValue = diss.evaluate(executions);
        assertThat(objValue, closeTo(0.0, 0.00001));
    }

    @Test
    public void testEvaluateOneExecution() throws Exception {
        FeaturedTransitionSystem fts = getFeaturedTransitionSystem();
        // Build executions
        List<Execution> executions = new ArrayList<>();
        Execution exec = new Execution()
                .enqueue(fts.getTransitions("s0", "a1", "s1").next())
                .enqueue(fts.getTransitions("s1", "a2", "s2").next())
                .enqueue(fts.getTransitions("s2", "a2", "s3").next())
                .enqueue(fts.getTransitions("s3", "a0", "s0").next());
        executions.add(exec);
        ProductsSimilarity diss = new ProductsSimilarity(fts, getFeatureModel());
        double objValue = diss.evaluate(executions);
        assertThat(objValue, closeTo(0.0, 0.00001));
    }

    @Test
    public void testEvaluateSameExecutions() throws Exception {
        FeaturedTransitionSystem fts = getFeaturedTransitionSystem();
        // Build executions
        List<Execution> executions = new ArrayList<>();
        Execution exec = new Execution()
                .enqueue(fts.getTransitions("s0", "a1", "s1").next())
                .enqueue(fts.getTransitions("s1", "a2", "s2").next())
                .enqueue(fts.getTransitions("s2", "a2", "s3").next())
                .enqueue(fts.getTransitions("s3", "a0", "s0").next());
        executions.add(exec);
        exec = new Execution()
                .enqueue(fts.getTransitions("s0", "a1", "s1").next())
                .enqueue(fts.getTransitions("s1", "a2", "s2").next())
                .enqueue(fts.getTransitions("s2", "a2", "s3").next())
                .enqueue(fts.getTransitions("s3", "a0", "s0").next());
        executions.add(exec);
        ProductsSimilarity diss = new ProductsSimilarity(fts, getFeatureModel());
        double objValue = diss.evaluate(executions);
        assertThat(objValue, closeTo(0.0, 0.00001));
    }

    @Test
    public void testEvaluateDifferentExecutions() throws Exception {
        FeaturedTransitionSystem fts = getFeaturedTransitionSystem();
        // Build executions
        List<Execution> executions = new ArrayList<>();
        Execution exec = new Execution()
                .enqueue(fts.getTransitions("s0", "a1", "s1").next())
                .enqueue(fts.getTransitions("s1", "a2", "s2").next())
                .enqueue(fts.getTransitions("s2", "a2", "s3").next())
                .enqueue(fts.getTransitions("s3", "a0", "s0").next());
        executions.add(exec);
        exec = new Execution()
                .enqueue(fts.getTransitions("s0", "a1", "s1").next())
                .enqueue(fts.getTransitions("s1", "a2", "s2").next())
                .enqueue(fts.getTransitions("s2", "a2", "s3").next())
                .enqueue(fts.getTransitions("s3", "a0", "s0").next());
        executions.add(exec);
        exec = new Execution()
                .enqueue(fts.getTransitions("s0", "a1", "s1").next())
                .enqueue(fts.getTransitions("s1", "a4", "s3").next())
                .enqueue(fts.getTransitions("s3", "a0", "s0").next());
        executions.add(exec);
        ProductsSimilarity diss = new ProductsSimilarity(fts, getFeatureModel());
        double objValue = diss.evaluate(executions);
        double expected = (0.0 + 1.0 + 1.0) / 3.0;
        assertThat(objValue, closeTo(expected, 0.00001));
    }

}