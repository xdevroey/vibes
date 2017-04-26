package be.unamur.transitionsystem.stat;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.TransitionSystem;
import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.define;

;

@SuppressWarnings("deprecation")
public class ModelStatisticsBuilderTest {

    private static final Logger logger = LoggerFactory
            .getLogger(ModelStatisticsBuilderTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };
	
	@Test
    public void testSimpleGraph() {
        TransitionSystem ts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s1").action("act2").to("s2")
                .from("s2").action("act2").to("s3")
                .from("s3").action("act0").to("s0")
                .build();
        assertNotNull(ts);
        TransitionSystemModelStatistics stat = new TransitionSystemModelStatistics(ts);
        assertEquals("Wrong BFS Height", 3, stat.getBfsHeight());
        assertEquals("Wrong Avg Degree", 1, stat.getAvgDegree(), 0.00000001);
        assertEquals("Wrong Nb Back Level Transitions", 1, stat.getNbBackLevelTransitions());

    }

}
