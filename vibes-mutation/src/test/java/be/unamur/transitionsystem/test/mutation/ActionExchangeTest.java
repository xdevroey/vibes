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
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Action;
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

public class ActionExchangeTest {

    private Logger logger = LoggerFactory.getLogger(ActionExchangeTest.class);

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
        logger.debug("Transition System {}", system);
        final Transition selectedTransition = system.getInitialState()
                .outgoingTransitions().next();
        logger.debug("Selected transition = {}", selectedTransition);
        final Action selectedAction = system.getAction("return");
        logger.debug("Selected Action = {}", selectedAction);
        ActionExchange op = new ActionExchange(system, new TransitionSelectionStrategy() {
            @Override
            public Transition selectTransition(MutationOperator op, TransitionSystem ts) {
                return selectedTransition;
            }
        }, new ActionSelectionStrategy() {
            @Override
            public Action selectAction(MutationOperator op, TransitionSystem ts) {
                return selectedAction;
            }
        });
        op.apply();
        assertThat(selectedAction, equalTo(op.getNewAction()));
        assertThat(selectedTransition, equalTo(op.getTransition()));
        LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();
        logger.debug("Mutant = {}", mutant);
        Transition modified = new Transition(mutant.getState(selectedTransition.getFrom()
                .getName()), mutant.getState(selectedTransition.getTo().getName()),
                mutant.getAction(selectedAction.getName()));
        assertNotNull("Modified transition not found!", mutant.getInitialState()
                .getOutTransition(modified));
    }

    @Test
    public void testTranspos() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("ts-sodaVendingMachine.xml");
        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
        assertNotNull(system);
        logger.debug("Transition System {}", system);
        final Transition selectedTransition = system.getState("state2")
                .outgoingTransitions().next();
        logger.debug("Selected transition = {}", selectedTransition);
        final Action selectedAction = system.getAction("return");
        logger.debug("Selected Action = {}", selectedAction);
        ActionExchange op = new ActionExchange(system, new TransitionSelectionStrategy() {
            @Override
            public Transition selectTransition(MutationOperator op, TransitionSystem ts) {
                return selectedTransition;
            }
        }, new ActionSelectionStrategy() {
            @Override
            public Action selectAction(MutationOperator op, TransitionSystem ts) {
                return selectedAction;
            }
        });
        op.apply();
        FeaturedTransitionSystem mutant = op.transpose(new FeaturedTransitionSystem(system));
        logger.debug("Mutant = {}", mutant);
        Set<FeaturedTransition> trsToCheck = Sets.newHashSet();
        for (Iterator iterator = mutant.getState("state2").outgoingTransitions(); iterator.hasNext();) {
            FeaturedTransition next = (FeaturedTransition) iterator.next();
            trsToCheck.add(next);
        }
        assertThat(trsToCheck, hasSize(2));
        assertThat(trsToCheck, containsInAnyOrder(new FeaturedTransition(mutant.getState(selectedTransition.getFrom().getName()), mutant.getState(selectedTransition.getTo().getName()), mutant.getAction(selectedTransition.getAction().getName()), 
                new FExpression("!" + op.getFeatureId())), new FeaturedTransition(mutant.getState(selectedTransition.getFrom().getName()), mutant.getState(selectedTransition.getTo().getName()), mutant.getAction(selectedAction.getName()), new FExpression(op.getFeatureId()))));
    }

}
