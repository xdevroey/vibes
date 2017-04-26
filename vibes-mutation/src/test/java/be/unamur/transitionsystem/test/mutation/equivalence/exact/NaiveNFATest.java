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
import be.unamur.transitionsystem.test.mutation.ActionSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.MutationOperator;
import be.unamur.transitionsystem.test.mutation.StateMissing;
import be.unamur.transitionsystem.test.mutation.StateSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.TransitionAdd;
import be.unamur.transitionsystem.test.mutation.TransitionMissing;
import be.unamur.transitionsystem.test.mutation.TransitionSelectionStrategy;
import be.unamur.transitionsystem.test.mutation.WrongInitialState;
import be.unamur.transitionsystem.transformation.xml.LtsHandler;
import be.unamur.transitionsystem.transformation.xml.XmlReader;

public class NaiveNFATest {

	private Logger logger = LoggerFactory.getLogger(NaiveNFATest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

    
    @Test
    public void testNFANaive() throws Exception  {
    	
    	
    	// From the examples "checking NFA equivalence with bisimulations up to congruence"  POPL '13 Proceedings of the 40th annual ACM SIGPLAN-SIGACT symposium on Principles of programming languages
    	//Pages 457-468 
    	 
    	
    	LabelledTransitionSystem a = new LabelledTransitionSystem();
    	// States
    	a.addState("x");
    	a.addState("y");
    	a.addState("z");
    	a.setInitialState(a.getState("y"));
    	
    	// action
    	Action  xy = a.addAction("a");
    	Action  zy = a.addAction("a");
    	Action  zx = a.addAction("a");
    	Action  yz = a.addAction("a");
    	// transitions
    	a.addTransition(a.getState("x"), a.getState("y"), xy);
    	a.addTransition(a.getState("z"), a.getState("y"), zy);
    	a.addTransition(a.getState("z"), a.getState("x"), zx);
    	a.addTransition(a.getState("y"), a.getState("z"), yz);
    	
    	
    	LabelledTransitionSystem b = new LabelledTransitionSystem();
    	// States
    	b.addState("u");
    	b.addState("w");
    	b.addState("v");
    	b.setInitialState(b.getState("v"));
    	// action
    	Action  uv = b.addAction("a");
    	Action  uw = b.addAction("a");
    	Action  vw = b.addAction("a");
    	Action  wu = b.addAction("a");
    	Action  uu = b.addAction("a");
    	// transitions
    	b.addTransition(b.getState("u"), b.getState("v"), uv);
    	b.addTransition(b.getState("u"), b.getState("w"), uw);
    	b.addTransition(b.getState("u"), b.getState("u"), uu);
    	b.addTransition(b.getState("v"), b.getState("w"), vw);
    	b.addTransition(b.getState("w"), b.getState("u"), wu);
    	
    	
    	 NFANaive naiveEq = new NFANaive();
    	   logger.info("Transition System {}", b);
	       
	        assertEquals(naiveEq.computeHKEquivalence(b, b), true);
    	
    }
   
    @Test
    public void testNFATransitionAdd() throws Exception {
    	InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("ts-sodaVendingMachine.xml");
        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
        assertNotNull(system);
        logger.info("Transition System {}", system);
        final State from = system.getState("state9");
        final State to = system.getState("state2");
        final Action action = system.addAction("close");
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
 	   
	       
	        assertEquals(naiveEq.computeHKEquivalence(mutant, system), false);
        
        
        
    }
    
    
    //@Test
    public void testNFATransitionAdd2() throws Exception {
    	InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("claroline.ts");
        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
        assertNotNull(system);
        logger.info("Transition System {}", system);
        final State from = system.getState("/claroline/announcements/announcements.php");
        final State to = system.getState("0");
        final Action action = system.addAction("clic(/claroline/auth/login.php)");
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
 	   
	       
	        assertEquals(naiveEq.computeHKEquivalence(mutant, mutant), true);
        
        
        
    }
}
