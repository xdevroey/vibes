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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;

/**
 * FIXME Incomplete and does not work !
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class AllTransitionsTestCaseGenerator extends AbstractTestCaseGenerator {

    private static final Logger LOG = LoggerFactory
            .getLogger(AllTransitionsTestCaseGenerator.class);

    public AllTransitionsTestCaseGenerator(TestCaseFactory testCaseFactory,
            TestCaseValidator validator, TestCaseWrapUp wrapUp) {
        super(testCaseFactory, validator, wrapUp);
    }

    @Override
    public void generateAbstractTestSet(TransitionSystem ts,
            TestCaseValidator validator, TestCaseWrapUp wrapUp)
            throws TestCaseSelectionException {
        throw new UnsupportedOperationException("Not yet implemented...");
    }
}
