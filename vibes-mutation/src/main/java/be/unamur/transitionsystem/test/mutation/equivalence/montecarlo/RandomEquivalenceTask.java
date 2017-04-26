package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.test.mutation.exception.ConnectivityHypothesisViolationException;
import be.unamur.transitionsystem.test.mutation.exception.CounterExampleFoundException;
import be.unamur.transitionsystem.test.selection.LocalRandomTestCaseSelector;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
class RandomEquivalenceTask implements Callable<EquivalenceResults> {

    private static final Logger LOG = LoggerFactory.getLogger(RandomEquivalenceTask.class);

    private static final int NB_MAX_TRY = 1000;

    private TraceSelector selector;
    private TraceExecutor executor;
    private int nbrExecutions;
    private boolean failFirst;

    public RandomEquivalenceTask(LabelledTransitionSystem source, LabelledTransitionSystem executed, int traceSize, int nbrExecutions, boolean testCasesSem) {
        this(source, executed, traceSize, nbrExecutions, null, testCasesSem);
    }

    public RandomEquivalenceTask(LabelledTransitionSystem source, LabelledTransitionSystem executed, int traceSize, int nbrExecutions, List<State> localStates, boolean testCasesSem) {
        if (localStates == null) {
            if (testCasesSem) {
                //TODO implement random test case selection.
                throw new UnsupportedOperationException("Random test-case semantic trace selection is not yet impelmented!");
            } else {
                this.selector = new RandomFixedLengthTraceSelector(source, traceSize);
            }
        } else if (testCasesSem) {
            this.selector = new TestCaseSelectorWrapper(new LocalRandomTestCaseSelector(source, NB_MAX_TRY, traceSize, Sets.newHashSet(localStates)));
        } else {
            this.selector = new LocalRandomFixedLengthTraceSelector(source, traceSize, localStates);
        }
        this.executor = new TraceExecutor(executed);
        this.nbrExecutions = nbrExecutions;
        this.failFirst = false;
    }

    public boolean isFailFirst() {
        return failFirst;
    }

    public void setFailFirst(boolean failFirst) {
        this.failFirst = failFirst;
    }

    @Override
    public EquivalenceResults call() throws CounterExampleFoundException, ConnectivityHypothesisViolationException {
        EquivalenceResults results = new EquivalenceResults();
        for (int i = 0; i < nbrExecutions; i++) {
            List<Action> trace = null;
            for (int nbTry = 0; nbTry < NB_MAX_TRY && trace == null; nbTry++) {
                try {
                    trace = selector.select();
                } catch (IllegalStateException ex) {
                    LOG.debug("Sink state found at try {}", nbTry + 1);
                }
            }
            if (trace == null) {
                LOG.debug("Was unnable to generate a trace for given lts (%s) within %s tries", selector.getTransitionSystem(), NB_MAX_TRY);
                throw new ConnectivityHypothesisViolationException("Was unnable to generate a trace for given lts within " + NB_MAX_TRY + " tries", selector.getTransitionSystem(), executor.getLts(), results);
            }
            if (executor.execute(trace)) {
                results.incrementSucceededRuns();
            } else {
                LtsMutableTestCase testCase = new LtsMutableTestCase();
                try {
                    for (Action act : trace) {
                        testCase.enqueue(act);
                    }
                } catch (TestCaseException ex) {
                    LOG.error("Unnable to add action to test case, this should not happen with LtsMutableTestCase!", ex);
                }
                results.incrementFailedRuns(testCase);
                if (isFailFirst()) {
                    throw new CounterExampleFoundException("Counter exemple test case found!", selector.getTransitionSystem(), executor.getLts(), results);
                }
            }
        }
        return results;
    }

}
