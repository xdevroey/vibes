package be.unamur.transitionsystem.test.selection;

import java.util.List;

import com.google.common.collect.Lists;

import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.selection.exception.DissimilarityComputationException;
import static com.google.common.base.Preconditions.*;

public class LocalMaximumDistancePrioritization implements PrioritizationTechnique {

    private TestCaseDissimilarityComputor comp;
    private double fitness;

    public LocalMaximumDistancePrioritization(TestCaseDissimilarityComputor comp) {
        this.comp = comp;
        this.fitness = 0;
    }

    @Override
    public List<TestCase> prioritize(List<TestCase> testCases) throws DissimilarityComputationException {
        List<TestCase> prioritized = Lists.newArrayList();
        List<TestCase> copy = Lists.newArrayList(testCases);
        double[][] distances = new double[copy.size()][copy.size()];
        // Initialize distances
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances.length; j++) {
                if (j > i) {
                    double dissimilarity = comp.dissimilarity(testCases.get(i), testCases.get(j));
                    checkState(dissimilarity >= 0, "Dissimilarity computed using %s has to be >=0!", comp.getClass().getName());
                    distances[i][j] = dissimilarity;
                    fitness = fitness + dissimilarity;
                } else {
                    distances[i][j] = -1;
                }
            }
        }
        // Prioritize testCases
        while (!copy.isEmpty()) {
            if (copy.size() != 1) {
                double dmax = -1;
                int toAddIIndex = -1;
                int toAddJIndex = -1;
                for (int i = 0; i < distances.length; i++) {
                    for (int j = 0; j < distances.length; j++) {
                        if (j > i) {
                            if (distances[i][j] > dmax) {
                                dmax = distances[i][j];
                                toAddIIndex = i;
                                toAddJIndex = j;
                            }
                        }
                    }
                }
                checkState(toAddIIndex >= 0);
                checkState(toAddJIndex >= 0);
                TestCase tci = testCases.get(toAddIIndex);
                TestCase tcj = testCases.get(toAddJIndex);
                prioritized.add(tci);
                prioritized.add(tcj);
                copy.remove(tci);
                copy.remove(tcj);
                for (int i = 0; i < distances.length; i++) {
                    distances[toAddIIndex][i] = distances[i][toAddIIndex] = distances[i][toAddJIndex] = distances[toAddJIndex][i] = -1;
                }
            } else {
                prioritized.add(copy.get(0));
                copy.clear();
            }
        }
        return prioritized;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

}
