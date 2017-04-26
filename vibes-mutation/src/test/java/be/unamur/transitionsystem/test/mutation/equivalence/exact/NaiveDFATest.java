package be.unamur.transitionsystem.test.mutation.equivalence.exact;

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
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.mutation.ActionExchange;
import be.unamur.transitionsystem.test.mutation.ActionMissing;
import be.unamur.transitionsystem.test.mutation.ActionSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.MutationOperator;
import be.unamur.transitionsystem.test.mutation.StateMissing;
import be.unamur.transitionsystem.test.mutation.StateSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.TransitionAdd;
import be.unamur.transitionsystem.test.mutation.TransitionDestinationExchange;
import be.unamur.transitionsystem.test.mutation.TransitionMissing;
import be.unamur.transitionsystem.test.mutation.TransitionSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.WrongInitialState;
import be.unamur.transitionsystem.transformation.xml.LtsHandler;
import be.unamur.transitionsystem.transformation.xml.XmlReader;

public class NaiveDFATest {

	private Logger logger = LoggerFactory.getLogger(NaiveDFATest.class);

	@Rule
	public TestRule watcher = new TestWatcher() {
		protected void starting(Description description) {
			logger.info(String.format("Starting test: %s()...",
					description.getMethodName()));
		}
		;

	};





	@Test
	public void testNaiveEquivalentSameSystem() throws Exception {
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("ts-sodaVendingMachine.xml");
		LtsHandler handler = new LtsHandler();
		XmlReader reader = new XmlReader(handler, input);
		reader.readDocument();
		LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
		//this.getClass().getClassLoader().getResou

		assertNotNull(system);
		logger.info("Transition System {}", system);


		NFANaive naiveEq = new NFANaive();

		assertEquals(naiveEq.computeHKEquivalence(system, system), true);


	}


	@Test
	public void testNaiveEquivalentSameSystem2() throws Exception {
		InputStream input2 = this.getClass().getClassLoader()
				.getResourceAsStream("claroline.ts");
		LtsHandler handler2 = new LtsHandler();
		XmlReader reader2 = new XmlReader(handler2, input2);
		reader2.readDocument();
		//assertNotNull(object);
		LabelledTransitionSystem ts = (LabelledTransitionSystem) handler2.geTransitionSystem();
		assertNotNull(ts);
		logger.info("Transition System {}", ts);

		NFANaive naiveEq = new NFANaive();
		assertEquals(naiveEq.computeHKEquivalence(ts, ts), true);

	}

	//@Test only for scalability
	public void testNaiveEquivalentSameSystem3() throws Exception {

		InputStream input3 = this.getClass().getClassLoader()
				.getResourceAsStream("largeRandom.ts");
		LtsHandler handler3 = new LtsHandler();
		XmlReader reader3 = new XmlReader(handler3, input3);
		reader3.readDocument();
		//assertNotNull(object);
		LabelledTransitionSystem ts2 = (LabelledTransitionSystem) handler3.geTransitionSystem();

		NFANaive naiveEq = new NFANaive();
		assertEquals(naiveEq.computeHKEquivalence(ts2, ts2), true);
	}

	@Test
	public void testNaiveTransitionMissing() throws Exception {
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("ts-sodaVendingMachine.xml");
		LtsHandler handler = new LtsHandler();
		XmlReader reader = new XmlReader(handler, input);
		reader.readDocument();
		LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
		assertNotNull(system);
		logger.info("Transition System {}", system);

		final Transition tr = system.getState("state2").outgoingTransitions().next();
		TransitionMissing op = new TransitionMissing(system, new TransitionSelectionStrategy() {
			@Override
			public Transition selectTransition(MutationOperator op, TransitionSystem ts) {
				return tr;
			}
		});
		op.apply();
		LabelledTransitionSystem system2 =  (LabelledTransitionSystem) op.result();

		NFANaive naiveEq = new NFANaive();

		assertEquals(naiveEq.computeHKEquivalence(system, system2), false);
	}


	@Test
	public void testDFATransitionAdd() throws Exception {
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("ts-sodaVendingMachine.xml");
		LtsHandler handler = new LtsHandler();
		XmlReader reader = new XmlReader(handler, input);
		reader.readDocument();
		LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
		assertNotNull(system);
		logger.info("Transition System {}", system);
		final State from = system.getState("state2");
		final State to = system.getState("state6");
		final Action action = system.addAction("serveSoda");
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

		NFANaive naiveEq = new NFANaive();
		assertEquals(naiveEq.computeHKEquivalence(system, mutant), false);   

	}

	@Test
    public void testDFATransitionDestinationExchange() throws Exception {
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
        LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();
        
        NFANaive naiveEq = new NFANaive();
		assertEquals(naiveEq.computeHKEquivalence(system, mutant), false);   
    }
	
	
	@Test
    public void testDFAActionMissing() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("ts-sodaVendingMachine.xml");
        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
        assertNotNull(system);
        logger.info("Transition System {}", system);
        final Transition selectedTransition = system.getInitialState()
                .outgoingTransitions().next();
        logger.debug("Selected transition = {}", selectedTransition);
        ActionMissing op = new ActionMissing(system, new TransitionSelectionStrategy() {
            @Override
            public Transition selectTransition(MutationOperator op, TransitionSystem ts) {
                return selectedTransition;
            }
        });
        op.apply();
        assertEquals("Wrong transition!", selectedTransition, op.getTransition());
        LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();
        logger.debug("Mutant = {}", mutant);
        
        NFANaive naiveEq = new NFANaive();
		assertEquals(naiveEq.computeHKEquivalence(system, mutant), false);   
    }
	
	@Test
	public void testDFAActionExchange() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("ts-sodaVendingMachine.xml");
        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
        assertNotNull(system);
        logger.info("Transition System {}", system);
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
        assertEquals("Wrong new action!", selectedAction, op.getNewAction());
        assertEquals("Wrong transition!", selectedTransition, op.getTransition());
        LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();
        logger.debug("Mutant = {}", mutant);
        
        NFANaive naiveEq = new NFANaive();
		assertEquals(naiveEq.computeHKEquivalence(system, mutant), false);
        
    }


	@Test
	public void testNaiveStateMissing() throws Exception {
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("ts-sodaVendingMachine.xml");
		LtsHandler handler = new LtsHandler();
		XmlReader reader = new XmlReader(handler, input);
		reader.readDocument();
		LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
		assertNotNull(system);
		logger.info("Transition System {}", system);

		final State s = system.getState("state4");

		StateMissing op = new StateMissing(system, new StateSelectionStrategy() {
			@Override
			public State selectState(MutationOperator op, TransitionSystem ts) {
				return s;
			}
		});
		op.apply();
		LabelledTransitionSystem system2 =  (LabelledTransitionSystem) op.result();

		NFANaive naiveEq = new NFANaive();
		assertEquals(naiveEq.computeHKEquivalence(system, system2), false);
	}
	
	
	

	@Test
	public void testNaiveWrongInitialState() throws Exception {
		InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("ts-sodaVendingMachine.xml");
		LtsHandler handler = new LtsHandler();
		XmlReader reader = new XmlReader(handler, input);
		reader.readDocument();
		LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
		assertNotNull(system);
		logger.info("Transition System {}", system);

		final State s = system.getState("state4");

		WrongInitialState op = new  WrongInitialState (system, new StateSelectionStrategy() {
			@Override
			public State selectState(MutationOperator op, TransitionSystem ts) {
				return s;
			}
		});
		op.apply();
		LabelledTransitionSystem system2 =  (LabelledTransitionSystem) op.result();

		NFANaive naiveEq = new NFANaive();
		assertEquals(naiveEq.computeHKEquivalence(system, system2), false);
	}


}
