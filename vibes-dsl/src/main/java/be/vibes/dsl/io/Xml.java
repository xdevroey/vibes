package be.vibes.dsl.io;

/*-
 * #%L
 * VIBeS: dsl
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import be.vibes.dsl.exception.TestCaseDefinitionException;
import be.vibes.dsl.exception.TransitionSystemIOException;
import be.vibes.solver.FeatureModel;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.FeaturedTransitionSystemExecutor;
import be.vibes.ts.TestSet;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.UsageModel;
import be.vibes.ts.exception.TransitionSystemDefinitionException;
import be.vibes.ts.io.xml.FtsTestCasePrinter;
import be.vibes.ts.io.xml.TestCasePrinter;
import be.vibes.ts.io.xml.TestSetXmlLoader;
import be.vibes.ts.io.xml.TestSetXmlPrinter;
import be.vibes.ts.io.xml.UsageModelTestCasePrinter;
import be.vibes.ts.io.xml.XmlLoaders;
import be.vibes.ts.io.xml.XmlSavers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class allows to load TSs from XML files.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Xml {

    private static final Logger LOG = LoggerFactory.getLogger(Xml.class);

    // **********************************************
    // Loaders 
    // **********************************************
    /**
     * Loads the TransitionSystem contained in the given stream.
     *
     * @param in The input stream.
     * @return The TransitionSystem contained in the given stream.
     * @throws TransitionSystemIOException if the LTS could not be loaded.
     */
    public static TransitionSystem loadTransitionSystem(InputStream in) {
        try {
            return XmlLoaders.loadTransitionSystem(in);
        } catch (TransitionSystemDefinitionException e) {
            LOG.error("Error while reading TS", e);
            throw new TransitionSystemIOException("Error while reading TS!", e);
        }
    }

    /**
     * Loads the TransitionSystem contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The TransitionSystem contained in the given file.
     * @throws TransitionSystemIOException if the LTS could not be loaded.
     */
    public static TransitionSystem loadTransitionSystem(File xmlFile) {
        try {
            return Xml.loadTransitionSystem(new FileInputStream(xmlFile));
        } catch (FileNotFoundException e) {
            LOG.error("Error while loading TS input ={}!", xmlFile, e);
            throw new TransitionSystemIOException("Error while loading TS!", e);
        }
    }

    /**
     * Loads the TransitionSystem contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The TransitionSystem contained in the given file.
     * @throws TransitionSystemIOException if the LTS could not be loaded.
     */
    public static TransitionSystem loadTransitionSystem(String xmlFile) {
        return Xml.loadTransitionSystem(new File(xmlFile));
    }

    /**
     * Loads the FeaturedTransitionSystem contained in the given stream.
     *
     * @param in The input stream.
     * @return The FeaturedTransitionSystem contained in the given stream.
     * @throws TransitionSystemIOException if the FTS could not be loaded.
     */
    public static FeaturedTransitionSystem loadFeaturedTransitionSystem(InputStream in) {
        try {
            return XmlLoaders.loadFeaturedTransitionSystem(in);
        } catch (TransitionSystemDefinitionException e) {
            LOG.error("Error while reading TS", e);
            throw new TransitionSystemIOException("Error while reading FTS!", e);
        }
    }

    /**
     * Loads the FeaturedTransitionSystem contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The FeaturedTransitionSystem contained in the given file.
     * @throws TransitionSystemIOException if the FTS could not be loaded.
     */
    public static FeaturedTransitionSystem loadFeaturedTransitionSystem(File xmlFile) {
        try {
            return loadFeaturedTransitionSystem(new FileInputStream(xmlFile));
        } catch (FileNotFoundException e) {
            LOG.error("Error while loading TS input ={}!", xmlFile, e);
            throw new TransitionSystemIOException("Error while loading TS!", e);
        }
    }

    /**
     * Loads the FeaturedTransitionSystem contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The FeaturedTransitionSystem contained in the given file.
     * @throws TransitionSystemIOException if the FTS could not be loaded.
     */
    public static FeaturedTransitionSystem loadFeaturedTransitionSystem(String xmlFile) {
        return loadFeaturedTransitionSystem(new File(xmlFile));
    }

    /**
     * Loads the UsageModel contained in the given stream.
     *
     * @param in The input stream.
     * @return The UsageModel contained in the given stream.
     * @throws TransitionSystemIOException if the FTS could not be loaded.
     */
    public static UsageModel loadUsageModel(InputStream in) {
        try {
            return XmlLoaders.loadUsageModel(in);
        } catch (TransitionSystemDefinitionException e) {
            LOG.error("Error while reading TS", e);
            throw new TransitionSystemIOException("Error while reading usage model!", e);
        }
    }

    /**
     * Loads the UsageModel contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The UsageModel contained in the given file.
     * @throws TransitionSystemIOException if the FTS could not be loaded.
     */
    public static UsageModel loadUsageModel(File xmlFile) {
        try {
            return loadUsageModel(new FileInputStream(xmlFile));
        } catch (FileNotFoundException e) {
            LOG.error("Error while loading TS input ={}!", xmlFile, e);
            throw new TransitionSystemIOException("Error while loading TS!", e);
        }
    }

    /**
     * Loads the UsageModel contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The UsageModel contained in the given file.
     * @throws TransitionSystemIOException if the FTS could not be loaded.
     */
    public static UsageModel loadUsageModel(String xmlFile) {
        return loadUsageModel(new File(xmlFile));
    }

    /**
     * Loads the test set contained in the given input. The test cases must be
     * valid test cases for the given transition system: transitions form a
     * continuous path in the ts.
     *
     * @param in The input stream.
     * @param ts The transition system on which the test cases are defined.
     * @return The set of test cases contained in the given input.
     * @throws TestCaseDefinitionException if the test set could not be loaded.
     */
    public static TestSet loadTestSet(InputStream in, TransitionSystem ts) {
        try {
            return TestSetXmlLoader.loadTestSet(in, ts);
        } catch (TransitionSystemDefinitionException e) {
            LOG.error("Error while loading test cases!", e);
            throw new TestCaseDefinitionException("Error while creating FTS test case reader!", e);
        }
    }

    /**
     * Loads the test set contained in the given input. The test cases must be
     * valid test cases for the given featured transition system: transitions
     * form a continuous path in the fts. Compatibility of the feature
     * expressions of the transitions is not checked at load time. See
     * {@link FeaturedTransitionSystemExecutor} to execute the test case on the
     * fts.
     *
     * @param in The input stream.
     * @param fts The featured transition system on which the test cases are
     * defined.
     * @param fm The feature model that goes with the given fts.
     * @return The set of test cases contained in the given input.
     * @throws TestCaseDefinitionException if the test set could not be loaded.
     */
    public static TestSet loadTestSet(InputStream in, FeaturedTransitionSystem fts, FeatureModel fm) {
        try {
            return TestSetXmlLoader.loadTestSet(in, fts, fm);
        } catch (TransitionSystemDefinitionException e) {
            LOG.error("Error while loading test cases!", e);
            throw new TestCaseDefinitionException("Error while creating FTS test case reader!", e);
        }
    }

    /**
     * Prints the given test set on the given output in XML format.
     *
     * @param set The test set to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(TestSet set, OutputStream out) {
        try {
            TestSetXmlPrinter printer = new TestSetXmlPrinter(out, new TestCasePrinter());
            printer.print(set);
        } catch (XMLStreamException ex) {
            LOG.error("Error while saving test set!", ex);
            throw new TransitionSystemIOException("Exception while printing XML!", ex);
        }
    }

    /**
     * Prints the given test set on the given output in XML format.
     *
     * @param set The test set to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(TestSet set, File out) {
        try {
            print(set, new FileOutputStream(out));
        } catch (FileNotFoundException ex) {
            LOG.error("Error while saving test set!", ex);
            throw new TransitionSystemIOException("Output file not found!", ex);
        }
    }

    /**
     * Prints the given test set on the given output in XML format.
     *
     * @param set The test set to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(TestSet set, String out) {
        print(set, new File(out));
    }

    /**
     * Prints the given test set on the given output in XML format.
     *
     * @param fts The featured transition system on which the test cases have
     * been defined.
     * @param set The test set to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(FeaturedTransitionSystem fts, TestSet set, OutputStream out) {
        try {
            TestSetXmlPrinter printer = new TestSetXmlPrinter(out, new FtsTestCasePrinter(fts));
            printer.print(set);
        } catch (XMLStreamException ex) {
            LOG.error("Error while saving test set!", ex);
            throw new TransitionSystemIOException("Exception while printing XML!", ex);
        }
    }

    /**
     * Prints the given test set on the given output in XML format.
     *
     * @param fts The featured transition system on which the test cases have
     * been defined.
     * @param set The test set to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(FeaturedTransitionSystem fts, TestSet set, File out) {
        try {
            print(fts, set, new FileOutputStream(out));
        } catch (FileNotFoundException ex) {
            LOG.error("Error while saving test set!", ex);
            throw new TransitionSystemIOException("Output file not found!", ex);
        }
    }

    /**
     * Prints the given test set on the given output in XML format.
     *
     * @param fts The featured transition system on which the test cases have
     * been defined.
     * @param set The test set to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(FeaturedTransitionSystem fts, TestSet set, String out) {
        print(fts, set, new File(out));
    }

    /**
     * Prints the given test set on the given output in XML format.
     *
     * @param um The usage model on which the test cases have
     * been defined.
     * @param set The test set to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(UsageModel um, TestSet set, OutputStream out) {
        try {
            TestSetXmlPrinter printer = new TestSetXmlPrinter(out, new UsageModelTestCasePrinter(um));
            printer.print(set);
        } catch (XMLStreamException ex) {
            LOG.error("Error while saving test set!", ex);
            throw new TransitionSystemIOException("Exception while printing XML!", ex);
        }
    }

    /**
     * Prints the given test set on the given output in XML format.
     *
     * @param um The usage model on which the test cases have
     * been defined.
     * @param set The test set to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(UsageModel um, TestSet set, File out) {
        try {
            print(um, set, new FileOutputStream(out));
        } catch (FileNotFoundException ex) {
            LOG.error("Error while saving test set!", ex);
            throw new TransitionSystemIOException("Output file not found!", ex);
        }
    }

    /**
     * Prints the given test set on the given output in XML format.
     *
     * @param um The usage model on which the test cases have
     * been defined.
     * @param set The test set to print.
     * @param out The output on which to print the TS.
     * @throws TransitionSystemIOException If an error occurs during the
     * printing.
     */
    public static void print(UsageModel um, TestSet set, String out) {
        print(um, set, new File(out));
    }
    
    // **********************************************
    // Printers 
    // **********************************************
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
