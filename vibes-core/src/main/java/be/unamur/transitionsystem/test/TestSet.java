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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * This class represents a set of test cases. Inv: Each test case should have a
 * unique ID.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class TestSet implements Iterable<TestCase> {

    protected List<TestCase> testCases;

    /**
     * Creates a new set of test cases.
     */
    public TestSet() {
        this.testCases = Lists.newArrayList();
    }

    /**
     * Creates a new set of test cases from the given collection of test cases.
     * @param testCases
     */
    public TestSet(Collection<? extends TestCase> testCases) {
        this.testCases = Lists.newArrayList(testCases);
    }

    /**
     * Creates a new set of test cases from the given iterator of test cases.
     * @param testCases
     */
    public TestSet(Iterator<? extends TestCase> testCases) {
        this.testCases = Lists.newArrayList(testCases);
    }

    /**
     * Add the given test case to the set.
     * @param tc
     */
    public void add(TestCase tc) {
        this.testCases.add(tc);
    }

    /**
     * Removes the given test case from the set.
     *
     * @param tc
     * @return True if the test case has been removed (if it belongs to the set)
     * and false otherwise.
     */
    public boolean remove(TestCase tc) {
        return this.testCases.remove(tc);
    }

    /**
     * Returns true if the set contains the given test case.
     * @param tc
     * @return 
     */
    public boolean contains(TestCase tc) {
        return this.testCases.contains(tc);
    }

    /**
     * Returns the number of test cases in the set.s
     * @return 
     */
    public int size() {
        return testCases.size();
    }

    /**
     * Returns the test case at position 'index'.
     * @param index
     * @return 
     */
    public TestCase get(int index) {
        return this.testCases.get(index);
    }

    @Override
    public Iterator<TestCase> iterator() {
        return this.testCases.iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        Iterator<TestCase> it = this.iterator();
        TestCase tc;
        while (it.hasNext()) {
            tc = it.next();
            builder.append('(');
            builder.append(tc.toString());
            builder.append(')');
            if (it.hasNext()) {
                builder.append(',');
            }
        }
        builder.append(']');
        return builder.toString();
    }

}
