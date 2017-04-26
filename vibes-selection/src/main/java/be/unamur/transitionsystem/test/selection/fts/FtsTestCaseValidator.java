package be.unamur.transitionsystem.test.selection.fts;

/*
 * #%L
 * vibes-selection
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.fts.solver.ConstraintIdentifier;
import be.unamur.fts.solver.SolverFacade;
import be.unamur.fts.solver.SolverFatalErrorException;
import be.unamur.fts.solver.exception.ConstraintNotFoundException;
import be.unamur.fts.solver.exception.ConstraintSolvingException;
import be.unamur.fts.solver.exception.SolverInitializationException;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.test.FtsTestCase;
import be.unamur.transitionsystem.test.selection.TestCaseValidator;

public class FtsTestCaseValidator implements TestCaseValidator {

    private static final Logger LOG = LoggerFactory
            .getLogger(FtsTestCaseValidator.class);

    private SolverFacade solver;

    /**
     * Build a new test case validator for a FTS based on the solver initialized
     * with the feature diagram of the FTS.
     *
     * @param solver The solver initialized with the feature diagram linked to
     * the FTS.
     */
    public FtsTestCaseValidator(SolverFacade solver) {
        this.solver = solver;
    }

    /**
     * Returns true if the product constraint of the test case is satisfiable
     * with the feature model of the FTS.
     */
    @Override
    public boolean isValid(TestCase testCase) {
        boolean valid = false;
        if (!(testCase instanceof FtsTestCase)) {
            return false;
        }
        try {
            ConstraintIdentifier id = solver
                    .addConstraint(((FtsTestCase) testCase)
                            .getProductConstraint());
            valid = solver.isSatisfiable();
            solver.removeConstraint(id);
        } catch (SolverInitializationException e) {
            LOG.debug("Could not add constraint, insatisfyable system ?");
        } catch (SolverFatalErrorException e) {
            LOG.warn(
                    "Fatal error while using solver to validate test case, will try to reset.",
                    e);
            try {
                solver.reset();
            } catch (SolverInitializationException e1) {
                LOG.error("Fatal error while resetting solver!", e);
            }
        } catch (ConstraintNotFoundException e) {
            LOG.warn(
                    "Constraint not found in solver. This exception should not happen!",
                    e);
            throw new IllegalStateException(
                    "Shit, which should not happen, happend while solving constraint!", e);
        } catch (ConstraintSolvingException e) {
            LOG.debug("Exception launched while solving constraints!", e);
        }
        return valid;
    }

    /**
     * Returns true if the product constraint of the test case is satisfiable
     * with the feature model of the FTS.
     */
    @Override
    public boolean isValid(TestCase testCase, TestSet set) {
        return isValid(testCase);
    }

}
