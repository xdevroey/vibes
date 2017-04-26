package be.unamur.transitionsystem.transformation.siberia;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;

import com.google.common.collect.Maps;

public class LtsSiberiaPrinter {

	protected LabelledTransitionSystem lts;
	protected PrintStream out;
	private Map<State, String> stateIds;
	private Map<Action, String> actionIds;
	private int stateCpt = 1;
	private int actionCpt = 0;

	public LtsSiberiaPrinter(PrintStream output, LabelledTransitionSystem lts) {
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

	public void printLTSiberia()  {
		out.println(lts.numberOfStates()); // Number of states
		out.println(lts.numberOfActions()); // Number of actions
		out.println(getStateId(lts.getInitialState())); // accepting states here only one, the initial state
		out.println("-"); // Separator for transition list...

		Iterator<State> stateIt=  lts.states();
		while (stateIt.hasNext()) {
			State s = stateIt.next();
			Iterator<Transition> transIt=  s.outgoingTransitions();
			while (transIt.hasNext()) {
				Transition tr =  transIt.next();
				out.println(getStateId(s));
				out.println(getActionId(tr.getAction()));
				out.println(getStateId(tr.getTo()));

				//out.println(getActionId(tr.getAction()) + ","+ "["+ getStateId(s) +"]"+ "->" + "["+ getStateId(tr.getTo()) +"]");
			}

		}
	}


}
