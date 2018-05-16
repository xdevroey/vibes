package be.vibes.dsl.ts;

import be.vibes.dsl.exception.TransitionSystemIOException;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.UsageModel;
import be.vibes.ts.exception.TransitionSystemDefinitionException;
import be.vibes.ts.io.xml.XmlLoaders;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class allows to load TSs from XML files.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class TransitionSystemXmlLoaders {

    private static final Logger LOG = LoggerFactory
            .getLogger(TransitionSystemXmlLoaders.class);

    /**
     * Loads the TransitionSystem contained in the given stream.
     *
     * @param in The input stream.
     * @return The TransitionSystem contained in the given stream.
     * @throws TransitionSystemIOException if the LTS could not be
     * loaded.
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
     * @throws TransitionSystemIOException if the LTS could not be
     * loaded.
     */
    public static TransitionSystem loadTransitionSystem(File xmlFile) {
        try {
            return TransitionSystemXmlLoaders.loadTransitionSystem(new FileInputStream(xmlFile));
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
     * @throws TransitionSystemIOException if the LTS could not be
     * loaded.
     */
    public static TransitionSystem loadTransitionSystem(String xmlFile) {
        return TransitionSystemXmlLoaders.loadTransitionSystem(new File(xmlFile));
    }

    /**
     * Loads the FeaturedTransitionSystem contained in the given stream.
     *
     * @param in The input stream.
     * @return The FeaturedTransitionSystem contained in the given stream.
     * @throws TransitionSystemIOException if the FTS could not be
     * loaded.
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
     * @throws TransitionSystemIOException if the FTS could not be
     * loaded.
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
     * @throws TransitionSystemIOException if the FTS could not be
     * loaded.
     */
    public static FeaturedTransitionSystem loadFeaturedTransitionSystem(String xmlFile) {
        return loadFeaturedTransitionSystem(new File(xmlFile));
    }

    /**
     * Loads the UsageModel contained in the given stream.
     *
     * @param in The input stream.
     * @return The UsageModel contained in the given stream.
     * @throws TransitionSystemIOException if the FTS could not be
     * loaded.
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
     * @throws TransitionSystemIOException if the FTS could not be
     * loaded.
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
     * @throws TransitionSystemIOException if the FTS could not be
     * loaded.
     */
    public static UsageModel loadUsageModel(String xmlFile) {
        return loadUsageModel(new File(xmlFile));
    }

}
