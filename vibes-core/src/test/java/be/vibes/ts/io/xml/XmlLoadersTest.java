package be.vibes.ts.io.xml;

/*-
 * #%L
 * VIBeS: core
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

import be.vibes.ts.Action;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.State;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.UsageModel;
import com.google.common.collect.Lists;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
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
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class XmlLoadersTest {

    private static final Logger LOG = LoggerFactory.getLogger(XmlLoadersTest.class);

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
    public void testLoadTransitionSystem() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("ts-sodaVendingMachine.xml");
        TransitionSystem ts = XmlLoaders.loadTransitionSystem(input);
        assertThat(ts, notNullValue());
        assertThat(ts.getInitialState(), equalTo(ts.getState("state1")));
        assertThat(ts.getStatesCount(), equalTo(9));
        // Check states
        List<String> states = Lists.newArrayList(ts.states()).stream().map(State::getName).collect(Collectors.toList());
        assertThat(states, containsInAnyOrder("state1", "state2", "state3", "state4", "state5", "state6", "state7", "state8", "state9"));
        // Check actions
        List<String> actions = Lists.newArrayList(ts.actions()).stream().map(Action::getName).collect(Collectors.toList());
        assertThat(actions, containsInAnyOrder("pay", "free", "change", "cancel", "tea", "soda", "return", "serveSoda", "serveTea", "open", "take", "close"));
        // Check transitions
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state1"), ts.getAction("pay"), ts.getState("state2"))), hasSize(1));
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state1"), ts.getAction("free"), ts.getState("state3"))), hasSize(1));
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state2"), ts.getAction("change"), ts.getState("state3"))), hasSize(1));
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state3"), ts.getAction("cancel"), ts.getState("state4"))), hasSize(1));
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state3"), ts.getAction("tea"), ts.getState("state6"))), hasSize(1));
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state3"), ts.getAction("soda"), ts.getState("state5"))), hasSize(1));
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state4"), ts.getAction("return"), ts.getState("state1"))), hasSize(1));
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state5"), ts.getAction("serveSoda"), ts.getState("state7"))), hasSize(1));
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state6"), ts.getAction("serveTea"), ts.getState("state7"))), hasSize(1));
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state7"), ts.getAction("open"), ts.getState("state8"))), hasSize(1));
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state7"), ts.getAction("take"), ts.getState("state1"))), hasSize(1));
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state8"), ts.getAction("take"), ts.getState("state9"))), hasSize(1));
        assertThat(Lists.newArrayList(ts.getTransitions(ts.getState("state9"), ts.getAction("close"), ts.getState("state1"))), hasSize(1));
    }

    @Test
    public void testLoadFeaturedTransitionSystem() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("fts-sodaVendingMachine.xml");
        FeaturedTransitionSystem fts = XmlLoaders.loadFeaturedTransitionSystem(input);
        assertThat(fts, notNullValue());
        assertThat(fts.getInitialState(), equalTo(fts.getState("state1")));
        assertThat(fts.getStatesCount(), equalTo(9));
        // Check states
        List<String> states = Lists.newArrayList(fts.states()).stream().map(State::getName).collect(Collectors.toList());
        assertThat(states, containsInAnyOrder("state1", "state2", "state3", "state4", "state5", "state6", "state7", "state8", "state9"));
        // Check actions
        List<String> actions = Lists.newArrayList(fts.actions()).stream().map(Action::getName).collect(Collectors.toList());
        assertThat(actions, containsInAnyOrder("pay", "free", "change", "cancel", "tea", "soda", "return", "serveSoda", "serveTea", "open", "take", "close"));
        // Check transitions
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state1"), fts.getAction("pay"), fts.getState("state2"))), hasSize(1));
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state1"), fts.getAction("free"), fts.getState("state3"))), hasSize(1));
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state2"), fts.getAction("change"), fts.getState("state3"))), hasSize(1));
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state3"), fts.getAction("cancel"), fts.getState("state4"))), hasSize(1));
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state3"), fts.getAction("tea"), fts.getState("state6"))), hasSize(1));
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state3"), fts.getAction("soda"), fts.getState("state5"))), hasSize(1));
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state4"), fts.getAction("return"), fts.getState("state1"))), hasSize(1));
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state5"), fts.getAction("serveSoda"), fts.getState("state7"))), hasSize(1));
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state6"), fts.getAction("serveTea"), fts.getState("state7"))), hasSize(1));
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state7"), fts.getAction("open"), fts.getState("state8"))), hasSize(1));
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state7"), fts.getAction("take"), fts.getState("state1"))), hasSize(1));
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state8"), fts.getAction("take"), fts.getState("state9"))), hasSize(1));
        assertThat(Lists.newArrayList(fts.getTransitions(fts.getState("state9"), fts.getAction("close"), fts.getState("state1"))), hasSize(1));
        // Check feature expressions
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state1"), fts.getAction("pay"), fts.getState("state2")).next()).toString(), equalToIgnoringWhiteSpace("!FreeDrinks"));
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state1"), fts.getAction("free"), fts.getState("state3")).next()).toString(), equalToIgnoringWhiteSpace("FreeDrinks"));
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state2"), fts.getAction("change"), fts.getState("state3")).next()).toString(), equalToIgnoringWhiteSpace("!FreeDrinks"));
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state3"), fts.getAction("cancel"), fts.getState("state4")).next()).toString(), equalToIgnoringWhiteSpace("CancelPurchase"));
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state3"), fts.getAction("tea"), fts.getState("state6")).next()).toString(), equalToIgnoringWhiteSpace("Tea"));
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state3"), fts.getAction("soda"), fts.getState("state5")).next()).toString(), equalToIgnoringWhiteSpace("Soda"));
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state4"), fts.getAction("return"), fts.getState("state1")).next()).toString(), equalToIgnoringWhiteSpace("CancelPurchase"));
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state5"), fts.getAction("serveSoda"), fts.getState("state7")).next()).toString(), equalToIgnoringWhiteSpace("Soda"));
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state6"), fts.getAction("serveTea"), fts.getState("state7")).next()).toString(), equalToIgnoringWhiteSpace("Tea"));
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state7"), fts.getAction("open"), fts.getState("state8")).next()).toString(), equalToIgnoringWhiteSpace("!FreeDrinks"));
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state7"), fts.getAction("take"), fts.getState("state1")).next()).toString(), equalToIgnoringWhiteSpace("FreeDrinks"));
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state8"), fts.getAction("take"), fts.getState("state9")).next()).toString(), equalToIgnoringWhiteSpace("!FreeDrinks"));
        assertThat(fts.getFExpression(fts.getTransitions(fts.getState("state9"), fts.getAction("close"), fts.getState("state1")).next()).toString(), equalToIgnoringWhiteSpace("!FreeDrinks"));
    }

    @Test
    public void testLoadUsageModel() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("um-sodaVendingMachine.xml");
        UsageModel um = XmlLoaders.loadUsageModel(input);
        assertThat(um, notNullValue());
        assertThat(um.getInitialState(), equalTo(um.getState("state1")));
        assertThat(um.getStatesCount(), equalTo(9));
        // Check states
        List<String> states = Lists.newArrayList(um.states()).stream().map(State::getName).collect(Collectors.toList());
        assertThat(states, containsInAnyOrder("state1", "state2", "state3", "state4", "state5", "state6", "state7", "state8", "state9"));
        // Check actions
        List<String> actions = Lists.newArrayList(um.actions()).stream().map(Action::getName).collect(Collectors.toList());
        assertThat(actions, containsInAnyOrder("pay", "free", "change", "cancel", "tea", "soda", "return", "serveSoda", "serveTea", "open", "take", "close"));
        // Check transitions
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state1"), um.getAction("pay"), um.getState("state2"))), hasSize(1));
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state1"), um.getAction("free"), um.getState("state3"))), hasSize(1));
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state2"), um.getAction("change"), um.getState("state3"))), hasSize(1));
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state3"), um.getAction("cancel"), um.getState("state4"))), hasSize(1));
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state3"), um.getAction("tea"), um.getState("state6"))), hasSize(1));
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state3"), um.getAction("soda"), um.getState("state5"))), hasSize(1));
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state4"), um.getAction("return"), um.getState("state1"))), hasSize(1));
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state5"), um.getAction("serveSoda"), um.getState("state7"))), hasSize(1));
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state6"), um.getAction("serveTea"), um.getState("state7"))), hasSize(1));
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state7"), um.getAction("open"), um.getState("state8"))), hasSize(1));
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state7"), um.getAction("take"), um.getState("state1"))), hasSize(1));
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state8"), um.getAction("take"), um.getState("state9"))), hasSize(1));
        assertThat(Lists.newArrayList(um.getTransitions(um.getState("state9"), um.getAction("close"), um.getState("state1"))), hasSize(1));
        // Check feature expressions
        assertThat(um.getProbability(um.getTransitions(um.getState("state1"), um.getAction("pay"), um.getState("state2")).next()), closeTo(0.5, 0.001));
        assertThat(um.getProbability(um.getTransitions(um.getState("state1"), um.getAction("free"), um.getState("state3")).next()), closeTo(0.5, 0.001));
        assertThat(um.getProbability(um.getTransitions(um.getState("state2"), um.getAction("change"), um.getState("state3")).next()), closeTo(1.0, 0.001));
        assertThat(um.getProbability(um.getTransitions(um.getState("state3"), um.getAction("cancel"), um.getState("state4")).next()), closeTo(0.33, 0.001));
        assertThat(um.getProbability(um.getTransitions(um.getState("state3"), um.getAction("tea"), um.getState("state6")).next()), closeTo(0.33, 0.001));
        assertThat(um.getProbability(um.getTransitions(um.getState("state3"), um.getAction("soda"), um.getState("state5")).next()), closeTo(0.33, 0.001));
        assertThat(um.getProbability(um.getTransitions(um.getState("state4"), um.getAction("return"), um.getState("state1")).next()), closeTo(1.0, 0.001));
        assertThat(um.getProbability(um.getTransitions(um.getState("state5"), um.getAction("serveSoda"), um.getState("state7")).next()), closeTo(1.0, 0.001));
        assertThat(um.getProbability(um.getTransitions(um.getState("state6"), um.getAction("serveTea"), um.getState("state7")).next()), closeTo(1.0, 0.001));
        assertThat(um.getProbability(um.getTransitions(um.getState("state7"), um.getAction("open"), um.getState("state8")).next()), closeTo(0.5, 0.001));
        assertThat(um.getProbability(um.getTransitions(um.getState("state7"), um.getAction("take"), um.getState("state1")).next()), closeTo(0.5, 0.001));
        assertThat(um.getProbability(um.getTransitions(um.getState("state8"), um.getAction("take"), um.getState("state9")).next()), closeTo(1.0, 0.001));
        assertThat(um.getProbability(um.getTransitions(um.getState("state9"), um.getAction("close"), um.getState("state1")).next()), closeTo(1.0, 0.001));
    }

}
