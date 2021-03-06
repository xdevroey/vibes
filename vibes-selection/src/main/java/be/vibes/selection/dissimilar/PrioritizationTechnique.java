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

import be.vibes.selection.exception.DissimilarityComputationException;
import be.vibes.ts.TestCase;

public interface PrioritizationTechnique {

    /**
     * Returns a prioritized (fresh list) of the given list of test-cases.
     *
     * @param testCases The list of test cases to prioritize.
     * @return A prioritized (fresh list) of the given list of test-cases.
     * @throws DissimilarityComputationException
     */
    public List<TestCase> prioritize(List<TestCase> testCases) throws DissimilarityComputationException;

    /**
     * Returns the fitness value for the last prioritized test-cases.
     * @return The fitness value for the last prioritized test-cases.
     */
    public double getFitness();

}
