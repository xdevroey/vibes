package be.vibes.selection.dissimilar;

import be.vibes.fexpression.DimacsModel;
import be.vibes.selection.dissimilar.DissimilarTestCaseSelector;
import be.vibes.selection.dissimilar.FtsTestCaseDissimilarityComputor;
import be.vibes.selection.dissimilar.LocalMaximumDistancePrioritization;
import be.vibes.selection.dissimilar.GlobalMaximumDistancePrioritization;
import be.vibes.solver.BDDSolverFacade;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TestCase;
import be.vibes.ts.io.xml.XmlLoaders;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DissimilarTestCaseGeneratorTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(DissimilarTestCaseGeneratorTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOG.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	/*
    @Test
    public void testLocalMaximumDistancePrioritization() throws Exception {
        File dimacsModel = new File(DissimilarTestCaseGeneratorTest.class.getClassLoader().getResource("svm.splot.dimacs").toURI());
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("fts-sodaVendingMachine.xml");
        FeaturedTransitionSystem fts = XmlLoaders.loadFeaturedTransitionSystem(input);
        DimacsModel model = DimacsModel.createFromDimacsFile(dimacsModel);
        BDDSolverFacade solver = new BDDSolverFacade(model);
        FtsTestCaseDissimilarityComputor comp = new FtsTestCaseDissimilarityComputor(solver, fts);
        LocalMaximumDistancePrioritization prior = new LocalMaximumDistancePrioritization(comp);
        DissimilarTestCaseSelector gen = new DissimilarTestCaseSelector(fts, prior);
        gen.setRunningTime(1000);
        List<TestCase> tests = gen.select(20);
        assertThat(tests, hasSize(20));
    }

    @Test
    public void testGlobalMaximumDistancePrioritization() throws Exception {
        File dimacsModel = new File(DissimilarTestCaseGeneratorTest.class.getClassLoader().getResource("svm.splot.dimacs").toURI());
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("fts-sodaVendingMachine.xml");
        FeaturedTransitionSystem fts = XmlLoaders.loadFeaturedTransitionSystem(input);
        DimacsModel model = DimacsModel.createFromDimacsFile(dimacsModel);
        BDDSolverFacade solver = new BDDSolverFacade(model);
        FtsTestCaseDissimilarityComputor comp = new FtsTestCaseDissimilarityComputor(solver, fts);
        GlobalMaximumDistancePrioritization prior = new GlobalMaximumDistancePrioritization(comp);
        DissimilarTestCaseSelector gen = new DissimilarTestCaseSelector(fts, prior);
        gen.setRunningTime(1000);
        List<TestCase> tests = gen.select(20);
        assertThat(tests, hasSize(20));
    }
*/
}
