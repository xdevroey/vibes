package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.define;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import com.google.common.collect.Sets;
import java.util.Set;
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
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class LocalRandomTestCaseSelectorTest {

    private static final Logger LOG = LoggerFactory.getLogger(LocalRandomTestCaseSelectorTest.class);

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
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act01").to("s1")
                .from("s1").action("act12").to("s2")
                .from("s1").action("act13").to("s3")
                .from("s1").action("act14").to("s4")
                .from("s2").action("act25").to("s5")
                .from("s3").action("act35").to("s5")
                .from("s4").action("act45").to("s5")
                .from("s5").action("act50").to("s0")
                .build();
        Set<State> states = Sets.newHashSet(lts.getState("s3"));
        LocalRandomTestCaseSelector<LabelledTransitionSystem> selector = new LocalRandomTestCaseSelector(lts, states);
        TestCase testCase = selector.select();
        assertThat(testCase, contains(lts.getAction("act01"), lts.getAction("act13"), lts.getAction("act35"), lts.getAction("act50")));
    }

    @Test(expected = TestCaseSelectionException.class)
    public void testSelectNoPath() throws Exception {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act01").to("s1")
                .from("s1").action("act12").to("s2")
                //.from("s1").action("act13").to("s3")
                .from("s1").action("act14").to("s4")
                .from("s2").action("act25").to("s5")
                .from("s3").action("act35").to("s5")
                .from("s4").action("act45").to("s5")
                .from("s5").action("act50").to("s0")
                .build();
        Set<State> states = Sets.newHashSet(lts.getState("s3"));
        LocalRandomTestCaseSelector<LabelledTransitionSystem> selector = new LocalRandomTestCaseSelector(lts, states);
        TestCase testCase = selector.select();
        fail("No possible path for given LTS and states, should hav launched a TestCaseSelectionException!");
    }

}
