package be.unamur.info.toolbox.testcasegeneration;

import be.unamur.fts.solver.SolverFacade;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders.*;
import static be.unamur.fts.fexpression.DimacsModel.*;
import static be.unamur.transitionsystem.dsl.selection.AllActions.*;
import static be.unamur.transitionsystem.dsl.selection.AllStates.*;
import static be.unamur.transitionsystem.dsl.selection.Random.*;
import static be.unamur.transitionsystem.dsl.test.TestCaseXmlPrinter.*;
import be.unamur.fts.solver.Sat4JSolverFacade;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import com.google.common.base.Preconditions;
import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final String NAME = "toolbox-testcase-generation";

    private static final String HELP = "help";

    private static final String FTS = "fts";
    private static final String LTS = "lts";
    private static final String UM = "um";

    private static final String ALL_ACTIONS = "allactions";
    private static final String ALL_STATES = "allstates";
    private static final String RANDOM = "random";
    private static final String DISSIMILAR = "dissimilar";

    private static final String OUTPUT = "o";

    private Options options;

    private SolverFacade solver;
    private LabelledTransitionSystem lts;
    private UsageModel um;
    private FeaturedTransitionSystem fts;

    private PrintStream out = System.out;

    public Main() {
        options = new Options();
        options.addOption(Option.builder(HELP)
                .desc("Prints this help message.")
                .build());

        options.addOption(Option.builder(FTS)
                .desc("Specify the FTS model to use. Second argument is the "
                        + "variability model defining valid product "
                        + "configurations in DIMACS format with feature mappign "
                        + "specified as comments at the begining of the file.")
                .argName("model.fts> <model.dimacs")
                .numberOfArgs(2)
                .build());
        options.addOption(Option.builder(LTS)
                .desc("Specify the LTS model to use.")
                .argName("model.lts")
                .hasArg()
                .build());
        options.addOption(Option.builder(UM)
                .desc("Specify the usage model model to use.")
                .argName("model.um")
                .hasArg()
                .build());

        options.addOption(Option.builder(ALL_ACTIONS)
                .desc("Generates test cases with all actions coverage from the"
                        + " given model.")
                .build());
        options.addOption(Option.builder(ALL_STATES)
                .desc("Generates test cases with all states coverage from the"
                        + " given model.")
                .build());
        options.addOption(Option.builder(RANDOM)
                .desc("Generates <nbtestcases> (unique) random test cases from "
                        + "the given model.")
                .argName("nbtestcases")
                .hasArg()
                .type(Integer.class)
                .build());
        options.addOption(Option.builder(DISSIMILAR)
                .desc("Generates dissimilar test cases using a local or global "
                        + "dissimilarity policy with the given dissimilarity"
                        + " function.")
                .argName("local|global> <function")
                .numberOfArgs(2)
                .build());

        options.addOption(Option.builder(OUTPUT)
                .desc("Specify the output file for the generated test cases."
                        + " If this option is not provided, test cases are"
                        + " printed on System.out")
                .hasArg()
                .argName("file.tc")
                .build());

    }

    private void printHelpMessage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(128);
        formatter.printHelp(String.format("java -jar %s.jar -%s | ((-%s | -%s | -%s) (-%s | -%s | -%s | -%s) (-%s)?)  ",
                NAME, HELP, LTS, FTS, UM, ALL_ACTIONS, ALL_STATES, RANDOM, DISSIMILAR, OUTPUT), options);
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
        main.lauchGeneration(line);
    }

    private void initialise(CommandLine line) throws Exception {
        logger.info("Initilization");
        if (line.hasOption(FTS)) {
            String[] files = line.getOptionValues(FTS);
            fts = loadFeaturedTransitionSystem(files[0]);
            solver = new Sat4JSolverFacade(createFromDimacsFile(files[1]));
            Preconditions.checkState(solver.isSatisfiable(), "Given FD is not SAT!");
        } else if (line.hasOption(LTS)) {
            lts = loadLabelledTransitionSystem(line.getOptionValue(LTS));
        } else if (line.hasOption(UM)) {
            um = loadUsageModel(line.getOptionValue(UM));
        } else {
            logger.error("Error while parsing command line: input model is mandatory!");
            printHelpMessage();
            System.exit(1);
        }
        if (line.hasOption(OUTPUT)) {
            File outputFile = new File(line.getOptionValue(OUTPUT));
            if (! outputFile.exists()) {
                outputFile.createNewFile();
            }
            out = new PrintStream(outputFile);
        }
        logger.info("Initilization: done");
    }

    private void lauchGeneration(CommandLine line) {
        logger.info("Test cases selection");
        TestSet set = null;
        if (line.hasOption(ALL_ACTIONS)) {
            if (lts != null) {
                set = allActionsSelection(lts);
            } else if (fts != null) {
                set = allActionsSelection(fts, solver);
            } else if (um != null) {
                set = allActionsSelection(um);
            } else {
                throw new RuntimeException("No suitable input model initialised!");
            }
        } else if (line.hasOption(ALL_STATES)) {
            if (lts != null) {
                set = allStatesSelection(lts);
            } else if (fts != null) {
                set = allStatesSelection(fts, solver);
            } else if (um != null) {
                set = allStatesSelection(um);
            } else {
                throw new RuntimeException("No suitable input model initialised!");
            }
        } else if (line.hasOption(RANDOM)) {
            int nbrTests = Integer.parseInt(line.getOptionValue(RANDOM));
            if (lts != null) {
                set = randomSelection(lts, nbrTests);
            } else if (fts != null) {
                set = randomSelection(fts, solver, nbrTests);
            } else if (um != null) {
                set = randomSelection(um, nbrTests);
            } else {
                throw new RuntimeException("No suitable input model initialised!");
            }
        } else if (line.hasOption(DISSIMILAR)) {
            //TODO Finish the implementation of diissimilar selection
            throw new UnsupportedOperationException("Not yet implemented!");
        } else {
            logger.error("Error while parsing command line: generation method is mandatory!");
            printHelpMessage();
            System.exit(1);
        }
        logger.info("Test cases selection: done");
        logger.info("Printing test set");
        print(set, out);
        logger.info("Printing test set: done");
    }

}
