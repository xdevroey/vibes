package be.vibes.selection.random;

/*-
 * #%L
 * VIBeS: test case selection
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
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

import be.vibes.ts.TestCase;
import be.vibes.ts.UsageModel;
import be.vibes.ts.io.xml.XmlLoaders;
import java.io.InputStream;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class UsageDrivenRandomSelectorTest {

    private static final Logger LOG = LoggerFactory.getLogger(UsageDrivenRandomSelectorTest.class);

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
    public void testSelect() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("um-sodaVendingMachine.xml");
        UsageModel um = XmlLoaders.loadUsageModel(input);
        assertThat(um, notNullValue());
        UsageDrivenRandomSelector selector = new UsageDrivenRandomSelector(um, 34, 42);
        assertThat(selector.getMaxLength(), equalTo(42));
        assertThat(selector.getMaxNbrAttempts(), equalTo(34));
        List<TestCase> testcases = selector.select(10);
        assertThat(testcases, hasSize(10));
        for (TestCase tc : testcases) {
            LOG.debug("Generated test case: {}", tc);
            assertThat("Initial state of the test case should be initial state of the transition system!", tc.getFirst().getSource(), equalTo(um.getInitialState()));
            assertThat("Final state of the test case should be initial state of the transition system!", tc.getLast().getTarget(), equalTo(um.getInitialState()));
        }
    }
    

    @Test
    public void testSelectSinglePath() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("um-singlepath.xml");
        UsageModel um = XmlLoaders.loadUsageModel(input);
        assertThat(um, notNullValue());
        UsageDrivenRandomSelector selector = new UsageDrivenRandomSelector(um, 34, 42);
        assertThat(selector.getMaxLength(), equalTo(42));
        assertThat(selector.getMaxNbrAttempts(), equalTo(34));
        List<TestCase> testcases = selector.select(10);
        assertThat(testcases, hasSize(10));
        for (TestCase tc : testcases) {
            assertThat("Initial state of the test case should be initial state of the transition system!", tc.getFirst().getSource(), equalTo(um.getInitialState()));
            assertThat("Final state of the test case should be initial state of the transition system!", tc.getLast().getTarget(), equalTo(um.getInitialState()));
            assertThat(tc.getSize(), equalTo(3));
        }
    }

}
