package be.vibes.selection.ec.objective;

import be.vibes.selection.dissimilar.SequenceBasedDissimilarity;
import be.vibes.selection.exception.DissimilarityComputationException;
import be.vibes.ts.Transition;
import be.vibes.ts.execution.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This objective seeks to increase the diversity (actually, minimizing similarity) of the paths of the different
 * test cases.
 *
 * @author Xavier Devroey
 */
public class DissimilarPaths implements Objective{

    private static final Logger LOG = LoggerFactory.getLogger(DissimilarPaths.class);

    private final SequenceBasedDissimilarity<List<Transition>> distance;

    public DissimilarPaths(SequenceBasedDissimilarity<List<Transition>> distance) {
        this.distance = distance;
    }

    @Override
    public double evaluate(List<Execution> executions) {
        List<Transition> exec1;
        List<Transition> exec2;
        int count = 0;
        double similarity = 0.0;
        for (int i = 0; i < executions.size() - 1; i++) {
            exec1 = collectPath(executions.get(i));
            for (int j = i + 1; j < executions.size(); j++) {
                exec2 = collectPath(executions.get(j));
                try {
                    similarity = similarity + (1.0 - this.distance.dissimilarity(exec1, exec2));
                } catch (DissimilarityComputationException e) {
                    LOG.error("Exception while calculating path similarity! Will ignore the couple ({},{})", i, j, e);
                }
                count++;
            }
        }
        return count > 0 ? similarity / count : 0.0;
    }

    private List<Transition> collectPath(Execution execution) {
        List<Transition> path = new ArrayList<>();
        execution.forEach((transition -> path.add(transition)));
        return path;
    }
}
