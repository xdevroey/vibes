/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package be.unamur.transitionsystem.dsl;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
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
public class LabelledTransitionSystemDefinitionTest extends LabelledTransitionSystemDefinition{

    private static final Logger logger = LoggerFactory.getLogger(LabelledTransitionSystemDefinitionTest.class);
    
    public static final String[] STATES = new String[]{"initial", "s1", "s2", "s3"};
    public static final String[] ACTIONS = new String[]{"ignate", "doX", "doY", "doXY", "exit", Action.NO_ACTION_NAME};

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
    public void testSimpleTSDef() {
        LabelledTransitionSystem lts = this.getTransitionSystem();
        
        List<Action> expectedActions = Lists.newArrayList();
        for(String s : ACTIONS){
            expectedActions.add(new Action(s));
        }
        
        List<State> expectedStates = Lists.newArrayList();
        for(String s : STATES){
            expectedStates.add(new State(s));
        }
        
        List<Action> effectiveActions = Lists.newArrayList(lts.actions());
        assertThat(effectiveActions, containsInAnyOrder(expectedActions.toArray(new Action[expectedActions.size()])));
        
        List<State> effectiveStates = Lists.newArrayList(lts.states());
        assertThat(effectiveStates, containsInAnyOrder(expectedStates.toArray(new State[expectedStates.size()])));
        
        assertThat(lts.getInitialState(), equalTo(new State(STATES[0])));
    }
    
    public LabelledTransitionSystemDefinitionTest(){
        super();
    }

    @Override
    protected void define() {
        initial(STATES[0]);
        from(STATES[0]).action(ACTIONS[0]).to(STATES[1]);
        from(STATES[1]).action(ACTIONS[1]).to(STATES[2]);
        from(STATES[1]).action(ACTIONS[3]).to(STATES[3]);
        from(STATES[2]).action(ACTIONS[2]).to(STATES[3]);
        from(STATES[3]).action(ACTIONS[4]).to(STATES[0]);
    }
    
    
    

}