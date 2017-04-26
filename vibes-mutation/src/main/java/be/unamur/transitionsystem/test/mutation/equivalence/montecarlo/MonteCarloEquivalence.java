/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.execution.TestCaseRunner;
import be.unamur.transitionsystem.test.mutation.exception.CounterExampleFoundException;
import be.unamur.transitionsystem.test.selection.RandomTestCaseGenerator;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import static com.google.common.base.Preconditions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
@Deprecated
public class MonteCarloEquivalence {

    private static final Logger logger = LoggerFactory.getLogger(MonteCarloEquivalence.class);

    private double delta;
    private double epsilon;
    private TestCaseRunner runner;
    private RandomTestCaseGenerator gen;
    private boolean failFirst = false;
    private boolean localized=false;

    private int estimatedNbrRuns;
    private String mutantName;

    public MonteCarloEquivalence(double delta, double epsilon, TestCaseRunner runner, RandomTestCaseGenerator gen) {
        checkArgument(delta < 1 && delta > 0, "Parameter delta must be between ]0;1[ !");
        checkArgument(epsilon > 0, "Parameter epsilon must be >0 !");
        checkNotNull(runner);
        checkNotNull(gen);
        this.delta = delta;
        this.epsilon = epsilon;
        this.runner = runner;
        this.gen = gen;
        this.estimatedNbrRuns = -1;
    }

    public int getEstimatedNbrRuns() {
        if (estimatedNbrRuns < 0) {
            estimatedNbrRuns = (int) ((8 * Math.log(2 / delta)) / (epsilon * epsilon)); // NB: the fomula starts by 4* Math but since we are performing to independent monte carlo simulations we have to double this number ot keep the same precision. 
        
        }
        return estimatedNbrRuns;
    }

    public double getDelta() {
        return delta;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setFailFirst(boolean failFirst) {
        this.failFirst = failFirst;
    }

    public boolean isFailFirst() {
        return failFirst;
    }
    
    public void setLocalized(boolean local) {
        this.localized = local;
    }
    
    public void setMutantName(String mName) {
        this.mutantName = mName;
    }

    public double getEquivalenceDegree(TransitionSystem original, TransitionSystem mutant) throws CounterExampleFoundException {
        return getEquivalenceDegree(original, mutant, 0.5);
    }

    public double getEquivalenceDegree(TransitionSystem original, TransitionSystem mutant, double originalTestCasesRatio) throws CounterExampleFoundException {
        checkArgument((0 <= originalTestCasesRatio) && (originalTestCasesRatio <= 1), "Argument 'originalTestCasesRatio' must be between [0;1]!");
        int nbrMaxRuns = getEstimatedNbrRuns();
        int nbrOriginal = (int) (nbrMaxRuns * originalTestCasesRatio);
        int nbrMutants = nbrMaxRuns - nbrOriginal;
        double deg = 0;
        int nbRunsOk = 0;
        int failedRuns =0;
        EquivalenceSimulationStats.addFailedRuns(mutantName, 0);
        logger.debug("Starting test case generation from original system (nbrOriginal={})", nbrOriginal);
        // Execution of the test cases on the mutant
        for (int i = 0; i < nbrOriginal;) {
            try {
                TestCase tc = gen.generateTestCase(original);
                if (tc != null) {
                    ExecutionTree tree = runner.run(mutant, tc);
                    if (tree.hasPath()) {
                        nbRunsOk++;
                    } else if (isFailFirst()) {
                        EquivalenceResults res = new EquivalenceResults();
                        res.incrementFailedRuns(tc);
                        throw new CounterExampleFoundException("Counter exemple test case found!", original, mutant, res);
                    }
                    i++;
                }
            } catch (TestCaseSelectionException ex) {
                i++;
                nbRunsOk++;
                failedRuns++;
                logger.debug("Exception while generating random test case in original system!", ex);
            }
        }
        logger.debug("Starting test case generation from mutant system (nbrMutants={})", nbrMutants);
        // Execution of the test cases on the original ts
        for (int i = 0; i < nbrMutants;) {
            try {
                TestCase tc = gen.generateTestCase(mutant);
                if (tc != null) {
                    ExecutionTree tree = runner.run(original, tc);
                    if (tree.hasPath()) {
                        nbRunsOk++;
                    } else if (isFailFirst()) {
                        EquivalenceResults res = new EquivalenceResults();
                        res.incrementFailedRuns(tc);
                        throw new CounterExampleFoundException("Counter exemple test case found!", original, mutant, res);
                    }
                    i++;
                }
            } catch (TestCaseSelectionException ex) {
                i++;
                nbRunsOk++;
                failedRuns++;
                logger.debug("Exception while generating random test case in mutant system!", ex);
            }
        }
        // Equivalence degree computation
         EquivalenceSimulationStats.addFailedRuns(mutantName, failedRuns);
        deg = ((double) nbRunsOk) / nbrMaxRuns;
        return deg;
    }

}
