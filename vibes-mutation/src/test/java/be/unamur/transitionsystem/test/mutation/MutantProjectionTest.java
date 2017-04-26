/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.unamur.transitionsystem.test.mutation;

import be.unamur.fts.fexpression.Feature;
import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.fts.fexpression.configuration.SimpleConfiguration;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;
import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.define;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
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
public class MutantProjectionTest {

    private static final Logger logger = LoggerFactory.getLogger(MutantProjectionTest.class);

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
    public void testProject() throws Exception{
        final LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act2").to("s2")
                .from("s2").action("act0").to("s0")
                .from("s0").action("act2").to("s2")
                .build();
        WrongInitialState wis = new WrongInitialState(lts, new StateSelectionStrategy() {
            @Override
            public State selectState(MutationOperator operator, TransitionSystem ts) {
                return lts.getState("s1");
            }
        });
        wis.apply();
        String featureId = wis.getFeatureId();
        Configuration config = new SimpleConfiguration();
        config.selectFeature(Feature.feature(featureId));
        // Transpose
        FeaturedTransitionSystem fts = wis.transpose(null);
        // Project WIS
        LabelledTransitionSystem mut = MutantProjection.getInstance().project(fts, config);
        assertThat(mut.getInitialState(), equalTo(mut.getState("s1")));
        // Check that WIS_ACTION_NAME has been removed from the projection
        assertThat(Lists.newArrayList(mut.actions()), not(hasItem(equalTo(fts.getAction(WrongInitialState.WIS_ACTION_NAME)))));
        // Project not WIS
        config.deselectFeature(Feature.feature(featureId));
        mut = MutantProjection.getInstance().project(fts, config);
        assertThat(mut.getInitialState(), equalTo(mut.getState("s0")));
        // Check that WIS_ACTION_NAME has been removed from the projection
        assertThat(Lists.newArrayList(mut.actions()), not(hasItem(equalTo(fts.getAction(WrongInitialState.WIS_ACTION_NAME)))));
    }

}