/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.vibes.ts;

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

    private static final Logger LOG = LoggerFactory.getLogger(DistanceFromInitialStateAnnotatorTest.class);

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
    public void testAnnotate() {
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addStates("s1", "s2", "s3");
        factory.addActions("a1", "a2", "a0");
        factory.addTransition("s0", "a1", "s1");
        factory.addTransition("s1", "a2", "s3");
        factory.addTransition("s0", "a1", "s3");
        factory.addTransition("s2", "a2", "s3");
        factory.addTransition("s3", "a0", "s0");
        TransitionSystem ts = factory.build();
        DistanceFromInitialStateAnnotator.getInstance().annotate(ts);
        assertThat(ts.getInitialState().getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1), equalTo(0));
        Iterator<State> it = ts.states();
        while (it.hasNext()) {
            assertThat(it.next().getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1), greaterThanOrEqualTo(0));
        }
    }

}
