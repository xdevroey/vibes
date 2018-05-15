package be.vibes.selection.dissimilar;

import be.vibes.selection.AbstractTestCaseSelector;
import be.vibes.selection.random.RandomTestCaseSelector;
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

    private PrioritizationTechnique prioritization;
    private long runningTime;
    private double lastFitness;
    private int lastNbrIterations = 0;

    public DissimilarTestCaseSelector(TransitionSystem ts, PrioritizationTechnique prioritization) {
        this(ts, prioritization, DEFAULT_RUNNING_TIME_MILLI);
    }

    public DissimilarTestCaseSelector(TransitionSystem ts, PrioritizationTechnique prioritization, long runningTime) {
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
        RandomTestCaseSelector gen = getRandomTestCaseSelector();
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

    protected RandomTestCaseSelector getRandomTestCaseSelector() {
        return new RandomTestCaseSelector(getTransitionSystem());
    }

}
