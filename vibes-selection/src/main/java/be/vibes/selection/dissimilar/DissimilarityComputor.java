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

import be.vibes.selection.exception.DissimilarityComputationException;

public interface DissimilarityComputor<T> {

    /**
     * Returns a value between 0.0 (dissimilar) and 1.0 (similar) indicating the
     * similarity degree between o1 and o2. If similarity(o1,o2) == 1.0, then
     * o1.equals(o2) is true.
     *
     * @param o1 The first object to compare.
     * @param o2 The second object to compare.
     * @return a Value between 0.0 (dissimilar) and 1.0 (similar).
     * @throws DissimilarityComputationException
     */
    public double dissimilarity(T o1, T o2) throws DissimilarityComputationException;

}
