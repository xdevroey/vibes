package be.unamur.transitionsystem.test.mutation;

/*
 * #%L
 * vibes-mutation
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
import be.unamur.fts.fexpression.FExpression;
import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.transformation.xml.LtsHandler;
import be.unamur.transitionsystem.transformation.xml.XmlReader;
import static org.hamcrest.Matchers.*;

public class TransitionMissingTest {

    private Logger LOG = LoggerFactory.getLogger(TransitionMissingTest.class);

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
    public void testResult() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("ts-sodaVendingMachine.xml");
        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
        assertNotNull(system);
        LOG.debug("Transition System {}", system);
        final Transition tr = system.getState("state2").outgoingTransitions().next();
        TransitionMissing op = new TransitionMissing(system, new TransitionSelectionStrategy() {
            @Override
            public Transition selectTransition(MutationOperator op, TransitionSystem ts) {
                return tr;
            }
        });
        op.apply();
        assertEquals("Wrong transition!", tr, op.getTransition());
        LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();
        LOG.debug("Mutant = {}", mutant);
        assertEquals("Wrong number of transitions!", 0, mutant.getState("state2").outgoingSize());
    }

    @Test
    public void testTranspose() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("ts-sodaVendingMachine.xml");
        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
        assertNotNull(system);
        LOG.debug("Transition System {}", system);
        final Transition tr = system.getState("state2").outgoingTransitions().next();
        TransitionMissing op = new TransitionMissing(system, new TransitionSelectionStrategy() {
            @Override
            public Transition selectTransition(MutationOperator op, TransitionSystem ts) {
                return tr;
            }
        });
        op.apply();
        assertEquals("Wrong transition!", tr, op.getTransition());
        FeaturedTransitionSystem mutant = op.transpose(new FeaturedTransitionSystem(system));
        LOG.debug("FTS Mutant = {}", mutant);
        State s = mutant.getState("state2");
        assertEquals(1, s.outgoingSize());
        assertThat((FeaturedTransition) s.outgoingTransitions().next(), equalTo(
                new FeaturedTransition(mutant.getState(tr.getFrom().getName()), mutant.getState(tr.getTo().getName()), mutant.getAction(tr.getAction().getName()), new FExpression("!" + op.getFeatureId()))));
    }

}
