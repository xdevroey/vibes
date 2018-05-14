
package be.vibes.selection.random;

import be.vibes.ts.TransitionSystem;
import be.vibes.ts.io.xml.XmlLoaders;
import java.io.InputStream;
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
public class RandomTestCaseSelectorTest {

    private static final Logger LOG = LoggerFactory.getLogger(RandomTestCaseSelectorTest.class);

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
                .getResourceAsStream("ts-sodaVendingMachine.xml");
        TransitionSystem ts = XmlLoaders.loadTransitionSystem(input);
        assertThat(ts, notNullValue());
        RandomTestCaseSelector selector = new RandomTestCaseSelector(ts, 34, 42);
        assertThat(selector.getMaxLength(), equalTo(42));
        assertThat(selector.getMaxNbrAttempts(), equalTo(34));
        
    }


}