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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestSet;

public class ValidatorsConjunction implements TestCaseValidator {

    private List<TestCaseValidator> validators;

    public ValidatorsConjunction(TestCaseValidator... validators) {
        this.validators = Arrays.asList(validators);
    }

    public ValidatorsConjunction() {
        this.validators = Lists.newLinkedList();
    }

    public void and(TestCaseValidator val) {
        this.validators.add(val);
    }

    @Override
    public boolean isValid(TestCase testCase) {
        boolean valid = true;
        Iterator<TestCaseValidator> it = validators.iterator();
        while (valid && it.hasNext()) {
            valid = it.next().isValid(testCase);
        }
        return valid;
    }

    @Override
    public boolean isValid(TestCase testCase, TestSet set) {
        boolean valid = true;
        Iterator<TestCaseValidator> it = validators.iterator();
        while (valid && it.hasNext()) {
            valid = it.next().isValid(testCase, set);
        }
        return valid;
    }

}
