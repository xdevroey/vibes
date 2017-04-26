package be.unamur.transitionsystem.test.mutation;

/*
 * #%L
 * vibes-mutation
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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.LabelledTransitionSystem;

public class MutationConfigurationTest {

    private Logger logger = LoggerFactory.getLogger(MutationConfigurationTest.class);

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
    public void test() throws Exception {
        MutationConfiguration config = new MutationConfiguration(getClass()
                .getClassLoader().getResource("default-config.xml").toURI());
        assertEquals("Wrong number of mutants!", 200, config.getDefaultMutationSize());
        assertTrue("Mutants should be unique", config.isMutantUniqueDefault());
        assertTrue(config.getDefaultActionSelectionStrategy() instanceof be.unamur.transitionsystem.test.mutation.RandomSelectionStrategy);
        assertTrue(config.getDefaultStateSelectionStrategy() instanceof be.unamur.transitionsystem.test.mutation.RandomSelectionStrategy);
        assertTrue(config.getDefaultTransitionSelectionStrategy() instanceof be.unamur.transitionsystem.test.mutation.RandomSelectionStrategy);
        assertEquals("Wrong number of operators", 7, config.getOperators(new LabelledTransitionSystem()).size());
        assertEquals("Wrong number of mutants application for be.unamur.transitionsystem.test.mutation.ActionExchange", 100, config.getMutationSize(ActionExchange.class));
    }

}
