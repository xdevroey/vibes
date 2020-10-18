package be.vibes.ts.coverage;

import be.vibes.solver.FeatureModel;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.State;
import be.vibes.ts.Transition;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.execution.Execution;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * State coverage criteria for transition systems. When a transition is executed, both source and destination states
 * are covered.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class StateCoverage extends StructuralCoverage<State> {

    /**
     * Create a new state coverage criteria for the given transition system.
     *
     * @param ts The transition system for which the coverage criteria will be reported.
     */
    public StateCoverage(TransitionSystem ts) {
        super(ts);
    }

    /**
     * Creates a new state coverage criteria for the given featured transition system and feature model.
     *
     * @param fts The featured transition system for which the coverage criteria will be reported.
     * @param fm  The feature model used during the execution of the tests to compute the coverage criteria.
     */
    public StateCoverage(FeaturedTransitionSystem fts, FeatureModel fm) {
        super(fts, fm);
    }

    @Override
    protected Set<State> getCoveredElements(Execution execution) {
        Set<State> covered = new HashSet<>();
        for (Transition transition : execution) {
            covered.add(transition.getSource());
            covered.add(transition.getTarget());
        }
        return covered;
    }

    @Override
    public Iterator<State> getElementsToBeCovered() {
        return getTs().states();
    }

}
