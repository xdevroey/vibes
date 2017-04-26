/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.unamur.transitionsystem.test.mutation;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.define;
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
public class FeaturedMutantsModelTest {

    private static final Logger LOG = LoggerFactory.getLogger(FeaturedMutantsModelTest.class);
    
    private static final long SEED = 100000;

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
    public void testMutate() throws Exception{
        LabelledTransitionSystem system = define().init("s0")
                .from("s0").action("act01").to("s1")
                .from("s1").action("act12").to("s2")
                .from("s1").action("act13").to("s3")
                .from("s1").action("act14").to("s4")
                .from("s2").action("act25").to("s5")
                .from("s3").action("act35").to("s5")
                .from("s4").action("act45").to("s5")
                .from("s5").action("act50").to("s0")
                .build();
        // Create the FMM
        FeaturedMutantsModel fmm = new FeaturedMutantsModel(system);
        assertThat(fmm.numberOfMutations(), equalTo(0));
        assertThat(fmm.getFts(), notNullValue());
        assertThat(fmm.getOriginal(), equalTo(system));
        RandomSelectionStrategy strategy = new RandomSelectionStrategy(SEED);
        fmm.mutate(new ActionExchange(system, strategy, strategy));
        fmm.mutate(new WrongInitialState(system, strategy));
        fmm.mutate(new StateMissing(system, strategy));
        assertThat(fmm.numberOfMutations(), equalTo(3));
        assertThat(fmm.possibleMutantsCount(2), equalTo(3.0));
        fmm.printTvl(2, System.out);
        
    }

}