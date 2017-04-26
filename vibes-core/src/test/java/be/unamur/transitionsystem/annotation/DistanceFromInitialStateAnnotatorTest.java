/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.annotation;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;
import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.define;
import java.util.Iterator;
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
public class DistanceFromInitialStateAnnotatorTest {

    private static final Logger logger = LoggerFactory.getLogger(DistanceFromInitialStateAnnotatorTest.class);

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
    public void testAnnotate() {
        TransitionSystem ts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act2").to("s2")
                .from("s0").action("act1").to("s3")
                .from("s2").action("act2").to("s3")
                .from("s3").action("act0").to("s0")
                .build();
        DistanceFromInitialStateAnnotator.getInstance().annotate(ts);
        assertThat(ts.getInitialState().getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1), equalTo(0));
        Iterator<State> it = ts.states();
        while (it.hasNext()) {
            assertThat(it.next().getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1), greaterThanOrEqualTo(0));
        }
    }

}
