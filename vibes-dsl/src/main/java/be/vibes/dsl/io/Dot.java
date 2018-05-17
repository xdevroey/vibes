package be.vibes.dsl.io;

import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.UsageModel;
import be.vibes.ts.io.dot.FeaturedTransitionSystemDotPrinter;
import be.vibes.ts.io.dot.TransitionSystemDotPrinter;
import be.vibes.ts.io.dot.UsageModelDotPrinter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Prints Models in Dot format. To be used with Graphviz tools.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Dot {

    public static String format(FeaturedTransitionSystem fts) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FeaturedTransitionSystemDotPrinter printer = new FeaturedTransitionSystemDotPrinter(fts, new PrintStream(out));
        printer.printDot();
        printer.flush();
        return out.toString();
    }

    public static String format(TransitionSystem lts) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransitionSystemDotPrinter printer = new TransitionSystemDotPrinter(lts, new PrintStream(out));
        printer.printDot();
        printer.flush();
        return out.toString();
    }

    public static String format(UsageModel um) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UsageModelDotPrinter printer = new UsageModelDotPrinter(um, new PrintStream(out));
        printer.printDot();
        printer.flush();
        return out.toString();
    }

}
