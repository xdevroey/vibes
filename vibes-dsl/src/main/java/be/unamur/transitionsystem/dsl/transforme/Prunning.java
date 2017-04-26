package be.unamur.transitionsystem.dsl.transforme;

/*
 * #%L
 * vibes-dsl
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

import be.unamur.fts.solver.SolverFacade;
import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.transformation.FtsStrictPruning;

public class Prunning {

    public static LabelledTransitionSystem prune(LabelledTransitionSystem ts, TestSet testSet) {
        // TODO Implementation
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public static LabelledTransitionSystem prune(LabelledTransitionSystem ts, TestCase testCase) {
        // TODO Implementation
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public static LabelledTransitionSystem prune(LabelledTransitionSystem ts, ExecutionTree<Transition> tree) {
        // TODO Implementation
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public static LabelledTransitionSystem prune(LabelledTransitionSystem ts, List<ExecutionTree<Transition>> tree) {
        // TODO Implementation
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public static FeaturedTransitionSystem prune(FeaturedTransitionSystem fts, SolverFacade solver, TestSet testSet) {
        FtsStrictPruning pruning = new FtsStrictPruning(solver);
        return pruning.prune(fts, testSet);
    }

    public static FeaturedTransitionSystem prune(FeaturedTransitionSystem fts, SolverFacade solver, TestCase testCase) {
        FtsStrictPruning pruning = new FtsStrictPruning(solver);
        return pruning.prune(fts, testCase);
    }

    public static FeaturedTransitionSystem prune(FeaturedTransitionSystem fts, ExecutionTree<FeaturedTransition> tree) {
        FtsStrictPruning pruning = new FtsStrictPruning();
        return pruning.prune(fts, tree);
    }

    public static FeaturedTransitionSystem prune(FeaturedTransitionSystem fts, List<ExecutionTree<FeaturedTransition>> trees) {
        FtsStrictPruning pruning = new FtsStrictPruning();
        return pruning.prune(fts, trees);
    }

}
