package be.unamur.transitionsystem.test.mutation.equivalence.main;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import static be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders.*;
import be.unamur.transitionsystem.test.mutation.equivalence.montecarlo.EquivalenceResults;
import be.unamur.transitionsystem.test.mutation.equivalence.montecarlo.MultiThreadedRandomEquivalence;
import be.unamur.transitionsystem.test.mutation.exception.ConnectivityHypothesisViolationException;
import be.unamur.transitionsystem.test.mutation.exception.CounterExampleFoundException;
import com.google.common.base.Stopwatch;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final String HELP = "help";
    private static final String LTS = "ts";
    private static final String EPSILON = "e";
    private static final String DELTA = "d";
    private static final String RATIO = "r";
    private static final String THREADS = "threads";
    private static final String LOCALIZED = "local";
    private static final String TEST_CASE_SEM = "testcase";
    private static final String OUT = "o";
    private static final String FULL_RUN = "full";
    private static final String TRACE_SIZE = "s";
    private static final String DEBUG = "debug";

    private static final Object[] CSV_HEADER = {"originalts", "mutantts",
        "failfirst", "delta", "epsilon", "estimatednbruns", "effectivenbruns", "failednbruns",
        "equivdegree", "timeinms", "counterexemples", "messages"};

    private final Options options;

    private LabelledTransitionSystem original;
    private String originalFileName;
    private LtsFileIterator mutants;
    private double delta = 0.1;
    private double epsilon = 0.1;
    private double ratio = 0.5;
    private boolean failFirst;
    private int threads = 1;
    private int tracesize;
    private boolean localized;
    private boolean testCasesSem;
    private CSVPrinter output;

    private Main() {
        options = new Options();

        options.addOption(Option.builder(LTS)
                .numberOfArgs(2)
                .argName("original.lts> <mutant.lts|mutantsdirectory/")
                .desc("Original and Mutant LTSs")
                .build());

        options.addOption(Option.builder(DELTA)
                .hasArg()
                .argName("Delta")
                .desc("CONFIG equivalence file (default is " + delta + ")")
                .build());

        options.addOption(Option.builder(EPSILON)
                .hasArg()
                .argName("Epsilon")
                .desc("CONFIG equivalence file (default is " + epsilon + ")")
                .build());

        options.addOption(Option.builder(RATIO)
                .hasArg()
                .argName("Original/Mutant Test case ratio")
                .desc("CONFIG equivalence file (default is " + ratio + ")")
                .build());

        options.addOption(Option.builder(THREADS)
                .hasArg()
                .argName("Max. Number of Threads")
                .desc("CONFIG equivalence file (default is " + threads + ")")
                .build());

        options.addOption(Option.builder(OUT)
                .hasArg()
                .argName("output.csv")
                .desc("Output file")
                .build());

        options.addOption(Option.builder(TRACE_SIZE)
                .hasArg()
                .argName("trace size")
                .desc("The size of the traces to select and execute in the given LTS (default equals the number of states of the original LTS)")
                .build());

        options.addOption(FULL_RUN, "Indicates that the algorithm has to perform a full monte carlo (default will stop as soon as a counter exemple is found)");

        options.addOption(LOCALIZED, "Activate a localized monte carlo based on states differing between original system and mutant");

        options.addOption(TEST_CASE_SEM, "Indicates to select test-cases in the TSs in place of random fixed length traces. "
                + "If this option is specified, option " + TRACE_SIZE + " is the maximal length for the test cases (if specified).");

        options.addOption(DEBUG, "Enables debug messages printing.");

        options.addOption(HELP, false, "Prints this message");
    }

    private void printHelpMessage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(128);
        formatter.printHelp("mutation-equivalence <options>", options);
    }

    private CommandLine initialise(String[] args) throws ParseException, IOException {
        LOG.info("Initilization");
        // Parse line 
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);
        // Check Help message 
        if (line.hasOption(HELP)) {
            printHelpMessage();
            return null;
        }
        // Check debug
        if (line.hasOption(DEBUG)) {
            // This only works with log4j (implem used for logging)
            LogManager.getLogger("be.unamur").setLevel(Level.DEBUG);
        }
        // Check arguments and initialise fields
        if (line.hasOption(LTS)) {
            // Original TS
            original = loadLabelledTransitionSystem(line.getOptionValues(LTS)[0]);
            originalFileName = new File(line.getOptionValues(LTS)[0]).getName();
            // Mutants 
            mutants = new LtsFileIterator(line.getOptionValues(LTS)[1]);
        } else {
            throw new ParseException("Option " + LTS + " is mandatory!");
        }
        // Output file
        if (line.hasOption(OUT)) {
            File outputFile = new File(line.getOptionValue(OUT));
            if (outputFile.exists()) {
                outputFile.delete();
            }
            outputFile.createNewFile();
            output = new CSVPrinter(new FileWriter(outputFile), CSVFormat.DEFAULT);
        } else {
            output = new CSVPrinter(System.out, CSVFormat.DEFAULT);
        }
        // Delta
        if (line.hasOption(DELTA)) {
            delta = Double.parseDouble(line.getOptionValue(DELTA));
        }
        // Epsilon
        if (line.hasOption(EPSILON)) {
            epsilon = Double.parseDouble(line.getOptionValue(EPSILON));
        }
        // ratio
        if (line.hasOption(RATIO)) {
            ratio = Double.parseDouble(line.getOptionValue(RATIO));
        }
        // Threads 
        if (line.hasOption(THREADS)) {
            threads = Integer.parseInt(line.getOptionValue(THREADS));
        }
        // Localized
        localized = line.hasOption(LOCALIZED);
        // Test-case semantic
        testCasesSem = line.hasOption(TEST_CASE_SEM);
        // Fail first
        failFirst = !line.hasOption(FULL_RUN);
        // Trace size
        if (line.hasOption(TRACE_SIZE)) {
            tracesize = Integer.parseInt(line.getOptionValue(TRACE_SIZE));
        } else {
            tracesize = original.numberOfStates() + 1;
        }

        LOG.info("Initilization: done");
        return line;
    }

    private void launch() throws IOException {
        output.printRecord(CSV_HEADER);
        MultiThreadedRandomEquivalence equiv = new MultiThreadedRandomEquivalence();
        equiv.setDelta(delta);
        equiv.setEpsilon(epsilon);
        equiv.setFailFirst(failFirst);
        equiv.setLocalized(localized);
        equiv.setTestCaseSemantic(testCasesSem);
        equiv.setMaxNbThreads(threads);
        equiv.setTracesize(tracesize);
        LOG.info("Estimated number of runs per mutant is {}", equiv.getEstimatedNbrRuns());
        Stopwatch computationWatch = new Stopwatch();
        while (mutants.hasNext()) {
            LabelledTransitionSystem mutant = mutants.next();
            EquivalenceResults equivRes;
            String message = "";
            computationWatch.reset();
            computationWatch.start();
            try {
                equivRes = equiv.computeEquivalence(original, mutant, ratio);
            } catch (CounterExampleFoundException ex) {
                LOG.debug("Counter example found, mutant is not equivalent to original system", ex.getPartialResults());
                equivRes = ex.getPartialResults();
                message = ex.getMessage();
            } catch (ConnectivityHypothesisViolationException ex) {
                LOG.debug("Was unnable to generate counter example", ex.getPartialResults());
                equivRes = ex.getPartialResults();
                message = ex.getMessage();
            }
            computationWatch.stop();
            output.printRecord(originalFileName,
                    mutants.getLastLtsFileName(),
                    failFirst,
                    delta,
                    epsilon,
                    equiv.getEstimatedNbrRuns(),
                    equivRes.getTotalRuns(),
                    equivRes.getFailedRuns(),
                    equivRes.getEquivalenceDegree(),
                    computationWatch.elapsed(TimeUnit.MILLISECONDS),
                    equivRes.counterExamplesToCsvString(),
                    message);
        }
    }

    private void close() {
        if (output != null) {
            try {
                output.flush();
            } catch (IOException ex) {
                LOG.error("Error while finalizing output stream!", ex);
            } finally {
                try {
                    output.close();
                } catch (IOException ex) {
                    LOG.error("Error while closing output stream!", ex);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            Stopwatch globalWatch = new Stopwatch();
            globalWatch.start();
            Main main = new Main();
            try {
                CommandLine line = main.initialise(args);
                if (line != null) {
                    main.launch();
                }
            } catch (ParseException ex) {
                LOG.error("Error while processing command line arguments! ", ex);
                main.printHelpMessage();
            } catch (IOException ex) {
                LOG.error("IO Error while printing results!", ex);
            } catch (Exception ex) {
                LOG.error("Exception occured during computation!", ex);
            } finally {
                main.close();
            }
            globalWatch.stop();
            LOG.info("Total computation time is {} ms", globalWatch.elapsed(TimeUnit.MILLISECONDS));
        } finally {
            System.exit(0);
        }
    }

}
