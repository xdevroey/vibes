package be.unamur.transitionsystem.transformation.main;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.transformation.ba.LtsBaPrinter;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.commons.cli.Option;

/**
 * Transforms the input TS to Buchi Automaton.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class BuchiAuromatonTransformator implements Transformator {

    private static final String OPTION_NAME = "buchi";

    public static final BuchiAuromatonTransformator BUCHI = new BuchiAuromatonTransformator();

    private BuchiAuromatonTransformator() {
    }

    @Override
    public Option getCommandLineOption() {
        return Option.builder(OPTION_NAME)
                .desc("Transform input to Buchi automaton (.ba).")
                .build();
    }

    @Override
    public void transform(LabelledTransitionSystem lts, OutputStream out, String... cmdArgs) throws IOException {
        PrintStream output = new PrintStream(out);
        new LtsBaPrinter(output, lts).printBa();
        output.flush();
    }

    @Override
    public void transform(FeaturedTransitionSystem fts, OutputStream out, String... cmdArgs) throws IOException {
        throw new UnsupportedOperationException("Buchi automaton does not support featured transition systems!");
    }

    @Override
    public void transform(UsageModel um, OutputStream out, String... cmdArgs) throws IOException {
        throw new UnsupportedOperationException("Buchi automaton does not support usage models!");
    }

}
