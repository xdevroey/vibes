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
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.UsageModelMutableTestCase;
import be.unamur.transitionsystem.test.selection.AbstractTestCaseGenerator;
import be.unamur.transitionsystem.test.selection.TestCaseValidator;
import be.unamur.transitionsystem.test.selection.TestCaseWrapUp;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;

public class BoundedProbabilityGenerator extends AbstractTestCaseGenerator {

    private double lowest = 0.0;
    private double highest = 1.0;
    private int maxlgt = -1;
    private int minlgt = Integer.MIN_VALUE;
    private boolean fireTransitionsOnce = false;

    public BoundedProbabilityGenerator(TestCaseFactory testCaseFactory,
            TestCaseValidator validator, TestCaseWrapUp wrapUp) {
        super(testCaseFactory, validator, wrapUp);
    }

    public BoundedProbabilityGenerator(TestCaseFactory testCaseFactory,
            TestCaseValidator validator, TestCaseWrapUp wrapUp, double lowest,
            double highest) {
        super(testCaseFactory, validator, wrapUp);
        this.lowest = lowest;
        this.highest = highest;
    }

    public BoundedProbabilityGenerator(TestCaseFactory testCaseFactory,
            TestCaseValidator validator, TestCaseWrapUp wrapUp, double lowest,
            double highest, int maxlgt, int minlgt, boolean fireTransitionsOnce) {
        super(testCaseFactory, validator, wrapUp);
        this.lowest = lowest;
        this.highest = highest;
        this.maxlgt = maxlgt;
        this.minlgt = minlgt;
        this.fireTransitionsOnce = fireTransitionsOnce;
    }

    public double getLowest() {
        return lowest;
    }

    public void setLowest(double lowest) {
        this.lowest = lowest;
    }

    public double getHighest() {
        return highest;
    }

    public void setHighest(double highest) {
        this.highest = highest;
    }

    public int getMaxlgt() {
        return maxlgt;
    }

    public void setMaxlgt(int maxlgt) {
        this.maxlgt = maxlgt;
    }

    public int getMinlgt() {
        return minlgt;
    }

    public void setMinlgt(int minlgt) {
        this.minlgt = minlgt;
    }

    public boolean isFireTransitionsOnce() {
        return fireTransitionsOnce;
    }

    public void setFireTransitionsOnce(boolean fireTransitionsOnce) {
        this.fireTransitionsOnce = fireTransitionsOnce;
    }

    @Override
    public void generateAbstractTestSet(TransitionSystem ts,
            TestCaseValidator validator, TestCaseWrapUp wrapUp)
            throws TestCaseSelectionException {
        int maxSize = (2 * ts.numberOfStates()) + 1;
        if (this.maxlgt > 0) {
            maxSize = this.maxlgt;
        }
        BoundedProbabilityVisitor visitor = new BoundedProbabilityVisitor(wrapUp,
                validator, minlgt, maxSize, lowest, highest, fireTransitionsOnce,
                new UsageModelMutableTestCase());
        try {
            ts.getInitialState().accept(visitor);
        } catch (VisitException e) {
            throw new TestCaseSelectionException(
                    "Exception while generating Test Cases!", e);
        }
    }
}
