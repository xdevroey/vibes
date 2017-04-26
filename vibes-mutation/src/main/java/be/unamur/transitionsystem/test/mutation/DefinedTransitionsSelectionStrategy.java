
package be.unamur.transitionsystem.test.mutation;

import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import com.google.common.base.Preconditions;

/**
 * This strategy returns transitions in a given order.
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class DefinedTransitionsSelectionStrategy implements TransitionSelectionStrategy{
    
    private Transition[] transitions;
    private int i = 0;
    
    /**
     * Create a new selection strategy which will select actions in the givne
     * array according to its defined order.
     *
     * @param transitions The actions to return (size must be &gt; 0). Actions will be
     * return in this order with a return to the first action when the end of
     * the table is reached.
     */
    public DefinedTransitionsSelectionStrategy(Transition... transitions){
        Preconditions.checkArgument(transitions.length > 0, "Must specify at least one transition!");
        this.transitions = transitions;
    }

    @Override
    public Transition selectTransition(MutationOperator operator, TransitionSystem ts) {
        Transition tr = transitions[i];
        tr = ts.getState(tr.getFrom().getName()).getOutTransition(tr);
        Preconditions.checkNotNull(tr, "Could not find transition {} in TS {}!", transitions[i], ts);
        i = (i + 1) % transitions.length;
        return tr;
    }
    
    

}
