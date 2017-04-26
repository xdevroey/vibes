package be.unamur.transitionsystem.transformation.main;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.transformation.aut.Lts2AutPrinter;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.commons.cli.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transforms input to ALDEBERAN format (CADP).
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class AldebaranTransformator implements Transformator {

    private static final Logger LOG = LoggerFactory.getLogger(AldebaranTransformator.class);

    public static final String OPTION_NAME = "aldebaran";

    public static final AldebaranTransformator ALDEBARAN = new AldebaranTransformator();

    private AldebaranTransformator() {
    }

    @Override
    public Option getCommandLineOption() {
        return Option.builder(OPTION_NAME)
                .desc("Prints the given TS to ALDEBARAN (CADP) format and prints "
                        + "the mapping file to the given file (or standard "
                        + "output if no file name is provided).")
                .hasArg()
                .optionalArg(true)
                .argName("mapping")
                .build();
    }

    @Override
    public void transform(LabelledTransitionSystem lts, OutputStream out, String... cmdArgs) throws IOException {
        PrintStream output = new PrintStream(out);
        Lts2AutPrinter printer = new Lts2AutPrinter(output, lts);
        printer.printAut();
        if (cmdArgs.length > 0) {
            File mapping = new File(cmdArgs[0]);
            LOG.info("Will print mapping file to {}", mapping);
            if (mapping.exists()) {
                mapping.delete();
            }
            mapping.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(mapping)) {
                printer.printMapping(fos);
            }
        } else {
            printer.printMapping(System.out);
        }
    }

    @Override
    public void transform(FeaturedTransitionSystem fts, OutputStream out, String... cmdArgs) throws IOException {
        throw new UnsupportedOperationException("ALDEBERAN format does not support featured transition systems!");
    }

    @Override
    public void transform(UsageModel um, OutputStream out, String... cmdArgs) throws IOException {
        throw new UnsupportedOperationException("ALDEBERAN format does not support featured transition systems!");
    }

}
