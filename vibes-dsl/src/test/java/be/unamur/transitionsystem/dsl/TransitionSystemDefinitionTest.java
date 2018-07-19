package be.unamur.transitionsystem.dsl;

/*-
 * #%L
 * VIBeS: dsl
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import be.vibes.dsl.ts.TransitionSystemDefinition;
import be.vibes.ts.Action;
import be.vibes.ts.TransitionSystem;
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
public class TransitionSystemDefinitionTest extends TransitionSystemDefinition{

    private static final Logger LOG = LoggerFactory.getLogger(TransitionSystemDefinitionTest.class);
    
    public static final String[] STATES = new String[]{"initial", "s1", "s2", "s3"};
    public static final String[] ACTIONS = new String[]{"ignate", "doX", "doY", "doXY", "exit"};

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
    public void testSimpleTSDef() {
        TransitionSystem builtTs = this.getTransitionSystem();
        
        List<String> expectedActions = Lists.newArrayList(ACTIONS);
        List<String> effectiveActions = Lists.newArrayList();
        builtTs.actions().forEachRemaining((action)->{
            effectiveActions.add(action.getName());
        });
        assertThat(effectiveActions, containsInAnyOrder(expectedActions.toArray(new String[expectedActions.size()])));
        
        List<String> expectedStates = Lists.newArrayList(STATES);
        List<String> effectiveStates = Lists.newArrayList();
        builtTs.states().forEachRemaining((state)->{
            effectiveStates.add(state.getName());
        });
        assertThat(effectiveStates, containsInAnyOrder(expectedStates.toArray(new String[expectedStates.size()])));
        
        assertThat(builtTs.getInitialState().getName(), equalTo(STATES[0]));
    }
    
    public TransitionSystemDefinitionTest(){
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
