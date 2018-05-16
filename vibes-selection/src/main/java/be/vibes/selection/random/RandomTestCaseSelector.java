package be.vibes.selection.random;

import be.vibes.selection.AbstractTestCaseSelector;
import be.vibes.selection.exception.SinkStateReachedException;
import be.vibes.selection.exception.TestCaseSelectionException;
import be.vibes.ts.State;
import be.vibes.ts.TestCase;
import be.vibes.ts.Transition;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.exception.TransitionSystenExecutionException;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Random test case selection using random walk in the transition system.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class RandomTestCaseSelector extends AbstractTestCaseSelector {

    private static final Logger LOG = LoggerFactory.getLogger(RandomTestCaseSelector.class);

    public static final int DEFAULT_MAX_NUMBER_TRY = 1000;
    public static final int DEFAULT_MAX_LENGTH = 2000;

    private int maxNbrAttempts;
    private int maxLength;
    protected final Random random;

    protected int id = 0;

    public RandomTestCaseSelector(TransitionSystem transitionSystem, int maxNbrTry, int maxLength) {
        super(transitionSystem);
        this.maxNbrAttempts = maxNbrTry;
        this.maxLength = maxLength;
        this.random = new Random();
    }

    public RandomTestCaseSelector(TransitionSystem transitionSystem) {
        this(transitionSystem, DEFAULT_MAX_NUMBER_TRY, DEFAULT_MAX_LENGTH);
    }
 
    public RandomTestCaseSelector(TransitionSystem transitionSystem,int maxLength) {
        this(transitionSystem, DEFAULT_MAX_NUMBER_TRY, maxLength);
    }

    public int getMaxNbrAttempts() {
        return maxNbrAttempts;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxNbrAttempts(int maxNbrAttempts) {
        this.maxNbrAttempts = maxNbrAttempts;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setRandomSeed(long randomSeed) {
        this.random.setSeed(randomSeed);
    }

    public TestCase select() throws TestCaseSelectionException {
        int tries = 0;
        TestCase tc = null;
        while (tc == null && tries < getMaxNbrAttempts()) {
            try {
                tc = trySelect();
            } catch (SinkStateReachedException ex) {
                LOG.debug("Sink state {} reached during selection, will try in next attempt!", ex.getSinkState());
            }
            tries++;
        }
        if (tc == null) {
            throw new TestCaseSelectionException("Could not select a random test case within " + getMaxNbrAttempts() + " attempts!");
        } else {
            return tc;
        }
    }

    private TestCase trySelect() throws SinkStateReachedException {
        TransitionSystem ts = getTransitionSystem();
        TestCase tc = new TestCase("random" + (this.id++));
        Transition tr = getRandomTransition(null);
        State last;
        try {
            tc.enqueue(tr);
            last = tc.getLast().getTarget();
            int length = 1;
            while (!last.equals(ts.getInitialState()) && length < getMaxLength()) {
                tr = getRandomTransition(tc);
                tc.enqueue(tr);
                last = tc.getLast().getTarget();
                length++;
            }
        } catch (TransitionSystenExecutionException ex) {
            // Should not happen if TransitionSystem class invariants and getRandomTransition are correct!
            LOG.error("Could not add transition {} to TestCase {}!", tr, tc);
            throw new IllegalStateException("Transition could not be added to test case!", ex);
        }
        if (last.equals(ts.getInitialState())) {
            //Could complete the test case
            return tc;
        } else {
            // Test case does not end in the initial state 
            return null;
        }
    }

    protected Transition getRandomTransition(TestCase tc) throws SinkStateReachedException {
        TransitionSystem ts = getTransitionSystem();
        State state = tc == null ? ts.getInitialState() : tc.getLast().getTarget();
        List<Transition> outgoings = Lists.newArrayList(ts.getOutgoing(state));
        if (outgoings.isEmpty()) {
            throw new SinkStateReachedException("Sink state " + state + " reached, could not select next transition!", state);
        } else {
            return outgoings.get(this.random.nextInt(outgoings.size()));
        }
    }

    @Override
    public List<TestCase> select(int nbr) throws TestCaseSelectionException {
        List<TestCase> lst = new ArrayList<>();
        for(int i = 0 ; i < nbr ; i++){
            lst.add(select());
        }
        return lst;
    }

}
