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
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.Integer.min;

/**
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @param <T>
 */
public class LevenshteinDissimilarityComputor<T extends List> implements SequenceBasedDissimilarityComputor<T> {

    private static final Logger logger = LoggerFactory.getLogger(LevenshteinDissimilarityComputor.class);

    private static final int INSERT_DELETE_COST = 1;
    private static final int REPLACE_COST = 1;

    public LevenshteinDissimilarityComputor() {
    }

    @Override
    public double dissimilarity(T l1, T l2) throws DissimilarityComputationException {
        if (l1 == l2) {
            return 0.0;
        } else if (l1.size() == 0 && l2.size() == 0) {
            return 0.0;
        } else if (l1.size() == 0) {
            return 1.0;
        } else if (l2.size() == 0) {
            return 1.0;
        }
        double maxDist = Math.max(l1.size(), l2.size());
        return ((double) getDistance(l1, l2)) / maxDist;
    }

    public int getDistance(T l1, T l2) {
        if (l1 == l2) {
            return 0;
        } else if (l1.size() == 0 && l2.size() == 0) {
            return 0;
        } else if (l1.size() == 0) {
            return l2.size() * INSERT_DELETE_COST;
        } else if (l2.size() == 0) {
            return l1.size() * INSERT_DELETE_COST;
        }
        // Previous row for computation
        int[] previous = new int[l2.size() + 1];
        // new row for computation
        int[] current = new int[l2.size() + 1];

        // Initialise previous to insert cost
        for (int j = 0; j < previous.length; j++) {
            previous[j] = j * INSERT_DELETE_COST;
        }

        for (int i = 0; i < l1.size(); i++) {
            logger.trace("Line in Levenshtein computation is {}", previous);
            current[0] = (i + 1) * INSERT_DELETE_COST;
            // Compute new line in the matrix
            for (int j = 0; j < l2.size(); j++) {
                int replace = l1.get(i).equals(l2.get(j)) ? 0 : 1;
                current[j + 1] = min(current[j] + INSERT_DELETE_COST,
                        min(previous[j + 1] + INSERT_DELETE_COST,
                                previous[j] + (REPLACE_COST * replace)));
            }
            System.arraycopy(current, 0, previous, 0, previous.length);
        }
        logger.trace("Line in Levenshtein computation is {}", previous);
        return current[l2.size()];
    }

}
