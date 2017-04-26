package be.unamur.transitionsystem.test.execution;

import be.unamur.transitionsystem.LabelledTransitionSystem;

/**
 * Labelled transition system executor class. See
 * {@link TransitionSystemExecutor}
 */
public class LabelledTransitionSystemExecutor extends TransitionSystemExecutor<LabelledTransitionSystem> {

    /**
     * Creates a new transition system executor.
     *
     * @param ts The transition system to execute.
     */
    public LabelledTransitionSystemExecutor(LabelledTransitionSystem ts) {
        super(ts);
    }

}
