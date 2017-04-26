/**
 * 
 */
package be.unamur.transitionsystem.test.mutation.equivalence.exact;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;



/**
 * @author gperroui
 *
 */
public class DFABasicSimulation {

	private static final Logger logger = LoggerFactory
			.getLogger(DFABasicSimulation.class);

	private ArrayList<DFAPair> simulatingPairs = new ArrayList<DFAPair>(); 
	private ArrayList<DFAPair> nonSimulatingPairs = new ArrayList<DFAPair>();
	private ArrayList<State> nonCoveredSates =  new ArrayList<State>();
	private HashMap<State,String> colorA = new HashMap<State,String>();
	private HashMap<State,String> colorB = new HashMap<State,String>();

	public boolean  isBreadthFirstSimulation(LabelledTransitionSystem a, LabelledTransitionSystem b) {

		ArrayDeque<State> bfQueueA = new ArrayDeque<State>();
		ArrayDeque<State> bfQueueB = new ArrayDeque<State>();
		

		/*Pair initPair = new Pair(a.getInitialState(),b.getInitialState()); 
		if (!isStateSimulation(initPair)) {
			logger.debug("initial states of A cannot be simulated by initial state of B ! ");
			return false;
		}*/

		// initializing breadth-first exploration  for LTS A ! 
		Iterator<State> aStateIt = a.states();  
		while (aStateIt.hasNext()) {
			State s = aStateIt.next();
			if (s!=a.getInitialState()) {
				colorA.put(s,"white");
			}
		}
		colorA.put(a.getInitialState(), "grey");
		bfQueueA.addFirst(a.getInitialState());
		
		// initializing breadth-first exploration  for LTS B ! 
		Iterator<State> bStateIt = b.states();  
		while (bStateIt.hasNext()) {
			State s = bStateIt.next();
			if (s!=b.getInitialState()) {
				colorB.put(s,"white");
			}
		}
		colorB.put(b.getInitialState(), "grey");
		bfQueueB.addFirst(b.getInitialState());

		while (!bfQueueA.isEmpty()) {
			State uX = bfQueueA.peekFirst(); // equivalent to method peek but more clear for code inspection  ;)
			State uY = bfQueueB.peekFirst();
			
			if ((uX == a.getInitialState()) && (uY == b.getInitialState())) {
				if (!isStateSimulation(new DFAPair(uX,uY))) {
					logger.debug("initial states of A cannot be simulated by initial state of B ! ");
					nonCoveredSates.add(uX);
					return false;
				}
			}		
			
			for (State succX:  adjacentStates(uX)) {
				
				if (!isSimulationCovered(succX, adjacentStates(uY))) {
					return false;
				}
				
				if (colorA.get(succX).equals("white")) {
					colorA.put(succX, "grey");
					bfQueueA.addLast(succX);
				}
			}
			bfQueueA.pollFirst();
			colorA.put(uX,"black");
			 
			for (State succY:  adjacentStates(uY)) {
				if (colorB.get(succY).equals("white")) {
					colorA.put(succY, "grey");
					bfQueueB.addLast(succY);
				}
			}
			bfQueueB.pollFirst();
			colorB.put(uY,"black");
			
		}


		return true;
	}

  
	
	public HashSet<State> adjacentStates(State s) {

		HashSet<State> adjStates =  new HashSet<State>();

		Iterator<Transition> transITout =  s.outgoingTransitions();
		Iterator<Transition> transITin =  s.incomingTransitions();
		while (transITout.hasNext()) {
			adjStates.add(transITout.next().getTo());

		}
		while (transITin.hasNext()) {
			Transition tr = transITin.next();
			//logger.debug("To: "+ tr.getTo().getName());
			//logger.debug("From: "+ tr.getFrom().getName());
			adjStates.add(tr.getFrom());

		}
		return adjStates;  
	}


	public HashSet<State> post(State s) {

		HashSet<State> postStates =  new HashSet<State>();

		Iterator<Transition> transIT =  s.outgoingTransitions();
		while (transIT.hasNext()) {
			postStates.add(transIT.next().getTo());

		}
		return postStates;  
	}


	public ArrayList<Action> actPost(State s) {

		ArrayList<Action> postActions =  new ArrayList<Action>();

		Iterator<Transition> transIT =  s.outgoingTransitions();
		while (transIT.hasNext()) {
			postActions.add(transIT.next().getAction());
		}
		return postActions;  
	}

	public boolean isStateSimulation(DFAPair pair) {

		if (actPost(pair.y).containsAll(actPost(pair.x))) {
			//logger.debug("State " +  pair.x.getName() +" is simulated by "+ pair.y.getName());
			simulatingPairs.add(pair);
			return true;
		} else { 
			//logger.debug("State " +  pair.x.getName() +" is not simulated by "+ pair.y.getName());
			nonSimulatingPairs.add(pair);
			return false;
		}



	}


	public boolean isSimulationCovered(State uX, HashSet<State> setY) {

		
			boolean res =false;
			for (State sY:  setY) {
				if (isStateSimulation(new DFAPair(uX,sY))) {
					res = true;
				}
			}
			if (!res) {
				logger.debug("state sX: " + uX.getName()+ " is not simulated by B !");
				nonCoveredSates.add(uX);
				return false;
			}

		return true;

	}
	
	
	public void printNonCoveredStates() {
		for (State s: nonCoveredSates) {
			System.out.print(s.getName()+" ");
		}
	}
	
	public HashMap<State,String> getAColors() {
		return colorA;
	}
	
	public void printSimulationPairs() {
		for (DFAPair p:  simulatingPairs) {
			System.out.println(p.toString());
		}
	}
	
}


