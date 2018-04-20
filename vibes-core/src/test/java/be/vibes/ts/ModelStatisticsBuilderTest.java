package be.vibes.ts;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


;

@SuppressWarnings("deprecation")
public class ModelStatisticsBuilderTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(ModelStatisticsBuilderTest.class);

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
    public void testSimpleGraph() {
        TransitionSystemFactory factory = new TransitionSystemFactory("s0");
        factory.addStates("s1", "s2", "s3");
        factory.addActions("a1", "a2", "a0");
        factory.addTransition("s0", "a1", "s1");
        factory.addTransition("s1", "a2", "s2");
        factory.addTransition("s2", "a2", "s3");
        factory.addTransition("s3", "a0", "s0");
        TransitionSystem ts = factory.build();
        TransitionSystemModelStatistics stat = TransitionSystemModelStatistics.getStatistics(ts);
        assertEquals("Wrong BFS Height", 3, stat.getBfsHeight());
        assertEquals("Wrong Avg Degree", 1, stat.getAvgDegree(), 0.00000001);
        assertEquals("Wrong Nb Back Level Transitions", 1, stat.getNbBackLevelTransitions());

    }

}
