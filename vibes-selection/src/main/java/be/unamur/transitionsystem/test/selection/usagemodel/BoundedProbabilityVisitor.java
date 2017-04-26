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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.Visitor;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.UsageModelTestCase;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.test.selection.TestCaseValidator;
import be.unamur.transitionsystem.test.selection.TestCaseWrapUp;

class BoundedProbabilityVisitor implements Visitor {

    private static final Logger logger = LoggerFactory.getLogger(BoundedProbabilityVisitor.class);

    private TestCaseWrapUp wrapUp;
    private TestCaseValidator validator;
    private int lMin = Integer.MIN_VALUE;
    private int lMax = Integer.MAX_VALUE;
    private double probaMin = 0.0;
    private double probaMax = 1.0;
    private boolean fireTransitionOnce = false;
    private UsageModelTestCase testCase;
    private State initialState;

    public BoundedProbabilityVisitor(TestCaseWrapUp wrapUp, TestCaseValidator validator,
            int lMin, int lMax, double probaMin, double probaMax,
            boolean fireTransitionOnce, UsageModelTestCase testCase) {
        this.wrapUp = wrapUp;
        this.validator = validator;
        this.lMin = lMin;
        this.lMax = lMax;
        this.probaMin = probaMin;
        this.probaMax = probaMax;
        this.fireTransitionOnce = fireTransitionOnce;
        this.testCase = testCase;
    }

    public BoundedProbabilityVisitor(TestCaseWrapUp wrapUp, TestCaseValidator validator,
            UsageModelTestCase testCase) {
        this.wrapUp = wrapUp;
        this.validator = validator;
        this.testCase = testCase;
    }

    public TestCaseWrapUp getWrapUp() {
        return wrapUp;
    }

    public void setWrapUp(TestCaseWrapUp wrapUp) {
        this.wrapUp = wrapUp;
    }

    public TestCaseValidator getValidator() {
        return validator;
    }

    public void setValidator(TestCaseValidator validator) {
        this.validator = validator;
    }

    public int getlMin() {
        return lMin;
    }

    public void setlMin(int lMin) {
        this.lMin = lMin;
    }

    public int getlMax() {
        return lMax;
    }

    public void setlMax(int lMax) {
        this.lMax = lMax;
    }

    public double getProbaMin() {
        return probaMin;
    }

    public void setProbaMin(double probaMin) {
        this.probaMin = probaMin;
    }

    public double getProbaMax() {
        return probaMax;
    }

    public void setProbaMax(double probaMax) {
        this.probaMax = probaMax;
    }

    public boolean isFireTransitionOnce() {
        return fireTransitionOnce;
    }

    public void setFireTransitionOnce(boolean fireTransitionOnce) {
        this.fireTransitionOnce = fireTransitionOnce;
    }

    private List<Transition> fired;

    @Override
    public void visit(State state) throws VisitException {
        logger.debug("Calling to visit with state {}", state);
        logger.trace("Current trace is {}", testCase);
        double proba = testCase.getProbability();

        if (this.initialState == null) {
            logger.trace("Initial state set");
            initialState = state; // first visited state == initial state
            fired = new LinkedList<Transition>();
        } else if (testCase.size() > this.lMax) {
            // Check the length of the trace is <= lMax
            logger.trace("  -> Length of the test case ({}) is > lMax ({})",
                    testCase.size(), this.lMax);
            return;
        } else if (testCase.size() > 0 && proba < probaMin) {
            logger.debug("Test-case has a probability ({}) lower than lower bound ({})", proba, probaMin);
            return;
        } else if (this.initialState == state) {
            logger.trace("  -> Returned in initial state");
            if (testCase.size() < lMin) {
                logger.trace(" Test-Case is too short, dropped");
                return;
            } else if (proba > probaMax) {
                logger.trace("Test-case has a probability ({}) above upper bound ({}), dropped", proba, probaMax);
                return;
            } else {
                // Check if returned in initial state (== accepting state)
                logger.debug("Finish building test case: {}", testCase);
                if (validator.isValid(testCase)) {
                    try {
                        wrapUp.wrapUp(testCase.copy());
                    } catch (TestCaseException e) {
                        logger.error("Error while wraping up test case!", e);
                    }
                } else {
                    logger.debug("Test case is invalid!");
                }
                return;
            }
        }

        // Continue exploration
        logger.trace("Continue exploration");
        for (Iterator<Transition> it = state.outgoingTransitions(); it.hasNext();) {
            Transition tr = (Transition) it.next();
            if (!(fireTransitionOnce && fired.contains(tr))) {
                // Explore in DFS mode
                logger.trace("Taking transition {}", tr);
                try {
                    fired.add(tr);
                    testCase.enqueue(tr);
                    tr.getTo().accept(this);
                    testCase.dequeue();
                    fired.remove(fired.size() - 1);
                } catch (Exception e) {
                    logger.error("Error while generating test cases!", e);
                }
            }
        }
    }

}
