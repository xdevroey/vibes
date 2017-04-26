package be.unamur.transitionsystem.test.selection.fts;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.test.selection.AccumulatorWrapUp;
import be.unamur.transitionsystem.test.selection.RandomTestCaseGenerator;
import be.unamur.transitionsystem.test.selection.TestCaseValidator;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;

public class DummyAllActionsGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(DummyAllActionsGenerator.class);

    private TestCaseFactory factory;
    private TestCaseValidator validator;

    public DummyAllActionsGenerator(TestCaseFactory factory, TestCaseValidator validatorl) {
        super();
        this.factory = factory;
        this.validator = validatorl;
    }

    public TestSet generateTestSet(TransitionSystem ts) throws TestCaseSelectionException {
        Set<Action> toCover = Sets.newHashSet(ts.actions());
        toCover.remove(ts.getAction(Action.NO_ACTION_NAME));
        LOG.debug("Actions to cover {}", toCover);
        TestSet set = new TestSet();
        AccumulatorWrapUp acc = new AccumulatorWrapUp();
        RandomTestCaseGenerator gen = new RandomTestCaseGenerator(factory, validator, acc);
        int maxLength = ts.numberOfStates() + (ts.numberOfStates() / 2);
        while (!toCover.isEmpty()) {
            TestCase candidate = gen.generateTestCase(ts, maxLength);
            if (candidate != null) {
                boolean removed = false;
                for (Action a : Lists.newArrayList(candidate.iterator())) {
                    removed = toCover.remove(a) || removed;
                }
                if (removed) {
                    set.add(candidate);
                    LOG.debug("Test case {} added to test set", candidate);
                } else {
                    LOG.debug("Test case {} rejected", candidate);
                }
            }
        }
        return set;
    }

}
