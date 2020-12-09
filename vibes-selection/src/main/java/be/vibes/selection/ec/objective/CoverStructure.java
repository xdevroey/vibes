package be.vibes.selection.ec.objective;

import be.vibes.ts.TransitionSystem;
import be.vibes.ts.coverage.*;
import be.vibes.ts.execution.Execution;

import java.util.List;

/**
 * This objective seeks to increase structural coverage for the given coverage criteria.
 *
 * @author Xavier Devroey
 */
public class CoverStructure implements Objective {

    private final StructuralCoverage criteria;

    public CoverStructure(StructuralCoverage criteria) {
        this.criteria = criteria;
    }

    @Override
    public double evaluate(List<Execution> executions) {
        return 1.0 - this.criteria.coverage(executions.iterator());
    }

    /* Static helper methods to build the different structural coverage objectives */

    public static CoverStructure coverTransitionPairs(TransitionSystem ts) {
        return new CoverStructure(new TransitionPairCoverage(ts));
    }

    public static CoverStructure coverTransitions(TransitionSystem ts) {
        return new CoverStructure(new TransitionCoverage(ts));
    }

    public static CoverStructure coverActions(TransitionSystem ts) {
        return new CoverStructure(new ActionCoverage(ts));
    }

    public static CoverStructure coverStates(TransitionSystem ts) {
        return new CoverStructure(new StateCoverage(ts));
    }
}
