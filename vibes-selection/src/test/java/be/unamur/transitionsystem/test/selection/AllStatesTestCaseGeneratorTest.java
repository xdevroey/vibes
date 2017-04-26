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
import static be.unamur.transitionsystem.dsl.FeaturedTransitionSystemBuilder.*;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.FtsMutableTestCase;
import be.unamur.transitionsystem.test.TestCase;

import com.google.common.collect.Lists;

public class AllStatesTestCaseGeneratorTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(AllStatesTestCaseGeneratorTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOG.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	@Test
    public void testLinearFts() throws Exception {
        FeaturedTransitionSystem fts = define().init("s0").from("s0").action("act1")
                .to("s1").from("s1").action("act2").to("s2").from("s2").action("act3")
                .to("s0").build();
        final ArrayList<FtsMutableTestCase> testCases = new ArrayList<>();
        WarshallScoreComputor comp = new WarshallScoreComputor();
        comp.initilise(fts);
        AllStatesTestCaseGenerator generator = new AllStatesTestCaseGenerator(
                FtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, (TestCase testCase) -> {
                    LOG.info("Test case generated: {}", testCase);
                    testCases.add((FtsMutableTestCase) testCase);
        }, comp);
        LOG.info("Initialise Warshall score computor");
        comp.warshall();
        LOG.info("Running Warshall algorithm, level {}", comp.getLevel());
        comp.warshall();
        LOG.info("Running Warshall algorithm, level {}", comp.getLevel());
        comp.warshall();
        LOG.info("Running Warshall algorithm, level {}", comp.getLevel());
        LOG.info("Starts test case genertion");
        generator.generateAbstractTestSet(fts);
        LOG.info("Generation done");
        assertEquals(1, testCases.size());
        assertEquals("Test case does not contain right action sequence!",
                Lists.newArrayList(new Action("act1"), new Action("act2"), new Action(
                                "act3")), Lists.newArrayList(testCases.get(0).iterator()));
    }

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
        final ArrayList<FtsMutableTestCase> testCases = new ArrayList<>();
        WarshallScoreComputor comp = new WarshallScoreComputor();
        comp.initilise(fts);
        AllStatesTestCaseGenerator generator = new AllStatesTestCaseGenerator(
                FtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, (TestCase testCase) -> {
                    LOG.info("Test case generated: {}", testCase);
                    testCases.add((FtsMutableTestCase) testCase);
        }, comp);
        LOG.info("Initialise Warshall score computor");
        comp.warshall();
        LOG.info("Running Warshall algorithm, level {}", comp.getLevel());
        comp.warshall();
        LOG.info("Running Warshall algorithm, level {}", comp.getLevel());
        comp.warshall();
        LOG.info("Running Warshall algorithm, level {}", comp.getLevel());
        LOG.info("Starts test case genertion");
        generator.generateAbstractTestSet(fts);
        LOG.info("Generation done");
        assertEquals(3, testCases.size());
    }

}
