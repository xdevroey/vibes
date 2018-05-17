package be.vibes.selection.dissimilar;

/*-
 * #%L
 * VIBeS: test case selection
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.List;

import com.google.common.collect.Lists;

import be.vibes.ts.TestCase;
import be.vibes.selection.exception.DissimilarityComputationException;
import static com.google.common.base.Preconditions.*;

public class LocalMaximumDistancePrioritization implements PrioritizationTechnique {

    private final TestCaseDissimilarityComputor comp;
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
