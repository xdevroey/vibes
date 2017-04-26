package be.unamur.transitionsystem.test.selection.usagemodel;

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
import static org.junit.Assert.*;
import static be.unamur.transitionsystem.dsl.UsageModelBuilder.*;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.test.UsageModelMutableTestCase;
import be.unamur.transitionsystem.test.selection.AccumulatorWrapUp;
import be.unamur.transitionsystem.test.selection.AlwaysTrueValidator;
import be.unamur.transitionsystem.test.selection.TestCaseValidator;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import be.unamur.transitionsystem.usagemodel.UsageModel;

public class BoundedProbabilityGeneratorTest {

    private static final Logger logger = LoggerFactory
            .getLogger(BoundedProbabilityGeneratorTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	@Test
    public void testSimpleModel() throws TestCaseSelectionException {
        UsageModel model = define().init("s0")
                .from("s0").action("act1").proba(1).to("s1")
                .from("s1").action("act2").proba(1).to("s2")
                .from("s2").action("act3").proba(1).to("s3")
                .from("s3").action("act4").proba(1).to("s0").build();
        TestCaseValidator validator = AlwaysTrueValidator.TRUE_VALIDATOR;
        AccumulatorWrapUp wrapUp = new AccumulatorWrapUp();
        // Generate test cases
        BoundedProbabilityGenerator gen = new BoundedProbabilityGenerator(
                UsageModelMutableTestCase.FACTORY, validator, wrapUp);
        gen.generateAbstractTestSet(model);
        @SuppressWarnings("rawtypes")
        List testCases = wrapUp.getTestCases();
        assertEquals("Should not be more than one test case", 1, testCases.size());
        UsageModelMutableTestCase tc = (UsageModelMutableTestCase) testCases
                .get(0);
        assertEquals("Probability should be 1.0!", 1.0, tc.getProbability(), 0.00001);
        assertEquals("Wrong test case!", Lists.newArrayList(new Action("act1"), new Action("act2"),
                new Action("act3"), new Action("act4")),
                Lists.newArrayList(tc.iterator()));
    }

    @Test
    public void testSimpleModel2() throws TestCaseSelectionException {
        UsageModel model = define().init("s0")
                .from("s0").action("act1").proba(1).to("s1")
                .from("s1").action("act2").proba(0.5).to("s2")
                .from("s2").action("act3").proba(0.5).to("s3")
                .from("s3").action("act4").proba(0.5).to("s0").build();
        TestCaseValidator validator = AlwaysTrueValidator.TRUE_VALIDATOR;
        AccumulatorWrapUp wrapUp = new AccumulatorWrapUp();
        // Generate test cases
        BoundedProbabilityGenerator gen = new BoundedProbabilityGenerator(
                UsageModelMutableTestCase.FACTORY, validator, wrapUp);
        gen.generateAbstractTestSet(model);
        @SuppressWarnings("rawtypes")
        List testCases = wrapUp.getTestCases();
        logger.debug("Test Cases are {}", testCases);
        assertEquals("Should not be more than one test case", 1, testCases.size());
        UsageModelMutableTestCase tc = (UsageModelMutableTestCase) testCases
                .get(0);
        assertEquals("Wrong Probability!", 0.125, tc.getProbability(), 0.00001);
        assertEquals("Wrong test case!", Lists.newArrayList(new Action("act1"), new Action("act2"),
                new Action("act3"), new Action("act4")),
                Lists.newArrayList(tc.iterator()));
    }

    @Test
    public void testSimpleModelLowerBond() throws TestCaseSelectionException {
        UsageModel model = define().init("s0")
                .from("s0").action("act1").proba(1).to("s1")
                .from("s1").action("act2").proba(0.5).to("s2")
                .from("s2").action("act3").proba(0.5).to("s3")
                .from("s2").action("err").proba(0.1).to("s3")
                .from("s2").action("err2").proba(0.1).to("s3")
                .from("s3").action("act4").proba(0.5).to("s0").build();
        TestCaseValidator validator = AlwaysTrueValidator.TRUE_VALIDATOR;
        AccumulatorWrapUp wrapUp = new AccumulatorWrapUp();
        // Generate test cases
        BoundedProbabilityGenerator gen = new BoundedProbabilityGenerator(
                UsageModelMutableTestCase.FACTORY, validator, wrapUp);
        gen.setLowest(0.1);
        gen.generateAbstractTestSet(model);
        @SuppressWarnings("rawtypes")
        List testCases = wrapUp.getTestCases();
        logger.debug("Test Cases are {}", testCases);
        assertEquals("Should not be more than one test case", 1, testCases.size());
        UsageModelMutableTestCase tc = (UsageModelMutableTestCase) testCases
                .get(0);
        assertEquals("Wrong Probability!", 0.125, tc.getProbability(), 0.00001);
        assertEquals("Wrong test case!", Lists.newArrayList(new Action("act1"), new Action("act2"),
                new Action("act3"), new Action("act4")),
                Lists.newArrayList(tc.iterator()));
    }
}
