package be.vibes.dsl.ts;

import be.vibes.dsl.exception.TransitionSystemIOException;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.UsageModel;
import be.vibes.ts.exception.TransitionSystemDefinitionException;
import be.vibes.ts.io.xml.XmlSavers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * This class allows to print transitions systems in XML format.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class TransitionSystemXmlPrinter {

    /**
     * Prints the given TS on the given output in XML format.
     *
     * @param ts The TS to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(TransitionSystem ts, OutputStream out) {
        try {
            XmlSavers.save(ts, out);
        } catch (TransitionSystemDefinitionException e) {
            throw new TransitionSystemIOException("Exception while printing XML!", e);
        }
    }

    /**
     * Prints the given TS on the given output in XML format.
     *
     * @param ts The TS to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(TransitionSystem ts, File out) {
        try {
            print(ts, new FileOutputStream(out));
        } catch (FileNotFoundException e) {
            throw new TransitionSystemIOException("Output file not found!", e);
        }
    }

    /**
     * Prints the given TS on the given output in XML format.
     *
     * @param ts The TS to print.
     * @param outputFileName The name of the file in which to print the TS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(TransitionSystem ts, String outputFileName) {
        print(ts, new File(outputFileName));
    }

    /**
     * Prints the given FTS on the given output in XML format.
     *
     * @param fts The FTS to print.
     * @param out The output on which to print the FTS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(FeaturedTransitionSystem fts, OutputStream out) {
        try {
            XmlSavers.save(fts, out);
        } catch (TransitionSystemDefinitionException e) {
            throw new TransitionSystemIOException("Exception while printing XML!", e);
        }
    }

    /**
     * Prints the given FTS on the given output in XML format.
     *
     * @param fts The FTS to print.
     * @param out The output on which to print the FTS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(FeaturedTransitionSystem fts, File out) {
        try {
            print(fts, new FileOutputStream(out));
        } catch (FileNotFoundException e) {
            throw new TransitionSystemIOException("Output file not found!", e);
        }
    }

    /**
     * Prints the given FTS on the given output in XML format.
     *
     * @param fts The FTS to print.
     * @param outputFileName The name of the file in which to print the FTS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(FeaturedTransitionSystem fts, String outputFileName) {
        print(fts, new File(outputFileName));
    }

    /**
     * Prints the given usage model on the given output in XML format.
     *
     * @param usageModel The usage model to print.
     * @param out The output on which to print the usage model.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(UsageModel usageModel, OutputStream out) {
        try {
            XmlSavers.save(usageModel, out);
        } catch (TransitionSystemDefinitionException e) {
            throw new TransitionSystemIOException("Exception while printing XML!", e);
        }
    }

    /**
     * Prints the given usage model on the given output in XML format.
     *
     * @param usageModel The usage model to print.
     * @param out The output on which to print the usage model.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(UsageModel usageModel, File out) {
        try {
            print(usageModel, new FileOutputStream(out));
        } catch (FileNotFoundException e) {
            throw new TransitionSystemIOException("Output file not found!", e);
        }
    }

    /**
     * Prints the given usage model on the given output in XML format.
     *
     * @param usageModel The usage model to print.
     * @param outputFileName The name of the file in which to print the usage
     * model.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(UsageModel usageModel, String outputFileName) {
        print(usageModel, new File(outputFileName));
    }

}
