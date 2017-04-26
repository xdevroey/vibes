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
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.dsl.exception.TestCaseDefinitionException;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.FtsMutableTestCase;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.test.UsageModelMutableTestCase;
import be.unamur.transitionsystem.test.selection.AccumulatorWrapUp;
import be.unamur.transitionsystem.test.selection.AlwaysTrueValidator;
import be.unamur.transitionsystem.test.selection.LocalRandomTestCaseGenerator;
import be.unamur.transitionsystem.test.selection.RandomTestCaseGenerator;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import be.unamur.transitionsystem.test.selection.fts.FtsTestCaseValidator;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import java.util.List;

public class Random {

    // LTS Random generation 
    public static TestSet randomSelection(LabelledTransitionSystem ts) {
        return randomSelection(ts, RandomTestCaseGenerator.DEFAULT_NUMBER_TEST_CASES);
    }

    public static TestSet randomSelection(LabelledTransitionSystem ts, int nbrTestCases) {
        return randomSelection(ts, nbrTestCases, RandomTestCaseGenerator.DEFAULT_MAX_LENGTH);
    }

    public static TestSet randomSelection(LabelledTransitionSystem ts, int nbrTestCases, int testCaseMaxLength) {
        AccumulatorWrapUp wrapUp = new AccumulatorWrapUp();
        RandomTestCaseGenerator gen = new RandomTestCaseGenerator(LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, wrapUp);
        try {
            gen.generateAbstractTestSet(ts, nbrTestCases, testCaseMaxLength);
            return new TestSet(wrapUp.getTestCases());
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }

    // FTS Random generation 
    public static TestSet randomSelection(FeaturedTransitionSystem fts, SolverFacade solver) {
        return randomSelection(fts, solver, RandomTestCaseGenerator.DEFAULT_NUMBER_TEST_CASES);
    }

    public static TestSet randomSelection(FeaturedTransitionSystem fts, SolverFacade solver, int nbrTestCases) {
        return randomSelection(fts, solver, nbrTestCases, RandomTestCaseGenerator.DEFAULT_MAX_LENGTH);
    }

    public static TestSet randomSelection(FeaturedTransitionSystem fts, SolverFacade solver, int nbrTestCases, int testCaseMaxLength) {
        AccumulatorWrapUp wrapup = new AccumulatorWrapUp();
        RandomTestCaseGenerator gen = new RandomTestCaseGenerator(FtsMutableTestCase.FACTORY,
                new FtsTestCaseValidator(solver), wrapup);
        try {
            gen.generateAbstractTestSet(fts, nbrTestCases, testCaseMaxLength);
            return new TestSet(wrapup.getTestCases());
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }

    // UsageModel Random generation 
    public static TestSet randomSelection(UsageModel um) {
        return randomSelection(um, RandomTestCaseGenerator.DEFAULT_NUMBER_TEST_CASES);
    }

    public static TestSet randomSelection(UsageModel um, int nbrTestCases) {
        return randomSelection(um, nbrTestCases, RandomTestCaseGenerator.DEFAULT_MAX_LENGTH);
    }

    public static TestSet randomSelection(UsageModel um, int nbrTestCases, int testCaseMaxLength) {
        AccumulatorWrapUp wrapUp = new AccumulatorWrapUp();
        RandomTestCaseGenerator gen = new RandomTestCaseGenerator(UsageModelMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, wrapUp);
        try {
            gen.generateAbstractTestSet(um, nbrTestCases, testCaseMaxLength);
            return new TestSet(wrapUp.getTestCases());
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }

    // LTS Local Random generation 
    public static TestSet localRandomSelection(LabelledTransitionSystem ts, List<State> states) {
        return localRandomSelection(ts, states, RandomTestCaseGenerator.DEFAULT_NUMBER_TEST_CASES);
    }

    public static TestSet localRandomSelection(LabelledTransitionSystem ts, List<State> states, int nbrTestCases) {
        return localRandomSelection(ts, states, nbrTestCases, RandomTestCaseGenerator.DEFAULT_MAX_LENGTH);
    }

    public static TestSet localRandomSelection(LabelledTransitionSystem ts, List<State> states, int nbrTestCases, int testCaseMaxLength) {
        AccumulatorWrapUp wrapUp = new AccumulatorWrapUp();
        LocalRandomTestCaseGenerator gen = new LocalRandomTestCaseGenerator(LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, wrapUp, states);
        try {
            gen.generateAbstractTestSet(ts, nbrTestCases, testCaseMaxLength);
            return new TestSet(wrapUp.getTestCases());
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }

    // FTS Local Random generation 
    public static TestSet localRandomSelection(FeaturedTransitionSystem fts, SolverFacade solver, List<State> states) {
        return localRandomSelection(fts, solver, states, RandomTestCaseGenerator.DEFAULT_NUMBER_TEST_CASES);
    }

    public static TestSet localRandomSelection(FeaturedTransitionSystem fts, SolverFacade solver, List<State> states, int nbrTestCases) {
        return localRandomSelection(fts, solver, states, nbrTestCases, RandomTestCaseGenerator.DEFAULT_MAX_LENGTH);
    }

    public static TestSet localRandomSelection(FeaturedTransitionSystem fts, SolverFacade solver, List<State> states, int nbrTestCases, int testCaseMaxLength) {
        AccumulatorWrapUp wrapup = new AccumulatorWrapUp();
        LocalRandomTestCaseGenerator gen = new LocalRandomTestCaseGenerator(FtsMutableTestCase.FACTORY,
                new FtsTestCaseValidator(solver), wrapup, states);
        try {
            gen.generateAbstractTestSet(fts, nbrTestCases, testCaseMaxLength);
            return new TestSet(wrapup.getTestCases());
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }

    // UsageModel Local Random generation 
    public static TestSet localRandomSelection(UsageModel um, List<State> states) {
        return localRandomSelection(um, states, RandomTestCaseGenerator.DEFAULT_NUMBER_TEST_CASES);
    }

    public static TestSet localRandomSelection(UsageModel um, List<State> states, int nbrTestCases) {
        return localRandomSelection(um, states, nbrTestCases, RandomTestCaseGenerator.DEFAULT_MAX_LENGTH);
    }

    public static TestSet localRandomSelection(UsageModel um, List<State> states, int nbrTestCases, int testCaseMaxLength) {
        AccumulatorWrapUp wrapUp = new AccumulatorWrapUp();
        LocalRandomTestCaseGenerator gen = new LocalRandomTestCaseGenerator(UsageModelMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, wrapUp, states);
        try {
            gen.generateAbstractTestSet(um, nbrTestCases, testCaseMaxLength);
            return new TestSet(wrapUp.getTestCases());
        } catch (TestCaseSelectionException e) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", e);
        }
    }

}
