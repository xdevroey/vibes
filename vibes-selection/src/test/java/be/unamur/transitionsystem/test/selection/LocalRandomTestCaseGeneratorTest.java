package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.*;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;

import com.google.common.collect.Lists;
import java.util.List;
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
public class LocalRandomTestCaseGeneratorTest {

    private static final Logger logger = LoggerFactory
            .getLogger(LocalRandomTestCaseGeneratorTest.class);

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
    public void testGenerateAbstractTestSet() throws Exception {
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
        List<State> states = Lists.newArrayList(lts.getState("s3"));
        AccumulatorWrapUp wrap = new AccumulatorWrapUp();
        LocalRandomTestCaseGenerator gen = new LocalRandomTestCaseGenerator(LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, wrap, states );
        gen.generateAbstractTestSet(lts, 1);
        List<TestCase> tcs = wrap.getTestCases();
        assertThat(tcs, hasSize(1));
        assertThat(tcs.get(0), contains(lts.getAction("act01"), lts.getAction("act13"), lts.getAction("act35"), lts.getAction("act50")));
    }

    @Test(expected = TestCaseSelectionException.class)
    public void testGenerateAbstractTestSetNoPath() throws Exception {
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
        List<State> states = Lists.newArrayList(lts.getState("s3"));
        AccumulatorWrapUp wrap = new AccumulatorWrapUp();
        LocalRandomTestCaseGenerator gen = new LocalRandomTestCaseGenerator(LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, wrap, states );
        gen.generateAbstractTestSet(lts, 1);
        fail();
    }
    
    /*
    public void testGenerateTC_TAD() {
        try {
            
              InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("ts-sodaVendingMachine.xml");
        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
        assertNotNull(system);
        logger.info("Transition System {}", system);
        final State from = system.getState("state2");
        final State to = system.getState("state6");
        final Action action = system.addAction("serveSoda");
        TransitionAdd op = new TransitionAdd(system, new StateSelectionStrategy() {
            private boolean used = false;

            @Override
            public State selectState(MutationOperator op, TransitionSystem ts) {
                if (!used) {
                    used = true;
                    return from;
                }
                return to;
            }
        }, new ActionSelectionStrategy() {
            @Override
            public Action selectAction(MutationOperator op, TransitionSystem ts) {
                return action;
            }
        });
        op.apply();
        
         LabelledTransitionSystem mutant= (LabelledTransitionSystem)op.result();
        
         ArrayList<State> states= MutantDifference.getLTSDiffStates(system, mutant);
         
         AccumulatorWrapUp wrap = new AccumulatorWrapUp();
        LocalRandomTestCaseGenerator gen = new LocalRandomTestCaseGenerator(LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, wrap, states );
        
          TestCaseRunner runner = new StrictTestCaseRunner();
         for (int i = 0; i < 100; i++) {
             TestCase tc =   gen.generateTestCase(mutant);
             ExecutionTree tree = runner.run(system, tc);
             if (!tree.hasPath()) {
                 logger.debug("found failing test case"+tc.toString());
             }
         }
      
         
         
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        
    }*/
    
}
