package be.unamur.transitionsystem.transformation.main;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.cli.Option;

/**
 * This interface defines a transformator used to the toolbox-transofrmation
 * command line utility.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public interface Transformator {

    /**
     * Returns the command line option that triggers this transformation.
     *
     * @return The command line option that triggers this transformation.
     */
    public Option getCommandLineOption();

    /**
     * Triggers the transformation for the given LTS.
     *
     * @param lts The input LTS.
     * @param out The output stream to print the result of the transformation.
     * @param cmdArgs Additional arguments passed to the option in the command
     * line.
     */
    public void transform(LabelledTransitionSystem lts, OutputStream out, String... cmdArgs) throws IOException;

    /**
     * Triggers the transformation for the given FTS.
     *
     * @param fts The input FTS.
     * @param out The output stream to print the result of the transformation.
     * @param cmdArgs Additional arguments passed to the option in the command
     * line.
     */
    public void transform(FeaturedTransitionSystem fts, OutputStream out, String... cmdArgs) throws IOException;

    /**
     * Triggers the transformation for the given usage model.
     *
     * @param um The input usage model.
     * @param out The output stream to print the result of the transformation.
     * @param cmdArgs Additional arguments passed to the option in the command
     * line.
     */
    public void transform(UsageModel um, OutputStream out, String... cmdArgs) throws IOException;

}
