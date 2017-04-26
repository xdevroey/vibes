/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.define;
import be.unamur.transitionsystem.test.mutation.exception.CounterExampleFoundException;
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
public class RandomEquivalenceTaskTest {

    private static final Logger LOG = LoggerFactory.getLogger(RandomEquivalenceTaskTest.class);

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
    public void testCallOne() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        int nbExec = 1;
        int traceSize = 5;
        RandomEquivalenceTask task = new RandomEquivalenceTask(lts, lts, traceSize, nbExec, false);
        EquivalenceResults results = task.call();
        assertThat(results.getSucceededRuns(), equalTo(nbExec));
        assertThat(results.getFailedRuns(), equalTo(0));
    }

    @Test
    public void testCallMultiple() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        int nbExec = 50;
        int traceSize = 5;
        RandomEquivalenceTask task = new RandomEquivalenceTask(lts, lts, traceSize, nbExec, false);
        EquivalenceResults results = task.call();
        assertThat(results.getSucceededRuns(), equalTo(nbExec));
        assertThat(results.getFailedRuns(), equalTo(0));
    }

    @Test
    public void testCallFailOne() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        LabelledTransitionSystem lts2 = define().init("s0")
                .from("s0").action("errrrr").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        int nbExec = 1;
        int traceSize = 5;
        RandomEquivalenceTask task = new RandomEquivalenceTask(lts, lts2, traceSize, nbExec, false);
        EquivalenceResults results = task.call();
        assertThat(results.getSucceededRuns(), equalTo(0));
        assertThat(results.getFailedRuns(), equalTo(nbExec));
    }

    @Test
    public void testCallFailMultiple() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        LabelledTransitionSystem lts2 = define().init("s0")
                .from("s0").action("errrrr").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        int nbExec = 50;
        int traceSize = 5;
        RandomEquivalenceTask task = new RandomEquivalenceTask(lts, lts2, traceSize, nbExec, false);
        EquivalenceResults results = task.call();
        assertThat(results.getSucceededRuns(), equalTo(0));
        assertThat(results.getFailedRuns(), equalTo(nbExec));
    }
    
    @Test
    public void testCallOneFailFirst() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        int nbExec = 1;
        int traceSize = 5;
        RandomEquivalenceTask task = new RandomEquivalenceTask(lts, lts, traceSize, nbExec, false);
        task.setFailFirst(true);
        EquivalenceResults results = task.call();
        assertThat(results.getSucceededRuns(), equalTo(nbExec));
        assertThat(results.getFailedRuns(), equalTo(0));
    }

    @Test
    public void testCallMultipleFailFirst() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        int nbExec = 50;
        int traceSize = 5;
        RandomEquivalenceTask task = new RandomEquivalenceTask(lts, lts, traceSize, nbExec, false);
        task.setFailFirst(true);
        EquivalenceResults results = task.call();
        assertThat(results.getSucceededRuns(), equalTo(nbExec));
        assertThat(results.getFailedRuns(), equalTo(0));
    }
    
    @Test(expected = CounterExampleFoundException.class)
    public void testCallFailOneFailFirst() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        LabelledTransitionSystem lts2 = define().init("s0")
                .from("s0").action("errrrr").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        int nbExec = 1;
        int traceSize = 5;
        RandomEquivalenceTask task = new RandomEquivalenceTask(lts, lts2, traceSize, nbExec, false);
        task.setFailFirst(true);
        task.call();
        fail("Should have lauched an exception");
    }

    @Test(expected = CounterExampleFoundException.class)
    public void testCallFailMultipleFailFirst() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        LabelledTransitionSystem lts2 = define().init("s0")
                .from("s0").action("errrrr").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        int nbExec = 50;
        int traceSize = 5;
        RandomEquivalenceTask task = new RandomEquivalenceTask(lts, lts2, traceSize, nbExec, false);
        task.setFailFirst(true);
        task.call();
        fail("Should have lauched an exception");
    }

}
