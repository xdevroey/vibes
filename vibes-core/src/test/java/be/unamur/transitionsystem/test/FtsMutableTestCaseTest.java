package be.unamur.transitionsystem.test;

/*
 * #%L
 * vibes-core
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
import static be.unamur.fts.fexpression.FExpression.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static be.unamur.transitionsystem.dsl.FeaturedTransitionSystemBuilder.define;

import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;

public class FtsMutableTestCaseTest {

    private static final Logger logger = LoggerFactory
            .getLogger(FtsMutableTestCaseTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	@Test
    public void testOneTransition() throws Exception {
        FeaturedTransitionSystem fts = define().init("s0").from("s0").action("act1")
                .fexpression("expr1").to("s1").build();
        logger.info("Using FTS {}", fts);
        FtsMutableTestCase testCase = (FtsMutableTestCase) FtsMutableTestCase.FACTORY
                .buildTestCase();
        testCase.enqueue(fts.getInitialState().outgoingTransitions().next());
        logger.info("Test case = {}", testCase);
        logger.info("Test case's product constraint = {}",
                testCase.getProductConstraint());
        assertThat(testCase.getProductConstraint(), equalTo(featureExpr("expr1")));
    }

    @Test
    public void testMultipleTransitions() throws Exception {
        FeaturedTransitionSystem fts = define().init("s0")
                .from("s0").action("act1").fexpression("expr1").to("s1")
                .from("s0").action("act2").fexpression("expr2").to("s2")
                .from("s0").action("act3").fexpression("expr3").to("s3")
                .build();
        logger.info("Using FTS {}", fts);
        FtsMutableTestCase testCase = (FtsMutableTestCase) FtsMutableTestCase.FACTORY
                .buildTestCase();
        Iterator<Transition> it = fts.getInitialState().outgoingTransitions();
        while (it.hasNext()) {
            testCase.enqueue(it.next());
        }
        logger.info("Test case = {}", testCase);
        logger.info("Test case's product constraint = {}",
                testCase.getProductConstraint());
        assertThat(testCase.getProductConstraint().applySimplification(),
                equalTo(featureExpr("expr1").and(featureExpr("expr2")).and(featureExpr("expr3")).applySimplification()));
    }

    @Test
    public void testMultipleTransitionsDoubles() throws Exception {
        FeaturedTransitionSystem fts = define().init("s0")
                .from("s0").action("act1").fexpression("expr1").to("s1")
                .from("s0").action("act2").fexpression("expr2").to("s2")
                .from("s0").action("act3").fexpression("expr2").to("s3")
                .from("s0").action("act3").fexpression("expr3").to("s4")
                .build();
        logger.info("Using FTS {}", fts);
        FtsMutableTestCase testCase = (FtsMutableTestCase) FtsMutableTestCase.FACTORY
                .buildTestCase();
        Iterator<Transition> it = fts.getInitialState().outgoingTransitions();
        while (it.hasNext()) {
            testCase.enqueue(it.next());
        }
        logger.info("Test case = {}", testCase);
        logger.info("Test case's product constraint = {}",
                testCase.getProductConstraint());
        assertThat(testCase.getProductConstraint().applySimplification(),
                equalTo(featureExpr("expr1").and(featureExpr("expr2")).and(featureExpr("expr3")).applySimplification()));
    }

    @Test
    public void testMultipleTransitionsDoubles2() throws Exception {
        FeaturedTransitionSystem fts = define().init("s0")
                .from("s0").action("act1").fexpression("expr1").to("s1")
                .from("s0").action("act2").fexpression("expr2").to("s2")
                .from("s0").action("act3").fexpression("expr2").to("s3")
                .from("s0").action("act3").fexpression("expr3").to("s4")
                .build();
        logger.info("Using FTS {}", fts);
        FtsMutableTestCase testCase = (FtsMutableTestCase) FtsMutableTestCase.FACTORY
                .buildTestCase();
        Iterator<Transition> it = fts.getInitialState().outgoingTransitions();
        while (it.hasNext()) {
            testCase.enqueue(it.next());
        }
        logger.info("Test case = {}", testCase);
        logger.info("Test case's product constraint = {}",
                testCase.getProductConstraint());
        assertThat(testCase.getProductConstraint().applySimplification(),
                equalTo(featureExpr("expr1").and(featureExpr("expr2")).and(featureExpr("expr3")).applySimplification()));
        while (it.hasNext()) {
            testCase.enqueue(it.next());
        }
        logger.info("Test case = {}", testCase);
        logger.info("Test case's product constraint = {}",
                testCase.getProductConstraint());
        assertThat(testCase.getProductConstraint().applySimplification(),
                equalTo(featureExpr("expr1").and(featureExpr("expr2")).and(featureExpr("expr3")).applySimplification()));
    }

}
