package be.unamur.transitionsystem.fts;

/*
 * #%L
 * vibes-core
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
import be.vibes.fexpression.FExpression;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;

public class FeaturedTransitionSystemTest extends FeaturedTransitionSystem {

    private static final Logger logger = LoggerFactory
            .getLogger(FeaturedTransitionSystemTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	protected FeaturedTransitionSystem getTransitionSystem() {
        return new FeaturedTransitionSystem();
    }

    // Transitions testing
    @Test
    public void testAddTranstition() {
        FeaturedTransitionSystem ts = getTransitionSystem();
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        ts.setInitialState(s0);
        Action act = ts.addAction("ts");
        Transition t1 = ts.addTransition(s0, s1, act);
        assertTrue("Transition is not instance of FTSTransition",
                t1 instanceof FeaturedTransition);
        assertTrue("Wrong action", t1.getAction() == act);
        assertTrue("Wrong source", t1.getFrom() == s0);
        assertTrue("Wrong destination", t1.getTo() == s1);
        assertTrue("Wrong feature expression",
                ((FeaturedTransition) t1).getFeatureExpression().equals(FExpression.trueValue()));
        assertTrue("s0.outgoing not up to date", s0.containsOutgoing(t1));
        assertTrue("s1.incoming not up to date", s1.containsIncoming(t1));
    }

    /* Removed as FTSTransitions with only different feature expressions are considered as equal
     @Test
     public void testAddTranstitionMultiples() {
     FeaturedTransitionSystem ts = getTransitionSystem();
     State s0 = ts.addState("s0");
     State s1 = ts.addState("s1");
     ts.setInitialState(s0);
     Action act = ts.addAction("ts");
     Transition t1 = ts.addTransition(s0, s1, act);
     FeaturedTransition t2 = ts.addTransition(s0, s1, act, Constant.FALSE);
     assertNotEquals(t1, t2);
     assertTrue("s0.outgoing not up to date", s0.containsOutgoing(t1));
     assertTrue("s1.incoming not up to date", s1.containsIncoming(t1));
     assertTrue("s0.outgoing not up to date", s0.containsOutgoing(t2));
     assertTrue("s1.incoming not up to date", s1.containsIncoming(t2));
     assertEquals("Wrong number of outgoing transitions in s0", 2, s0.outgoingSize());
     assertEquals("Wrong number of incoming transitions in s1", 2, s1.incomingSize());
     }
     */
    @Test
    public void testAddTranstitionExisting() {
        FeaturedTransitionSystem ts = getTransitionSystem();
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        ts.setInitialState(s0);
        Action act = ts.addAction("ts");
        Transition t1 = ts.addTransition(s0, s1, act);
        FeaturedTransition t2 = ts.addTransition(s0, s1, act, FExpression.trueValue());
        assertEquals(t1, t2);
        assertTrue("s0.outgoing not up to date", s0.containsOutgoing(t1));
        assertTrue("s1.incoming not up to date", s1.containsIncoming(t1));
        assertTrue("s0.outgoing not up to date", s0.containsOutgoing(t2));
        assertTrue("s1.incoming not up to date", s1.containsIncoming(t2));
        assertEquals("Wrong number of outgoing transitions in s0", 1, s0.outgoingSize());
        assertEquals("Wrong number of incoming transitions in s1", 1, s1.incomingSize());
    }

    @Test
    public void testNewFtsCopy() {
        FeaturedTransitionSystem ts = getTransitionSystem();
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        ts.setInitialState(s0);
        Action act = ts.addAction("ts");
        FeaturedTransition t1 = ts.addTransition(s0, s1, act, FExpression.featureExpr("feat"));
        FeaturedTransitionSystem copy = new FeaturedTransitionSystem(ts);
        assertTrue("Wrong initial state!", copy.getInitialState().equals(s0));
        assertEquals("Wrong number of state!", 2, copy.numberOfStates());
        assertEquals("Wrong number of transitions!", 1, copy.getInitialState().outgoingSize());
        FeaturedTransition tr = (FeaturedTransition) copy.getInitialState().outgoingTransitions().next();
        assertEquals("Wrong feature expression!", tr.getFeatureExpression(), FExpression.featureExpr("feat"));
    }

}
