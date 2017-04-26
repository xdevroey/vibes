/**
 * 
 */
package be.unamur.transitionsystem.transformation.ba;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;

/**
 * @author gperroui
 *
 */
public class LtsBaPrinter {
	
	protected LabelledTransitionSystem lts;
    protected PrintStream out;
    private Map<State, String> stateIds;
    private Map<Action, String> actionIds;
    private int stateCpt = 0;
    private int actionCpt = 0;
	
    public LtsBaPrinter(PrintStream output, LabelledTransitionSystem lts) {
    	this.out = output;
    	this.lts = lts;
    	 this.stateIds = Maps.newHashMap();
    	 this.actionIds = Maps.newHashMap();
    }
    
   

    protected String getStateId(State state) {
        String id = stateIds.get(state);
        if (id == null) {
            id = (new Integer(stateCpt)).toString();
            stateIds.put(state, id);
            stateCpt++;
        }
        return id;
    }
    
    protected String getActionId(Action action) {
        String id = actionIds.get(action);
        if (id == null) {
            id = (new Integer(actionCpt)).toString();
            actionIds.put(action, id);
            actionCpt++;
        }
        return id;
    }
    
    public void printBa() {
    	// printing initial state   
    	out.println("["+ getStateId(lts.getInitialState()) +"]");
    	
    	// printing transition according to BA format
    	Iterator<State> stateIt=  lts.states();
    	while (stateIt.hasNext()) {
    		State s = stateIt.next();
    		Iterator<Transition> transIt=  s.outgoingTransitions();
    		while (transIt.hasNext()) {
    			Transition tr =  transIt.next();
    			out.println(getActionId(tr.getAction()) + ","+ "["+ getStateId(s) +"]"+ "->" + "["+ getStateId(tr.getTo()) +"]");
    		}
    		
    		}
    	
    	
    	// printing final state (in our case it is also the initial state)
    	out.println("["+ getStateId(lts.getInitialState()) +"]");
    }

}
