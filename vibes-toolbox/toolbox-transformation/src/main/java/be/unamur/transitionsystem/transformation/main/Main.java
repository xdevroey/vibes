package be.unamur.transitionsystem.transformation.main;

/*
 * #%L
 * vibes-transformation
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import static be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders.*;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final String HELP = "help";
    private static final String FTS = "fts";
    private static final String LTS = "lts";
    private static final String USAGE_MODEL = "um";
    private static final String OUTPUT = "out";

    private Options options;

    private Map<String, Transformator> transformators;

    Main() {
        options = new Options();
        options.addOption(HELP, "Prints this help message.");
        options.addOption(FTS, true, "FTS input file.");
        options.addOption(LTS, true, "LTS input file.");
        options.addOption(USAGE_MODEL, true, "Usage model input file.");
        options.addOption(OUTPUT, true, "Output file (will use standard output if no output file is provided).");
        transformators = Maps.newHashMap();
        addOption(AldebaranTransformator.ALDEBARAN);
        addOption(BuchiAuromatonTransformator.BUCHI);
        addOption(DOTTransformator.DOT);
        addOption(FPromelaTransformator.FPROMELA);
        addOption(SiberiaTransformator.SIBERIA);
        addOption(TimbukTransformator.TIMBUK);
        addOption(BFSHeightSlicerTransformator.BFS_SLICER);
    }

    private void addOption(Transformator transfo) {
        Option opt = transfo.getCommandLineOption();
        checkState(!transformators.containsKey(opt.getOpt()),
                "Option %s already defined, cannot add transformator %s!", opt.getOpt(), transfo.getClass());
        transformators.put(opt.getOpt(), transfo);
        options.addOption(opt);
    }

    private void printHelpMessage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(128);
        formatter.printHelp(String.format("toolbox-transformation -(%s|%s|%s) <input-file> <options> (-%s <output>)?", LTS, FTS, USAGE_MODEL, OUTPUT), options);
    }

    void execute(String[] args) throws IOException, ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine line = parser.parse(options, args);
        // Check if help option has been requested 
        if (line.hasOption(HELP)) {
            printHelpMessage();
            return;
        }
        // Check if an input has been provided
        checkArgument(line.hasOption(LTS) || line.hasOption(FTS) || line.hasOption(USAGE_MODEL), "No input file provided (use '-help' to see usage)!");
        // Initialise input
        FeaturedTransitionSystem fts = line.hasOption(FTS) ? loadFeaturedTransitionSystem(line.getOptionValue(FTS)) : null;
        LabelledTransitionSystem lts = line.hasOption(LTS) ? loadLabelledTransitionSystem(line.getOptionValue(LTS)) : null;
        UsageModel um = line.hasOption(USAGE_MODEL) ? loadUsageModel(line.getOptionValue(USAGE_MODEL)) : null;
        // Initialise output 
        PrintStream output;
        if (line.hasOption(OUTPUT)) {
            File out = new File(line.getOptionValue(OUTPUT));
            if (out.exists()) {
                out.delete();
            }
            out.createNewFile();
            output = new PrintStream(out);
        } else {
            output = System.out;
        }
        // Perform the transformations
        for (Option opt : line.getOptions()) {
            Transformator transfo = transformators.get(opt.getOpt());
            if (transfo != null) {
                if (lts != null) {
                    transfo.transform(lts, output, line.getOptionValues(opt.getOpt()));
                } else if (fts != null) {
                    transfo.transform(fts, output, line.getOptionValues(opt.getOpt()));
                } else if (um != null) {
                    transfo.transform(um, output, line.getOptionValues(opt.getOpt()));
                } else {
                    throw new IllegalStateException("No input file provided (use '-help' to see usage)!");
                }
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.execute(args);
        } catch (IOException ex) {
            LOG.error("IOException occured during transformation!", ex);
        } catch (ParseException ex) {
            LOG.error("Could not parse command line arguments (use '-help' to see usage)!", ex);
        }
        System.exit(0);
    }

}
