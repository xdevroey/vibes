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

import java.util.Collection;
import java.util.Iterator;

import be.vibes.selection.exception.DissimilarityComputationException;
import be.vibes.ts.TestCase;
import be.vibes.ts.TestSet;

public class DissimilarTestSet extends TestSet implements Comparable<DissimilarTestSet> {

    private double fitness;
    private final Prioritization priorotizationTech;

    public DissimilarTestSet(Iterator<? extends TestCase> testCases, Prioritization technique) {
        super(testCases);
        fitness = -1;
        priorotizationTech = technique;
    }

    public DissimilarTestSet(Collection<? extends TestCase> testCases, Prioritization technique) {
        super(testCases);
        fitness = -1;
        priorotizationTech = technique;
    }

    public double getFitness() throws DissimilarityComputationException {
        if (fitness == -1) {
            computeFitnessAndOrder();
        }
        return fitness;
    }

    private void computeFitnessAndOrder() throws DissimilarityComputationException {
        this.testCases = priorotizationTech.prioritize(testCases);
        fitness = priorotizationTech.getFitness();
    }

    @Override
    public void add(TestCase tc) {
        super.add(tc);
        fitness = -1;
    }

    @Override
    public boolean remove(TestCase tc) {
        fitness = -1;
        return super.remove(tc);
    }

    @Override
    public TestCase get(int index) {
        if (fitness == -1) {
            try {
                computeFitnessAndOrder();
            } catch (DissimilarityComputationException e) {
                throw new RuntimeException("Error while computing fitness!", e);
            }
        }
        return super.get(index);
    }

    @Override
    public int compareTo(DissimilarTestSet ts) {
        try {
            double fitness1 = getFitness();
            double fitness2 = ts.getFitness();
            return Double.compare(fitness1, fitness2);
        } catch (DissimilarityComputationException e) {
            throw new RuntimeException("Error while computing fitness!", e);
        }

    }

}
