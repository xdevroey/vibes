package be.vibes.selection.random;

/*-
 * #%L
 * VIBeS: test case selection
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

import be.vibes.selection.exception.TestCaseSelectionException;
import be.vibes.ts.Action;
import be.vibes.ts.State;
import be.vibes.ts.TestCase;
import be.vibes.ts.Transition;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.TransitionSystemFactory;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.*;
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
public class LocalRandomSelectionFromTSTest {

    private static final Logger LOG = LoggerFactory.getLogger(LocalRandomSelectionFromTSTest.class);

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
    public void testSelect() throws Exception {
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addStates("s1", "s2", "s3", "s4", "s5");
        factory.addActions("act01", "act12", "act13", "act14", "act25", "act35", "act45", "act50");
        factory.addTransition("s0", "act01", "s1");
        factory.addTransition("s1", "act12", "s2");
        factory.addTransition("s1", "act13", "s3");
        factory.addTransition("s1", "act14", "s4");
        factory.addTransition("s2", "act25", "s5");
        factory.addTransition("s3", "act35", "s5");
        factory.addTransition("s4", "act45", "s5");
        factory.addTransition("s5", "act50", "s0");
        TransitionSystem lts = factory.build();
        Set<State> states = Sets.newHashSet(lts.getState("s3"));
        LocalRandomSelectionFromTS selector = new LocalRandomSelectionFromTS(lts, states);
        TestCase testCase = selector.select();
        List<Action> actions = new ArrayList<>();
        testCase.iterator().forEachRemaining((Transition t) ->{
            actions.add(t.getAction());
        });
        assertThat(actions, contains(lts.getAction("act01"), lts.getAction("act13"), lts.getAction("act35"), lts.getAction("act50")));
    }

    @Test(expected = TestCaseSelectionException.class)
    public void testSelectNoPath() throws Exception {
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addStates("s1", "s2", "s3", "s4", "s5");
        factory.addActions("act01", "act12", "act13", "act14", "act25", "act35", "act45", "act50");
        factory.addTransition("s0", "act01", "s1");
        factory.addTransition("s1", "act12", "s2");
        //factory.addTransition("s1", "act13", "s3");
        factory.addTransition("s1", "act14", "s4");
        factory.addTransition("s2", "act25", "s5");
        factory.addTransition("s3", "act35", "s5");
        factory.addTransition("s4", "act45", "s5");
        factory.addTransition("s5", "act50", "s0");
        TransitionSystem lts = factory.build();
        Set<State> states = Sets.newHashSet(lts.getState("s3"));
        LocalRandomSelectionFromTS selector = new LocalRandomSelectionFromTS(lts, states);
        TestCase testCase = selector.select();
        LOG.error("No possible path for given LTS and states, should hav launched a TestCaseSelectionException!");
    }

}
