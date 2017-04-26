package be.unamur.transitionsystem.transformation.main;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.transformation.dot.FtsDotPrinter;
import be.unamur.transitionsystem.transformation.dot.LtsDotPrinter;
import be.unamur.transitionsystem.transformation.dot.UsageModelDotPrinter;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.commons.cli.Option;

/**
 * Transforms the input TS to DOT format.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class DOTTransformator implements Transformator {

    private static final String OPTION_NAME = "dot";

    public static final DOTTransformator DOT = new DOTTransformator();

    private DOTTransformator() {
    }

    @Override
    public Option getCommandLineOption() {
        return Option.builder(OPTION_NAME)
                .desc("Transforms input TS to dot (.dot) file")
                .build();
    }

    @Override
    public void transform(LabelledTransitionSystem lts, OutputStream out, String... cmdArgs) throws IOException {
        PrintStream print = new PrintStream(out);
        new LtsDotPrinter(print, lts)
                .printDot();
        print.flush();
    }

    @Override
    public void transform(FeaturedTransitionSystem fts, OutputStream out, String... cmdArgs) throws IOException {
        PrintStream print = new PrintStream(out);
        new FtsDotPrinter(print, fts)
                .printDot();
        print.flush();
    }

    @Override
    public void transform(UsageModel um, OutputStream out, String... cmdArgs) throws IOException {
        PrintStream print = new PrintStream(out);
        new UsageModelDotPrinter(print, um)
                .printDot();
        print.flush();
    }

}
