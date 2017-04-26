package be.unamur.transitionsystem.test.selection;

import java.util.List;

import com.google.common.collect.Lists;

import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.selection.exception.DissimilarityComputationException;

public class GlobalMaximumDistancePrioritization implements PrioritizationTechnique {

    private TestCaseDissimilarityComputor comp;
    private double fitness;

    public GlobalMaximumDistancePrioritization(TestCaseDissimilarityComputor comp) {
        this.comp = comp;
        this.fitness = 0;
    }

    @Override
    public List<TestCase> prioritize(List<TestCase> testCases) throws DissimilarityComputationException {
        List<TestCase> prioritized = Lists.newArrayList();
        List<TestCase> copy = Lists.newArrayList(testCases);
        double[][] distances = new double[testCases.size()][testCases.size()];
        fitness = 0;
        // Initialise distance matrix
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances.length; j++) {
                if (j > i) {
                    double dissimilarity = comp.dissimilarity(copy.get(i), copy.get(j));
                    distances[i][j] = dissimilarity;
                    fitness = fitness + dissimilarity;
                } else {
                    distances[i][j] = -1;
                }
            }
        }
        // Take the couple with the maximal distance
        List<Integer> possibleIndices = Lists.newArrayList();
        List<Integer> doneIndices = Lists.newArrayList();
        for (int i = 0; i < testCases.size(); i++) {
            possibleIndices.add(i);
        }
        double maxDistance = -1;
        int toAddIIndex = -1;
        int toAddJIndex = -1;
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances.length; j++) {
                if (j > i) {
                    if (distances[i][j] > maxDistance) {
                        maxDistance = distances[i][j];
                        toAddIIndex = i;
                        toAddJIndex = j;
                    }
                }
            }
        }
        TestCase pi = testCases.get(toAddIIndex);
        TestCase pj = testCases.get(toAddJIndex);

        prioritized.add(pi);
        prioritized.add(pj);
        copy.remove(pi);
        copy.remove(pj);
        possibleIndices.remove((Integer) toAddIIndex);
        possibleIndices.remove((Integer) toAddJIndex);
        doneIndices.add(toAddIIndex);
        doneIndices.add(toAddJIndex);

        while (!copy.isEmpty()) {
            if (possibleIndices.size() > 1) {
                double maxSim = -1;
                int toAdd = -1;
                for (Integer i : possibleIndices) {
                    double dissimilarity = 0;
                    for (Integer j : doneIndices) {
                        dissimilarity += (j > i) ? distances[i][j] : distances[j][i];
                    }
                    if (dissimilarity > maxSim) {
                        maxSim = dissimilarity;
                        toAdd = i;
                    }
                }
                TestCase p = testCases.get(toAdd);
                prioritized.add(p);
                copy.remove(p);
                possibleIndices.remove((Integer) toAdd);
                doneIndices.add(toAdd);
            } else {
                prioritized.add(testCases.get(possibleIndices.get(0)));
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
