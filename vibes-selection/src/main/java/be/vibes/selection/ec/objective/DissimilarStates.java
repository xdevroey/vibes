package be.vibes.selection.ec.objective;

import be.vibes.selection.dissimilar.SetBasedDissimilarity;
import be.vibes.selection.exception.DissimilarityComputationException;
import be.vibes.ts.State;
import be.vibes.ts.execution.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This objective seeks to increase the diversity (actually, minimizing similarity) of the states in the different
 * test cases.
 *
 * @author Xavier Devroey
 */
public class DissimilarStates implements Objective {

    private static final Logger LOG = LoggerFactory.getLogger(DissimilarStates.class);

    private final SetBasedDissimilarity<Set<State>> distance;

    public DissimilarStates(SetBasedDissimilarity<Set<State>> distance) {
        this.distance = distance;
    }

    @Override
    public double evaluate(List<Execution> executions) {
        Set<State> exec1;
        Set<State> exec2;
        int count = 0;
        double similarity = 0.0;
        for (int i = 0; i < executions.size() - 1; i++) {
            exec1 = collectStates(executions.get(i));
            for (int j = i + 1; j < executions.size(); j++) {
                exec2 = collectStates(executions.get(j));
                try {
                    similarity = similarity + (1.0 - this.distance.dissimilarity(exec1, exec2));
                } catch (DissimilarityComputationException e) {
                    LOG.error("Exception while calculating state similarity! Will ignore the couple ({},{})", i, j, e);
                }
                count++;
            }
        }
        return count > 0 ? similarity / count : 0.0;
    }

    private Set<State> collectStates(Execution execution) {
        Set<State> states = new HashSet<>();
        states.add(execution.getFirst().getSource());
        execution.forEach((transition -> states.add(transition.getTarget())));
        return states;
    }
    
}
