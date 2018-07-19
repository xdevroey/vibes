package be.vibes.fexpression;

/*
 * #%L
 * VIBeS: featured expressions
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

import be.vibes.fexpression.configuration.Configuration;
import be.vibes.fexpression.configuration.SimpleConfiguration;
import static be.vibes.fexpression.Feature.*;

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
