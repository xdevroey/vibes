package be.unamur.transitionsystem.transformation;

/*
 * #%L
 * vibes-transformation
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
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import be.unamur.fts.solver.SolverFacade;
import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.test.execution.StrictFtsTestCaseRunner;
import static com.google.common.base.Preconditions.*;

public class FtsStrictPruning extends AbstractTransitionSystemPruning<FeaturedTransition> {

    private SolverFacade solver = null;

    public FtsStrictPruning(SolverFacade solver) {
        this.solver = solver;
    }

    /**
     * If the object is built without solver, the prune method with test cases
     * and test set may not be called.
     */
    public FtsStrictPruning() {

    }

    @Override
    public FeaturedTransitionSystem prune(TransitionSystem source, TestSet testCases) {
        checkArgument(source instanceof FeaturedTransitionSystem);
        checkNotNull(solver);
        StrictFtsTestCaseRunner runner;
        Set<FeaturedTransition> toKeep = Sets.newHashSet();
        ExecutionTree<FeaturedTransition> tree;
        for (TestCase tc : testCases) {
            runner = new StrictFtsTestCaseRunner(solver);
            tree = runner.run((FeaturedTransitionSystem) source, tc);
            toKeep.addAll(transitionsToKeep(tree));
        }
        return (FeaturedTransitionSystem) prune(source, toKeep);
    }

    @Override
    public FeaturedTransitionSystem prune(TransitionSystem source, TestCase testCase) {
        checkArgument(source instanceof FeaturedTransitionSystem);
        checkNotNull(solver);
        StrictFtsTestCaseRunner runner = new StrictFtsTestCaseRunner(solver);
        ExecutionTree<FeaturedTransition> tree = runner.run((FeaturedTransitionSystem) source, testCase);
        return (FeaturedTransitionSystem) prune(source, transitionsToKeep(tree));
    }

    @Override
    public FeaturedTransitionSystem prune(TransitionSystem source,
            ExecutionTree<FeaturedTransition> tree) {
        checkArgument(source instanceof FeaturedTransitionSystem);
        return (FeaturedTransitionSystem) prune(source, transitionsToKeep(tree));
    }

    @Override
    public FeaturedTransitionSystem prune(TransitionSystem source, Set<FeaturedTransition> toKeep) {
        checkArgument(source instanceof FeaturedTransitionSystem);
        return (FeaturedTransitionSystem) super.prune(source, toKeep);
    }

    @Override
    public FeaturedTransitionSystem prune(TransitionSystem source,
            List<ExecutionTree<FeaturedTransition>> trees) {
        checkArgument(source instanceof FeaturedTransitionSystem);
        return (FeaturedTransitionSystem) super.prune(source, trees);
    }

}
