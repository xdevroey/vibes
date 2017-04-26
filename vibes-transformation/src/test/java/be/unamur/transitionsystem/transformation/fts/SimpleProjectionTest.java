package be.unamur.transitionsystem.transformation.fts;

/*
 * #%L
 * vibes-transformation
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
import static be.unamur.transitionsystem.dsl.FeaturedTransitionSystemBuilder.define;
import static org.junit.Assert.*;
import static be.unamur.fts.fexpression.Feature.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.fts.fexpression.configuration.SimpleConfiguration;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;

@SuppressWarnings("deprecation")
public class SimpleProjectionTest {

    private static final Logger logger = LoggerFactory
            .getLogger(SimpleProjectionTest.class);

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
    public void testSimpleInaccessibleState() throws Exception {
        Configuration config = new SimpleConfiguration(feature("f1"), feature("f2"),
                feature("f3"));
        Projection projection = SimpleProjection.getInstance();
        FeaturedTransitionSystem fts = define().init("s0")
                .from("s0").action("act1").fexpression("f1").to("s1")
                .from("s1").action("act2").to("s2")
                .from("s1").action("actBis").fexpression("f4").to("sExt")
                .from("sExt").action("actTer").to("s2")
                .from("s2").action("act3").to("s0")
                .build();
        TransitionSystem lts = projection.project(fts, config);
        assertNotNull(lts);
        assertNotNull("Accessible states should still be there!", lts.getState("s0"));
        assertNotNull("Accessible states should still be there!", lts.getState("s1"));
        assertNotNull("Accessible states should still be there!", lts.getState("s2"));
        assertEquals("Wrong initial state!", fts.getInitialState().getName(), lts.getInitialState().getName());
        assertNull("Unaccessible states should be removed!", lts.getState("sExt"));
    }

    @Test
    public void testInaccessibleStateWithFExpressions() throws Exception {
        Configuration config = new SimpleConfiguration(feature("f1"), feature("f2"),
                feature("f3"));
        Projection projection = SimpleProjection.getInstance();
        FeaturedTransitionSystem fts = define().init("s0")
                .from("s0").action("act1").fexpression("f1").to("s1")
                .from("s1").action("act2").to("s2")
                .from("s1").action("actBis").fexpression("!f1").to("sExt")
                .from("sExt").action("actTer").to("s2")
                .from("s1").action("actBis").fexpression("f1").to("sExt2")
                .from("sExt2").action("actTer").to("s2")
                .from("s1").action("actBis").fexpression("f1 && false").to("sExt3")
                .from("sExt3").action("actTer").to("s2")
                .from("s2").action("act3").to("s0")
                .build();
        TransitionSystem lts = projection.project(fts, config);
        assertNotNull(lts);
        assertEquals("Wrong initial state!", fts.getInitialState().getName(), lts.getInitialState().getName());
        assertNotNull("Accessible states should still be there!", lts.getState("s0"));
        assertNotNull("Accessible states should still be there!", lts.getState("s1"));
        assertNotNull("Accessible states should still be there!", lts.getState("s2"));
        assertNotNull("Accessible states should still be there!", lts.getState("sExt2"));
        assertNull("Unaccessible states should be removed!", lts.getState("sExt"));
        assertNull("Unaccessible states should be removed!", lts.getState("sExt3"));
    }

}
