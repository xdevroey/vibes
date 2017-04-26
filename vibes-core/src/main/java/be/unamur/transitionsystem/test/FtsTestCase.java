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
import be.unamur.fts.fexpression.FExpression;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.test.exception.TestCaseException;

/**
 * An interface defining methods used to manipulate test cases in FTSs.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public interface FtsTestCase extends TestCase {

    /**
     * Enqueue the given action and constraint to the test case and returns it.
     *
     * @param action
     * @param constraint
     * @return The current test case or a new (fresh) test case depending on the
     * implementation.
     * @throws TestCaseException
     */
    public FtsMutableTestCase enqueue(Action action, FExpression constraint)
            throws TestCaseException;

    /**
     * Returns the constraint associated to this test case.
     *
     * @return The conjunction of feature expressions on every transitions fired
     * by the test case (considered transitions are those provided to the
     * enqueue() methods).
     */
    public FExpression getProductConstraint();

}
