/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.test.mutation.equivalence.exact;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.mutation.ActionSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.MutationOperator;
import be.unamur.transitionsystem.test.mutation.StateSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.TransitionAdd;
import be.unamur.transitionsystem.transformation.xml.LtsHandler;
import be.unamur.transitionsystem.transformation.xml.XmlReader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Random;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author gperroui
 */
public class DeterminismTest {
    
    private Logger logger = LoggerFactory.getLogger(DeterminismTest.class);

	@Rule
	public TestRule watcher = new TestWatcher() {
		protected void starting(Description description) {
			logger.info(String.format("Starting test: %s()...",
					description.getMethodName()));
		}
		;

	};

        @Test
        public void TestDeterminismOK() throws Exception {
            InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("largeRandom.ts");
		LtsHandler handler = new LtsHandler();
		XmlReader reader = new XmlReader(handler, input);
                
                reader.readDocument();
		LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
                assertTrue(Determinism.isDeterministic(system));
        }

        @Test
        public void testDeterminismKOBasic()  throws Exception {
        
         InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("ts-sodaVendingMachine.xml");
		LtsHandler handler = new LtsHandler();
		XmlReader reader = new XmlReader(handler, input);
		reader.readDocument();
		LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
		assertNotNull(system);
		logger.info("Transition System {}", system);
		final State from = system.getState("state1");
		final State to = system.getState("state9");
		final Action action = system.addAction("pay");
		TransitionAdd op = new TransitionAdd(system, new StateSelectionStrategy() {
			private boolean used = false;

			@Override
			public State selectState(MutationOperator op, TransitionSystem ts) {
				if (!used) {
					used = true;
					return from;
				}
				return to;
			}
		}, new ActionSelectionStrategy() {
			@Override
			public Action selectAction(MutationOperator op, TransitionSystem ts) {
				return action;
			}
		});
		op.apply();
		LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();
		logger.debug("Mutant = {}", mutant);
                
                
                assertFalse(Determinism.isDeterministic(mutant));
        }
        
        
        
        @Test
        public void testDeterminismKORandom()  throws Exception {
        
         InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("largeRandom.ts");
		LtsHandler handler = new LtsHandler();
		XmlReader reader = new XmlReader(handler, input);
                
                reader.readDocument();
		LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
		
		assertNotNull(system);
		//logger.info("Transition System {}", system);
		
                Random  rn  = new java.util.Random();
                int rs = rn.nextInt(system.numberOfStates());
                
                ImmutableList<State> listStates = ImmutableList.copyOf(system.states());
                
                State from = listStates.get(rs);
                while( from.outgoingSize() == 0) {
                    from = listStates.get(rn.nextInt(system.numberOfStates()));
                }
               ImmutableList<Transition> listTrans = ImmutableList.copyOf(from.outgoingTransitions());
               
               Transition tr = listTrans.get(rn.nextInt(from.outgoingSize()));
                
               State to  = listStates.get(rn.nextInt(system.numberOfStates()));
               while (to.getName().equals(tr.getTo().getName())) {
                to  = listStates.get(rn.nextInt(system.numberOfStates()));   
               }
               
                
              system.addTransition(from, to, tr.getAction());
              
                
                assertFalse(Determinism.isDeterministic(system));
        }
        
        
        
        

    
}
