package be.vibes.selection.ec.objective;

import be.vibes.ts.TransitionSystem;
import be.vibes.ts.coverage.StructuralCoverage;
import be.vibes.ts.execution.Execution;

import java.util.List;

public class CoverStructure<T extends TransitionSystem> implements Objective {

    private final StructuralCoverage<T> criteria;

    public CoverStructure(StructuralCoverage<T> criteria) {
        this.criteria = criteria;
    }

    @Override
    public double evaluate(List<Execution> executions) {
        return this.criteria.coverage(executions.iterator());
    }
}
