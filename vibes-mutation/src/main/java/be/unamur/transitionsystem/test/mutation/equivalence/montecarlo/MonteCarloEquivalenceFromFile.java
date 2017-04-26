/**
 *
 */
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.fts.fexpression.Feature;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.fts.solver.Sat4JSolverFacade;
import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.stat.ModelStatistics;
import be.unamur.transitionsystem.stat.ModelStatisticsBuilder;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.execution.StrictTestCaseRunner;
import be.unamur.transitionsystem.test.mutation.selection.MutantSelectionStrategy;

import be.unamur.transitionsystem.test.selection.AlwaysTrueValidator;
import be.unamur.transitionsystem.test.selection.RandomTestCaseGenerator;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import be.unamur.transitionsystem.transformation.fts.SimpleProjection;
import be.unamur.transitionsystem.transformation.xml.LtsHandler;
import be.unamur.transitionsystem.transformation.xml.XmlReader;
import com.google.common.collect.Maps;

/**
 * @author Gilles Perrouin
 *
 */
@Deprecated
public class MonteCarloEquivalenceFromFile {

    private static final Logger logger = LoggerFactory
            .getLogger(MonteCarloEquivalenceFromFile.class);
    public static HashMap<Configuration, Double> resultsMap = Maps.newHashMap();
    public static HashMap<String, Double> resultsMap2 = Maps.newHashMap();
    public static String[][] csvRes;
    private static CSVPrinter printer;
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final Object[] FILE_HEADER = {"FileName", "MutantType",
        "Probability", "Time (ms)"};

    public static void checkEquivalence(FeaturedTransitionSystem fts,
            LabelledTransitionSystem ts, Sat4JSolverFacade facade, XMLConfiguration config)
            throws IOException, InstantiationException, IllegalAccessException,
            ClassNotFoundException, XMLStreamException {

        FileWriter fileWriter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT
                .withRecordSeparator(NEW_LINE_SEPARATOR);

        //initialize FileWriter object
        fileWriter = new FileWriter(config.getString("file.csv-path",
                System.getProperty("user.dir").concat("results.txt")));

        //initialize CSVPrinter object 
        printer = new CSVPrinter(fileWriter, csvFileFormat);

        //Create CSV file header
        printer.printRecord(FILE_HEADER);

        // checking if mutants should be loaded from existing files rather than sampled
        if (config.getBoolean("file.enabled")) {
            File dir = new File(config.getString("file.path"));

            FilenameFilter textFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String lowercaseName = name.toLowerCase();
                    if (lowercaseName.endsWith(".ts")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };

            //csvRes=  new String[][]();
            for (File f : dir.listFiles(textFilter)) {
                logger.debug("loading file " + f.getName());

                LtsHandler handler = new LtsHandler();
                XmlReader reader = new XmlReader(handler, new FileInputStream(f));
                reader.readDocument();
                LabelledTransitionSystem lts = (LabelledTransitionSystem) handler
                        .geTransitionSystem();
                final long current = System.currentTimeMillis();
                double res = getMonteCarloEquivalence(ts, lts,
                        config.getDouble("mc.delta"), config.getDouble("mc.epsilon"));
                final long after = System.currentTimeMillis();
                final long t = after - current;

                if (res >= config.getDouble("mc.pmin", 0.0)) {
                    Object[] record = {f.getName(), f.getName().split("_")[0], res, t};
                    printer.printRecord(record);

                }

            }

        } else {

            logger.info("trying to load: " + config.getString("strategy.class")
                    + " strategy for mutants sampling");

            MutantSelectionStrategy strategy = (MutantSelectionStrategy) Class.forName(
                    config.getString("strategy.class")).newInstance();

            ArrayList<Configuration> mutants = strategy.getMutants(config, facade);

            int cpt = 0;
            for (Configuration c : mutants) {
                LabelledTransitionSystem lts = SimpleProjection.getInstance().project(
                        fts, c);
                final long current = System.currentTimeMillis();
                double res = getMonteCarloEquivalence(ts, lts,
                        config.getDouble("mc.delta"), config.getDouble("mc.epsilon"));
                final long after = System.currentTimeMillis();
                final long t = after - current;

                if (res >= config.getDouble("mc.pmin", 0.0)) {

                    StringBuilder buff = new StringBuilder();
                    Feature[] tab = c.getFeatures();
                    for (int i = 0; i < tab.length - 1; i++) {
                        buff.append(tab[i].getName());
                        buff.append("&");
                    }
                    buff.append(tab[tab.length - 1].getName());
                    String flist = buff.toString();

                    Object[] record = {cpt, flist, res, t};
                    printer.printRecord(record);
                }
            }
        }
        fileWriter.flush();
        fileWriter.close();
        printer.close();
    }

    /*	
     * @param ltsprev
     * @param lts
     *
     *
     * private static void checkLTS(LabelledTransitionSystem ltsprev,
     * LabelledTransitionSystem lts) {
     * 
     * if (ltsprev != null &&ltsprev.numberOfStates() == lts.numberOfActions()
     * && lts.numberOfActions() == ltsprev.numberOfActions()) {
     * System.out.println("LTS is same"); }
     * 
     * 
     * // TODO Auto-generated method stub
     * 
     * }
     */
    public static double getMonteCarloEquivalence(LabelledTransitionSystem source,
            LabelledTransitionSystem mutant, double delta, double epsilon) {

        double N = (4 * Math.log(2 / delta)) / (epsilon * epsilon);

        int n = new Double(Math.floor(N)).intValue();
        logger.debug("n, N: " + n + "," + N);

        ModelStatistics stats = ModelStatisticsBuilder.INSTANCE.build(mutant);
        logger.info("Mutant statistics: {}", stats.getStatistics());

        EquivalenceWrapUp up = new EquivalenceWrapUp(mutant);
        RandomTestCaseGenerator gen = new RandomTestCaseGenerator(
                LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, up);

        try {
            logger.debug("Trying to generate " + n + " test cases for equivalence...");
            gen.generateAbstractTestSet(source, n);
			//gen.generateAbstractTestSet(source, n);

            //return new TestSet(wrapUp.getTestCases());
        } catch (TestCaseSelectionException e) {
            throw new IllegalStateException(
                    "Exception while generating test cases!", e);
        }
        System.out.println("nbEquiv: " + up.getEquivalentsNb() + " n: " + n);

        return ((double) up.getEquivalentsNb() / n);

    }

    public static boolean checkValidity(TransitionSystem source, TestCase tc) {

        logger.debug("Now checking for valiity of generated source test case over mutant");
        StrictTestCaseRunner str = new StrictTestCaseRunner();
        ExecutionTree<Transition> execTree = str.run(source,tc);
        logger.debug("execTree has path: " + execTree.hasPath());
        return execTree.getRoot().numberOfSons() != 0;
    }

    public static void printEquivalenceResults() {

        for (Configuration c : resultsMap.keySet()) {
            System.out.println("p: " + resultsMap.get(c));
        }

    }

    public static void printEquivalenceResults2() {

        for (String c : resultsMap2.keySet()) {
            System.out.println("n: " + c + " p: " + resultsMap2.get(c));
        }

    }

}
