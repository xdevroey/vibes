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

import be.vibes.fexpression.FExpression;
import be.vibes.fexpression.ConstraintTransformation;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import com.google.common.collect.Lists;

/**
 * A mutable implementation for test cases used in FTS.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class FtsMutableTestCase extends
        MutableTestCase<Action, State, FeaturedTransition> implements FtsTestCase {

    public static final TestCaseFactory FACTORY = new TestCaseFactory() {
        @Override
        public TestCase buildTestCase() {
            return new FtsMutableTestCase();
        }
    };

    protected FExpression constraint = null;
    protected ArrayList<FExpression> constraints = Lists.newArrayList();

    public FtsMutableTestCase() {
        super();
    }

    @Override
    public FtsMutableTestCase enqueue(Transition trans) throws TestCaseException {
        if(trans instanceof FeaturedTransition){
            return enqueue(trans.getAction(),
                ((FeaturedTransition) trans).getFeatureExpression());
        } else {
            return enqueue(trans.getAction());
        } 
    }

    @Override
    public FtsMutableTestCase enqueue(Action action) throws TestCaseException {
        return enqueue(action, FExpression.trueValue());
    }

    @Override
    public FtsMutableTestCase enqueue(Action action, FExpression constraint)
            throws TestCaseException {
        if (constraint != null && constraint != FExpression.trueValue()) {
            this.constraint = null;
        }
        constraints.add(constraint);
        return (FtsMutableTestCase) super.enqueue(action);
    }

    @Override
    public FtsMutableTestCase dequeue() throws TestCaseException {
        FExpression last = constraints.remove(constraints.size() - 1);
        if (last != null && last != FExpression.trueValue()) {
            this.constraint = null;
        }
        return (FtsMutableTestCase) super.dequeue();
    }

    @Override
    public FExpression getProductConstraint() {
        return constraint == null ? constraint = ConstraintTransformation
                .getConjunction(constraints) : constraint;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj)
                && getProductConstraint().equals(
                        ((FtsMutableTestCase) obj).getProductConstraint());
    }

    @Override
    public FtsMutableTestCase copy() throws TestCaseException {
        FtsMutableTestCase tc = new FtsMutableTestCase();
        tc.actions = Lists.newArrayList(actions);
        tc.actionSet = HashMultiset.create(actionSet);
        tc.constraint = constraint;
        tc.constraints = Lists.newArrayList(constraints);
        return tc;
    }

}
