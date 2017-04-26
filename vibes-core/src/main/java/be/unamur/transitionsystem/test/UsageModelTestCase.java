package be.unamur.transitionsystem.test;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.usagemodel.UsageModel;

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
/**
 * An interface for {@link UsageModel} test cases.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public interface UsageModelTestCase extends TestCase {

    /**
     * Returns the probability of execution of the test-case.
     *
     * @return The probability of execution of the test-case.
     */
    public double getProbability();

    /**
     * Enqueue the given action with the given action to the test case.
     *
     * @param action The action to enqueue to the test case.
     * @param proba The probability to 'action' to be executed.
     * @return The current test case or a new (fresh) test case depending on the
     * implementation.
     * @throws TestCaseException
     */
    public UsageModelMutableTestCase enqueue(Action action, double proba)
            throws TestCaseException;

}
