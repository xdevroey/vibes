package be.unamur.transitionsystem.dsl;

/*
 * #%L
 * vibes-dsl
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.stream.XMLStreamException;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.dsl.exception.TransitionSystemDefinitionException;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.transformation.xml.FtsPrinter;
import be.unamur.transitionsystem.transformation.xml.LtsPrinter;
import be.unamur.transitionsystem.transformation.xml.UsageModelPrinter;
import be.unamur.transitionsystem.transformation.xml.XmlPrinter;
import be.unamur.transitionsystem.usagemodel.UsageModel;

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
     * @throws TransitionSystemDefinitionException If an error occurs during the
     * printing.
     */
    public static void print(LabelledTransitionSystem ts, OutputStream out) {
        LtsPrinter printer = new LtsPrinter();
        XmlPrinter xmlOut = new XmlPrinter(out, printer);
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
    public static void print(LabelledTransitionSystem ts, File out) {
        try {
            print(ts, new FileOutputStream(out));
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
    public static void print(LabelledTransitionSystem ts, String outputFileName) {
        print(ts, new File(outputFileName));
    }

    /**
     * Prints the given FTS on the given output in XML format.
     *
     * @param fts The FTS to print.
     * @param out The output on which to print the FTS.
     * @throws TransitionSystemDefinitionException If an error occurs during the
     * printing.
     */
    public static void print(FeaturedTransitionSystem fts, OutputStream out) {
        FtsPrinter printer = new FtsPrinter();
        XmlPrinter xmlOut = new XmlPrinter(out, printer);
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
    public static void print(FeaturedTransitionSystem fts, File out) {
        try {
            print(fts, new FileOutputStream(out));
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
    public static void print(FeaturedTransitionSystem fts, String outputFileName) {
        print(fts, new File(outputFileName));
    }

    /**
     * Prints the given usage model on the given output in XML format.
     *
     * @param usageModel The usage model to print.
     * @param out The output on which to print the usage model.
     * @throws TransitionSystemDefinitionException If an error occurs during the
     * printing.
     */
    public static void print(UsageModel usageModel, OutputStream out) {
        UsageModelPrinter printer = new UsageModelPrinter();
        XmlPrinter xmlOut = new XmlPrinter(out, printer);
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
    public static void print(UsageModel usageModel, File out) {
        try {
            print(usageModel, new FileOutputStream(out));
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
    public static void print(UsageModel usageModel, String outputFileName) {
        print(usageModel, new File(outputFileName));
    }

}
