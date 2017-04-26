package be.unamur.transitionsystem.dsl.test;

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
import java.util.Iterator;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.dsl.exception.TestCaseDefinitionException;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.exception.TestCaseException;

public abstract class TestSuiteDefinition {

    private boolean defined = false;
    protected TestSet testSet = new TestSet();

    protected TestCaseDefinition id(String identifier) {
        return new TestCaseDefinition(identifier, this);
    }

    void notifyTestCaseDefinitionComplete(TestCaseDefinition def) {
        TestCase testCase = new LtsMutableTestCase();
        testCase.setId(def.getId());
        for (Iterator<Action> it = def.actions().iterator(); it.hasNext();) {
            Action act = (Action) it.next();
            try {
                testCase.enqueue(act);
            } catch (TestCaseException e) {
                throw new TestCaseDefinitionException("Unable to add action to test case", e);
            }
        }
        testSet.add(testCase);
    }

    /**
     * The method that has to be implemented by sub-classes in order to define
     * the test-case
     */
    protected abstract void define();

    public TestSet getTestSet() {
        if (!defined) {
            defined = true;
            define();
        }
        return testSet;
    }

}
