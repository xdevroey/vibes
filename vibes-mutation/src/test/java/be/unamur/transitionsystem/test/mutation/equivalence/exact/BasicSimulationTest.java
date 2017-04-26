/**
 * 
 */
package be.unamur.transitionsystem.test.mutation.equivalence.exact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.mutation.MutationOperator;
import be.unamur.transitionsystem.test.mutation.TransitionMissing;
import be.unamur.transitionsystem.test.mutation.TransitionSelectionStrategy;
import be.unamur.transitionsystem.transformation.xml.LtsHandler;
import be.unamur.transitionsystem.transformation.xml.XmlReader;
//import be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders;

/**
 * @author gperroui
 *
 */
public class BasicSimulationTest {
	
	 private Logger logger = LoggerFactory.getLogger(BasicSimulationTest.class);

	    @Rule
	    public TestRule watcher = new TestWatcher() {
	        protected void starting(Description description) {
	            logger.info(String.format("Starting test: %s()...",
	                    description.getMethodName()));
	        }
	    ;

	    };
	
	    
	    @Test
	    public void testSimulationEquivalentSameSystem() throws Exception {
	    	 InputStream input = this.getClass().getClassLoader()
		                .getResourceAsStream("ts-sodaVendingMachine.xml");
		        LtsHandler handler = new LtsHandler();
		        XmlReader reader = new XmlReader(handler, input);
		        reader.readDocument();
		        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
		       //this.getClass().getClassLoader().getResou
		        
		        assertNotNull(system);
		        logger.info("Transition System {}", system);
		        
		        
		        DFABasicSimulation sim = new DFABasicSimulation();
			       
		        assertEquals(sim.isBreadthFirstSimulation(system, system), true);
		        sim.printSimulationPairs();
		        InputStream input2 = this.getClass().getClassLoader()
		                .getResourceAsStream("claroline.ts");
		        LtsHandler handler2 = new LtsHandler();
		        XmlReader reader2 = new XmlReader(handler2, input2);
		        reader2.readDocument();
		        //assertNotNull(object);
		        LabelledTransitionSystem ts = (LabelledTransitionSystem) handler2.geTransitionSystem();
		        
		        
		        
		        assertEquals(sim.isBreadthFirstSimulation(ts, ts), true);
	    }
	    
	    @Test
	    public void testSimulationTransitionMissing() throws Exception {
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
	    
	        DFABasicSimulation sim = new DFABasicSimulation();
	       
	        assertEquals(sim.isBreadthFirstSimulation(system, system2), false);
	    }

	    

}
