package be.unamur.transitionsystem.test.selection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.test.selection.exception.DissimilarityComputationException;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;

public class DissimilarTestCaseGenerator extends AbstractTestCaseGenerator {

    private static final Logger logger = LoggerFactory
            .getLogger(DissimilarTestCaseGenerator.class);

    public static final long DEFAULT_RUNNING_TIME_MILLI = 30000;

    public static final int DEFAULT_NUMBER_OF_TEST_CASES = 100;

    private PrioritizationTechnique prioritization;
    private long runningTime;
    private int nbrTestCases;
    private double lastFitness;
    private int lastNbrIterations = 0;

    public DissimilarTestCaseGenerator(TestCaseFactory testCaseFactory,
            TestCaseValidator validator, TestCaseWrapUp wrapUp,
            PrioritizationTechnique prioritization) {
        this(testCaseFactory, validator, wrapUp, prioritization,
                DEFAULT_RUNNING_TIME_MILLI, DEFAULT_NUMBER_OF_TEST_CASES);
    }

    public DissimilarTestCaseGenerator(TestCaseFactory testCaseFactory,
            TestCaseValidator validator, TestCaseWrapUp wrapUp,
            PrioritizationTechnique prioritization, long runningTime, int nbrTestCases) {
        super(testCaseFactory, validator, wrapUp);
        this.runningTime = runningTime;
        this.nbrTestCases = nbrTestCases;
        this.prioritization = prioritization;
    }

    public long getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
    }

    public int getNbrTestCases() {
        return nbrTestCases;
    }

    public void setNbrTestCases(int nbrTestCases) {
        this.nbrTestCases = nbrTestCases;
    }

    public double getLastFitness() {
        return lastFitness;
    }

    public int getLastNbrIterations() {
        return lastNbrIterations;
    }
    
    @Override
    public void generateAbstractTestSet(TransitionSystem ts, TestCaseValidator validator,
            TestCaseWrapUp wrapUp) throws TestCaseSelectionException {
        DissimilarTestSet set = null;
        try {
            AccumulatorWrapUp acc = new AccumulatorWrapUp();
            RandomTestCaseGenerator gen = new RandomTestCaseGenerator(
                    getTestCaseFactory(), getValidator(), acc);
            gen.generateAbstractTestSet(ts, nbrTestCases);
            set = new DissimilarTestSet(acc.getTestCases(),
                    prioritization);
            double fitness = set.getFitness();
            logger.debug("Fitness of initial set  {}", fitness);
            long startTimeMS = System.currentTimeMillis();
            lastNbrIterations = 0;
            while (System.currentTimeMillis() - startTimeMS < runningTime) {
                logger.trace("Iteration {}", lastNbrIterations);
                DissimilarTestSet oldSet = new DissimilarTestSet(set.iterator(),
                        prioritization);
                // Remove worst test case (lest dissimilar)
                if (!set.remove(set.get(set.size() - 1))) {
                    logger.error(
                            "Could not remove last test case in dissimilar test set {}!",
                            set);
                }
                // Add random test case
                acc = new AccumulatorWrapUp();
                gen.setWrapUp(acc);
                gen.generateAbstractTestSet(ts, 1);
                set.add(acc.getTestCases().get(0));
                if (set.getFitness() < oldSet.getFitness()) {
                    set = oldSet;
                }
                lastNbrIterations++;
                lastFitness = set.getFitness();
                logger.trace("New fitness = {}", lastFitness);
            }
        } catch (DissimilarityComputationException e) {
            throw new TestCaseSelectionException(
                    "Error while computing fitness function!", e);
        }
        logger.debug("Final fitness = {}", lastFitness);
        for (TestCase tc : set) {
            try {
                wrapUp.wrapUp(tc);
            } catch (TestCaseException e) {
                throw new TestCaseSelectionException("Error while wrapping up test case!", e);
            }
        }
    }

}
