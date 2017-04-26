package be.unamur.transitionsystem.transformation.xml;

/*
 * #%L
 * vibes-transformation
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

import java.io.InputStream;
import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;

public class FtsHandlerTest {

    private static final Logger logger = LoggerFactory.getLogger(FtsHandlerTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	@Test
    public void test() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("fts-sodaVendingMachine.xml");
        FtsHandler handler = new FtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        FeaturedTransitionSystem system = (FeaturedTransitionSystem) handler.geTransitionSystem();
        assertNotNull(system);
        logger.info("Transition System {}", system);
        assertEquals("Wrong initial state!", "state1", system.getInitialState().getName());
    }

    @Test
    public void testNewFeaturedTransitionSystemFromTransitionSystemInterface()
            throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("ts-sodaVendingMachine.xml");
        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
        assertNotNull(system);
        logger.info("Otiginal Transition System {}", system);
        // Construct FTS from given TS
        FeaturedTransitionSystem fts = new FeaturedTransitionSystem(system);
        logger.info("Copy Features Transition System {}", fts);
        assertEquals("Wrong Initial State!", system.getInitialState(),
                fts.getInitialState());
        Iterator<State> copyStatesIt = fts.states();
        Iterator<State> originalStatesIt = system.states();
        State s, sCopy;
        while (originalStatesIt.hasNext()) {
            assertTrue("Wrong number of states in FTS copy!", copyStatesIt.hasNext());
            copyStatesIt.next();
            s = originalStatesIt.next();
            sCopy = fts.getState(s.getName());
            assertNotNull("State " + s.getName() + " not found in copy!", sCopy);
            assertEquals(s, sCopy);
            // Check Incoming Transitions
            assertEquals("Wrong number of incoming transitions", s.incomingSize(), sCopy.incomingSize());
            // Check Outgoing Transitions
            assertEquals("Wrong number of outgoing transitions", s.outgoingSize(), sCopy.outgoingSize());
        }
        assertFalse("Wrong number of states in FTS copy!", copyStatesIt.hasNext());
    }

}
