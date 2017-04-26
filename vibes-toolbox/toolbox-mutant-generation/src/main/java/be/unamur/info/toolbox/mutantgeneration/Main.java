package be.unamur.info.toolbox.mutantgeneration;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.TransitionSystem;
import static be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders.loadLabelledTransitionSystem;
import static be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders.loadFeaturedTransitionSystem;
import be.unamur.transitionsystem.dsl.test.mutation.ConfiguredMutagen;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for mutation toolbox using VIBeS's DSL to perform mutation on LTS
 * or FTS model.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final String NAME = "toolbox-mutant-generation";

    private static final String HELP = "help";

    private static final String LTS = "lts";
    private static final String FTS = "fts";
    private static final String MUT_CONFIG = "config";

    private static final String FMM_OUT = "fmm";
    private static final String MUT_DIR_OUT = "outdir";

    private final Options options;

    private TransitionSystem original;
    private File config;

    private File outputDir;
    private File tvlFile;
    private File mutantsFtsFile;

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

        options.addOption(Option.builder(FMM_OUT)
                .desc("Specify the output files for the FMM.")
                .argName("mutants.fts> <mutants.tvl")
                .numberOfArgs(2)
                .build());

        options.addOption(Option.builder(MUT_DIR_OUT)
                .desc("Specify the output directory where mutants will be written."
                        + " The directory is created/cleared before execution.")
                .hasArg()
                .argName("path")
                .build());
    }

    private void printHelpMessage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(128);
        formatter.printHelp(String.format("java -jar %s.jar -%s | ((-%s | -%s) -%s (-%s)? (-%s)?)",
                NAME, HELP, LTS, FTS, MUT_CONFIG, FMM_OUT, MUT_DIR_OUT), options);
    }

    private void initialise(CommandLine line) throws Exception {
        LOG.info("Initilization");
        if (line.hasOption(LTS)) {
            original = loadLabelledTransitionSystem(line.getOptionValue(LTS));
        } else if (line.hasOption(FTS)) {
            original = loadFeaturedTransitionSystem(line.getOptionValue(FTS));
        } else {
            LOG.error("Error while parsing command line: input original LTS or FTS model is mandatory!");
            printHelpMessage();
            System.exit(1);
        }
        if (line.hasOption(MUT_CONFIG)) {
            config = new File(line.getOptionValue(MUT_CONFIG));
            if (!config.isFile()) {
                LOG.error("Error while parsing command line: specified mutation configuration file is not a file!");
                printHelpMessage();
                System.exit(1);
            }
        } else {
            LOG.error("Error while parsing command line: input mutation configuration file is mandatory!");
            printHelpMessage();
            System.exit(1);
        }

        if (line.hasOption(MUT_DIR_OUT)) {
            outputDir = new File(line.getOptionValue(MUT_DIR_OUT));
            outputDir.mkdirs();
            for (File f : outputDir.listFiles()) {
                f.delete();
            }
        }

        if (line.hasOption(FMM_OUT)) {
            String[] files = line.getOptionValues(FMM_OUT);
            mutantsFtsFile = new File(files[0]);
            if (mutantsFtsFile.exists()) {
                mutantsFtsFile.delete();
            }
            mutantsFtsFile.createNewFile();
            tvlFile = new File(files[1]);
            if (tvlFile.exists()) {
                tvlFile.delete();
            }
            tvlFile.createNewFile();
        }

        LOG.info("Initilization: done");
    }

    private void launch(CommandLine line) throws Exception {
        ConfiguredMutagen configMut = ConfiguredMutagen.configure(config);
        if (line.hasOption(FMM_OUT)) {
            configMut = configMut.ftsMutant(mutantsFtsFile)
                    .tvlMutant(tvlFile);
        }
        if (line.hasOption(MUT_DIR_OUT)) {
            configMut = configMut.outputDir(outputDir);
        }
        if (line.hasOption(LTS)) {
            configMut.mutate((LabelledTransitionSystem) original);
        } else if (line.hasOption(FTS)) {
            configMut.mutate((FeaturedTransitionSystem) original);
        }
    }

    public static void main(String[] args) throws Exception {
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
        main.launch(line);
    }

}
