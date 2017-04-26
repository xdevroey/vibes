package be.unamur.transitionsystem.test.mutation;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;
import com.google.common.base.Preconditions;

/**
 * This strategy returns states in a given order.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class DefinedStatesSelectionStrategy implements StateSelectionStrategy {

    private State[] states;
    private int i = 0;

    /**
     * Create a new selection strategy which will select states in the givne
     * array according to its defined order.
     *
     * @param states The states to return (size must be &gt; 0). States will be
     * returned in this order with a return to the first state when then end of
     * the table is reached.F
     */
    public DefinedStatesSelectionStrategy(State... states) {
        Preconditions.checkArgument(states.length > 0, "Must specify at least one state!");
        this.states = states;
    }

    @Override
    public State selectState(MutationOperator operator, TransitionSystem ts) {
        State state = ts.getState(states[i].getName());
        Preconditions.checkNotNull(state, "Could not find state {} in TS {}!", states[i], ts);
        i = (i + 1) % states.length;
        return state;
    }

}
