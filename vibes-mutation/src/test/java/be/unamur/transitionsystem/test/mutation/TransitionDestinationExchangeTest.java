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

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.transformation.xml.LtsHandler;
import be.unamur.transitionsystem.transformation.xml.XmlReader;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

public class TransitionDestinationExchangeTest {

    private Logger logger = LoggerFactory.getLogger(TransitionDestinationExchangeTest.class);

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
    public void testResult() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("ts-sodaVendingMachine.xml");
        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
        assertNotNull(system);
        logger.info("Transition System {}", system);
        final Transition tr = system.getState("state2").outgoingTransitions().next();
        final State to = system.getState("state6");
        TransitionDestinationExchange op = new TransitionDestinationExchange(system, new TransitionSelectionStrategy() {
            @Override
            public Transition selectTransition(MutationOperator op, TransitionSystem ts) {
                return tr;
            }
        }, new StateSelectionStrategy() {
            @Override
            public State selectState(MutationOperator op, TransitionSystem ts) {
                return to;
            }
        });
        op.apply();
        assertEquals("Wrong From state!", tr, op.getTransition());
        assertEquals("Wrong To state!", to, op.getNewDest());
        LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();
        logger.debug("Mutant = {}", mutant);
        assertEquals(mutant.getState("state6"), mutant.getState("state2").outgoingTransitions().next().getTo());
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
        logger.info("Transition System {}", system);
        final Transition tr = system.getState("state2").outgoingTransitions().next();
        final State to = system.getState("state6");
        TransitionDestinationExchange op = new TransitionDestinationExchange(system, new TransitionSelectionStrategy() {
            @Override
            public Transition selectTransition(MutationOperator op, TransitionSystem ts) {
                return tr;
            }
        }, new StateSelectionStrategy() {
            @Override
            public State selectState(MutationOperator op, TransitionSystem ts) {
                return to;
            }
        });
        op.apply();
        FeaturedTransitionSystem mutant = op.transpose(new FeaturedTransitionSystem(system));
        logger.debug("FTS Mutant = {}", mutant);
        State s = mutant.getState("state2");
        Set<FeaturedTransition> trsToCheck = Sets.newHashSet();
        for (Iterator iterator = s.outgoingTransitions(); iterator.hasNext();) {
            FeaturedTransition next = (FeaturedTransition) iterator.next();
            trsToCheck.add(next);
        }
        assertThat(trsToCheck, hasSize(2));
        assertThat(trsToCheck, containsInAnyOrder(new FeaturedTransition(mutant.getState(s.getName()), mutant.getState(tr.getTo().getName()), mutant.getAction(tr.getAction().getName()),new FExpression("!"+op.getFeatureId())), 
                new FeaturedTransition(mutant.getState(s.getName()), mutant.getState(to.getName()), mutant.getAction(tr.getAction().getName()), new FExpression(op.getFeatureId()))));
    }

}
