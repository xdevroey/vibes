package be.vibes.selection.ec.objective;

import be.vibes.ts.coverage.StructuralCoverage;
import be.vibes.ts.execution.Execution;

import java.util.List;

public class CoverStructure implements Objective {

    private final StructuralCoverage criteria;

    public CoverStructure(StructuralCoverage criteria) {
        this.criteria = criteria;
    }

    @Override
    public double evaluate(List<Execution> executions) {
        return 1.0 - this.criteria.coverage(executions.iterator());
    }
}
