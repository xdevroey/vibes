package be.vibes.dsl.selection;

/*-
 * #%L
 * VIBeS: dsl
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

import be.vibes.dsl.exception.TestCaseDefinitionException;
import be.vibes.selection.dissimilar.*;
import be.vibes.selection.exception.TestCaseSelectionException;
import be.vibes.solver.FeatureModel;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TestSet;
import be.vibes.ts.TransitionSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Sets;
import java.util.function.BinaryOperator;

/**
 * This class contains methods to select dissimilar test cases. The algorithms
 * will maximize the distance between the test cases of the generated test set.
 * User may call static from method to parameterize a new selection.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Dissimilar {

    private static final Logger LOG = LoggerFactory.getLogger(Dissimilar.class);

    private final TransitionSystem ts;
    private final FeatureModel fm;
    private Prioritization prior;
    private int nbrTestCases = DissimilarTestCaseSelector.DEFAULT_NUMBER_OF_TEST_CASES;
    private long runningTime = DissimilarTestCaseSelector.DEFAULT_RUNNING_TIME_MILLI;

    private Dissimilar(TransitionSystem ts, FeatureModel fm) {
        this.ts = ts;
        this.fm = fm;
    }

    /**
     * Allows to parameterize a new dissimilar selection for the given
     * transition system.
     *
     * @param ts The LTS from which test cases will be selected.
     * @return A partially configured algorithm.
     */
    public static Dissimilar from(TransitionSystem ts) {
        Dissimilar diss = new Dissimilar(ts, null);
        return diss;
    }

    /**
     * Allows to parameterize a new dissimilar selection for the given featured
     * transition system.
     *
     * @param fts The FTS from which test cases will be selected.
     * @param fm The solver representing the feature diagram associate to the
     * given FTS.
     * @return A partially configured algorithm.
     */
    public static Dissimilar from(FeaturedTransitionSystem fts, FeatureModel fm) {
        Dissimilar diss = new Dissimilar(fts, fm);
        return diss;
    }

    /**
     * Specifies that the selection will use local distance during test case
     * sorting at each iteration of the main loop.
     *
     * @param computor The computor used to compute the distance between two
     * test cases.
     * @return A partially configured algorithm.
     */
    public Dissimilar withLocalMaxDistance(TestCaseDissimilarity computor) {
        checkArgument(!(computor instanceof FtsTestCaseDissimilarity) || ((ts instanceof FeaturedTransitionSystem) && fm != null),
                "A FtsTestCaseDissimilarityComputor computor may only be used with a FeaturedTransitionSystem and a FeatureModel!");
        if (computor instanceof FtsTestCaseDissimilarity) {
            ((FtsTestCaseDissimilarity) computor).setFts((FeaturedTransitionSystem) ts);
            ((FtsTestCaseDissimilarity) computor).setFm(fm);
        }
        this.prior = new LocalMaximumDistancePrioritization(computor);
        return this;
    }

    /**
     * Specifies that the selection will use global distance during test case
     * sorting at each iteration of the main loop.
     *
     * @param computor The computor used to compute the distance between two
     * test cases.
     * @return A partially confifured algorithm.
     */
    public Dissimilar withGlobalMaxDistance(TestCaseDissimilarity computor) {
        checkArgument(!(computor instanceof FtsTestCaseDissimilarity) || ((ts instanceof FeaturedTransitionSystem) && fm != null),
                "A FtsTestCaseDissimilarityComputor computor may only be used with a FeaturedTransitionSystem and a FeatureModel!");
        if (computor instanceof FtsTestCaseDissimilarity) {
            ((FtsTestCaseDissimilarity) computor).setFts((FeaturedTransitionSystem) ts);
            ((FtsTestCaseDissimilarity) computor).setFm(fm);
        }
        this.prior = new GlobalMaximumDistancePrioritization(computor);
        return this;
    }

    /**
     * Specifies the maximum time (in milliseconds) during which the algorithm
     * will be executed. Default value is
     * DissimilarTestCaseGenerator.DEFAULT_RUNNING_TIME_MILLI.
     *
     * @param time The maximum time during which the algorithm will be executed.
     * @return A partially configured algorithm.
     */
    public Dissimilar during(long time) {
        this.runningTime = time;
        return this;
    }

    /**
     * Launch the generation of a test set with the specified number of test
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
     * Launch the generation of a test set with the default number of test cases
     * using this configuration. Default number of test cases is
     * DissimilarTestCaseGenerator.DEFAULT_NUMBER_OF_TEST_CASES.
     *
     * @return The test cases generated using this configuration of the
     * algorithm.
     */
    public TestSet generate() {
        checkNotNull(prior, "Prioritization technique has to be specified using withLocalMaxDistance or withGlobalMaxDistance method!");
        DissimilarTestCaseSelector gen = new DissimilarTestCaseSelector(ts, prior);
        gen.setRunningTime(runningTime);
        TestSet testSet = null;
        try {
            testSet = new TestSet(gen.select(nbrTestCases));
        } catch (TestCaseSelectionException ex) {
            throw new TestCaseDefinitionException("Exception while generating test cases!", ex);
        }
        LOG.info("Last fitness value = {}", gen.getLastFitness());
        LOG.info("Last number of iterations = {}", gen.getLastNbrIterations());
        return testSet;
    }

    /**
     * Returns a Hamming dissimilarity computor with the actions in the given
     * transition system as the set of all possible values.
     *
     * @param ts The transition system from which test cases are selected.
     * @return A new Hamming dissimilarity computor.
     * @see HammingDissimilarity
     */
    public static TestCaseDissimilarity hamming(TransitionSystem ts) {
        return TestCaseDissimilarity.toTestCaseDissimilarityComputor(new HammingDissimilarity(Sets.newHashSet(ts.actions())));
    }

    /**
     * Returns a Jaccard dissimilarity computor.
     *
     * @return A new Jaccard dissimilarity computor.
     * @see JaccardDissimilarity
     */
    public static TestCaseDissimilarity jaccard() {
        return TestCaseDissimilarity.toTestCaseDissimilarityComputor(new JaccardDissimilarity());
    }

    /**
     * Returns a Dice dissimilarity computor.
     *
     * @return A new Dice dissimilarity computor.
     * @see DiceDissimilarity
     */
    public static TestCaseDissimilarity dice() {
        return TestCaseDissimilarity.toTestCaseDissimilarityComputor(new DiceDissimilarity());
    }

    /**
     * Returns a Antidice dissimilarity computor.
     *
     * @return A new Antidice dissimilarity computor.
     * @see AntiDiceDissimilarity
     */
    public static TestCaseDissimilarity antidice() {
        return TestCaseDissimilarity.toTestCaseDissimilarityComputor(new AntiDiceDissimilarity());
    }

    /**
     * Returns a Levenshtein dissimilarity computor.
     *
     * @return A new Levenshtein dissimilarity computor.
     * @see LevenshteinDissimilarity
     */
    public static TestCaseDissimilarity levenshtein() {
        return TestCaseDissimilarity.toTestCaseDissimilarityComputor(new LevenshteinDissimilarity());
    }

    /**
     * Returns a new dissimilarity computor for test cases selected from a FTS.
     * It will use Jaccard distance for the products and the actions of the test
     * case. The dissimilarity computed by the returned object will be equal to
     * jaccard(prods(tc1), prods(tc2)) * jaccard(tc1, tc3).
     *
     * @return A new FTS test cases dissimilarity computor.
     * @see FtsTestCaseDissimilarity
     */
    public static FtsTestCaseDissimilarity ftsDissimilarity() {
        return new FtsTestCaseDissimilarity(null, null);
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
     * Allows to create a dissimilarity computor on actions of the test cases
     * and products covered by the test cases. It will use Jaccard distance for
     * the products and the given distance computor for the actions of the test
     * case. The dissimilarity computed by the returned object will be equal to
     * jaccard(prods(tc1), prods(tc2)) * computor(tc1, tc3).
     *
     * @param fm The solver representing the feature diagram of the FTS from
     * which the test cases are generated.
     * @param computor The dissimilarity computor to use on the actions of the
     * test case.
     * @return A dissimilarity computer to use in method withLocalMaxDistance or
     * withGlobalMaxDistance.
     */
    public static FtsTestCaseDissimilarity ftsDissimilarity(FeatureModel fm, TestCaseDissimilarity computor) {
        return new FtsTestCaseDissimilarity(null, computor, null);
    }

    /**
     * Allows to create a dissimilarity computor on actions of the test cases
     * and products covered by the test cases. It will use Jaccard distance for
     * the products and the given distance computor for the actions of the test
     * case. The dissimilarity computed by the returned object will be equal to
     * combineOperator(jaccard(prods(tc1),prods(tc2)),computor(tc1, tc3)).
     *
     * @param fm The solver representing the feature diagram of the FTS from
     * which the test cases are generated.
     * @param computor The dissimilarity computor to use on the actions of the
     * test case.
     * @param combineOperator The function used to combine product and actions
     * dissimilarity.
     * @return A dissimilarity computer to use in method withLocalMaxDistance or
     * withGlobalMaxDistance.
     */
    public static FtsTestCaseDissimilarity ftsDissimilarity(FeatureModel fm, TestCaseDissimilarity computor, BinaryOperator<Double> combineOperator) {
        return new FtsTestCaseDissimilarity(null, computor, combineOperator, null);
    }

}
