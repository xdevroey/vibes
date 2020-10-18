package be.vibes.ts.coverage;

import be.vibes.solver.FeatureModel;
import be.vibes.ts.Action;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.Transition;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.execution.Execution;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Action coverage criteria for transition systems.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class ActionCoverage extends StructuralCoverage<Action> {

    /**
     * Create a new action coverage criteria for the given transition system.
     *
     * @param ts The transition system for which the coverage criteria will be reported.
     */
    public ActionCoverage(TransitionSystem ts) {
        super(ts);
    }

    /**
     * Creates a new action coverage criteria for the given featured transition system and feature model.
     *
     * @param fts The featured transition system for which the coverage criteria will be reported.
     * @param fm  The feature model used during the execution of the tests to compute the coverage criteria.
     */
    public ActionCoverage(FeaturedTransitionSystem fts, FeatureModel fm) {
        super(fts, fm);
    }

    @Override
    protected Set<Action> getCoveredElements(Execution execution) {
        Set<Action> covered = new HashSet<>();
        for (Transition transition : execution) {
            covered.add(transition.getAction());
        }
        return covered;
    }

    @Override
    public Iterator<Action> getElementsToBeCovered() {
        return getTs().actions();
    }

}
