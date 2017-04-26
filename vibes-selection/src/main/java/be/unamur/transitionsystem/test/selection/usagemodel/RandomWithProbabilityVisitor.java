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
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.Visitor;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.UsageModelTestCase;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.test.selection.TestCaseValidator;
import be.unamur.transitionsystem.test.selection.TestCaseWrapUp;
import be.unamur.transitionsystem.usagemodel.UsageModelTransition;

class RandomWithProbabilityVisitor implements Visitor {

    private static final Logger logger = LoggerFactory
            .getLogger(RandomWithProbabilityVisitor.class);

    private TestCaseWrapUp wrapUp;
    private TestCaseValidator validator;
    private int lMax = Integer.MAX_VALUE;
    private Random random;
    private UsageModelTestCase testCase;

    private State initialState;

    public RandomWithProbabilityVisitor(TestCaseWrapUp wrapUp,
            TestCaseValidator validator, int lMax, Random random,
            UsageModelTestCase testCase) {
        this.wrapUp = wrapUp;
        this.validator = validator;
        this.lMax = lMax;
        this.random = random;
        this.testCase = testCase;
    }

    @Override
    public void visit(State state) throws VisitException {
        logger.trace("Visiting state {}", state.getName());

        if (initialState == null) {
            logger.trace("Setting initial state");
            initialState = state;
        } else if (testCase.size() > lMax) {
            // Check the length of the trace is <= maxLength
            logger.trace("  -> Length of test case ({}) is > lMax ({})", testCase.size(),
                    lMax);
            return;
        } else if (this.initialState == state) {
            // Check if returned in initial state (== accepting state)
            logger.trace("  -> Returned in initial state");
            if (validator.isValid(testCase)) {
                logger.debug("Finish building test case : {}", testCase);
                try {
                    wrapUp.wrapUp(testCase.copy());
                } catch (TestCaseException e) {
                    throw new VisitException("Exception while copying test case!", e);
                }
                initialState = null;
            } else {
                logger.trace("Test case {} is invalid", testCase);
            }
            return;
        }

        // Continue exploration
        logger.trace("  -> Continue exploration");

        Transition tr;
        List<Transition> lst = Lists.newArrayList(state.outgoingTransitions());
        int elemIdx;
        double maxProba, randomNbr;
        Iterator<Transition> it;
        UsageModelTransition t;
        while (initialState != null && lst.size() > 0) {
            // Get Maximum proba for remaining transitions
            maxProba = 0;
            it = lst.iterator();
            while (it.hasNext()) {
                t = (UsageModelTransition) it.next();
                maxProba = maxProba + t.getProbability();
            }
            // Get the element with the proba
            randomNbr = random.nextDouble() * maxProba;
            elemIdx = -1;
            maxProba = 0;
            it = lst.iterator();
            while (it.hasNext() && randomNbr >= maxProba) {
                t = (UsageModelTransition) it.next();
                maxProba = maxProba + t.getProbability();
                elemIdx++;
            }
            // Remove the element from the list to avoid double visit
            tr = lst.remove(elemIdx);
            try {
                // Add transition to the trace
                testCase.enqueue(tr);
                // Explore in Random mode
                logger.trace("Taking transition {}", tr.getAction());
                tr.getTo().accept(this);
                // Restore current trace
                testCase.dequeue();
            } catch (TestCaseException e) {
                throw new VisitException("Exception while generating test case!", e);
            }
        }
    }

}
