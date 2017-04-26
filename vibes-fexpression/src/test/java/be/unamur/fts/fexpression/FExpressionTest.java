package be.unamur.fts.fexpression;

/*
 * #%L
 * VIBeS: featured expressions
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

import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.fts.fexpression.configuration.SimpleConfiguration;
import static be.unamur.fts.fexpression.Feature.*;

public class FExpressionTest {

    private static final Logger logger = LoggerFactory.getLogger(FExpressionTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    };

    @Test
    public void testEvaluationConstantTrue() throws Exception {
        Configuration config = new SimpleConfiguration(feature("f1"), feature("f2"), feature("f3"));
        FExpression expr = ParserUtil.getInstance().parse("true");
        expr = expr.assignTrue(config);
        assertTrue(expr.isTrue());
    }

    @Test
    public void testEvaluationConstantFalse() throws Exception {
        Configuration config = new SimpleConfiguration(feature("f1"), feature("f2"), feature("f3"));
        FExpression expr = ParserUtil.getInstance().parse("false");
        expr = expr.assignTrue(config);
        assertTrue(expr.isFalse());
    }

    @Test
    public void testEvaluationFeatureSelected() throws Exception {
        Configuration config = new SimpleConfiguration(feature("f1"), feature("f2"), feature("f3"));
        FExpression expr = ParserUtil.getInstance().parse("f1");
        expr = expr.assignTrue(config);
        assertTrue(expr.isTrue());
    }

    @Test
    public void testEvaluationFeatureDeselected() throws Exception {
        Configuration config = new SimpleConfiguration(feature("f1"), feature("f2"), feature("f3"));
        FExpression expr = ParserUtil.getInstance().parse("f4");
        expr = expr.assignTrue(config);
        assertFalse(expr.isTrue());
        assertFalse(expr.isFalse());
    }

    @Test
    public void testEvaluationAnd() throws Exception {
        Configuration config = new SimpleConfiguration(feature("f1"), feature("f2"), feature("f3"));
        FExpression expr = ParserUtil.getInstance().parse("f1 && f2 && f3 && true");
        expr = expr.assignTrue(config).applySimplification();
        assertTrue(expr.isTrue());
    }

    @Test
    public void testEvaluationAndFalse() throws Exception {
        Configuration config = new SimpleConfiguration(feature("f1"), feature("f2"), feature("f3"));
        FExpression expr = ParserUtil.getInstance().parse(" f1 && f4");
        expr = expr.assignTrue(config).applySimplification();
        assertThat(expr.getFeatures(), hasSize(1));
        assertThat(expr.getFeatures(), contains(feature("f4")));
        assertFalse(expr.isTrue());
        assertFalse(expr.isFalse());
    }

    @Test
    public void testEvaluationOr() throws Exception {
        Configuration config = new SimpleConfiguration(feature("f1"), feature("f2"), feature("f3"));
        FExpression expr = ParserUtil.getInstance().parse("f1 || f2 || f4");
        expr = expr.assignTrue(config).applySimplification();
        assertTrue(expr.isTrue());
    }

    @Test
    public void testEvaluationOrFalse() throws Exception {
        Configuration config = new SimpleConfiguration(feature("f1"), feature("f2"), feature("f3"));
        FExpression expr = ParserUtil.getInstance().parse("false || f4 || f5");
        expr = expr.assignTrue(config).applySimplification();
        assertThat(expr.getFeatures(), hasSize(2));
        assertThat(expr.getFeatures(), containsInAnyOrder(feature("f4"), feature("f5")));
        assertFalse(expr.isTrue());
        assertFalse(expr.isFalse());
    }

}
