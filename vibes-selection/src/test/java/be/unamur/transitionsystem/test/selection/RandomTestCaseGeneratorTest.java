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
import static org.junit.Assert.*;
import static be.unamur.transitionsystem.dsl.FeaturedTransitionSystemBuilder.define;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.selection.AlwaysTrueValidator;
import be.unamur.transitionsystem.test.selection.RandomTestCaseGenerator;

public class RandomTestCaseGeneratorTest {

    private static final Logger logger = LoggerFactory
            .getLogger(RandomTestCaseGeneratorTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	
	@Test
    public void testMultiplePathsFts() throws Exception {
        FeaturedTransitionSystem fts = define().init("s0")
                .from("s0").action("act01").to("s1")
                .from("s1").action("act12").to("s2")
                .from("s1").action("act13").to("s3")
                .from("s1").action("act14").to("s4")
                .from("s2").action("act25").to("s5")
                .from("s3").action("act35").to("s5")
                .from("s4").action("act45").to("s5")
                .from("s5").action("act50").to("s0")
                .build();
        final TestSet set = new TestSet();
        RandomTestCaseGenerator generator = new RandomTestCaseGenerator(LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, new TestCaseWrapUp() {
            @Override
            public void wrapUp(TestCase testCase) {
                set.add(testCase);
            }
        });
        logger.info("Starts test case genertion");
        generator.generateAbstractTestSet(fts, 10);
        logger.info("Generation done");
        assertEquals(10, set.size());
    }

}
