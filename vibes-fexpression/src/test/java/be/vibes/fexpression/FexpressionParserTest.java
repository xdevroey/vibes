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

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static be.vibes.fexpression.Feature.*;
import static be.vibes.fexpression.FExpression.*;

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
        assertThat(expression.getFeatures(), containsInAnyOrder(Feature.feature(input)));
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
        assertThat(expression.getFeatures(), containsInAnyOrder(feature(fName)));
        assertThat(expression, equalTo(featureExpr(fName).not()));
    }

    @Test
    public void tesParsingAnd() throws Exception {
        String f1 = "f1";
        String f2 = "f2";
        String input = f1 + "&&" + f2;
        FExpression expression = ParserUtil.getInstance().parse(input);
        assertThat(expression.getFeatures(), hasSize(2));
        assertThat(expression.getFeatures(), containsInAnyOrder(feature(f1), feature(f2)));
        assertThat(expression, equalTo(featureExpr(f1).and(featureExpr(f2))));
    }

    @Test
    public void tesParsingOr() throws Exception {
        String f1 = "f1";
        String f2 = "f2";
        String input = f1 + "||" + f2;
        FExpression expression = ParserUtil.getInstance().parse(input);
        assertThat(expression.getFeatures(), hasSize(2));
        assertThat(expression.getFeatures(), containsInAnyOrder(feature(f1), feature(f2)));
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
        assertThat(expression.getFeatures(), containsInAnyOrder(feature(f1), feature(f2), feature(f3)));
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
        assertThat(expression.getFeatures(), containsInAnyOrder(feature(f1), feature(f2), feature(f3)));
        assertThat(expression, equalTo(featureExpr(f1).or(featureExpr(f2)).and(featureExpr(f3))));
    }

}
