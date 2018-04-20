package be.vibes.ts;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class DefaultTransitionSystem implements TransitionSystem {

    private final State initialState;

    private final Map<String, State> states;

    private final Map<String, Action> actions;

    private final Set<Transition> allTransitions;
    private final Table<State, State, Set<Transition>> transitions;

    private final Map<String, AtomicProposition> propositions;

    private final Map<State, AtomicProposition> labels;

    DefaultTransitionSystem(String initialState) {
        Preconditions.checkNotNull(initialState, "InitialState may not be null!");
        this.initialState = new State(initialState);
        this.states = new HashMap<>();
        this.states.put(initialState, this.initialState);
        this.actions = new HashMap<>();
        this.allTransitions = new HashSet<>();
        this.transitions = HashBasedTable.create();
        this.propositions = new HashMap<>();
        this.labels = new HashMap<>();
    }

    @Override
    public Iterator<State> states() {
        return this.states.values().iterator();
    }

    @Override
    public State getState(String name) {
        return this.states.get(name);
    }

    @Override
    public Iterator<Action> actions() {
        return this.actions.values().iterator();
    }

    @Override
    public Action getAction(String name) {
        return this.actions.get(name);
    }

    @Override
    public Iterator<Transition> transitions() {
        return allTransitions.iterator();
    }

    @Override
    public Iterator<Transition> getTransitions(State source, Action action, State target) {
        Set<Transition> set = transitions.row(source).get(target);
        if (set == null) {
            set = new HashSet<>();
        }
        return set.stream()
                .filter((t) -> t.getAction().equals(action))
                .iterator();
    }

    @Override
    public Iterator<Transition> getOutgoing(State source) {
        return Iterables.concat(transitions.row(source).values()).iterator();
    }

    @Override
    public Iterator<Transition> getIncoming(State target) {
        return Iterables.concat(transitions.column(target).values()).iterator();
    }

    @Override
    public Iterator<Transition> getTransitions(Action action) {
        return allTransitions.stream()
                .filter((t) -> t.getAction().equals(action))
                .iterator();
    }

    @Override
    public Iterator<AtomicProposition> atomicPropositions() {
        return propositions.values().iterator();
    }

    @Override
    public AtomicProposition getAtomicProposition(String name) {
        return propositions.get(name);
    }
    
    @Override
    public AtomicProposition getLabel(State state) {
        return labels.get(state);
    }

    @Override
    public State getInitialState() {
        return this.initialState;
    }

    State addState(String stateName) {
        State state = states.get(stateName);
        if (state == null) {
            state = new State(stateName);
            this.states.put(stateName, state);
        }
        return state;
    }

    Action addAction(String actionName) {
        Action action = actions.get(actionName);
        if (action == null) {
            action = new Action(actionName);
            this.actions.put(actionName, action);
        }
        return action;
    }

    Transition addTransition(State source, Action action, State target) {
        Preconditions.checkNotNull(source, "Source state may not be null!");
        Preconditions.checkNotNull(action, "Action may not be null!");
        Preconditions.checkNotNull(target, "Source target may not be null!");
        Preconditions.checkArgument(states.values().contains(source), "Source state does not belong to this transition system!");
        Preconditions.checkArgument(states.values().contains(target), "Target state does not belong to this transition system!");
        Preconditions.checkArgument(actions.values().contains(action), "Action does not belong to this transition system!");
        Iterator<Transition> it = getTransitions(source, action, target);
        Transition transition;
        if(it.hasNext()){
            transition = it.next();
        } else {
            if(!transitions.contains(source, target)){
                transitions.put(source, target, new HashSet<>());
            }
            transition = new Transition(source, action, target);
            transitions.get(source, target).add(transition);
            allTransitions.add(transition);
        }
        return transition;
    }
    
    AtomicProposition addProposition(String name){
        AtomicProposition prop = propositions.get(name);
        if(prop == null){
            prop = new AtomicProposition(name);
            this.propositions.put(name, prop);
        }
        return prop;
    }
    
    void setLabel(State state, AtomicProposition prop){
        Preconditions.checkNotNull(state, "State may not be null!");
        Preconditions.checkNotNull(prop, "Prop may not be null!");
        Preconditions.checkArgument(states.values().contains(state), "State does not belong to this transition system!");
        Preconditions.checkArgument(propositions.values().contains(prop), "Prop does not belong to this transition system!");
        this.labels.put(state, prop);
    }

    @Override
    public int getTransitionsCount() {
        return transitions.size();
    }

    @Override
    public int getActionsCount() {
        return actions.size();
    }

    @Override
    public int getStatesCount() {
        return states.size();
    }

    @Override
    public int getPropositionsCount() {
        return propositions.size();
    }

    @Override
    public int getOutgoingCount(State source) {
        return transitions.row(source).values().stream()
                .collect(Collectors.summingInt(Set::size));
    }

    @Override
    public int getIncomingCount(State target) {
        return transitions.column(target).values().stream()
                .collect(Collectors.summingInt(Set::size));
    }

}
