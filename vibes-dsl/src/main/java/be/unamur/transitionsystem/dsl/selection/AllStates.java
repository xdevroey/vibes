package be.unamur.transitionsystem.dsl.selection;

/*
 * #%L
 * vibes-dsl
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
import be.unamur.fts.solver.SolverFacade;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.dsl.exception.TestCaseDefinitionException;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.FtsMutableTestCase;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.test.UsageModelMutableTestCase;
import be.unamur.transitionsystem.test.selection.AccumulatorWrapUp;
import be.unamur.transitionsystem.test.selection.AllStatesTestCaseGenerator;
import be.unamur.transitionsystem.test.selection.AlwaysTrueValidator;
import be.unamur.transitionsystem.test.selection.WarshallScoreComputor;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import be.unamur.transitionsystem.test.selection.fts.FtsTestCaseValidator;
import be.unamur.transitionsystem.usagemodel.UsageModel;

/**
 * This class contains methods to select test cases from a transition system
 * such as the set of selected test cases respect the al-states coverage
 * criteria.
 *
 * @see TestSet
 * @see AllStatesTestCaseGenerator
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class AllStates {

    /**
     * Generates a set of test cases from the given transition system.
     *
     * @param ts The transition system on which test case selection is
     * performed.
     * @return A set of test cases satysfying the all states criteria.
     */
    public static TestSet allStatesSelection(LabelledTransitionSystem ts) {
        AccumulatorWrapUp wrapup = new AccumulatorWrapUp();
        WarshallScoreComputor computor = new WarshallScoreComputor();
        computor.initilise(ts);
        computor.warshall(5);
        AllStatesTestCaseGenerator gen = new AllStatesTestCaseGenerator(LtsMutableTestCase.FACTORY,
                AlwaysTrueValidator.TRUE_VALIDATOR, wrapup, computor);
        try {
            gen.generateAbstractTestSet(ts);
            return new TestSet(wrapup.getTestCases());
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }

    /**
     * Generates a set of test cases from the given transition system.
     *
     * @param fts The fetured transition system on which test case selection is
     * performed.
     * @param solver The soilver containing the variability model liked to the
     * FTS. Each test case of the returned test set will be valid when executed
     * on the fts with the given solver.
     * @return A set of test cases satysfying the all states criteria.
     */
    public static TestSet allStatesSelection(FeaturedTransitionSystem fts, SolverFacade solver) {
        AccumulatorWrapUp wrapup = new AccumulatorWrapUp();
        WarshallScoreComputor computor = new WarshallScoreComputor();
        computor.initilise(fts);
        computor.warshall(5);
        AllStatesTestCaseGenerator gen = new AllStatesTestCaseGenerator(FtsMutableTestCase.FACTORY,
                new FtsTestCaseValidator(solver), wrapup, computor);
        try {
            gen.generateAbstractTestSet(fts);
            return new TestSet(wrapup.getTestCases());
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }

    /**
     * Generates a set of test cases from the given transition system.
     *
     * @param um The usage model on which test case selection is performed.
     * @return A set of test cases satysfying the all states criteria.
     */
    public static TestSet allStatesSelection(UsageModel um) {
        AccumulatorWrapUp wrapup = new AccumulatorWrapUp();
        WarshallScoreComputor computor = new WarshallScoreComputor();
        computor.initilise(um);
        computor.warshall(5);
        AllStatesTestCaseGenerator gen = new AllStatesTestCaseGenerator(UsageModelMutableTestCase.FACTORY,
                AlwaysTrueValidator.TRUE_VALIDATOR, wrapup, computor);
        try {
            gen.generateAbstractTestSet(um);
            return new TestSet(wrapup.getTestCases());
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }

}
