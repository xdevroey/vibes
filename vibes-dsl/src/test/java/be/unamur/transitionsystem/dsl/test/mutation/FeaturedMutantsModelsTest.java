/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.unamur.transitionsystem.dsl.test.mutation;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.dsl.LabelledTransitionSystemDefinitionTest;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.mutation.MutationOperator;
import be.unamur.transitionsystem.test.mutation.StateSelectionStrategy;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static be.unamur.transitionsystem.dsl.test.mutation.FeaturedMutantsModels.getOriginalInitialState;
import be.unamur.transitionsystem.test.mutation.RandomSelectionStrategy;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class FeaturedMutantsModelsTest {

    private static final Logger logger = LoggerFactory.getLogger(FeaturedMutantsModelsTest.class);

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
    public void testGetOriginalInitialStateMultipleWIS() throws Exception{
        LabelledTransitionSystem lts = new LabelledTransitionSystemDefinitionTest().getTransitionSystem();
        FeaturedTransitionSystem ftsFMM = new FeaturedTransitionSystem(lts);
        
        MutationOperator op = Mutagen.wrongInitialState(lts).stateSelectionStrategy(new StateSelectionStrategy() {
            @Override
            public State selectState(MutationOperator operator, TransitionSystem ts) {
                return ts.getState(LabelledTransitionSystemDefinitionTest.STATES[1]);
            }
        }).done();
        op.apply();
        op.transpose(ftsFMM);
        
        op = Mutagen.wrongInitialState(lts).stateSelectionStrategy(new StateSelectionStrategy() {
            @Override
            public State selectState(MutationOperator operator, TransitionSystem ts) {
                return ts.getState(LabelledTransitionSystemDefinitionTest.STATES[2]);
            }
        }).done();
        op.apply();
        op.transpose(ftsFMM);
        
        op = Mutagen.wrongInitialState(lts).stateSelectionStrategy(new StateSelectionStrategy() {
            @Override
            public State selectState(MutationOperator operator, TransitionSystem ts) {
                return ts.getState(LabelledTransitionSystemDefinitionTest.STATES[3]);
            }
        }).done();
        op.apply();
        op.transpose(ftsFMM);
        
        assertThat(ftsFMM.getInitialState(), not(lts.getInitialState()));
        assertThat(getOriginalInitialState(ftsFMM), equalTo(lts.getInitialState()));
    }
    
    @Test
    public void testGetOriginalInitialStateMultipleWISMixOps() throws Exception{
        LabelledTransitionSystem lts = new LabelledTransitionSystemDefinitionTest().getTransitionSystem();
        FeaturedTransitionSystem ftsFMM = new FeaturedTransitionSystem(lts);
        
        RandomSelectionStrategy rand = new RandomSelectionStrategy(100000);
        
        MutationOperator op = Mutagen.wrongInitialState(lts).stateSelectionStrategy(new StateSelectionStrategy() {
            @Override
            public State selectState(MutationOperator operator, TransitionSystem ts) {
                return ts.getState(LabelledTransitionSystemDefinitionTest.STATES[1]);
            }
        }).done();
        op.apply();
        op.transpose(ftsFMM);
        
        op = Mutagen.wrongInitialState(lts).stateSelectionStrategy(new StateSelectionStrategy() {
            @Override
            public State selectState(MutationOperator operator, TransitionSystem ts) {
                return ts.getState(LabelledTransitionSystemDefinitionTest.STATES[2]);
            }
        }).done();
        op.apply();
        op.transpose(ftsFMM);
        
        op = Mutagen.wrongInitialState(lts).stateSelectionStrategy(new StateSelectionStrategy() {
            @Override
            public State selectState(MutationOperator operator, TransitionSystem ts) {
                return ts.getState(LabelledTransitionSystemDefinitionTest.STATES[3]);
            }
        }).done();
        op.apply();
        op.transpose(ftsFMM);
        
        op = Mutagen.actionExchange(lts)
                .actionSelectionStrategy(rand)
                .stateSelectionStrategy(rand)
                .transitionSelectionStrategy(rand)
                .done();
        op.apply();
        op.transpose(ftsFMM);
        
        op = Mutagen.actionExchange(lts)
                .actionSelectionStrategy(rand)
                .stateSelectionStrategy(rand)
                .transitionSelectionStrategy(rand)
                .done();
        op.apply();
        op.transpose(ftsFMM);
        
        op = Mutagen.actionMissing(lts)
                .actionSelectionStrategy(rand)
                .stateSelectionStrategy(rand)
                .transitionSelectionStrategy(rand)
                .done();
        op.apply();
        op.transpose(ftsFMM);
                
        op = Mutagen.actionMissing(lts)
                .actionSelectionStrategy(rand)
                .stateSelectionStrategy(rand)
                .transitionSelectionStrategy(rand)
                .done();
        op.apply();
        op.transpose(ftsFMM);
                
        op = Mutagen.stateMissing(lts)
                .actionSelectionStrategy(rand)
                .stateSelectionStrategy(rand)
                .transitionSelectionStrategy(rand)
                .done();
        op.apply();
        op.transpose(ftsFMM);
              
        op = Mutagen.stateMissing(lts)
                .actionSelectionStrategy(rand)
                .stateSelectionStrategy(rand)
                .transitionSelectionStrategy(rand)
                .done();
        op.apply();
        op.transpose(ftsFMM);
              
        op = Mutagen.transitionAdd(lts)
                .actionSelectionStrategy(rand)
                .stateSelectionStrategy(rand)
                .transitionSelectionStrategy(rand)
                .done();
        op.apply();
        op.transpose(ftsFMM);
             
        op = Mutagen.transitionAdd(lts)
                .actionSelectionStrategy(rand)
                .stateSelectionStrategy(rand)
                .transitionSelectionStrategy(rand)
                .done();
        op.apply();
        op.transpose(ftsFMM);
             
        op = Mutagen.transitionDestinationExchange(lts)
                .actionSelectionStrategy(rand)
                .stateSelectionStrategy(rand)
                .transitionSelectionStrategy(rand)
                .done();
        op.apply();
        op.transpose(ftsFMM);
         
        op = Mutagen.transitionDestinationExchange(lts)
                .actionSelectionStrategy(rand)
                .stateSelectionStrategy(rand)
                .transitionSelectionStrategy(rand)
                .done();
        op.apply();
        op.transpose(ftsFMM);
         
        op = Mutagen.transitionMissing(lts)
                .actionSelectionStrategy(rand)
                .stateSelectionStrategy(rand)
                .transitionSelectionStrategy(rand)
                .done();
        op.apply();
        op.transpose(ftsFMM);
        
        op = Mutagen.transitionMissing(lts)
                .actionSelectionStrategy(rand)
                .stateSelectionStrategy(rand)
                .transitionSelectionStrategy(rand)
                .done();
        op.apply();
        op.transpose(ftsFMM);
        
        assertThat(ftsFMM.getInitialState(), not(lts.getInitialState()));
        assertThat(getOriginalInitialState(ftsFMM), equalTo(lts.getInitialState()));
    }
    

    @Test
    public void testGetOriginalInitialStateOneWIS() throws Exception{
        LabelledTransitionSystem lts = new LabelledTransitionSystemDefinitionTest().getTransitionSystem();
        FeaturedTransitionSystem ftsFMM = new FeaturedTransitionSystem(lts);
        
        MutationOperator op = Mutagen.wrongInitialState(lts).stateSelectionStrategy(new StateSelectionStrategy() {
            @Override
            public State selectState(MutationOperator operator, TransitionSystem ts) {
                return ts.getState(LabelledTransitionSystemDefinitionTest.STATES[1]);
            }
        }).done();
        op.apply();
        op.transpose(ftsFMM);
        
        assertThat(ftsFMM.getInitialState(), not(lts.getInitialState()));
        assertThat(getOriginalInitialState(ftsFMM), equalTo(lts.getInitialState()));
    }
    
    @Test
    public void testGetOriginalInitialStateZeroWIS() throws Exception{
        LabelledTransitionSystem lts = new LabelledTransitionSystemDefinitionTest().getTransitionSystem();
        FeaturedTransitionSystem ftsFMM = new FeaturedTransitionSystem(lts);
        
        assertThat(getOriginalInitialState(ftsFMM), equalTo(lts.getInitialState()));
    }

}