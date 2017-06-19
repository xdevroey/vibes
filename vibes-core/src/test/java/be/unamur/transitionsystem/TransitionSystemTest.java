package be.unamur.transitionsystem;

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
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransitionSystemTest extends LabelledTransitionSystem {

    private static final Logger logger = LoggerFactory.getLogger(TransitionSystemTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };
	
	protected LabelledTransitionSystem getTransitionSystem() {
        return new LabelledTransitionSystem();
    }

    // States testing
    @Test
    public void testAddState() {
        LabelledTransitionSystem ts = getTransitionSystem();
        State s = ts.addState("initial");
        assertNotNull(s);
        assertEquals("State name invalid", "initial", s.getName());
        assertEquals("New state should not have incoming transitions", 0, s.getIncoming().size());
        assertEquals("New state should not have outgoing transitions", 0, s.getOutgoing().size());
        State s2 = ts.getState("initial");
        assertTrue("States should be the same using '=='", s == s2);
    }

    @Test
    public void testAddStateExisting() {
        LabelledTransitionSystem ts = getTransitionSystem();
        State s = ts.addState("initial");
        assertNotNull(s);
        State s2 = ts.addState("initial");
        assertTrue("States should be the same using '=='", s == s2);
    }

    @Test
    public void testSetIntiialState() {
        LabelledTransitionSystem ts = getTransitionSystem();
        State s = ts.addState("initial");
        assertNotNull(s);
        ts.setInitialState(s);
        State s2 = ts.getInitialState();
        assertTrue("States should be the same using '=='", s == s2);
    }

    // Actions testing 
    @Test
    public void testAddAction() {
        LabelledTransitionSystem ts = getTransitionSystem();
        Action act = ts.addAction("act");
        assertNotNull(act);
        Action act2 = ts.getAction("act");
        assertTrue("Actions should be the same using '=='", act == act2);
    }

    @Test
    public void testAddActionExisting() {
        LabelledTransitionSystem ts = getTransitionSystem();
        Action act = ts.addAction("act");
        assertNotNull(act);
        Action act2 = ts.addAction("act");
        assertTrue("Actions should be the same using '=='", act == act2);
    }

    // Transitions testing
    @Test
    public void testAddTranstition() {
        LabelledTransitionSystem ts = getTransitionSystem();
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        ts.setInitialState(s0);
        Action act = ts.addAction("ts");
        Transition t1 = ts.addTransition(s0, s1, act);
        assertTrue("Wrong action", t1.getAction() == act);
        assertTrue("Wrong source", t1.getFrom() == s0);
        assertTrue("Wrong destination", t1.getTo() == s1);
        assertTrue("s0.outgoing not up to date", s0.getOutgoing().contains(t1));
        assertTrue("s1.incoming not up to date", s1.getIncoming().contains(t1));
    }

    @Test
    public void testAddTranstitionExisting() {
        LabelledTransitionSystem ts = getTransitionSystem();
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        ts.setInitialState(s0);
        Action act = ts.addAction("ts");
        Transition t1 = ts.addTransition(s0, s1, act);
        assertTrue("s0.outgoing not up to date", s0.getOutgoing().contains(t1));
        assertTrue("s1.incoming not up to date", s1.getIncoming().contains(t1));
        Transition t2 = ts.addTransition(s0, s1, act);
        assertTrue("s0.outgoing not up to date", s0.getOutgoing().contains(t2));
        assertTrue("s1.incoming not up to date", s1.getIncoming().contains(t2));
        assertEquals("Wrong size for s0.outgoing", 1, s0.getOutgoing().size());
        assertEquals("Wrong size for s1.incoming", 1, s1.getIncoming().size());
    }

    @Test
    public void testAddTranstitionMultiples() {
        LabelledTransitionSystem ts = getTransitionSystem();
        State s0 = ts.addState("s0");
        State s1 = ts.addState("s1");
        ts.setInitialState(s0);
        Action act = ts.addAction("ts");
        Transition t1 = ts.addTransition(s0, s1, act);
        Transition t2 = ts.addTransition(s1, s1, act);
        Transition t3 = ts.addTransition(s1, s0, act);
        // t1
        assertTrue("s0.outgoing not up to date", s0.getOutgoing().contains(t1));
        assertTrue("s1.incoming not up to date", s1.getIncoming().contains(t1));
        // t2
        assertTrue("s1.outgoing not up to date", s1.getOutgoing().contains(t2));
        assertTrue("s1.incoming not up to date", s1.getIncoming().contains(t2));
        // t3
        assertTrue("s1.outgoing not up to date", s1.getOutgoing().contains(t3));
        assertTrue("s0.incoming not up to date", s0.getIncoming().contains(t3));
    }

}
