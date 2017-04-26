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
import static be.unamur.fts.fexpression.Feature.*;
import static be.unamur.fts.fexpression.FExpression.*;

public class FexpressionParserTest {

    private static final Logger logger = LoggerFactory
            .getLogger(FexpressionParserTest.class);

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
    public void tesParsingFeature() throws Exception {
        String input = "feature1";
        FExpression expression = ParserUtil.getInstance().parse(input);
        assertThat(expression.getFeatures(), hasSize(1));
        assertThat(expression.getFeatures(), contains(Feature.feature(input)));
    }

    @Test
    public void tesParsingTrue() throws Exception {
        String input = "true";
        FExpression expression = ParserUtil.getInstance().parse(input);
        assertThat(expression.getFeatures(), hasSize(0));
        assertThat(expression, equalTo(trueValue()));
        assertThat(expression.isTrue(), equalTo(true));
    }

    @Test
    public void tesParsingFalse() throws Exception {
        String input = "false";
        FExpression expression = ParserUtil.getInstance().parse(input);
        assertThat(expression.getFeatures(), hasSize(0));
        assertThat(expression, equalTo(falseValue()));
        assertThat(expression.isTrue(), equalTo(false));
    }

    @Test
    public void tesParsingNot() throws Exception {
        String fName = "feature1";
        String input = "!" + fName;
        FExpression expression = ParserUtil.getInstance().parse(input);
        assertThat(expression.getFeatures(), hasSize(1));
        assertThat(expression.getFeatures(), contains(feature(fName)));
        assertThat(expression, equalTo(featureExpr(fName).not()));
    }

    @Test
    public void tesParsingAnd() throws Exception {
        String f1 = "f1";
        String f2 = "f2";
        String input = f1 + "&&" + f2;
        FExpression expression = ParserUtil.getInstance().parse(input);
        assertThat(expression.getFeatures(), hasSize(2));
        assertThat(expression.getFeatures(), contains(feature(f1), feature(f2)));
        assertThat(expression, equalTo(featureExpr(f1).and(featureExpr(f2))));
    }

    @Test
    public void tesParsingOr() throws Exception {
        String f1 = "f1";
        String f2 = "f2";
        String input = f1 + "||" + f2;
        FExpression expression = ParserUtil.getInstance().parse(input);
        assertThat(expression.getFeatures(), hasSize(2));
        assertThat(expression.getFeatures(), contains(feature(f1), feature(f2)));
        assertThat(expression, equalTo(featureExpr(f1).or(featureExpr(f2))));
    }

    @Test
    public void tesParsingParenthesis() throws Exception {
        String f1 = "f1";
        String f2 = "f2";
        String f3 = "f3";
        String input = "(" + f1 + " || " + f2 + ") && " + f3;
        FExpression expression = ParserUtil.getInstance().parse(input);
        assertThat(expression.getFeatures(), hasSize(3));
        assertThat(expression.getFeatures(), contains(feature(f1), feature(f2), feature(f3)));
        assertThat(expression, equalTo(featureExpr(f1).or(featureExpr(f2)).and(featureExpr(f3))));
    }

    @Test
    public void tesParsingPriority() throws Exception {
        String f1 = "f1";
        String f2 = "f2";
        String f3 = "f3";
        String input = f1 + " || " + f2 + " && " + f3;
        FExpression expression = ParserUtil.getInstance().parse(input);
        assertThat(expression.getFeatures(), hasSize(3));
        assertThat(expression.getFeatures(), contains(feature(f1), feature(f2), feature(f3)));
        assertThat(expression, equalTo(featureExpr(f1).or(featureExpr(f2)).and(featureExpr(f3))));
    }

}
