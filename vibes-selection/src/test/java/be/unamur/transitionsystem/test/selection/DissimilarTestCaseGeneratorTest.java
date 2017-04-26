package be.unamur.transitionsystem.test.selection;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.fts.fexpression.DimacsModel;
import be.unamur.fts.solver.BDDSolverFacade;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.FtsMutableTestCase;
import be.unamur.transitionsystem.test.selection.fts.FtsTestCaseDissimilarityComputor;
import be.unamur.transitionsystem.test.selection.fts.FtsTestCaseValidator;
import static be.unamur.transitionsystem.dsl.FeaturedTransitionSystemBuilder.*;

public class DissimilarTestCaseGeneratorTest {

    private static final Logger logger = LoggerFactory
            .getLogger(DissimilarTestCaseGeneratorTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	
	@SuppressWarnings("deprecation")
    @Test
    public void testLocalMaximumDistancePrioritization() throws Exception {
        File dimacsModel = new File(DissimilarTestCaseGeneratorTest.class.getClassLoader().getResource("svm.splot.dimacs").toURI());
        assertTrue("Test file not found: svm.splot.dimacs!", dimacsModel.exists());
        File ftsModel = new File(DissimilarTestCaseGeneratorTest.class.getClassLoader().getResource("svm.fts").toURI());
        assertTrue("Test file not found: svm.fts!", ftsModel.exists());
        DimacsModel model = DimacsModel.createFromDimacsFile(dimacsModel);

        AccumulatorWrapUp acc = new AccumulatorWrapUp();
        BDDSolverFacade solver = new BDDSolverFacade(model.getFeatures(), model.getFd());
        TestCaseValidator val = new FtsTestCaseValidator(solver);
        FeaturedTransitionSystem fts = define().init("state1")
                .from("state2").action("change").fexpression("!FreeDrinks").to("state3")
                .from("state1").action("pay").fexpression("!FreeDrinks").to("state2")
                .from("state1").action("free").fexpression("FreeDrinks").to("state3")
                .from("state3").action("cancel").fexpression("CancelPurchase").to("state4")
                .from("state3").action("tea").fexpression("Tea").to("state6")
                .from("state3").action("soda").fexpression("Soda").to("state5")
                .from("state4").action("return").fexpression("CancelPurchase").to("state1")
                .from("state5").action("serveSoda").fexpression("Soda").to("state7")
                .from("state6").action("serveTea").fexpression("Tea").to("state7")
                .from("state7").action("open").fexpression("!FreeDrinks").to("state8")
                .from("state7").action("take").fexpression("FreeDrinks").to("state1")
                .from("state8").action("take").fexpression("!FreeDrinks").to("state9")
                .from("state9").action("close").fexpression("!FreeDrinks").to("state1")
                .build();

        FtsTestCaseDissimilarityComputor comp = new FtsTestCaseDissimilarityComputor(solver);
        LocalMaximumDistancePrioritization prior = new LocalMaximumDistancePrioritization(comp);
        DissimilarTestCaseGenerator gen = new DissimilarTestCaseGenerator(FtsMutableTestCase.FACTORY, val, acc, prior);
        gen.setNbrTestCases(20);
        gen.setRunningTime(1000);
        gen.generateAbstractTestSet(fts);
        assertTrue(acc.getTestCases().size() > 0);
        logger.debug("Generated Test-Cases = {}", acc.getTestCases());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testGlobalMaximumDistancePrioritization() throws Exception {
        File dimacsModel = new File(DissimilarTestCaseGeneratorTest.class.getClassLoader().getResource("svm.splot.dimacs").toURI());
        assertTrue("Test file not found: svm.splot.dimacs!", dimacsModel.exists());
        File ftsModel = new File(DissimilarTestCaseGeneratorTest.class.getClassLoader().getResource("svm.fts").toURI());
        assertTrue("Test file not found: svm.fts!", ftsModel.exists());
        DimacsModel model = DimacsModel.createFromDimacsFile(dimacsModel);

        AccumulatorWrapUp acc = new AccumulatorWrapUp();
        BDDSolverFacade solver = new BDDSolverFacade(model.getFeatures(), model.getFd());
        TestCaseValidator val = new FtsTestCaseValidator(solver);
        FeaturedTransitionSystem fts = define().init("state1")
                .from("state2").action("change").fexpression("!FreeDrinks").to("state3")
                .from("state1").action("pay").fexpression("!FreeDrinks").to("state2")
                .from("state1").action("free").fexpression("FreeDrinks").to("state3")
                .from("state3").action("cancel").fexpression("CancelPurchase").to("state4")
                .from("state3").action("tea").fexpression("Tea").to("state6")
                .from("state3").action("soda").fexpression("Soda").to("state5")
                .from("state4").action("return").fexpression("CancelPurchase").to("state1")
                .from("state5").action("serveSoda").fexpression("Soda").to("state7")
                .from("state6").action("serveTea").fexpression("Tea").to("state7")
                .from("state7").action("open").fexpression("!FreeDrinks").to("state8")
                .from("state7").action("take").fexpression("FreeDrinks").to("state1")
                .from("state8").action("take").fexpression("!FreeDrinks").to("state9")
                .from("state9").action("close").fexpression("!FreeDrinks").to("state1")
                .build();

        FtsTestCaseDissimilarityComputor comp = new FtsTestCaseDissimilarityComputor(solver);
        GlobalMaximumDistancePrioritization prior = new GlobalMaximumDistancePrioritization(comp);
        DissimilarTestCaseGenerator gen = new DissimilarTestCaseGenerator(FtsMutableTestCase.FACTORY, val, acc, prior);
        gen.setNbrTestCases(20);
        gen.setRunningTime(1000);
        gen.generateAbstractTestSet(fts);
        assertTrue(acc.getTestCases().size() > 0);
        logger.debug("Generated Test-Cases = {}", acc.getTestCases());
    }

}
