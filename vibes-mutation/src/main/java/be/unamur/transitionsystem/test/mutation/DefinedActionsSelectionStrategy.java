package be.unamur.transitionsystem.test.mutation;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.TransitionSystem;
import com.google.common.base.Preconditions;

/**
 * This strategy returns actions in a given order.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class DefinedActionsSelectionStrategy implements ActionSelectionStrategy {

    private Action[] actions;
    private int i = 0;

    /**
     * Create a new selection strategy which will select actions in the givne
     * array according to its defined order.
     *
     * @param actions The actions to return (size must be &gt; 0). Actions will be
     * return in this order with a return to the first action when the end of
     * the table is reached.
     */
    public DefinedActionsSelectionStrategy(Action... actions) {
        Preconditions.checkArgument(actions.length > 0, "Must specify at least one action!");
        this.actions = actions;
    }

    @Override
    public Action selectAction(MutationOperator operator, TransitionSystem ts) {
        Action act = ts.getAction(actions[i].getName());
        Preconditions.checkNotNull(act, "Could not find action {} in TS {}!", actions[i], ts);
        i = (i + 1) % actions.length;
        return act;
    }

}
