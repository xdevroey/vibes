package be.unamur.transitionsystem.dsl.selection;

import be.unamur.fts.solver.SolverFacade;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.dsl.exception.TestCaseDefinitionException;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.FtsMutableTestCase;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.test.UsageModelMutableTestCase;
import be.unamur.transitionsystem.test.selection.AlwaysTrueValidator;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import be.unamur.transitionsystem.test.selection.fts.DummyAllActionsGenerator;
import be.unamur.transitionsystem.test.selection.fts.FtsTestCaseValidator;
import be.unamur.transitionsystem.usagemodel.UsageModel;

/**
 * This class contains methods to select test cases from a transition system
 * such as the set of selected test cases respect the al-actions coverage
 * criteria.
 *
 * @see TestSet
 * @see DummyAllActionsGenerator
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class AllActions {

    /**
     * Generates a set of test cases from the given transition system.
     *
     * @param ts The transition system on which test case selection is
     * performed.
     * @return A set of test cases satysfying the all actions criteria.
     */
    public static TestSet allActionsSelection(LabelledTransitionSystem ts) {
        TestSet set = null;
        DummyAllActionsGenerator gen = new DummyAllActionsGenerator(
                LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR);
        try {
            set = gen.generateTestSet(ts);
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException(
                    "Exception while generating test cases!", e);
        }
        return set;
    }

    /**
     * Generates a set of test cases from the given transition system.
     *
     * @param um The usage model on which test case selection is performed.
     * @return A set of test cases satysfying the all actions criteria.
     */
    public static TestSet allActionsSelection(UsageModel um) {
        TestSet set = null;
        DummyAllActionsGenerator gen = new DummyAllActionsGenerator(
                FtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR);
        try {
            set = gen.generateTestSet(um);
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException(
                    "Exception while generating test cases!", e);
        }
        return set;
    }

    /**
     * Generates a set of test cases from the given transition system.
     *
     * @param fts The fetured transition system on which test case selection is
     * performed.
     * @param solver The soilver containing the variability model liked to the
     * FTS. Each test case of the returned test set will be valid when executed
     * on the fts with the given solver.
     * @return A set of test cases satysfying the all actions criteria.
     */
    public static TestSet allActionsSelection(FeaturedTransitionSystem fts,
            SolverFacade solver) {
        TestSet set = null;
        DummyAllActionsGenerator gen = new DummyAllActionsGenerator(
                UsageModelMutableTestCase.FACTORY, new FtsTestCaseValidator(solver));
        try {
            set = gen.generateTestSet(fts);
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException(
                    "Exception while generating test cases!", e);
        }
        return set;
    }

}
