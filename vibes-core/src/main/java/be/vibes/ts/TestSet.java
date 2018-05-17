package be.vibes.ts;

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
     * @param tc The test case to add.
     */
    public void add(TestCase tc) {
        this.testCases.add(tc);
    }

    /**
     * Removes the given test case from the set.
     *
     * @param tc The test case to remove.
     * @return True if the test case has been removed (if it belongs to the set)
     * and false otherwise.
     */
    public boolean remove(TestCase tc) {
        return this.testCases.remove(tc);
    }

    /**
     * Returns true if the set contains the given test case.
     * @param tc The test case to check.
     * @return True if the set contains the given test case.
     */
    public boolean contains(TestCase tc) {
        return this.testCases.contains(tc);
    }

    /**
     * Returns the number of test cases in the set.s
     * @return The number of test cases in the set.s
     */
    public int size() {
        return testCases.size();
    }

    /**
     * Returns the test case at position 'index'.
     * @param index The position of the test case to retrieve.
     * @return The test case at position 'index'.
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