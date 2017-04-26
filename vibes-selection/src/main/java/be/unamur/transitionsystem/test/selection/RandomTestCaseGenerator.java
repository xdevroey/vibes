package be.unamur.transitionsystem.test.selection;

/*
 * #%L vibes-selection %% Copyright (C) 2014 PReCISE, University of Namur %%
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. #L%
 */
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;

public class RandomTestCaseGenerator extends AbstractTestCaseGenerator {

    private static final Logger logger = LoggerFactory
            .getLogger(RandomTestCaseGenerator.class);

    public static final int MAX_NUMBER_TRY = 1000;

    public static final int DEFAULT_NUMBER_TEST_CASES = 100;

    public static final int DEFAULT_MAX_LENGTH = 2000;

    private Random random;
    private RandomVisitor visitor;

    public RandomTestCaseGenerator(TestCaseFactory testCaseFactory,
            TestCaseValidator validator, TestCaseWrapUp wrapUp) {
        super(testCaseFactory, validator, wrapUp);
        this.random = new Random();
        this.visitor = new RandomVisitor(random, testCaseFactory, validator);
    }

    protected Random getRandom() {
        return random;
    }

    public RandomTestCaseGenerator(TestCaseFactory testCaseFactory,
            TestCaseValidator validator, TestCaseWrapUp wrapUp, long seed) {
        this(testCaseFactory, validator, wrapUp);
        this.random.setSeed(seed);
    }

    public void generateAbstractTestSet(TransitionSystem ts, int maxNbrTestCases)
            throws TestCaseSelectionException {
        generateAbstractTestSet(ts, maxNbrTestCases, DEFAULT_MAX_LENGTH);
    }

    public void generateAbstractTestSet(TransitionSystem ts, int maxNbrTestCases, int maxLength)
            throws TestCaseSelectionException {
        generateAbstractTestSet(ts, getValidator(), getWrapUp(), maxNbrTestCases, maxLength);
    }

    public void generateAbstractTestSet(TransitionSystem ts, TestCaseValidator validator,
            TestCaseWrapUp wrapUp, int maxNbrTestCases, int maxLength)
            throws TestCaseSelectionException {
        int cpt = 0;
        while (cpt < maxNbrTestCases) {
            try {
                TestCase tc = generateTestCase(ts, validator, maxLength);
                if (tc != null) {
                    wrapUp.wrapUp(tc);
                }
                cpt++;
            } catch (TestCaseException ex) {
                throw new TestCaseSelectionException("Exception while wrepping up test case!", ex);
            }
        }
    }

    @Override
    public void generateAbstractTestSet(TransitionSystem ts, TestCaseValidator validator,
            TestCaseWrapUp wrapUp) throws TestCaseSelectionException {
        generateAbstractTestSet(ts, validator, wrapUp, DEFAULT_NUMBER_TEST_CASES, DEFAULT_MAX_LENGTH);
    }

    public TestCase generateTestCase(TransitionSystem ts, TestCaseValidator validator, int maxLength) throws TestCaseSelectionException {
        int invalid = 0;
        while (invalid < MAX_NUMBER_TRY) {
            try {
                visitor.reset();
                visitor.setMaxLength(maxLength);
                ts.getInitialState().accept(visitor);
                if (visitor.getTestCase() != null && validator.isValid(visitor.getTestCase())) {
                    logger.debug("Test case generated: {}", visitor.getTestCase());
                    return visitor.getTestCase();
                } else {
                    invalid++;
                }
            } catch (VisitException e) {
                throw new TestCaseSelectionException(
                        "Error while generating test case!", e);
            }
        }
        return null;
    }

    public TestCase generateTestCase(TransitionSystem ts, TestCaseValidator validator) throws TestCaseSelectionException {
        return generateTestCase(ts, validator, DEFAULT_MAX_LENGTH);
    }

    public TestCase generateTestCase(TransitionSystem ts) throws TestCaseSelectionException {
        return generateTestCase(ts, getValidator(), DEFAULT_MAX_LENGTH);
    }

    public TestCase generateTestCase(TransitionSystem ts, int maxLength) throws TestCaseSelectionException {
        return generateTestCase(ts, getValidator(), maxLength);
    }

}
