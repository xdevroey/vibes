package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.annotation.DistanceFromInitialStateAnnotator;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.*;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import java.util.Random;
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
public class RandomPrefixVisitorTest {

    private static final Logger logger = LoggerFactory
            .getLogger(RandomPrefixVisitorTest.class);

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
    public void testVisit() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act01").to("s1")
                .from("s1").action("act12").to("s2")
                .from("s1").action("act13").to("s3")
                .from("s1").action("act14").to("s4")
                .from("s2").action("act25").to("s5")
                .from("s3").action("act35").to("s5")
                .from("s4").action("act45").to("s5")
                .from("s5").action("act50").to("s0")
                .build();
        DistanceFromInitialStateAnnotator.getInstance().annotate(lts);
        RandomPrefixVisitor visitor = new RandomPrefixVisitor(lts.getInitialState(), new Random(), LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR);
        assertThat(visitor.visit(lts.getState("s4")), is(true));
        assertThat(visitor.getTransitions(), hasSize(2));
        assertThat(visitor.getTransitions(), contains(transition(lts, "s0", "s1", "act01"), transition(lts, "s1", "s4", "act14")));
    }
    
    @Test
    public void testVisitNoPath() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act01").to("s1")
                .from("s1").action("act12").to("s2")
                .from("s1").action("act13").to("s3")
                //.from("s1").action("act14").to("s4")
                .from("s2").action("act25").to("s5")
                .from("s3").action("act35").to("s5")
                .from("s4").action("act45").to("s5")
                .from("s5").action("act50").to("s0")
                .build();
        DistanceFromInitialStateAnnotator.getInstance().annotate(lts);
        RandomPrefixVisitor visitor = new RandomPrefixVisitor(lts.getInitialState(), new Random(), LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR);
        assertThat(visitor.visit(lts.getState("s4")), is(false));
        assertThat(visitor.getTransitions(), hasSize(0));
    }
    
    @Test
    public void testMultiplePaths() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act01").to("s1")
                .from("s1").action("act12").to("s2")
                .from("s1").action("act13").to("s3")
                .from("s1").action("act14").to("s4")
                .from("s2").action("act25").to("s5")
                .from("s3").action("act35").to("s5")
                .from("s4").action("act45").to("s5")
                .from("s5").action("act50").to("s0")
                .build();
        DistanceFromInitialStateAnnotator.getInstance().annotate(lts);
        RandomPrefixVisitor visitor = new RandomPrefixVisitor(lts.getInitialState(), new Random(), LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR);
        assertThat(visitor.visit(lts.getState("s5")), is(true));
        assertThat(visitor.getTransitions(), hasSize(3));
        assertThat(visitor.getTransitions(), anyOf(contains(transition(lts, "s0", "s1", "act01"), transition(lts, "s1", "s4", "act14"), transition(lts, "s4", "s5", "act45")),
                contains(transition(lts, "s0", "s1", "act01"), transition(lts, "s1", "s3", "act13"), transition(lts, "s3", "s5", "act35")),
                contains(transition(lts, "s0", "s1", "act01"), transition(lts, "s1", "s2", "act12"), transition(lts, "s2", "s5", "act25"))));
    }

}
