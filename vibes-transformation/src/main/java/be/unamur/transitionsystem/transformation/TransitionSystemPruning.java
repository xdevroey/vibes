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

import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestSet;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public interface TransitionSystemPruning<T extends Transition> {

    /**
     * Returns a copy of the source TS where all paths that are not in the test
     * cases execution of the test set have been removed. The strict or
     * non-deterministic definition of execution is left to the implementation.
     *
     * @param source
     * @param testCases
     * @return
     */
    public TransitionSystem prune(TransitionSystem source,
            TestSet testCases);

    /**
     * Returns a copy of the source TS where all paths that are not in the test
     * case execution have been removed. The strict or non-deterministic
     * definition of execution is left to the implementation.
     *
     * @param source
     * @param testCase
     * @return
     */
    public TransitionSystem prune(TransitionSystem source,
            TestCase testCase);

    /**
     * Returns a copy of the source TS where only the paths of the execution
     * tree have been kept.
     *
     * @param source
     * @param tree
     * @return
     */
    public TransitionSystem prune(TransitionSystem source, ExecutionTree<T> tree);

    public TransitionSystem prune(TransitionSystem source, List<ExecutionTree<T>> trees);

    public TransitionSystem prune(TransitionSystem source, Set<T> toKeep);

}
