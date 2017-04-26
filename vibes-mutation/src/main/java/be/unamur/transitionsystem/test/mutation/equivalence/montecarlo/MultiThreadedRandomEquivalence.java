package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.test.mutation.exception.ConnectivityHypothesisViolationException;
import be.unamur.transitionsystem.test.mutation.exception.CounterExampleFoundException;
import static com.google.common.base.Preconditions.*;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class MultiThreadedRandomEquivalence {

    private static final Logger LOG = LoggerFactory.getLogger(MultiThreadedRandomEquivalence.class);

    private static final int MAX_NB_EXEC_PER_TASK = 500;

    private double delta = 0.1;
    private double epsilon = 0.1;
    private boolean failFirst = false;
    private boolean localized = false;
    private int maxNbThreads = 1;
    private int estimatedNbrRuns = -1;
    private int tracesize;
    private boolean testCasesSem = false;

    public MultiThreadedRandomEquivalence() {

    }

    public double getDelta() {
        return delta;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public boolean isFailFirst() {
        return failFirst;
    }

    public boolean isLocalized() {
        return localized;
    }

    public int getMaxNbThreads() {
        return maxNbThreads;
    }

    public int getEstimatedNbrRuns() {
        if (estimatedNbrRuns < 0) {
            estimatedNbrRuns = (int) ((8 * Math.log(2 / delta)) / (epsilon * epsilon)); // NB: the fomula starts by 4* Math but since we are performing to independent monte carlo simulations we have to double this number ot keep the same precision. 
        }
        return estimatedNbrRuns;
    }

    public int getTracesize() {
        return tracesize;
    }

    public void setTracesize(int tracesize) {
        this.tracesize = tracesize;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public void setFailFirst(boolean failFirst) {
        this.failFirst = failFirst;
    }

    public void setLocalized(boolean localized) {
        this.localized = localized;
    }

    public void setMaxNbThreads(int maxNbThreads) {
        this.maxNbThreads = maxNbThreads;
    }

    public EquivalenceResults computeEquivalence(LabelledTransitionSystem original, LabelledTransitionSystem mutant) throws CounterExampleFoundException, ConnectivityHypothesisViolationException {
        return MultiThreadedRandomEquivalence.this.computeEquivalence(original, mutant, 0.5);
    }

    public EquivalenceResults computeEquivalence(LabelledTransitionSystem original, LabelledTransitionSystem mutant, double originalTestCasesRatio) throws CounterExampleFoundException, ConnectivityHypothesisViolationException {
        checkArgument(originalTestCasesRatio >= 0 && originalTestCasesRatio <= 1, "Argument 'originalTestCasesRatio' must be between 0 and 1 (incl.)!");
        // Computer number of executions
        int nbrOriginal = (int) (getEstimatedNbrRuns() * originalTestCasesRatio);
        int nbrMutants = getEstimatedNbrRuns() - nbrOriginal;
        // See if there is local random
        List<State> local = null;
        if (isLocalized()) {
            local = MutantDifference.getLTSDiffStates(original, mutant);
        }
        // Create Threads pool and completion execution service
        ExecutorService service = null;
        EquivalenceResults result = new EquivalenceResults();
        try {
            service = Executors.newFixedThreadPool(getMaxNbThreads());
            CompletionService<EquivalenceResults> executor = new ExecutorCompletionService(service);
            // Launch Execution
            int nbrSubmissions = 0;
            while (nbrOriginal > 0 || nbrMutants > 0) {
                if (nbrOriginal > 0) {
                    int nbOrigMutTasks = Math.min(nbrOriginal, MAX_NB_EXEC_PER_TASK);
                    RandomEquivalenceTask task = new RandomEquivalenceTask(original, mutant, getTracesize(), nbOrigMutTasks, local, testCasesSem);
                    task.setFailFirst(isFailFirst());
                    executor.submit(task);
                    nbrSubmissions++;
                    nbrOriginal = nbrOriginal - nbOrigMutTasks;
                    LOG.debug("Task ORIGINAL (selection) to MUTANT (execution) submitted with {} executions (remains {})", nbOrigMutTasks, nbrOriginal);
                }
                if (nbrMutants > 0) {
                    int nbMutOrigTasks = Math.min(nbrMutants, MAX_NB_EXEC_PER_TASK);
                    RandomEquivalenceTask task = new RandomEquivalenceTask(mutant, original, getTracesize(), nbMutOrigTasks, local, testCasesSem);
                    task.setFailFirst(isFailFirst());
                    executor.submit(task);
                    nbrSubmissions++;
                    nbrMutants = nbrMutants - nbMutOrigTasks;
                    LOG.debug("Task MUTANT (selection) to ORIGINAL (execution) submitted with {} executions (remains {})", nbMutOrigTasks, nbrMutants);
                }
            }
            // Compute results
            LOG.debug("Starts results computation ({} submissions)", nbrSubmissions);
            for (int i = 0; i < nbrSubmissions; i++) {
                try {
                    EquivalenceResults r = executor.take().get();
                    result = result.merge(r);
                    LOG.debug("Number of results processed: {}/{}", i + 1, nbrSubmissions);
                } catch (InterruptedException ex) {
                    LOG.error("Interrupted while processing results!");
                } catch (ExecutionException ex) {
                    // If an exception has been lauched => the RandomEquivalenceTask was set to fail first
                    if (ex.getCause() instanceof CounterExampleFoundException) {
                        CounterExampleFoundException cause = (CounterExampleFoundException) ex.getCause();
                        result = result.merge(cause.getPartialResults());
                        throw new CounterExampleFoundException("Counter example found during computation", cause.getOriginal(), cause.getMutant(), result);
                    } else if(ex.getCause() instanceof ConnectivityHypothesisViolationException){
                        ConnectivityHypothesisViolationException cause = (ConnectivityHypothesisViolationException) ex.getCause();
                        result = result.merge(cause.getPartialResults());
                        throw new ConnectivityHypothesisViolationException("Connectivity hypothesis violated in mutant", cause.getOriginal(), cause.getMutant(), result);
                    }
                    throw new IllegalStateException("Unexpected exception occured during computation!", ex.getCause());
                } catch (CancellationException ex) {
                    LOG.info("Task {} has been canceled", i, ex);
                } catch (IllegalStateException ex) {
                    LOG.error("Task {} was unnable to select a trace of size {} in given LTS!", i, getTracesize(), ex);
                }
            }
        } finally {
            if (service != null) {
                service.shutdownNow();
            }
        }
        return result;
    }

    public void setTestCaseSemantic(boolean testCasesSem) {
        this.testCasesSem = testCasesSem;
    }

}
