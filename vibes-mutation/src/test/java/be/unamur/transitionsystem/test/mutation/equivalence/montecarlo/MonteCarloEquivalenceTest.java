/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.*;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.execution.StrictTestCaseRunner;
import be.unamur.transitionsystem.test.selection.AccumulatorWrapUp;
import be.unamur.transitionsystem.test.selection.AlwaysTrueValidator;
import be.unamur.transitionsystem.test.selection.RandomTestCaseGenerator;
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
public class MonteCarloEquivalenceTest {

    private Logger logger = LoggerFactory.getLogger(MonteCarloEquivalenceTest.class);

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
    public void testGetEquivalenceDegree() throws Exception {
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
        LabelledTransitionSystem mutant = define().init("s0")
                .from("s0").action("act01").to("s1")
                .from("s1").action("act12").to("s2")
                //.from("s1").action("act13").to("s3")
                .from("s1").action("act14").to("s4")
                .from("s2").action("act25").to("s5")
                //.from("s3").action("act35").to("s5")
                .from("s4").action("act45").to("s5")
                .from("s5").action("act50").to("s0")
                .build();
        StrictTestCaseRunner runner = new StrictTestCaseRunner();
        RandomTestCaseGenerator gen = new RandomTestCaseGenerator(LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, new AccumulatorWrapUp());
        MonteCarloEquivalence eq = new MonteCarloEquivalence(0.5, 0.4, runner, gen);
        assertThat(eq.getDelta(), closeTo(0.5, 0.00000001));
        assertThat(eq.getEpsilon(), closeTo(0.4, 0.00000001));
        logger.debug("Estimate nbr runs = {}", eq.getEstimatedNbrRuns());
        assertThat(eq.getEstimatedNbrRuns(), greaterThan(0));
        double value = eq.getEquivalenceDegree(lts, mutant, 1.0);
        logger.debug("Equivalence degree={}", value);
        assertThat(value, closeTo(0.6, 0.3));
    }
    
    @Test
    public void testGetEquivalenceDegreeZero() throws Exception {
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
        LabelledTransitionSystem mutant = define().init("s0")
                .from("s0").action("act").to("s1")
                .from("s1").action("act12").to("s2")
                .from("s1").action("act13").to("s3")
                .from("s1").action("act14").to("s4")
                .from("s2").action("act25").to("s5")
                .from("s3").action("act35").to("s5")
                .from("s4").action("act45").to("s5")
                .from("s5").action("act50").to("s0")
                .build();
        StrictTestCaseRunner runner = new StrictTestCaseRunner();
        RandomTestCaseGenerator gen = new RandomTestCaseGenerator(LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, new AccumulatorWrapUp());
        MonteCarloEquivalence eq = new MonteCarloEquivalence(0.5, 0.4, runner, gen);
        assertThat(eq.getDelta(), closeTo(0.5, 0.00000001));
        assertThat(eq.getEpsilon(), closeTo(0.4, 0.00000001));
        logger.debug("Estimate nbr runs = {}", eq.getEstimatedNbrRuns());
        assertThat(eq.getEstimatedNbrRuns(), greaterThan(0));
        double value = eq.getEquivalenceDegree(lts, mutant);
        logger.debug("Equivalence degree={}", value);
        assertThat(value, closeTo(0, 0.0000001));
    }
    
    @Test
    public void testGetEquivalenceDegreeOne() throws Exception {
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
        LabelledTransitionSystem mutant = define().init("s0")
                .from("s0").action("act01").to("s1")
                .from("s1").action("act12").to("s2")
                .from("s1").action("act13").to("s3")
                .from("s1").action("act14").to("s4")
                .from("s2").action("act25").to("s5")
                .from("s3").action("act35").to("s5")
                .from("s4").action("act45").to("s5")
                .from("s5").action("act50").to("s0")
                .build();
        StrictTestCaseRunner runner = new StrictTestCaseRunner();
        RandomTestCaseGenerator gen = new RandomTestCaseGenerator(LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, new AccumulatorWrapUp());
        MonteCarloEquivalence eq = new MonteCarloEquivalence(0.5, 0.4, runner, gen);
        assertThat(eq.getDelta(), closeTo(0.5, 0.00000001));
        assertThat(eq.getEpsilon(), closeTo(0.4, 0.00000001));
        logger.debug("Estimate nbr runs = {}", eq.getEstimatedNbrRuns());
        assertThat(eq.getEstimatedNbrRuns(), greaterThan(0));
        double value = eq.getEquivalenceDegree(lts, mutant);
        logger.debug("Equivalence degree={}", value);
        assertThat(value, closeTo(1, 0.0000001));
    }

}
