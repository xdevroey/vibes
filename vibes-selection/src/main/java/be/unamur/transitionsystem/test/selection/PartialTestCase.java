package be.unamur.transitionsystem.test.selection;

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
import be.unamur.fts.fexpression.FExpression;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.test.FtsMutableTestCase;
import be.unamur.transitionsystem.test.FtsTestCase;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.exception.TestCaseException;

class PartialTestCase implements Comparable<PartialTestCase>, FtsTestCase {

    private int score;
    private TestCase testCase;
    private List<State> visitedStates;

    public PartialTestCase(int score, TestCase testCase) {
        this(score, testCase, new ArrayList<>());
    }

    public PartialTestCase(int score, TestCase testCase, List<State> visitedStates) {
        this.score = score;
        this.testCase = testCase;
        this.visitedStates = visitedStates;
    }

    public List<State> getVisitedStates() {
        return visitedStates;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    @Override
    public int compareTo(PartialTestCase o) {
        return getScore() - o.getScore();
    }

    @Override
    public String toString() {
        return "PartialTestCase [score=" + score + ", testCase=" + testCase + "]";
    }

    @Override
    public Iterator<Action> iterator() {
        return this.testCase.iterator();
    }

    @Override
    public boolean contains(Action act) {
        return this.testCase.contains(act);
    }

    @Override
    public String getId() {
        return this.testCase.getId();
    }

    @Override
    public void setId(String id) {
        this.testCase.setId(id);
    }

    @Override
    public int size() {
        return this.testCase.size();
    }

    @Override
    public TestCase enqueue(Transition trans) throws TestCaseException {
        this.testCase.enqueue(trans);
        this.visitedStates.add(trans.getTo());
        return this;
    }

    @Override
    public TestCase enqueue(Action action) throws TestCaseException {
        throw new UnsupportedOperationException("Not implemented for PartialTestCase!");
    }

    @Override
    public TestCase dequeue() throws TestCaseException {
        throw new UnsupportedOperationException("Not implemented for PartialTestCase!");
    }

    @Override
    public TestCase copy() throws TestCaseException {
        return new PartialTestCase(score, testCase.copy(), new ArrayList<>(this.visitedStates));
    }

    public State getLastVisitedState() {
        return visitedStates.get(visitedStates.size() - 1);
    }

    @Override
    public FtsMutableTestCase enqueue(Action action, FExpression constraint) throws TestCaseException {
        throw new UnsupportedOperationException("Not implemented for PartialTestCase!");
    }

    @Override
    public FExpression getProductConstraint() {
        if(testCase instanceof FtsTestCase){
            return ((FtsTestCase)testCase).getProductConstraint();
        } else {
            return FExpression.trueValue();
        }
    }

}
