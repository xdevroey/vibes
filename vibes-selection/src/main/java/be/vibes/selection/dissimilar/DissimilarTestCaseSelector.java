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

import be.vibes.selection.AbstractTestCaseSelector;
import be.vibes.selection.random.RandomSelection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.vibes.selection.exception.DissimilarityComputationException;
import be.vibes.selection.exception.TestCaseSelectionException;
import be.vibes.ts.TestCase;
import be.vibes.ts.TransitionSystem;
import com.google.common.collect.Lists;
import java.util.List;

public class DissimilarTestCaseSelector extends AbstractTestCaseSelector {

    private static final Logger LOG = LoggerFactory
            .getLogger(DissimilarTestCaseSelector.class);

    public static final long DEFAULT_RUNNING_TIME_MILLI = 30000;

    public static final int DEFAULT_NUMBER_OF_TEST_CASES = 100;

    private Prioritization prioritization;
    private long runningTime;
    private double lastFitness;
    private int lastNbrIterations = 0;

    public DissimilarTestCaseSelector(TransitionSystem ts, Prioritization prioritization) {
        this(ts, prioritization, DEFAULT_RUNNING_TIME_MILLI);
    }

    public DissimilarTestCaseSelector(TransitionSystem ts, Prioritization prioritization, long runningTime) {
        super(ts);
        this.runningTime = runningTime;
        this.prioritization = prioritization;
    }

    public long getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
    }

    public double getLastFitness() {
        return lastFitness;
    }

    public int getLastNbrIterations() {
        return lastNbrIterations;
    }

    public List<TestCase> select() throws TestCaseSelectionException {
        return select(DEFAULT_NUMBER_OF_TEST_CASES);
    }

    @Override
    public List<TestCase> select(int nbr) throws TestCaseSelectionException {
        DissimilarTestSet set;
        RandomSelection gen = getRandomTestCaseSelector();
        try {
            LOG.debug("Generating initial population");
            set = new DissimilarTestSet(gen.select(nbr), prioritization);
            double fitness = set.getFitness();
            LOG.debug("Fitness of initial population {}", fitness);
            long startTimeMS = System.currentTimeMillis();
            lastNbrIterations = 0;
            while (System.currentTimeMillis() - startTimeMS < runningTime) {
                LOG.trace("Iteration {}", lastNbrIterations);
                DissimilarTestSet oldSet = new DissimilarTestSet(set.iterator(),
                        prioritization);
                // Remove worst test case (lest dissimilar)
                if (!set.remove(set.get(set.size() - 1))) {
                    LOG.error("Could not remove last test case in dissimilar test set {}!", set);
                }
                // Add random test case
                set.add(gen.select());
                if (set.getFitness() < oldSet.getFitness()) {
                    set = oldSet;
                }
                lastNbrIterations++;
                lastFitness = set.getFitness();
                LOG.trace("New fitness = {}", lastFitness);
            }
        } catch (DissimilarityComputationException e) {
            throw new TestCaseSelectionException(
                    "Error while computing fitness function!", e);
        }
        LOG.debug("Final fitness = {}", lastFitness);
        return Lists.newArrayList(set);
    }

    protected RandomSelection getRandomTestCaseSelector() {
        return new RandomSelection(getTransitionSystem());
    }

}
