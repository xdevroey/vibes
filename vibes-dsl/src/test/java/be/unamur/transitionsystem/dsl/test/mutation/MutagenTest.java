/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.unamur.transitionsystem.dsl.test.mutation;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.dsl.LabelledTransitionSystemDefinitionTest;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static be.unamur.transitionsystem.dsl.test.mutation.Mutagen.*;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.mutation.MutationOperator;
import be.unamur.transitionsystem.test.mutation.RandomSelectionStrategy;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class MutagenTest {

    private static final Logger logger = LoggerFactory.getLogger(MutagenTest.class);

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
    public void testSomeMethod() throws Exception {
        LabelledTransitionSystem lts = new LabelledTransitionSystemDefinitionTest().getTransitionSystem();
        FeaturedTransitionSystem ftsFMM = new FeaturedTransitionSystem(lts);
        
        MutationOperator op = transitionMissing(lts).transitionSelectionStrategy(new RandomSelectionStrategy()).done();
        assertThat(op, notNullValue());
        
        op.apply();
        
        LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();
        op.transpose(ftsFMM);
        op.getFeatureId();
    }

}