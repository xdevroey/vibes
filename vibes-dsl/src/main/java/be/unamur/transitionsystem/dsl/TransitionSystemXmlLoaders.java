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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.dsl.exception.TransitionSystemDefinitionException;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.transformation.xml.FtsHandler;
import be.unamur.transitionsystem.transformation.xml.LtsHandler;
import be.unamur.transitionsystem.transformation.xml.UsageModelHandler;
import be.unamur.transitionsystem.transformation.xml.XmlReader;
import be.unamur.transitionsystem.usagemodel.UsageModel;

/**
 * This class allows to load TSs from XML files.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class TransitionSystemXmlLoaders {

    private static final Logger logger = LoggerFactory
            .getLogger(TransitionSystemXmlLoaders.class);

    /**
     * Loads the LabelledTransitionSystem contained in the given stream.
     *
     * @param in The input stream.
     * @return The LabelledTransitionSystem contained in the given stream.
     * @throws TransitionSystemDefinitionException if the LTS could not be
     * loaded.
     */
    public static LabelledTransitionSystem loadLabelledTransitionSystem(InputStream in) {
        LtsHandler handler = new LtsHandler();
        try {
            XmlReader reader = new XmlReader(handler, in);
            reader.readDocument();
        } catch (XMLStreamException e) {
            logger.error("Error while reading TS", e);
            throw new TransitionSystemDefinitionException("Error while reading TS!", e);
        }
        return (LabelledTransitionSystem) handler.geTransitionSystem();
    }

    /**
     * Loads the LabelledTransitionSystem contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The LabelledTransitionSystem contained in the given file.
     * @throws TransitionSystemDefinitionException if the LTS could not be
     * loaded.
     */
    public static LabelledTransitionSystem loadLabelledTransitionSystem(File xmlFile) {
        try {
            return TransitionSystemXmlLoaders.loadLabelledTransitionSystem(new FileInputStream(xmlFile));
        } catch (FileNotFoundException e) {
            logger.error("Error while loading TS input ={}!", xmlFile, e);
            throw new TransitionSystemDefinitionException("Error while loading TS!", e);
        }
    }

    /**
     * Loads the LabelledTransitionSystem contained in the given file.
     *
     * @param xmlFile The input file.
     * @return The LabelledTransitionSystem contained in the given file.
     * @throws TransitionSystemDefinitionException if the LTS could not be
     * loaded.
     */
    public static LabelledTransitionSystem loadLabelledTransitionSystem(String xmlFile) {
        return TransitionSystemXmlLoaders.loadLabelledTransitionSystem(new File(xmlFile));
    }

    /**
     * Loads the FeaturedTransitionSystem contained in the given stream.
     *
     * @param in The input stream.
     * @return The FeaturedTransitionSystem contained in the given stream.
     * @throws TransitionSystemDefinitionException if the FTS could not be
     * loaded.
     */
    public static FeaturedTransitionSystem loadFeaturedTransitionSystem(InputStream in) {
        FtsHandler handler = new FtsHandler();
        try {
            XmlReader reader = new XmlReader(handler, in);
            reader.readDocument();
        } catch (XMLStreamException e) {
            logger.error("Error while reading TS!", e);
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
    public static FeaturedTransitionSystem loadFeaturedTransitionSystem(File xmlFile) {
        try {
            return loadFeaturedTransitionSystem(new FileInputStream(xmlFile));
        } catch (FileNotFoundException e) {
            logger.error("Error while loading TS input ={}!", xmlFile, e);
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
    public static FeaturedTransitionSystem loadFeaturedTransitionSystem(String xmlFile) {
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
    public static UsageModel loadUsageModel(InputStream in) {
        UsageModelHandler handler = new UsageModelHandler();
        try {
            XmlReader reader = new XmlReader(handler, in);
            reader.readDocument();
        } catch (XMLStreamException e) {
            logger.error("Error while reading TS input!", e);
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
    public static UsageModel loadUsageModel(File xmlFile) {
        try {
            return loadUsageModel(new FileInputStream(xmlFile));
        } catch (FileNotFoundException e) {
            logger.error("Error while loading TS input ={}!", xmlFile, e);
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
    public static UsageModel loadUsageModel(String xmlFile) {
        return loadUsageModel(new File(xmlFile));
    }

}
