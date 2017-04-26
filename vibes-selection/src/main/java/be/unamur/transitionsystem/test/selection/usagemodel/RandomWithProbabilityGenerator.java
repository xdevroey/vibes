package be.unamur.transitionsystem.test.selection.usagemodel;

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
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.UsageModelMutableTestCase;
import be.unamur.transitionsystem.test.selection.AbstractTestCaseGenerator;
import be.unamur.transitionsystem.test.selection.TestCaseValidator;
import be.unamur.transitionsystem.test.selection.TestCaseWrapUp;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;

public class RandomWithProbabilityGenerator extends AbstractTestCaseGenerator {

    private static final Logger logger = LoggerFactory.getLogger(RandomWithProbabilityGenerator.class);

    private Random random;
    private int numberOfTestCases = 100;
    private int lMax;

    public RandomWithProbabilityGenerator(TestCaseFactory testCaseFactory,
            TestCaseValidator validator, TestCaseWrapUp wrapUp, int lMax) {
        super(testCaseFactory, validator, wrapUp);
        this.random = new Random();
        this.lMax = lMax;
    }

    public int getNumberOfTestCases() {
        return numberOfTestCases;
    }

    public void setNumberOfTestCases(int numberOfTestCases) {
        this.numberOfTestCases = numberOfTestCases;
    }

    public void setSeed(long seed) {
        random.setSeed(seed);
    }

    @Override
    public void generateAbstractTestSet(TransitionSystem ts,
            TestCaseValidator validator, TestCaseWrapUp wrapUp)
            throws TestCaseSelectionException {
        for (int i = 0; i < numberOfTestCases; i++) {
            RandomWithProbabilityVisitor visitor = new RandomWithProbabilityVisitor(
                    wrapUp, validator, lMax, random,
                    new UsageModelMutableTestCase());
            try {
                ts.getInitialState().accept(visitor);
            } catch (VisitException e) {
                logger.error("Exception while generating test cases!", e);
            }
        }
    }

}
