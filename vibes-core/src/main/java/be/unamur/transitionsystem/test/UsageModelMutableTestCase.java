package be.unamur.transitionsystem.test;

/*
 * #%L vibes-core %% Copyright (C) 2014 PReCISE, University of Namur %%
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
import java.util.ArrayList;

import com.google.common.collect.HashMultiset;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import be.unamur.transitionsystem.usagemodel.UsageModelTransition;

/**
 * A mutable implementation for {@link UsageModel} test cases. Enqueue and
 * Dequeue methods will return this.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class UsageModelMutableTestCase extends
        MutableTestCase<Action, State, UsageModelTransition> implements
        UsageModelTestCase {

    /**
     * The {@link TestCaseFactory} implementation for
     * {@link UsageModelMutableTestCase} test cases.
     */
    public static final TestCaseFactory FACTORY = new TestCaseFactory() {
        @Override
        public TestCase buildTestCase() {
            return new UsageModelMutableTestCase();
        }
    };

    private ArrayList<Double> probabilities;

    public UsageModelMutableTestCase() {
        super();
        this.probabilities = new ArrayList<Double>();
    }

    @Override
    public UsageModelMutableTestCase copy() throws TestCaseException {
        UsageModelMutableTestCase tc = new UsageModelMutableTestCase();
        tc.actions = new ArrayList<Action>(actions);
        tc.actionSet = HashMultiset.create(actionSet);
        tc.probabilities = new ArrayList<Double>(probabilities);
        return tc;
    }

    @Override
    public UsageModelMutableTestCase enqueue(Action action) throws TestCaseException {
        this.probabilities.add(1.0);
        super.enqueue(action);
        return this;
    }

    @Override
    public UsageModelMutableTestCase enqueue(Action action, double proba)
            throws TestCaseException {
        this.probabilities.add(proba);
        super.enqueue(action);
        return this;
    }

    @Override
    public UsageModelMutableTestCase enqueue(Transition trans) throws TestCaseException {
        if (trans instanceof UsageModelTransition) {
            this.probabilities.add(((UsageModelTransition) trans).getProbability());
        } else {
            this.probabilities.add(1.0);
        }
        super.enqueue(trans);
        return this;
    }

    @Override
    public MutableTestCase<Action, State, UsageModelTransition> dequeue()
            throws TestCaseException {
        this.probabilities.remove(this.probabilities.size() - 1);
        return super.dequeue();
    }

    @Override
    public double getProbability() {
        double proba = 1.0;
        for (Double val : this.probabilities) {
            proba = proba * val;
        }
        return proba;
    }

    @Override
    public String toString() {
        return "UsageModelMutableAbstractTestCase [probability=" + getProbability()
                + ", toString()=" + super.toString() + "]";
    }

}
