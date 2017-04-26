package be.unamur.info.toolbox.mutant.sampling;

import be.unamur.fts.fexpression.Feature;
import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.mutation.FeaturedMutantsModel;
import be.unamur.transitionsystem.test.mutation.MutationConfiguration;
import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import java.util.Arrays;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import static be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders.loadFeaturedTransitionSystem;
import static be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders.loadLabelledTransitionSystem;
import static be.unamur.transitionsystem.dsl.TransitionSystemXmlPrinter.print;
import static com.google.common.base.Preconditions.checkArgument;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Main {

    public static final int[] DEFAULT_NBR_MUTANTS_PER_ORDER = new int[]{-1, 100, 100, 100};
    public static final int[] DEFAULT_ORDERS = new int[]{1, 2, 5, 10};

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static final String NAME = "toolbox-mutant-sampling";

    private static final String HELP = "help";
    private static final String LTS = "lts";
    private static final String FTS = "fts";
    private static final String MUT_CONFIG = "config";
    private static final String MUT_DIR_OUT = "outdir";
    private static final String ORDERS = "orders";
    private static final String MUTANTS_PER_ORDER = "mcount";

    private final Options options;

    private FeaturedMutantsModel fmm;

    private File outputDir;

    private int[] orders = DEFAULT_ORDERS;
    private int[] nbPerOrder = DEFAULT_NBR_MUTANTS_PER_ORDER;

    public Main() {
        options = new Options();
        options.addOption(Option.builder(HELP)
                .desc("Prints this help message.")
                .build());

        options.addOption(Option.builder(LTS)
                .desc("Specify the original LTS model to use.")
                .argName("model.lts")
                .hasArg()
                .build());

        options.addOption(Option.builder(FTS)
                .desc("Specify the original FTS model to use.")
                .argName("model.lts")
                .hasArg()
                .build());

        options.addOption(Option.builder(MUT_CONFIG)
                .desc("Specify the mutation configuration to use.")
                .argName("config.xml")
                .hasArg()
                .build());

        options.addOption(Option.builder(MUT_DIR_OUT)
                .desc("Specify the output directory where mutants will be written."
                        + " The directory is created/cleared before execution.")
                .hasArg()
                .argName("path")
                .build());

        options.addOption(Option.builder(ORDERS)
                .desc("The orders for which mutants have to be generated (as a csv list), default is: "
                        + Arrays.stream(DEFAULT_ORDERS).mapToObj(x -> String.valueOf(x)).reduce("", (x, y) -> (x.length() > 0 ? x + "," + y : y)))
                .hasArg()
                .argName("orders")
                .build());

        options.addOption(Option.builder(MUTANTS_PER_ORDER)
                .desc("The number of mutants to select per order (as a csv list), default is "
                        + Arrays.stream(DEFAULT_NBR_MUTANTS_PER_ORDER).mapToObj(x -> String.valueOf(x)).reduce("", (x, y) -> (x.length() > 0 ? x + "," + y : y)))
                .hasArg()
                .argName("count")
                .build());
    }

    private void printHelpMessage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(128);
        formatter.printHelp(String.format("java -jar %s.jar -%s | ((-%s | -%s) -%s -%s (-%s)? (-%s)?)",
                NAME, HELP, LTS, FTS, MUT_CONFIG, MUT_DIR_OUT, ORDERS, MUTANTS_PER_ORDER), options);
    }

    private void initialise(CommandLine line) throws Exception {
        LOG.info("Initilization");
        TransitionSystem original = null;
        if (line.hasOption(LTS)) {
            original = loadLabelledTransitionSystem(line.getOptionValue(LTS));
        } else if (line.hasOption(FTS)) {
            original = loadFeaturedTransitionSystem(line.getOptionValue(FTS));
        } else {
            LOG.error("Error while parsing command line: input original LTS or FTS model is mandatory!");
            printHelpMessage();
            System.exit(-1);
        }
        fmm = new FeaturedMutantsModel(original);
        LOG.info("Original TS loaded");

        MutationConfiguration config = null;
        if (line.hasOption(MUT_CONFIG)) {
            File configFile = new File(line.getOptionValue(MUT_CONFIG));
            if (!configFile.isFile()) {
                LOG.error("Error while parsing command line: specified mutation configuration file is not a file!");
                printHelpMessage();
                System.exit(1);
            }
            config = new MutationConfiguration(configFile);
        } else {
            LOG.error("Error while parsing command line: input mutation configuration file is mandatory!");
            printHelpMessage();
            System.exit(1);
        }
        fmm.mutate(config);
        LOG.info("Mutation applied on the original TS, will start mutants selection");

        if (line.hasOption(MUT_DIR_OUT)) {
            outputDir = new File(line.getOptionValue(MUT_DIR_OUT));
            outputDir.mkdirs();
            for (File f : outputDir.listFiles()) {
                f.delete();
            }
        } else {
            LOG.error("Error while parsing command line: input mutation configuration file is mandatory!");
            printHelpMessage();
            System.exit(1);
        }
        LOG.info("Output directory cleanded");

        if (line.hasOption(ORDERS)) {
            orders = Arrays.stream(line.getOptionValue(ORDERS).split(",")).mapToInt(x -> Integer.parseInt(x)).toArray();
        }
        if (line.hasOption(MUTANTS_PER_ORDER)) {
            nbPerOrder = Arrays.stream(line.getOptionValue(MUTANTS_PER_ORDER).split(",")).mapToInt(x -> Integer.parseInt(x)).toArray();
        }
        LOG.info("Generating {} mutants for orders {}", nbPerOrder, orders);
        checkArgument(orders.length == nbPerOrder.length, "Parameters %s and % must have the same number of values!", ORDERS, MUTANTS_PER_ORDER);
        LOG.info("Initilization: done");
    }

    private void process() throws Exception {
        for (int i = 0; i < orders.length; i++) {
            int nbMutants = nbPerOrder[i];
            if (nbMutants > fmm.possibleMutantsCount(orders[i])) {
                nbMutants = -1; //If the number of requested mutants is higher than the number of possible mutants, print all mutants
            }
            LOG.info("Printing {} mutants order {}", (nbMutants < 0 ? "all" : "" + nbMutants), orders[i]);
            File orderDir = new File(outputDir, "order" + orders[i]);
            clearDir(orderDir);
            if (nbMutants < 0) {
                printAllMutants(orders[i], orderDir);
            } else {
                printMutants(orders[i], nbMutants, orderDir);
            }
            LOG.info("Printing TVL order {}", orders[i]);
            printTvl(orders[i], new File(outputDir, "order" + orders[i] + ".tvl"));
        }
        LOG.info("Printing FMM");
        print(fmm.getFts(), new File(outputDir, "fmm.fts"));
        fmm.printTvl(new PrintStream(new File(outputDir, "fmm.tvl")));
    }

    private void printTvl(int order, File output) throws Exception {
        clearFile(output);
        try (PrintStream out = new PrintStream(output)) {
            fmm.printTvl(order, out);
        }
    }

    private void printAllMutants(int order, File outputDir) throws Exception {
        Iterator<Configuration> configsIt = fmm.getMutantsConfigs(order);
        while (configsIt.hasNext()) {
            Configuration config = configsIt.next();
            printMutant(config, outputDir);
        }
    }

    private void printMutants(int order, int nbrMutants, File outputDir) throws Exception {
        double nbrPossibleMutants = fmm.possibleMutantsCount(order);
        checkArgument(nbrMutants < nbrPossibleMutants, "Number of mutants to print must be < number of possible mutants!");
        // Get random Configurations
        List<Configuration> mutants = fmm.getRandomMutantConfigurations(order, nbrMutants);
        // Print mutants 
        for (Configuration config : mutants) {
            printMutant(config, outputDir);
        }
    }

    private void printMutant(Configuration config, File outputDir) {
        LabelledTransitionSystem mutant = fmm.getMutant(config);
        String fileName = getFileName(config.getFeatures());
        File output = new File(outputDir, fileName + ".ts");
        print(mutant, output);
    }

    private String getFileName(Feature[] config) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < config.length; i++) {
            builder.append(config[i].getName());
            if (i < config.length - 1) {
                builder.append('-');
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) throws Exception {
        LOG.info("Starging");
        Main main = new Main();
        CommandLineParser parser = new DefaultParser();
        CommandLine line = null;
        try {
            line = parser.parse(main.options, args);
        } catch (ParseException ex) {
            LOG.error("Error while parsing command line!", ex);
            main.printHelpMessage();
            System.exit(1);
        }
        if (line.hasOption(HELP)) {
            main.printHelpMessage();
            System.exit(0);
        }
        main.initialise(line);
        main.process();
        LOG.info("Done");
    }

    private static void clearFile(File file) throws Exception {
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

    private static void clearDir(File dir) throws Exception {
        if (dir.exists() && dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                }
            }
        } else {
            dir.mkdir();
        }
    }

}
