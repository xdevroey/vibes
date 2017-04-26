package be.unamur.transitionsystem.test.execution;

/*
 * #%L
 * vibes-execution
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

import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.TestCase;

public class RelaxedTestCaseRunner implements TestCaseRunner<TransitionSystem> {

    private static final Logger logger = LoggerFactory.getLogger(RelaxedTestCaseRunner.class);

    public RelaxedTestCaseRunner() {
    }

    @Override
    public ExecutionTree<Transition> run(TransitionSystem ts, TestCase testCase) {
        ExecutionTree<Transition> tree = new ExecutionTree();
        RelaxedTestCaseVisitor visitor = new RelaxedTestCaseVisitor(testCase, tree.getRoot());
        try {
            ts.getInitialState().accept(visitor);
        } catch (VisitException e) {
            logger.error("Error while executing test case!", e);
            throw new IllegalStateException("Error while executing test case, this should not happen but it has ...", e);
        }
        return tree;
    }

}
