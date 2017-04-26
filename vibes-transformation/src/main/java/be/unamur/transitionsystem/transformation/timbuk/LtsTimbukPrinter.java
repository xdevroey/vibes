package be.unamur.transitionsystem.transformation.timbuk;

import java.io.PrintStream;
import java.util.Iterator;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class LtsTimbukPrinter {

    protected LabelledTransitionSystem lts;
    protected PrintStream out;
    private final Map<Action, String> actionIds;
    private final Map<State, String> stateIds;
    //private int stateCpt = 1;

    public LtsTimbukPrinter(PrintStream output, LabelledTransitionSystem lts) {
        this.out = output;
        this.lts = lts;
        this.actionIds = Maps.newHashMap();
        this.stateIds = Maps.newHashMap();

    }

    /**
     * Prints the Timbuk automata with this LTS's initial state as accepting
     * state.
     */
    public void printLTSTimbuk() {
        printLTSTimbuk(lts.getInitialState());
    }

    /**
     * Prints the Timbuk automata with the given state as accepting state.
     *
     * @param acceptingState The accepting state, must be one state of this'
     * LTS.
     */
    public void printLTSTimbuk(State acceptingState) {
        checkArgument(lts.contains(acceptingState), "Given state %s is not part of this' TS", acceptingState);
        List<State> lst = Lists.newArrayList();
        lst.add(acceptingState);
        printLTSTimbuk(lst.iterator());
    }

    /**
     * Prints the Timbuk automata with the fiven states as accepting states.
     *
     * @param acceptingStates THe accepting states, each state must belong to
     * this's LTS.
     */
    public void printLTSTimbuk(Iterable<State> acceptingStates) {
        printLTSTimbuk(acceptingStates.iterator());
    }

    /**
     * Prints the Timbuk automata with the fiven states as accepting states.
     *
     * @param acceptingStates THe accepting states, each state must belong to
     * this's LTS.
     */
    public void printLTSTimbuk(Iterator<State> acceptingStates) {
        printLTSTimbuk(acceptingStates, lts.actions());
    }

    /**
     * Prints the Timbuk automata with the fiven states as accepting states.
     *
     * @param acceptingStates The accepting states, each state must belong to
     * this's LTS.
     * @param alphabet The actions to consider in the alphabet of the produced
     * automata.
     * @throws IllegalArgumentException If one of the accepting state is not a
     * state of this LTS or if one action of this LTS is not part of the given
     * alphabet.
     */
    public void printLTSTimbuk(Iterator<State> acceptingStates, Iterator<Action> alphabet) {
        fillActionsMap(alphabet);
        fillStateMap(lts.states());

        out.print("Ops ");

        // Print actions in alphabetical order
        actionIds.values().stream().sorted().forEach(action -> out.print(action + ":1 "));
        out.println("x:0"); // special action to trigger initial state
        out.println();
        out.println("Automaton A");
        out.print("States ");
        // Print states in alphabetical order
        stateIds.values().stream().sorted().forEach(state -> out.print(state + " "));
        out.println();
        out.print("Final States"); // Indicates which states are accepting states.
        while (acceptingStates.hasNext()) {
            State s = acceptingStates.next();
            checkArgument(lts.contains(s), "Given state %s is not part of this' TS", s);
            out.print(" ");
            out.print(stateIds.get(s));
        }
        out.println();
        out.println("Transitions");
        out.println("x() -> " + stateIds.get(lts.getInitialState()));

        Iterator<State> stateIt3 = lts.states();
        while (stateIt3.hasNext()) {
            State s = stateIt3.next();
            Iterator<Transition> transIt = s.outgoingTransitions();
            while (transIt.hasNext()) {
                Transition tr = transIt.next();
                //if (!tr.getAction().getName().equals(Action.NO_ACTION_NAME)) {
                checkArgument(actionIds.containsKey(tr.getAction()), "Action %s is not part of the automata's alphabet!", tr.getAction());
                out.println(actionIds.get(tr.getAction()) + "(" + stateIds.get(tr.getFrom()) + ")" + " -> " + stateIds.get(tr.getTo()));
                //}
            }

        }
    }

    private void fillActionsMap(Iterator<Action> act) {
        while (act.hasNext()) {
            Action action = act.next();
            //if (!action.getName().equals(Action.NO_ACTION_NAME)) {
            if (action.getName().contains("_") || action.getName().contains("(") || action.getName().contains(")")) {
                actionIds.put(action, "act" + Integer.toUnsignedString(action.getName().hashCode()));
            } else {
                actionIds.put(action, "act" + action.getName());
            }
            //}
        }
    }

    private void fillStateMap(Iterator<State> stateIt) {
        while (stateIt.hasNext()) {
            State s = stateIt.next();
            if (s.getName().contains("/") || s.getName().equals("0") || s.getName().contains("_")) {
                stateIds.put(s, "state" + Integer.toUnsignedString(s.getName().hashCode()));
            } else {
                stateIds.put(s, "state" + s.getName());
            }
        }
    }

    public void printActionMapping() {
        printActionMapping(out);
    }

    public void printActionMapping(PrintStream out) {
        out.println("timbukaction,ltsaction");
        for(Map.Entry<Action, String> entry : actionIds.entrySet()){
            out.println(String.format("%s,%s", entry.getValue(), entry.getKey().getName()));
        }
    }

    public void printStateMapping() {
        printStateMapping(out);
    }

    public void printStateMapping(PrintStream out) {
        out.println("timbukstate,ltsstate");
        for(Map.Entry<State,String> entry : stateIds.entrySet()){
            out.println(String.format("%s,%s", entry.getValue(), entry.getKey().getName()));
        }
    }

}
