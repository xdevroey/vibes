package be.unamur.transitionsystem.dsl.selection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.fts.solver.SolverFacade;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.dsl.exception.TestCaseDefinitionException;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.FtsMutableTestCase;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.test.selection.AccumulatorWrapUp;
import be.unamur.transitionsystem.test.selection.AlwaysTrueValidator;
import be.unamur.transitionsystem.test.selection.AntiDiceDissimilarityComputor;
import be.unamur.transitionsystem.test.selection.DiceDissimilarityComputor;
import be.unamur.transitionsystem.test.selection.DissimilarTestCaseGenerator;
import be.unamur.transitionsystem.test.selection.GlobalMaximumDistancePrioritization;
import be.unamur.transitionsystem.test.selection.HammingDissimilarityComputor;
import be.unamur.transitionsystem.test.selection.JaccardDissimilarityComputor;
import be.unamur.transitionsystem.test.selection.LevenshteinDissimilarityComputor;
import be.unamur.transitionsystem.test.selection.LocalMaximumDistancePrioritization;
import be.unamur.transitionsystem.test.selection.PrioritizationTechnique;
import be.unamur.transitionsystem.test.selection.TestCaseDissimilarityComputor;
import be.unamur.transitionsystem.test.selection.TestCaseValidator;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import be.unamur.transitionsystem.test.selection.fts.FtsTestCaseDissimilarityComputor;
import be.unamur.transitionsystem.test.selection.fts.FtsTestCaseValidator;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Sets;
import java.util.function.BinaryOperator;

/**
 * This class contains methods to select dissimilar test cases. The algorithms
 * will maximise the distance between the test cases of the generated test set.
 * User may call static from method to parametrize a new selection.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Dissimilar {

    private static final Logger logger = LoggerFactory.getLogger(Dissimilar.class);

    private TransitionSystem ts;
    private PrioritizationTechnique prior;
    private TestCaseValidator validator;
    private TestCaseFactory testCaseFactory;
    private int nbrTestCases = DissimilarTestCaseGenerator.DEFAULT_NUMBER_OF_TEST_CASES;
    private long runningTime = DissimilarTestCaseGenerator.DEFAULT_RUNNING_TIME_MILLI;

    private Dissimilar(TransitionSystem ts) {
        this.ts = ts;
    }

    /**
     * Allows to parametrize a new dissimilar selection for the givent labelled
     * transition system.
     *
     * @param lts The LTS from which test cases will be selected.
     * @return A partially condifured algorithm.
     */
    public static Dissimilar from(LabelledTransitionSystem lts) {
        Dissimilar diss = new Dissimilar(lts);
        diss.testCaseFactory = LtsMutableTestCase.FACTORY;
        diss.validator = AlwaysTrueValidator.TRUE_VALIDATOR;
        return diss;
    }

    /**
     * Allows to parametrize a new dissimilar selection for the givent featured
     * transition system.
     *
     * @param fts The FTS from wich test cases will be selected.
     * @param solver The solver representing the feature diagram associate to
     * the given FTS.
     * @return A partially condifured algorithm.
     */
    public static Dissimilar from(FeaturedTransitionSystem fts, SolverFacade solver) {
        Dissimilar diss = new Dissimilar(fts);
        diss.validator = new FtsTestCaseValidator(solver);
        diss.testCaseFactory = FtsMutableTestCase.FACTORY;
        return diss;
    }

    /**
     * Specifies that the selection will use local distance during test case
     * sorting at each iteration of the main loop.
     *
     * @param computor The computor used to compute the distance between two
     * test cases.
     * @return A partially condifured algorithm.
     */
    public Dissimilar withLocalMaxDistance(TestCaseDissimilarityComputor computor) {
        checkArgument(!(computor instanceof FtsTestCaseDissimilarityComputor) || (ts instanceof FeaturedTransitionSystem),
                "A FtsTestCaseDissimilarityComputor computor may only be used with a FeaturedTransitionSystem!");
        this.prior = new LocalMaximumDistancePrioritization(computor);
        return this;
    }

    /**
     * Specifies that the selection will use global distance during test case
     * sorting at each iteration of the main loop.
     *
     * @param computor The computor used to compute the distance between two
     * test cases.
     * @return A partially condifured algorithm.
     */
    public Dissimilar withGlobalMaxDistance(TestCaseDissimilarityComputor computor) {
        checkArgument(!(computor instanceof FtsTestCaseDissimilarityComputor) || (ts instanceof FeaturedTransitionSystem),
                "A FtsTestCaseDissimilarityComputor computor may only be used with a FeaturedTransitionSystem!");
        this.prior = new GlobalMaximumDistancePrioritization(computor);
        return this;
    }

    /**
     * Specifies the maximum time (in milliseconds) during which the algorithm
     * will be executed. Default value is
     * DissimilarTestCaseGenerator.DEFAULT_RUNNING_TIME_MILLI.
     *
     * @param time The maximum time during which the algorithm will be executed.
     * @return A partially condifured algorithm.
     */
    public Dissimilar during(long time) {
        this.runningTime = time;
        return this;
    }

    /**
     * Lauch the generation of a test set with the specified number of test
     * cases using this configuration.
     *
     * @param testCasesCount The number of test cases to generate.
     * @return The generated test cases.
     */
    public TestSet generate(int testCasesCount) {
        this.nbrTestCases = testCasesCount;
        return generate();
    }

    /**
     * Lauch the generation of a test set with the default number of test cases
     * using this configuration. Default number of test cases is
     * DissimilarTestCaseGenerator.DEFAULT_NUMBER_OF_TEST_CASES.
     *
     * @return The test cases generated using this configuration of the
     * algorithm.
     */
    public TestSet generate() {
        checkNotNull(prior, "Prioritization technique has to be specified using withLocalMaxDistance or withGlobalMaxDistance method!");
        AccumulatorWrapUp acc = new AccumulatorWrapUp();
        DissimilarTestCaseGenerator gen = new DissimilarTestCaseGenerator(testCaseFactory, validator, acc, prior);
        gen.setNbrTestCases(nbrTestCases);
        gen.setRunningTime(runningTime);
        try {
            gen.generateAbstractTestSet(ts);
        } catch (TestCaseSelectionException ex) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", ex);
        }
        logger.info("Last fitness value = {}", gen.getLastFitness());
        logger.info("Last number of iterations = {}", gen.getLastNbrIterations());
        return new TestSet(acc.getTestCases());
    }

    /**
     * Returns a Hamming dissimilarity computor with the actions in the given
     * transition system as the set of all possible values.
     *
     * @param ts The transition system from which test cases are selected.
     * @return A new Hamming dissimilarity computor.
     * @see HammingDissimilarityComputor
     */
    public static TestCaseDissimilarityComputor hamming(TransitionSystem ts) {
        return TestCaseDissimilarityComputor.toTestCaseDissimilarityComputor(new HammingDissimilarityComputor(Sets.newHashSet(ts.actions())));
    }

    /**
     * Returns a Jaccard dissimilarity computor.
     *
     * @return A new Jaccard dissimilarity computor.
     * @see JaccardDissimilarityComputor
     */
    public static TestCaseDissimilarityComputor jaccard() {
        return TestCaseDissimilarityComputor.toTestCaseDissimilarityComputor(new JaccardDissimilarityComputor());
    }

    /**
     * Returns a Dice dissimilarity computor.
     *
     * @return A new Dice dissimilarity computor.
     * @see DiceDissimilarityComputor
     */
    public static TestCaseDissimilarityComputor dice() {
        return TestCaseDissimilarityComputor.toTestCaseDissimilarityComputor(new DiceDissimilarityComputor());
    }

    /**
     * Returns a Antidice dissimilarity computor.
     *
     * @return A new Antidice dissimilarity computor.
     * @see AntiDiceDissimilarityComputor
     */
    public static TestCaseDissimilarityComputor antidice() {
        return TestCaseDissimilarityComputor.toTestCaseDissimilarityComputor(new AntiDiceDissimilarityComputor());
    }

    /**
     * Returns a Levenshtein dissimilarity computor.
     *
     * @return A new Levenshtein dissimilarity computor.
     * @see LevenshteinDissimilarityComputor
     */
    public static TestCaseDissimilarityComputor levenshtein() {
        return TestCaseDissimilarityComputor.toTestCaseDissimilarityComputor(new LevenshteinDissimilarityComputor());
    }

    /**
     * Returns a new dissimilarity computor for test cases selected from a FTS.
     * It will use Jaccard distance for the products and the actions of the test
     * case. The dissimilarity computed by the returned object will be equal to
     * jaccard(prods(tc1), prods(tc2)) * jaccard(tc1, tc3).
     *
     * @param solver The solver representing the FD associated to the FTS from
     * which test cases are selected.
     * @return A new FTS test cases dissimilarity computor.
     * @see FtsTestCaseDissimilarityComputor
     */
    public static FtsTestCaseDissimilarityComputor ftsDissimilarity(SolverFacade solver) {
        return new FtsTestCaseDissimilarityComputor(solver);
    }

    /**
     * Returns a new binary operator representing the multiply.
     *
     * @return The multiply operator.
     */
    public static BinaryOperator<Double> multiply() {
        return (Double x, Double y) -> {
            return x * y;
        };
    }

    /**
     * Returns a new binary operator computing the average value of the given
     * operands.
     *
     * @return The avg(left, right) operator.
     */
    public static BinaryOperator<Double> avg() {
        return (Double x, Double y) -> {
            return (x + y) / 2;
        };
    }

    /**
     * Allows to create a dissumilatiry computor on actions of the test cases
     * and products covered by the test cases. It will use Jaccard distance for
     * the products and the given distance computor for the actions of the test
     * case. The dissimilarity computed by the returned object will be equal to
     * jaccard(prods(tc1), prods(tc2)) * computor(tc1, tc3).
     *
     * @param solver The solver representing the feature diagram of the FTS from
     * wich the test cases are generated.
     * @param computor The dissimilarity computor to use on the actions of the
     * test case.
     * @return A dissimilarity computer to use in method withLocalMaxDistance or
     * withGlobalMaxDistance.
     */
    public static FtsTestCaseDissimilarityComputor ftsDissimilarity(SolverFacade solver, TestCaseDissimilarityComputor computor) {
        return new FtsTestCaseDissimilarityComputor(solver, computor);
    }

    /**
     * Allows to create a dissumilatiry computor on actions of the test cases
     * and products covered by the test cases. It will use Jaccard distance for
     * the products and the given distance computor for the actions of the test
     * case. The dissimilarity computed by the returned object will be equal to
     * combineOperator(jaccard(prods(tc1),prods(tc2)),computor(tc1, tc3)).
     *
     * @param solver The solver representing the feature diagram of the FTS from
     * wich the test cases are generated.
     * @param computor The dissimilarity computor to use on the actions of the
     * test case.
     * @return A dissimilarity computer to use in method withLocalMaxDistance or
     * withGlobalMaxDistance.
     */
    public static FtsTestCaseDissimilarityComputor ftsDissimilarity(SolverFacade solver, TestCaseDissimilarityComputor computor, BinaryOperator<Double> combineOperator) {
        return new FtsTestCaseDissimilarityComputor(solver, computor, combineOperator);
    }

}
