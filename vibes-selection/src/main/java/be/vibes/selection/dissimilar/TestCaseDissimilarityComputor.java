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
import be.vibes.ts.Action;
import be.vibes.ts.TestCase;
import be.vibes.ts.Transition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class TestCaseDissimilarityComputor implements DissimilarityComputor<TestCase> {

    private DissimilarityComputor computor;
    private Function<TestCase, ? extends Collection> converter;
    
    protected TestCaseDissimilarityComputor(){}
    
    private TestCaseDissimilarityComputor(SetBasedDissimilarityComputor computor, Function<TestCase, ? extends Collection> converter) {
        this.computor = computor;
        this.converter = converter;
    }
        
    private TestCaseDissimilarityComputor(SequenceBasedDissimilarityComputor computor, Function<TestCase, ? extends Collection> converter) {
        this.computor = computor;
        this.converter = converter;
    }

    @Override
    public double dissimilarity(TestCase tc1, TestCase tc2) throws DissimilarityComputationException {
        return this.computor.dissimilarity(converter.apply(tc1), converter.apply(tc2));
    }
    
    /**
     * Returns a new dissimilarity computor that will compute a set based dissimilarity between the actions of two test cases.
     * @param computor The dissimilarity computor to use with the sets of actions of two test cases.
     * @return A TestCaseDissimilarityComputor that will use the given dissimilarity computor to compute dissimilarity between two test cases.
     */
    public static TestCaseDissimilarityComputor toTestCaseDissimilarityComputor(SetBasedDissimilarityComputor computor){
        return new TestCaseDissimilarityComputor(computor, (TestCase testcase) -> {
            Set<Action> set = new HashSet<>();
            testcase.iterator().forEachRemaining((Transition tr) -> {set.add(tr.getAction());});
            return set;
        });
    }
    
    /**
     * Returns a new dissimilarity computor that will compute a sequence based dissimilarity between the sequence of actions of two test cases.
     * @param computor The dissimilarity computor to use with the sequences of actions of two test cases.
     * @return A TestCaseDissimilarityComputor that will use the given dissimilarity computor to compute dissimilarity between two test cases.
     */
    public static TestCaseDissimilarityComputor toTestCaseDissimilarityComputor(SequenceBasedDissimilarityComputor computor){
        return new TestCaseDissimilarityComputor(computor, (TestCase testcase) -> {
            List<Action> set = new ArrayList<>();
            testcase.iterator().forEachRemaining((Transition tr) -> {set.add(tr.getAction());});
            return set;
        });
    }

}
