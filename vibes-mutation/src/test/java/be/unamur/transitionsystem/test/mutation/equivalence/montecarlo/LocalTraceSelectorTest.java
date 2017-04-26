/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.define;
import com.google.common.collect.Lists;
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
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class LocalTraceSelectorTest {

    private static final Logger LOG = LoggerFactory.getLogger(LocalTraceSelectorTest.class);

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
    public void testSelect() {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s1").action("act").to("s3")
                .from("s3").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .build();
        int size = 4;
        List<State> localStates = Lists.newArrayList(lts.getState("s3")); // Only one path to/from s3
        LocalRandomFixedLengthTraceSelector selector = new LocalRandomFixedLengthTraceSelector(lts, size, localStates);
        List<Action> result = selector.select();
        assertThat(result, contains(lts.getAction("act1"), 
                lts.getAction("act"), 
                lts.getAction("act2"),
                lts.getAction("act0")));
        assertThat(result, hasSize(size));
        result = selector.select();
        assertThat(result, contains(lts.getAction("act1"), 
                lts.getAction("act"), 
                lts.getAction("act2"),
                lts.getAction("act0")));
        assertThat(result, hasSize(size));
        result = selector.select();
        assertThat(result, contains(lts.getAction("act1"), 
                lts.getAction("act"), 
                lts.getAction("act2"),
                lts.getAction("act0")));
        assertThat(result, hasSize(size));
    }

}
