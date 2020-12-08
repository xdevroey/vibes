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

import be.vibes.fexpression.FExpression;
import static be.vibes.fexpression.FExpression.*;
import com.google.common.collect.Lists;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeaturedTransitionSystemTest{

    private static final Logger LOG = LoggerFactory
            .getLogger(FeaturedTransitionSystemTest.class);

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
        DefaultFeaturedTransitionSystem ts = new DefaultFeaturedTransitionSystem("s0");
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        Action act = ts.addAction("ts");
        Transition t1 = ts.addTransition(s0, act, s1);
        assertThat(t1.getAction(), equalTo(act));
        assertThat(t1.getSource(), equalTo(s0));
        assertThat(t1.getTarget(), equalTo(s1));
        assertThat(Lists.newArrayList(ts.getOutgoing(s0)), contains(t1));
        assertThat(Lists.newArrayList(ts.getIncoming(s1)), contains(t1));
        assertThat("Wrong feature expression", ts.getFExpression(t1), equalTo(trueValue()));
    }
    
    @Test
    public void testSetFExpression() {
        DefaultFeaturedTransitionSystem ts = new DefaultFeaturedTransitionSystem("s0");
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        Action act = ts.addAction("ts");
        Transition t1 = ts.addTransition(s0, act, s1);
        assertThat("Wrong feature expression", ts.getFExpression(t1), equalTo(trueValue()));
        FExpression expr =  featureExpr("f").and(featureExpr("g"));
        ts.setFExpression(t1, expr);
        assertThat("Wrong feature expression", ts.getFExpression(t1), equalTo(expr));
        FExpression expr2 =  featureExpr("h").or(featureExpr("i"));
        ts.setFExpression(t1, expr2);
        assertThat("Wrong feature expression", ts.getFExpression(t1), equalTo(expr2));
    }


}
