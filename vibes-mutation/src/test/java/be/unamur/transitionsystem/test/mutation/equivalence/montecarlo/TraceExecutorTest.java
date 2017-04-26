/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.define;
import com.google.common.collect.Lists;
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
public class TraceExecutorTest {

    private static final Logger logger = LoggerFactory.getLogger(TraceExecutorTest.class);

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
    public void testExecuteEndsInInitialState() {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        TraceExecutor exec = new TraceExecutor(lts);
        boolean res = exec.execute(Lists.newArrayList(lts.getAction("act1"),
                lts.getAction("act"),
                lts.getAction("act0")));
        assertThat(res, is(true));
        res = exec.execute(Lists.newArrayList(lts.getAction("act1"),
                lts.getAction("act"),
                lts.getAction("act2")));
        assertThat(res, is(true));
        res = exec.execute(Lists.newArrayList(lts.getAction("act1"),
                lts.getAction("act2"),
                lts.getAction("act0")));
        assertThat(res, is(true));
    }
    
    @Test
    public void testExecuteEndsAfterInitialState() {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        TraceExecutor exec = new TraceExecutor(lts);
        boolean res = exec.execute(Lists.newArrayList(lts.getAction("act1"),
                lts.getAction("act"),
                lts.getAction("act0"),
                lts.getAction("act1"),
                lts.getAction("act2")));
        assertThat(res, is(true));
        res = exec.execute(Lists.newArrayList(lts.getAction("act1"),
                lts.getAction("act"),
                lts.getAction("act0"),
                lts.getAction("act1"),
                lts.getAction("act2"),
                lts.getAction("act0"),
                lts.getAction("act1"),
                lts.getAction("act")));
        assertThat(res, is(true));
    }
    
    @Test
    public void testExecuteEndsBeforeInitialState() {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        TraceExecutor exec = new TraceExecutor(lts);
        boolean res = exec.execute(Lists.newArrayList(lts.getAction("act1"),
                lts.getAction("act")));
        assertThat(res, is(true));
        res = exec.execute(Lists.newArrayList(lts.getAction("act1")));
        assertThat(res, is(true));
        res = exec.execute(Lists.newArrayList(lts.getAction("act1"),
                lts.getAction("act2")));
        assertThat(res, is(true));
    }
    
    @Test
    public void testExecuteFails() {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        TraceExecutor exec = new TraceExecutor(lts);
        boolean res = exec.execute(Lists.newArrayList(lts.getAction("act2"),
                lts.getAction("act")));
        assertThat(res, is(false));
        res = exec.execute(Lists.newArrayList(lts.getAction("act1"), 
                lts.getAction("act3")));
        assertThat(res, is(false));
        res = exec.execute(Lists.newArrayList(lts.getAction("act1"),
                lts.getAction("act"),
                lts.getAction("act")));
        assertThat(res, is(false));
    }

}