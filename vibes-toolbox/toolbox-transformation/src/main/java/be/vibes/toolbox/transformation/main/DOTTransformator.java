package be.vibes.toolbox.transformation.main;

import be.vibes.dsl.io.Dot;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.UsageModel;
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

    public static final String OPTION_NAME = "dot";

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
    public void transform(TransitionSystem lts, OutputStream out, String... cmdArgs) throws IOException {
        PrintStream print = new PrintStream(out);
        print.print(Dot.format(lts));
        print.flush();
    }

    @Override
    public void transform(FeaturedTransitionSystem fts, OutputStream out, String... cmdArgs) throws IOException {
        PrintStream print = new PrintStream(out);
        print.print(Dot.format(fts));
        print.flush();
    }

    @Override
    public void transform(UsageModel um, OutputStream out, String... cmdArgs) throws IOException {
        PrintStream print = new PrintStream(out);
        print.print(Dot.format(um));
        print.flush();
    }

}
