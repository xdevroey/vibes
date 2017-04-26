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
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import be.unamur.transitionsystem.test.ImmutableTestCase;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.exception.TestCaseException;

public class AccumulatorWrapUp implements TestCaseWrapUp, Iterable<TestCase> {

    private List<TestCase> testCases;

    public AccumulatorWrapUp() {
        this.testCases = Lists.newArrayList();
    }

    @Override
    public void wrapUp(TestCase testCase) throws TestCaseException {
        if (testCase instanceof ImmutableTestCase) {
            testCases.add(testCase);
        } else {
            testCases.add(testCase.copy());
        }
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    @Override
    public Iterator<TestCase> iterator() {
        return testCases.iterator();
    }

    public void clear() {
        testCases.clear();
    }

}
