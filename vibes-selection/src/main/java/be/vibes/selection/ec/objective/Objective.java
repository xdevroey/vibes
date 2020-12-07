package be.vibes.selection.ec.objective;

import be.vibes.ts.TransitionSystem;
import be.vibes.ts.execution.Execution;

import java.util.List;

public interface Objective {

    public double evaluate(List<Execution> executions);

}
