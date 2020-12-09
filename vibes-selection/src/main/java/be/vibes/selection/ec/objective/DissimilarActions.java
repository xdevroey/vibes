package be.vibes.selection.ec.objective;

import be.vibes.selection.dissimilar.SetBasedDissimilarity;
import be.vibes.selection.exception.DissimilarityComputationException;
import be.vibes.ts.Action;
import be.vibes.ts.execution.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This objective seeks to increase the diversity (actually, minimizing similarity) of the actions in the different
 * test cases.
 *
 * @author Xavier Devroey
 */
public class DissimilarActions implements Objective {

    private static final Logger LOG = LoggerFactory.getLogger(DissimilarActions.class);

    private final SetBasedDissimilarity<Set<Action>> distance;

    public DissimilarActions(SetBasedDissimilarity<Set<Action>> distance) {
        this.distance = distance;
    }

    @Override
    public double evaluate(List<Execution> executions) {
        Set<Action> exec1;
        Set<Action> exec2;
        int count = 0;
        double similarity = 0.0;
        for (int i = 0; i < executions.size() - 1; i++) {
            exec1 = collectActions(executions.get(i));
            for (int j = i + 1; j < executions.size(); j++) {
                exec2 = collectActions(executions.get(j));
                try {
                    similarity = similarity + (1.0 - this.distance.dissimilarity(exec1, exec2));
                } catch (DissimilarityComputationException e) {
                    LOG.error("Exception while calculating action similarity! Will ignore the couple ({},{})", i, j, e);
                }
                count++;
            }
        }
        return count > 0 ? similarity / count : 0.0;
    }

    private Set<Action> collectActions(Execution execution) {
        Set<Action> actions = new HashSet<>();
        execution.forEach((transition -> actions.add(transition.getAction())));
        return actions;
    }
}
