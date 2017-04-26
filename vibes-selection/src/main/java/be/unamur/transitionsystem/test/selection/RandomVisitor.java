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
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.Visitor;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;

class RandomVisitor implements Visitor {

    private State initialState = null;
    private TestCase testCase;
    private Random random;
    private TestCaseFactory testCaseFactory;
    private TestCaseValidator validator;
    private int maxLength = Integer.MAX_VALUE;

    private Set<Transition> alreadyFired;

    public RandomVisitor(Random random, TestCaseFactory testCaseFactory,
            TestCaseValidator validator) {
        this.random = random;
        this.testCaseFactory = testCaseFactory;
        this.validator = validator;
        this.alreadyFired = Sets.newHashSet();
    }

    public void reset() {
        this.initialState = null;
        this.testCase = null;
        this.alreadyFired = Sets.newHashSet();
        System.gc();
    }

    void setInitialState(State initialState) {
        this.initialState = initialState;
    }

    void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    void addAlreadyFiredTransition(Transition tr) {
        alreadyFired.add(tr);
    }

    /**
     * Return the generated test case or null if it failed to generate a valid
     * test case with the given maximal length.
     *
     * @return
     */
    public TestCase getTestCase() {
        return this.testCase;
    }

    @Override
    public void visit(State state) throws VisitException {
        if (initialState == null) {
            initialState = state;
            testCase = testCaseFactory.buildTestCase();
        } else if (testCase.size() > this.maxLength) {
            this.testCase = null;
            return;
        } else if (initialState.equals(state)) {
            return;
        }
        Transition next = null;
        try {
            next = getRandomTransition(state);
            if (next == null) {
                this.testCase = null;
                return;
            }
            testCase = testCase.enqueue(next);
            next.getTo().accept(this);
        } catch (TestCaseException e) {
            throw new VisitException("Unnable to add next tansition (" + next + ")!", e);
        }
    }

    private Transition getRandomTransition(State state) throws VisitException,
            TestCaseException {
        List<Transition> out = getValidOutgoingTransitions(state);
        if (out.isEmpty()) {
            return null;
        }
        int nbr = this.random.nextInt(out.size());
        return out.get(nbr);
    }

    private List<Transition> getValidOutgoingTransitions(State state)
            throws TestCaseException {
        List<Transition> valid = Lists.newArrayList();
        Iterator<Transition> candidates = state.outgoingTransitions();
        while (candidates.hasNext()) {
            Transition candidate = candidates.next();
            if (isValidCandidate(candidate)) {
                valid.add(candidate);
            }
        }
        return valid;
    }

    /**
     * Returns true if the candidate transition may be added to this's current test case.
     * @param candidate The candidate transition to add to current test case.
     * @return True if the candidate transition may be added to this's current test case.
     * @throws TestCaseException If an error occurs during verification of the validity.
     */
    protected boolean isValidCandidate(Transition candidate) throws TestCaseException {
        if(this.validator instanceof AlwaysTrueValidator){
            return true;
        }
        TestCase copy = testCase.enqueue(candidate);
        boolean valid = validator.isValid(copy);
        testCase = copy.dequeue();
        return valid;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

}
