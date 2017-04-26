package be.unamur.transitionsystem.test.mutation;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.fexpression.Feature;
import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.fts.fexpression.configuration.SimpleConfiguration;
import be.unamur.fts.fexpression.exception.ConfigurationException;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.exception.TestCaseExecutionException;
import be.unamur.transitionsystem.test.mutation.exception.CounterExampleFoundException;
import be.unamur.transitionsystem.test.mutation.exception.MutationException;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.PrintStream;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

/**
 * This class represents a featured mutants model, i.e., a FTS representing a
 * set of mutations performed on a Transition System and the variability model
 * representing all the possible mutants.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class FeaturedMutantsModel {

    private final TransitionSystem original;
    private final FeaturedTransitionSystem fts;
    private final Map<String, Set<State>> modifiedStates;
    private final Random rand;

    /**
     * Creates a new FMM using the givent labelled transition system. When the
     * FMM is build, no mutation has been applied yet.
     *
     * @param ts The LTS that will be mutate.
     */
    public FeaturedMutantsModel(TransitionSystem ts) {
        this.original = ts;
        this.fts = new FeaturedTransitionSystem(ts);
        this.modifiedStates = Maps.newHashMap();
        this.rand = new Random();
    }

    /**
     * Returns the original transition system.
     *
     * @return The original transition system.
     */
    public TransitionSystem getOriginal() {
        return original;
    }

    /**
     * Returns the FMM's FTS.
     *
     * @return The FMM's FTS.
     */
    public FeaturedTransitionSystem getFts() {
        return fts;
    }

    /**
     * Returns the list of states mdoified per mutation. For instance
     * getModifiedStates().get("SMI1") returns the set of states modified by the
     * SMI1 mutation.
     *
     * @return A map representing the modified states for each mutation.
     */
    public Map<String, Set<State>> getModifiedStates() {
        return modifiedStates;
    }

    /**
     * Returns the list of mutations' feature names corresponding to the
     * mutation that have been applied on the original transition system of this
     * FMM. Those feature names will be in the FMM's FD.
     *
     * @return The list of mutations' features.
     */
    public List<String> getMutationFeatures() {
        return Lists.newArrayList(modifiedStates.keySet());
    }

    /**
     * Enrich this with the mutation result of the given operator. If the
     * operator has not yet been applied, op.apply() is called.
     *
     * @param op The operator to apply. Must be applied on the same transition
     * system as this FMM (op.getTransitionSystem().equals(this.getOriginal())
     * must be true).
     * @throws MutationException If a required structure is not present in the
     * FTS.
     * @throws CounterExampleFoundException If an equivalent mutant has been
     * detected.
     * @throws IllegalArgumentException if not
     * op.getTransitionSystem().equals(this.getOriginal()).
     */
    public void mutate(MutationOperator op) throws MutationException, CounterExampleFoundException {
        checkArgument(op.getTransitionSystem().equals(original));
        if (!op.hasBeenApplied()) {
            op.apply();
        }
        op.transpose(fts);
        modifiedStates.put(op.getFeatureId(), op.getModifiedStates());
    }

    /**
     * Enrich this FMM with the mutations of the operator contained in the given
     * configuration.
     *
     * @param config The configuration containing the operators that will be
     * applied.
     * @throws MutationException If a required structure is not present in the
     * FTS.
     * @throws CounterExampleFoundException If an equivalent mutant has been
     * detected.
     */
    public void mutate(MutationConfiguration config) throws MutationException, CounterExampleFoundException {
        for (MutationOperator op : config.getOperators(original)) {
            for (int i = 0; i < config.getMutationSize(op.getClass()); i++) {
                op.apply();
                mutate(op);
            }
        }
    }

    /**
     * Returns the number of possible mutants for the given order for this FMM.
     *
     * @param order The order of the mutants to consider.
     * @return The number of possible mutants or Double.POSITIVE_INFINITY if
     * this number overflows a double value.
     * @throws IllegalArgumentException if the given order is not between 0 and
     * this.numberOfMutations().
     */
    public double possibleMutantsCount(int order) {
        checkArgument(order >= 0 && order <= numberOfMutations(), "Parameter 'order' has an illegal value (%s), should be between 0 and %s!", order, numberOfMutations());
        return CombinatoricsUtils.binomialCoefficientDouble(modifiedStates.size(), order);
    }

    /**
     * Return the number of mutations applied to the base model.
     *
     * @return The number of mutations applied to the base model.
     */
    public int numberOfMutations() {
        return modifiedStates.size();
    }

    /**
     * Returns an iterator over the possible mutants for the given order.
     *
     * @param order The mutation order to consider.
     * @return An iterator over the possible mutants.
     * @throws IllegalArgumentException if the given order is not between 0 and
     * this.numberOfMutations().
     */
    public Iterator<Configuration> getMutantsConfigs(int order) {
        checkArgument(order >= 0 && order <= numberOfMutations(), "Parameter 'order' has an illegal value, should be between 0 and %s!", numberOfMutations());
        Generator<String> gen = Factory.createSimpleCombinationGenerator(Factory.createVector(modifiedStates.keySet()), order);
        final Iterator<ICombinatoricsVector<String>> it = gen.iterator();
        return new Iterator<Configuration>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Configuration next() {
                Configuration config = new SimpleConfiguration();
                for (String str : it.next()) {
                    try {
                        config.selectFeature(Feature.feature(str));
                    } catch (ConfigurationException ex) {
                        throw new IllegalStateException("S***t happens, but should not in this case!", ex);
                    }
                }
                return config;
            }
        };
    }

    /**
     * Returns a list of unique randomly selected mutants configuration for the
     * given order.
     *
     * @param order The order of the random mutant configurations.
     * @param nbrConfigurations The desired number of configurations.
     * @return A list with randomly selected mutant configurations
     * (configurations are unique in the list).
     * @throws IllegalArgumentException If the given number of configuration is
     * higher than the maximal number of possible mutations for the givne order.
     */
    public List<Configuration> getRandomMutantConfigurations(int order, int nbrConfigurations) {
        checkArgument(order >= 0 && order <= numberOfMutations(), "Parameter 'order' has an illegal value, should be between 0 and %s!", numberOfMutations());
        List<String> features = Lists.newArrayList(modifiedStates.keySet());
        List<Configuration> configurations = Lists.newArrayList();
        int min = (int) possibleMutantsCount(order);
        min = nbrConfigurations < min ? nbrConfigurations : min;
        for (int i = 0; i < min; i++) {
            Configuration c = new SimpleConfiguration();
            for (int j = 0; j < order; j++) {
                Feature f = Feature.feature(features.get(rand.nextInt(features.size())));
                if (c.isSelected(f)) {
                    j--;
                } else {
                    try {
                        c.selectFeature(f);
                    } catch (ConfigurationException ex) {
                        // Should not appen with simple configuration
                        throw new IllegalStateException("S***t happens, but should not in this case!", ex);
                    }
                }
            }
            if (configurations.contains(c)) {
                i--;
            } else {
                configurations.add(c);
            }
        }
        return configurations;
    }

    /**
     * Returns a random mutant configuration for the given order.
     *
     * @param order The order of the desired mutant configuration.
     * @return A random mutant configuration with the given order.
     */
    public Configuration getRandomMutantConfiguration(int order) {
        return getRandomMutantConfigurations(order, 1).get(0);
    }

    /**
     * Return the mutant Labelled Transition System for the given mutant
     * configuration.
     *
     * @param config The configuration corresponding to the mutant to select.
     * @return The mutant Labelled Transition System for the given mutant
     * configuration.
     */
    public LabelledTransitionSystem getMutant(Configuration config) {
        return MutantProjection.getInstance().project(fts, config);
    }

    /**
     * Executes the given test case on this FMM and returns a feature expression
     * representing all the alive mutants.
     *
     * @param testCase The test case to execute.
     * @return A feature expression representing the alive mutants.
     * @throws TestCaseExecutionException If an error occurs during the test
     * case execution.
     */
    public FExpression getAliveMutants(TestCase testCase) throws TestCaseExecutionException {
        FtsMutantVisitor visitor = new FtsMutantVisitor(testCase, getOriginal().getInitialState().getName());
        try {
            getFts().getInitialState().accept(visitor);
        } catch (VisitException e) {
            throw new TestCaseExecutionException("Error while executing mutant!", e);
        }
        return visitor.getAlive();
    }

    /**
     * Prints the TVL model corresponding to all the mutants of all orders.
     *
     * @param out The output stream on which the TVL will be printed.
     */
    public void printTvl(PrintStream out) {
        printTvl(1, numberOfMutations(), out);
    }

    /**
     * Prints the TVL model corresponding to the mutants of the given order.
     *
     * @param order The order of the mutants. Must be between [1 ;
     * this.numberOfMutations()]
     * @param out The output stream on which the TVL will be printed.
     */
    public void printTvl(int order, PrintStream out) {
        printTvl(order, order, out);
    }

    /**
     * Prints the TVL model corresponding to the mutants with an order between
     * [lowestOrder ; highestOrder].
     *
     * @param lowestOrder The lowest order of the mutants. Must be between [1 ;
     * highestOrder]
     * @param highestOrder The highest order of the mutants. Must be between
     * [lowestOrder ; this.numberOfMutations()]
     * @param out The output stream on which the TVL will be printed.
     */
    public void printTvl(int lowestOrder, int highestOrder, PrintStream out) {
        checkArgument(lowestOrder >= 1 && lowestOrder <= highestOrder && highestOrder <= modifiedStates.size());
        out.println("/*");
        out.println(" * Generated on " + new GregorianCalendar().getTime());
        out.println(" * using VIBeS (https://projects.info.unamur.be/vibes/)");
        for (int i = lowestOrder; i <= highestOrder; i++) {
            out.printf(Locale.US, " * Number of possible configurations for order %d = %.5g", i, possibleMutantsCount(i)).println();
        }
        out.println(" */");
        out.println();
        out.println("root Mutant {");
        out.println("    group [" + lowestOrder + "," + highestOrder + "]{");
        // Print mutants features
        Iterator<Map.Entry<String, Set<State>>> it = modifiedStates.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Set<State>> e = it.next();
            out.print("        ");
            out.print(e.getKey());
            if (it.hasNext()) {
                out.print(',');
            } else {
                out.print(' ');
            }
            out.print(" /* modified states: ");
            Iterator<State> itStates = e.getValue().iterator();
            while (itStates.hasNext()) {
                State s = itStates.next();
                out.print(s.getName());
                if (itStates.hasNext()) {
                    out.print(" ,");
                }
            }
            out.print("*/");
            out.println();
        }
        out.println("    }");
        out.println('}');
        out.println();
        out.flush();
    }

}
