package be.vibes.ts;

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

import com.google.common.collect.Lists;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultUsageModelTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(DefaultUsageModelTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOG.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };


    // Transitions testing
    @Test
    public void testAddTranstition() {
        DefaultUsageModel ts = new DefaultUsageModel("s0");
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        Action act = ts.addAction("ts");
        Transition t1 = ts.addTransition(s0, act, s1);
        assertThat(t1.getAction(), equalTo(act));
        assertThat(t1.getSource(), equalTo(s0));
        assertThat(t1.getTarget(), equalTo(s1));
        assertThat(Lists.newArrayList(ts.getOutgoing(s0)), contains(t1));
        assertThat(Lists.newArrayList(ts.getIncoming(s1)), contains(t1));
    }

    @Test
    public void testSetTranstitionProbability() {
        DefaultUsageModel ts = new DefaultUsageModel("s0");
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        Action act = ts.addAction("ts");
        Transition t1 = ts.addTransition(s0, act, s1);
        assertThat(ts.getProbability(t1), greaterThanOrEqualTo(0.0));
        assertThat(ts.getProbability(t1), lessThanOrEqualTo(1.0));
        ts.setProbability(t1, 0.42);
        assertThat(ts.getProbability(t1), equalTo(0.42));
    }

}
