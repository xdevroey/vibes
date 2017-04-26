package be.unamur.info.toolbox.fmmexecution;

import be.unamur.fts.fexpression.FExpression;
import static be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders.loadFeaturedTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import java.io.PrintStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static be.unamur.transitionsystem.dsl.test.TestCaseXmlLoader.*;
import static be.unamur.transitionsystem.dsl.test.mutation.FeaturedMutantsModels.*;
import be.unamur.transitionsystem.test.TestCase;
import java.io.File;
import java.util.Iterator;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final String NAME = "toolbox-fmm-execution";

    private static final String HELP = "help";
    private static final String FTS = "fts";
    private static final String TEST_CASES = "testcases";
    private static final String OUTPUT = "out";

    private Options options;

    private FeaturedTransitionSystem fts;
    private String originalInitialStateName;
    private Iterator<TestCase> testcases;

    private PrintStream out = System.out;

    public Main() {
        options = new Options();
        options.addOption(Option.builder(HELP)
                .desc("Prints this help message.")
                .build());

        options.addOption(Option.builder(FTS)
                .desc("Specify the FTS model to use.")
                .argName("model.fts")
                .numberOfArgs(1)
                .build());

        options.addOption(Option.builder(TEST_CASES)
                .desc("Specify the test cases to execute.")
                .argName("testcases")
                .numberOfArgs(1)
                .build());

        options.addOption(Option.builder(OUTPUT)
                .desc("Specify the output file for the results of the execution."
                        + " If this option is not provided, results are"
                        + " printed on System.out")
                .hasArg()
                .argName("results.csv")
                .build());

    }

    private void printHelpMessage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(128);
        formatter.printHelp(String.format("java -jar %s.jar -%s | (-%s  -%s (-%s)?)",
                NAME, HELP, FTS, TEST_CASES, OUTPUT), options);
    }

    private void initialise(CommandLine line) throws Exception {
        logger.info("Initilization");
        if (line.hasOption(FTS)) {
            String file = line.getOptionValue(FTS);
            fts = loadFeaturedTransitionSystem(file);
            originalInitialStateName = getOriginalInitialState(fts).getName();
        } else {
            logger.error("Error while parsing command line: input model is mandatory!");
            printHelpMessage();
            System.exit(1);
        }
        if (line.hasOption(TEST_CASES)) {
            String file = line.getOptionValue(TEST_CASES);
            testcases = loadLtsTestCases(file);
        } else {
            logger.error("Error while parsing command line: test cases file is mandatory!");
            printHelpMessage();
            System.exit(1);
        }

        if (line.hasOption(OUTPUT)) {
            File outputFile = new File(line.getOptionValue(OUTPUT));
            if (outputFile.exists()) {
                outputFile.delete();
            }
            outputFile.createNewFile();
            out = new PrintStream(outputFile);
        }
        logger.info("Initilization: done");
    }

    private void lauch(CommandLine line) throws Exception {
        logger.info("Test cases execution");
        printHeader();
        TestCase testCase;
        while(testcases.hasNext()){
            testCase = testcases.next();
            logger.info("Execution of test {}", testCase.getId());
            FExpression alive = getAliveMutants(testCase, fts, originalInitialStateName);
            printExecutionLine(testCase, alive);
            logger.info("Execution of test {}: done", testCase.getId());
            System.gc();
        }
        logger.info("Test cases execution: done");
    }

    private void printHeader() {
        out.println("testcase;alive");
    }

    private void printExecutionLine(TestCase testCase, FExpression alive) {
        String tc = testCase.getId();
        out.println(tc + ";" + alive.toString());
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        CommandLineParser parser = new DefaultParser();
        CommandLine line = null;
        try {
            line = parser.parse(main.options, args);
        } catch (ParseException ex) {
            logger.error("Error while parsing command line!", ex);
            main.printHelpMessage();
            System.exit(1);
        }

        if (line.hasOption(HELP)) {
            main.printHelpMessage();
            System.exit(0);
        }
        main.initialise(line);
        main.lauch(line);
    }

}
