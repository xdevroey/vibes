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

import be.vibes.selection.random.RandomSelectionFromFTS;
import be.vibes.selection.random.RandomSelection;
import be.vibes.solver.FeatureModel;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TransitionSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class FtsDissimilarTestCaseSelector extends DissimilarTestCaseSelector{
    
    private static final Logger LOG = LoggerFactory.getLogger(DissimilarTestCaseSelector.class);
    private FeatureModel fm;

    public FtsDissimilarTestCaseSelector(TransitionSystem ts, FeatureModel fm, PrioritizationTechnique prioritization) {
        super(ts, prioritization);
        this.fm = fm;
    }
    
    public FtsDissimilarTestCaseSelector(TransitionSystem ts, FeatureModel fm, PrioritizationTechnique prioritization, long runningTime) {
        super(ts, prioritization, runningTime);
        this.fm = fm;
    }

    @Override
    public FeaturedTransitionSystem getTransitionSystem() {
        return (FeaturedTransitionSystem) super.getTransitionSystem();
    }

    @Override
    protected RandomSelection getRandomTestCaseSelector() {
        return new RandomSelectionFromFTS(getTransitionSystem(), this.fm);
    }
    
}
