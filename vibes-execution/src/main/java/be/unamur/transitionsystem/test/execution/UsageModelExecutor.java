package be.unamur.transitionsystem.test.execution;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import be.unamur.transitionsystem.usagemodel.UsageModelTransition;

/**
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class UsageModelExecutor extends TransitionSystemExecutor<UsageModel> {

    private boolean modified = true;

    private double proba = 0.0;

    /**
     * Creates a new transition system executor.
     *
     * @param ts The transition system to execute.
     */
    public UsageModelExecutor(UsageModel ts) {
        super(ts);
    }

    @Override
    protected void setCurrentState(State state) {
        super.setCurrentState(state);
        modified = true;
    }

    /**
     * Returns the probability of the current execution of the usage model.
     *
     * @return The probability of the execution of the usage model.
     */
    public double getExecutionProbability() {
        if (modified) {
            computeProbability();
        }
        return proba;
    }

    private void computeProbability() {
        proba = 1.0;
        for (Transition tr : getExecutedTransitions()) {
            proba = proba * ((UsageModelTransition) tr).getProbability();
        }
    }

}
