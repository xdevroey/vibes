package be.vibes.selection.ec.objective;

import be.vibes.ts.execution.Execution;

import java.util.List;

public interface Objective {

    double evaluate(List<Execution> executions);

}
