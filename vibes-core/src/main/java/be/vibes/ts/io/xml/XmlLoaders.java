package be.vibes.ts.io.xml;

import be.vibes.solver.FeatureModel;
import be.vibes.ts.exception.TransitionSystemDefinitionException;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TestSet;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.UsageModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class allows to load TSs from XML files.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class XmlLoaders {

    private static final Logger LOG = LoggerFactory
            .getLogger(XmlLoaders.class);

    /**
     * Loads the TransitionSystem contained in the given stream.
     *
     * @param in The input stream.
     * @return The TransitionSystem contained in the given stream.
     * @throws TransitionSystemDefinitionException if the LTS could not be
     * loaded.
     */
    public static TransitionSystem loadTransitionSystem(InputStream in) throws TransitionSystemDefinitionException {
        TransitionSystemHandler handler = new TransitionSystemHandler();
        try {
            XmlReader reader = new XmlReader(handler, in);
            reader.readDocument();
        } catch (XMLStreamException e) {
            LOG.error("Error while reading TS", e);
            throw new TransitionSystemDefinitionException("Error while reading TS!", e);
        }
        return (TransitionSystem) handler.geTransitionSystem();
    }

    /**
     * Loads the TransitionSystem contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The TransitionSystem contained in the given file.
     * @throws TransitionSystemDefinitionException if the LTS could not be
     * loaded.
     */
    public static TransitionSystem loadTransitionSystem(File xmlFile) throws TransitionSystemDefinitionException {
        try {
            return XmlLoaders.loadTransitionSystem(new FileInputStream(xmlFile));
        } catch (FileNotFoundException e) {
            LOG.error("Error while loading TS input ={}!", xmlFile, e);
            throw new TransitionSystemDefinitionException("Error while loading TS!", e);
        }
    }

    /**
     * Loads the TransitionSystem contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The TransitionSystem contained in the given file.
     * @throws TransitionSystemDefinitionException if the LTS could not be
     * loaded.
     */
    public static TransitionSystem loadTransitionSystem(String xmlFile) throws TransitionSystemDefinitionException {
        return XmlLoaders.loadTransitionSystem(new File(xmlFile));
    }

    /**
     * Loads the FeaturedTransitionSystem contained in the given stream.
     *
     * @param in The input stream.
     * @return The FeaturedTransitionSystem contained in the given stream.
     * @throws TransitionSystemDefinitionException if the FTS could not be
     * loaded.
     */
    public static FeaturedTransitionSystem loadFeaturedTransitionSystem(InputStream in) throws TransitionSystemDefinitionException {
        FeaturedTransitionSystemHandler handler = new FeaturedTransitionSystemHandler();
        try {
            XmlReader reader = new XmlReader(handler, in);
            reader.readDocument();
        } catch (XMLStreamException e) {
            LOG.error("Error while reading TS!", e);
            throw new TransitionSystemDefinitionException("Error while reading TS!", e);
        }
        return (FeaturedTransitionSystem) handler.geTransitionSystem();
    }

    /**
     * Loads the FeaturedTransitionSystem contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The FeaturedTransitionSystem contained in the given file.
     * @throws TransitionSystemDefinitionException if the FTS could not be
     * loaded.
     */
    public static FeaturedTransitionSystem loadFeaturedTransitionSystem(File xmlFile) throws TransitionSystemDefinitionException {
        try {
            return loadFeaturedTransitionSystem(new FileInputStream(xmlFile));
        } catch (FileNotFoundException e) {
            LOG.error("Error while loading TS input ={}!", xmlFile, e);
            throw new TransitionSystemDefinitionException("Error while loading TS!", e);
        }
    }

    /**
     * Loads the FeaturedTransitionSystem contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The FeaturedTransitionSystem contained in the given file.
     * @throws TransitionSystemDefinitionException if the FTS could not be
     * loaded.
     */
    public static FeaturedTransitionSystem loadFeaturedTransitionSystem(String xmlFile) throws TransitionSystemDefinitionException {
        return loadFeaturedTransitionSystem(new File(xmlFile));
    }

    /**
     * Loads the UsageModel contained in the given stream.
     *
     * @param in The input stream.
     * @return The UsageModel contained in the given stream.
     * @throws TransitionSystemDefinitionException if the FTS could not be
     * loaded.
     */
    public static UsageModel loadUsageModel(InputStream in) throws TransitionSystemDefinitionException {
        UsageModelHandler handler = new UsageModelHandler();
        try {
            XmlReader reader = new XmlReader(handler, in);
            reader.readDocument();
        } catch (XMLStreamException e) {
            LOG.error("Error while reading TS input!", e);
            throw new TransitionSystemDefinitionException("Error while reading TS!", e);
        }
        return handler.geTransitionSystem();
    }

    /**
     * Loads the UsageModel contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The UsageModel contained in the given file.
     * @throws TransitionSystemDefinitionException if the FTS could not be
     * loaded.
     */
    public static UsageModel loadUsageModel(File xmlFile) throws TransitionSystemDefinitionException {
        try {
            return loadUsageModel(new FileInputStream(xmlFile));
        } catch (FileNotFoundException e) {
            LOG.error("Error while loading TS input ={}!", xmlFile, e);
            throw new TransitionSystemDefinitionException("Error while loading TS!", e);
        }
    }

    /**
     * Loads the UsageModel contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The UsageModel contained in the given file.
     * @throws TransitionSystemDefinitionException if the FTS could not be
     * loaded.
     */
    public static UsageModel loadUsageModel(String xmlFile) throws TransitionSystemDefinitionException {
        return loadUsageModel(new File(xmlFile));
    }

    public static TestSet loadTestSet(InputStream in, TransitionSystem ts) throws TransitionSystemDefinitionException {
        TestCaseHandler handler = new TestCaseHandler(ts);
        try {
            XmlReader reader = new XmlReader(handler, in);
            reader.readDocument();
        } catch (XMLStreamException e) {
            LOG.error("Error while reading TS input!", e);
            throw new TransitionSystemDefinitionException("Error while reading TS!", e);
        }
        return handler.getTestSet();
    }

    public static TestSet loadTestSet(InputStream in, FeaturedTransitionSystem fts, FeatureModel fm) throws TransitionSystemDefinitionException {
        FtsTestCaseHandler handler = new FtsTestCaseHandler(fts, fm);
        try {
            XmlReader reader = new XmlReader(handler, in);
            reader.readDocument();
        } catch (XMLStreamException e) {
            LOG.error("Error while reading TS input!", e);
            throw new TransitionSystemDefinitionException("Error while reading TS!", e);
        }
        return handler.getTestSet();
    }

}
