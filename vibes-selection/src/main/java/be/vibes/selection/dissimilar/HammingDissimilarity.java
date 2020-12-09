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
import com.google.common.base.Preconditions;

import java.util.Set;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @param <T>
 */
public class HammingDissimilarity<T extends Set> implements SetBasedDissimilarity<T> {

    private final T allElements;

    /**
     * Build a new Hamming dissimilarity computor.
     *
     * @param allElements The input domain of the sets to compare. All the
     * elements of the sets used with the dissimilarity method must be included
     * in allElements.
     */
    public HammingDissimilarity(T allElements) {
        Preconditions.checkArgument(!allElements.isEmpty(), "The set of all elements cannot be empty!");
        this.allElements = allElements;
    }

    @Override
    public double dissimilarity(T s1, T s2) throws DissimilarityComputationException {
        return 1 - getDistance(s1, s2);
    }

    public double getDistance(T s1, T s2) {
        int cptIdentical = 0;
        for (Object o : allElements) {
            boolean inS1 = s1.contains(o);
            boolean inS2 = s2.contains(o);
            if ((inS1 && inS2) || (!inS1 && !inS2)) {
                cptIdentical++;
            }
        }
        return ((double) cptIdentical) / allPossibleElementsCount();
    }
    
    public int allPossibleElementsCount(){
        return allElements.size();
    }
    
}
