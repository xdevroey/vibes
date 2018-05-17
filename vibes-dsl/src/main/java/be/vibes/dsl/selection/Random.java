package be.vibes.dsl.selection;

/*-
 * #%L
 * VIBeS: dsl
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

import be.vibes.dsl.exception.TestCaseDefinitionException;
import be.vibes.selection.exception.TestCaseSelectionException;
import be.vibes.selection.random.FtsRandomTestCaseSelector;
import be.vibes.selection.random.LocalRandomTestCaseSelector;
import be.vibes.selection.random.RandomTestCaseSelector;
import be.vibes.selection.random.UsageDrivenRandomSelector;
import be.vibes.solver.FeatureModel;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.State;
import be.vibes.ts.TestSet;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.UsageModel;
import java.util.Set;

public class Random {
    
    public static final int DEFAULT_TESTSUITE_SIZE = 100;

    // TS Random generation 
    public static TestSet randomSelection(TransitionSystem ts) {
        return randomSelection(ts, DEFAULT_TESTSUITE_SIZE);
    }

    public static TestSet randomSelection(TransitionSystem ts, int nbrTestCases) {
        return randomSelection(ts, nbrTestCases, RandomTestCaseSelector.DEFAULT_MAX_LENGTH);
    }

    public static TestSet randomSelection(TransitionSystem ts, int nbrTestCases, int testCaseMaxLength) {
        RandomTestCaseSelector selector = new RandomTestCaseSelector(ts, testCaseMaxLength);
        try {
            return new TestSet(selector.select(nbrTestCases));
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }

    // FTS Random generation 
    public static TestSet randomSelection(FeaturedTransitionSystem fts, FeatureModel solver) {
        return randomSelection(fts, solver, DEFAULT_TESTSUITE_SIZE);
    }

    public static TestSet randomSelection(FeaturedTransitionSystem fts, FeatureModel solver, int nbrTestCases) {
        return randomSelection(fts, solver, nbrTestCases, RandomTestCaseSelector.DEFAULT_MAX_LENGTH);
    }

    public static TestSet randomSelection(FeaturedTransitionSystem fts, FeatureModel fm, int nbrTestCases, int testCaseMaxLength) {
        FtsRandomTestCaseSelector selector = new FtsRandomTestCaseSelector(fts, fm, testCaseMaxLength);
        try {
            return new TestSet(selector.select(nbrTestCases));
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }

    // UsageModel Random generation 
    public static TestSet randomSelection(UsageModel um) {
        return randomSelection(um, DEFAULT_TESTSUITE_SIZE);
    }

    public static TestSet randomSelection(UsageModel um, int nbrTestCases) {
        return randomSelection(um, nbrTestCases, RandomTestCaseSelector.DEFAULT_MAX_LENGTH);
    }

    public static TestSet randomSelection(UsageModel um, int nbrTestCases, int testCaseMaxLength) {
         UsageDrivenRandomSelector selector = new UsageDrivenRandomSelector(um, testCaseMaxLength);
        try {
            return new TestSet(selector.select(nbrTestCases));
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }

    // TS Local Random generation 
    public static TestSet localRandomSelection(TransitionSystem ts, Set<State> states) {
        return localRandomSelection(ts, states, DEFAULT_TESTSUITE_SIZE);
    }

    public static TestSet localRandomSelection(TransitionSystem ts, Set<State> states, int nbrTestCases) {
        return localRandomSelection(ts, states, nbrTestCases, RandomTestCaseSelector.DEFAULT_MAX_LENGTH);
    }

    public static TestSet localRandomSelection(TransitionSystem ts, Set<State> states, int nbrTestCases, int testCaseMaxLength) {
        LocalRandomTestCaseSelector selector = new LocalRandomTestCaseSelector(ts, testCaseMaxLength, states);
        try {
            return new TestSet(selector.select(nbrTestCases));
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }
    
}
