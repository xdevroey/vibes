package be.vibes.ts.io.xml;

import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TestSet;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.UsageModel;
import be.vibes.ts.exception.TransitionSystemDefinitionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLStreamException;

/**
 * This class allows to print transitions systems in XML format.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class XmlSavers {

    /**
     * Prints the given TS on the given output in XML format.
     *
     * @param ts The TS to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemDefinitionException If an error occurs during the
     * printing.
     */
    public static void save(TransitionSystem ts, OutputStream out) throws TransitionSystemDefinitionException {
        TransitionSystemPrinter printer = new TransitionSystemPrinter();
        TransitionSystemXmlPrinter xmlOut = new TransitionSystemXmlPrinter(out, printer);
        try {
            xmlOut.print(ts);
        } catch (XMLStreamException e) {
            throw new TransitionSystemDefinitionException("Exception while printing XML!", e);
        }
    }

    /**
     * Prints the given TS on the given output in XML format.
     *
     * @param ts The TS to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemDefinitionException If an error occurs during the
     * printing.
     */
    public static void save(TransitionSystem ts, File out) throws TransitionSystemDefinitionException {
        try {
            save(ts, new FileOutputStream(out));
        } catch (FileNotFoundException e) {
            throw new TransitionSystemDefinitionException("Output file not found!", e);
        }
    }

    /**
     * Prints the given TS on the given output in XML format.
     *
     * @param ts The TS to print.
     * @param outputFileName The name of the file in which to print the TS.
     * @throws TransitionSystemDefinitionException If an error occurs during the
     * printing.
     */
    public static void save(TransitionSystem ts, String outputFileName) throws TransitionSystemDefinitionException {
        save(ts, new File(outputFileName));
    }

    /**
     * Prints the given FTS on the given output in XML format.
     *
     * @param fts The FTS to print.
     * @param out The output on which to print the FTS.
     * @throws TransitionSystemDefinitionException If an error occurs during the
     * printing.
     */
    public static void save(FeaturedTransitionSystem fts, OutputStream out) throws TransitionSystemDefinitionException {
        FeaturedTransitionSystemPrinter printer = new FeaturedTransitionSystemPrinter();
        TransitionSystemXmlPrinter xmlOut = new TransitionSystemXmlPrinter(out, printer);
        try {
            xmlOut.print(fts);
        } catch (XMLStreamException e) {
            throw new TransitionSystemDefinitionException("Exception while printing XML!", e);
        }
    }

    /**
     * Prints the given FTS on the given output in XML format.
     *
     * @param fts The FTS to print.
     * @param out The output on which to print the FTS.
     * @throws TransitionSystemDefinitionException If an error occurs during the
     * printing.
     */
    public static void save(FeaturedTransitionSystem fts, File out) throws TransitionSystemDefinitionException {
        try {
            save(fts, new FileOutputStream(out));
        } catch (FileNotFoundException e) {
            throw new TransitionSystemDefinitionException("Output file not found!", e);
        }
    }

    /**
     * Prints the given FTS on the given output in XML format.
     *
     * @param fts The FTS to print.
     * @param outputFileName The name of the file in which to print the FTS.
     * @throws TransitionSystemDefinitionException If an error occurs during the
     * printing.
     */
    public static void save(FeaturedTransitionSystem fts, String outputFileName) throws TransitionSystemDefinitionException {
        save(fts, new File(outputFileName));
    }

    /**
     * Prints the given usage model on the given output in XML format.
     *
     * @param usageModel The usage model to print.
     * @param out The output on which to print the usage model.
     * @throws TransitionSystemDefinitionException If an error occurs during the
     * printing.
     */
    public static void save(UsageModel usageModel, OutputStream out) throws TransitionSystemDefinitionException {
        UsageModelPrinter printer = new UsageModelPrinter();
        TransitionSystemXmlPrinter xmlOut = new TransitionSystemXmlPrinter(out, printer);
        try {
            xmlOut.print(usageModel);
        } catch (XMLStreamException e) {
            throw new TransitionSystemDefinitionException("Exception while printing XML!", e);
        }
    }

    /**
     * Prints the given usage model on the given output in XML format.
     *
     * @param usageModel The usage model to print.
     * @param out The output on which to print the usage model.
     * @throws TransitionSystemDefinitionException If an error occurs during the
     * printing.
     */
    public static void save(UsageModel usageModel, File out) throws TransitionSystemDefinitionException {
        try {
            save(usageModel, new FileOutputStream(out));
        } catch (FileNotFoundException e) {
            throw new TransitionSystemDefinitionException("Output file not found!", e);
        }
    }

    /**
     * Prints the given usage model on the given output in XML format.
     *
     * @param usageModel The usage model to print.
     * @param outputFileName The name of the file in which to print the usage
     * model.
     * @throws TransitionSystemDefinitionException If an error occurs during the
     * printing.
     */
    public static void save(UsageModel usageModel, String outputFileName) throws TransitionSystemDefinitionException {
        save(usageModel, new File(outputFileName));
    }
        
    public static void save(TestSet set, OutputStream out) throws TransitionSystemDefinitionException {
        TestCasePrinter printer = new TestCasePrinter();
        TestSetXmlPrinter xmlOut = new TestSetXmlPrinter(out, printer);
        try {
            xmlOut.print(set);
        } catch (XMLStreamException e) {
            throw new TransitionSystemDefinitionException("Exception while printing XML!", e);
        }
    }
    
    public static void save(FeaturedTransitionSystem fts, TestSet set, OutputStream out) throws TransitionSystemDefinitionException {
        FtsTestCasePrinter printer = new FtsTestCasePrinter(fts);
        TestSetXmlPrinter xmlOut = new TestSetXmlPrinter(out, printer);
        try {
            xmlOut.print(set);
        } catch (XMLStreamException e) {
            throw new TransitionSystemDefinitionException("Exception while printing XML!", e);
        }
    }
    
    public static void save(UsageModel um, TestSet set, OutputStream out) throws TransitionSystemDefinitionException {
        UsageModelTestCasePrinter printer = new UsageModelTestCasePrinter(um);
        TestSetXmlPrinter xmlOut = new TestSetXmlPrinter(out, printer);
        try {
            xmlOut.print(set);
        } catch (XMLStreamException e) {
            throw new TransitionSystemDefinitionException("Exception while printing XML!", e);
        }
    }
    

}
