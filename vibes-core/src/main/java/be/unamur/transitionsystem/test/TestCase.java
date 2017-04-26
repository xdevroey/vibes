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
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.test.exception.TestCaseException;

/**
 * This interface defines methods to manipulate a test case. A test case defined
 * over a TS is a sequence of actions with an Id. Implementations may be mutable
 * or immutable.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public interface TestCase extends Iterable<Action> {

    /**
     * Returns true if this contains the given action.
     * @param act
     * @return 
     */
    public boolean contains(Action act);

    /**
     * Return the id of the test case. A test case ID should be unique in a
     * {@link TestSet}.
     * @return 
     */
    public String getId();

    /**
     * Sets the test case id.
     * @param id
     */
    public void setId(String id);

    /**
     * Returns the size of test case, i.e., the number of actions to execute.
     * @return 
     */
    public int size();

    /**
     * Add the action of the given transition to the end of the test case and
     * returns the test case formed.
     *
     * @param trans
     * @return The current test case or a new (fresh) test case depending on the
     * implementation.
     * @throws be.unamur.transitionsystem.test.exception.TestCaseException
     */
    public TestCase enqueue(Transition trans) throws TestCaseException;

    /**
     * Adds the given action to the end of the test case and returns the test
     * case formed.
     *
     * @param action
     * @return The current test case or a new (fresh) test case depending on the
     * implementation.
     * @throws be.unamur.transitionsystem.test.exception.TestCaseException
     */
    public TestCase enqueue(Action action) throws TestCaseException;

    /**
     * Removes the last action of the test case (if there is one) and return the
     * test case formed.
     *
     * @return The current test case or a new (fresh) test case depending on the
     * implementation.
     * @throws TestCaseException
     */
    public TestCase dequeue() throws TestCaseException;

    /**
     * Returns a fresh copy of this.
     *
     * @return A fresh copy of this.
     * @throws TestCaseException
     */
    public TestCase copy() throws TestCaseException;

}
