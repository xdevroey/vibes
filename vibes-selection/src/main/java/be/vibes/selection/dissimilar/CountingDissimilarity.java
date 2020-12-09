
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
import com.google.common.collect.Multiset;
import java.util.Iterator;
import static java.lang.Integer.min;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @param <T>
 */
public class CountingDissimilarity<T extends Multiset> implements Dissimilarity<T> {

    public CountingDissimilarity() {
    }

    @Override
    public double dissimilarity(T s1, T s2) throws DissimilarityComputationException {
        int countIdentical = 0;
        Iterator it = s1.entrySet().iterator();
        while(it.hasNext()){
            Multiset.Entry e = (Multiset.Entry) it.next();
            countIdentical = countIdentical + min(e.getCount(), s2.count(e.getElement()));
        }
        return 1.0 - (countIdentical / ((s1.size() + s2.size()) / 2.0));
    }
    
    

}
