package be.vibes.ts.execution;

import be.vibes.solver.FeatureModel;
import be.vibes.solver.Sat4JSolverFacade;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.exception.TransitionSystenExecutionException;
import be.vibes.ts.io.xml.XmlLoaders;
import com.google.common.collect.Lists;
import com.google.common.io.CharSource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class FeaturedTransitionSystemExecutorTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(FeaturedTransitionSystemExecutorTest.class);

    private static final String SODA_VENDING_MACHINE_FTS_FILE = "";

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
    public void testCanExecuteFromInitialState() throws Exception {
        File dimacsModel = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("vending-machine.dimacs").toURI());
        File featureMapping = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("vending-machine.map").toURI());
        File ftsModel = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("fts-sodaVendingMachine.xml").toURI());
        FeaturedTransitionSystem fts = XmlLoaders.loadFeaturedTransitionSystem(ftsModel);
        FeatureModel fm = new Sat4JSolverFacade(dimacsModel, featureMapping);
        FeaturedTransitionSystemExecutor executor = new FeaturedTransitionSystemExecutor(fts, fm);
        assertThat(executor.canExecute("pay"), is(true));
        assertThat(executor.canExecute("free"), is(true));
        assertThat(executor.canExecute("tea"), is(false));
        assertThat(executor.canExecute("open"), is(false));
        // Test Idempotence
        assertThat(executor.canExecute("pay"), is(true));
        assertThat(executor.canExecute("free"), is(true));
        assertThat(executor.canExecute("tea"), is(false));
        assertThat(executor.canExecute("open"), is(false));
    }

    @Test
    public void testCanExecuteFromIntermediateState() throws Exception {
        File dimacsModel = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("vending-machine.dimacs").toURI());
        File featureMapping = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("vending-machine.map").toURI());
        File ftsModel = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("fts-sodaVendingMachine.xml").toURI());
        FeaturedTransitionSystem fts = XmlLoaders.loadFeaturedTransitionSystem(ftsModel);
        FeatureModel fm = new Sat4JSolverFacade(dimacsModel, featureMapping);
        FeaturedTransitionSystemExecutor executor = new FeaturedTransitionSystemExecutor(fts, fm);
        executor.execute("free");
        executor.execute("tea");
        executor.execute("serveTea");
        assertThat(executor.canExecute("take"), is(true));
        assertThat(executor.canExecute("open"), is(false));
        assertThat(executor.canExecute("tea"), is(false));
        // Test Idempotence
        assertThat(executor.canExecute("take"), is(true));
        assertThat(executor.canExecute("open"), is(false));
        assertThat(executor.canExecute("tea"), is(false));
    }

    @Test
    public void testExecuteValidPath() throws Exception {
        File dimacsModel = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("vending-machine.dimacs").toURI());
        File featureMapping = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("vending-machine.map").toURI());
        File ftsModel = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("fts-sodaVendingMachine.xml").toURI());
        FeaturedTransitionSystem fts = XmlLoaders.loadFeaturedTransitionSystem(ftsModel);
        FeatureModel fm = new Sat4JSolverFacade(dimacsModel, featureMapping);
        FeaturedTransitionSystemExecutor executor = new FeaturedTransitionSystemExecutor(fts, fm);
        executor.execute("pay");
        executor.execute("change");
        executor.execute("cancel");
        executor.execute("return");
        List<Execution> executions = Lists.newArrayList(executor.getCurrentExecutions());
        assertThat(executions, hasSize(1));
        Execution execution = executions.get(0);
        assertThat(execution, contains(
                fts.getTransitions("state1", "pay", "state2").next(),
                fts.getTransitions("state2", "change", "state3").next(),
                fts.getTransitions("state3", "cancel", "state4").next(),
                fts.getTransitions("state4", "return", "state1").next()
        ));
    }

    @Test(expected = TransitionSystenExecutionException.class)
    public void testExecuteInvalidPathNoAction() throws Exception {
        File dimacsModel = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("vending-machine.dimacs").toURI());
        File featureMapping = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("vending-machine.map").toURI());
        File ftsModel = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("fts-sodaVendingMachine.xml").toURI());
        FeaturedTransitionSystem fts = XmlLoaders.loadFeaturedTransitionSystem(ftsModel);
        FeatureModel fm = new Sat4JSolverFacade(dimacsModel, featureMapping);
        FeaturedTransitionSystemExecutor executor = new FeaturedTransitionSystemExecutor(fts, fm);
        executor.execute("free");
        executor.execute("soda");
        executor.execute("serveSoda");
        executor.execute("cancel");
    }


    @Test(expected = TransitionSystenExecutionException.class)
    public void testExecuteInvalidPathInvalidFeatureExpression() throws Exception {
        File dimacsModel = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("vending-machine.dimacs").toURI());
        File featureMapping = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("vending-machine.map").toURI());
        File ftsModel = new File(FeaturedTransitionSystemExecutorTest.class.getClassLoader().getResource("fts-sodaVendingMachine.xml").toURI());
        FeaturedTransitionSystem fts = XmlLoaders.loadFeaturedTransitionSystem(ftsModel);
        FeatureModel fm = new Sat4JSolverFacade(dimacsModel, featureMapping);
        FeaturedTransitionSystemExecutor executor = new FeaturedTransitionSystemExecutor(fts, fm);
        executor.execute("free");
        executor.execute("tea");
        executor.execute("serveTea");
        executor.execute("open");
    }

}