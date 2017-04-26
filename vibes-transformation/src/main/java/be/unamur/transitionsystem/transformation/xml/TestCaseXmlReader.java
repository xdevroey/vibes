package be.unamur.transitionsystem.transformation.xml;

/*
 * #%L
 * vibes-transformation
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
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.test.TestCase;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public abstract class TestCaseXmlReader<T extends TestCase> implements Iterator<T>, TestCaseXmlConstants {

    private static final Logger logger = LoggerFactory.getLogger(TestCaseXmlReader.class);

    private static XMLInputFactory factory;

    protected XMLEventReader reader;
    protected T nextTestCase = null;
    protected boolean hasNext = false;
    protected boolean endDocument = false;

    public TestCaseXmlReader(File input) throws FileNotFoundException,
            XMLStreamException {
        this(new FileInputStream(input));
    }

    public TestCaseXmlReader(InputStream inputStream) throws XMLStreamException {
        this.reader = getXMLInputFactory().createXMLEventReader(
                inputStream);
        readNextTestCase();
    }

    public TestCaseXmlReader(Reader reader) throws XMLStreamException {
        this.reader = getXMLInputFactory().createXMLEventReader(reader);
        readNextTestCase();
    }

    private static XMLInputFactory getXMLInputFactory() {
        if (factory == null) {
            factory = XMLInputFactory.newInstance();
            factory.setProperty("javax.xml.stream.isValidating", Boolean.FALSE);
            factory.setProperty("javax.xml.stream.isCoalescing", true);
            factory.setXMLReporter(new XMLReporter() {
                public void report(String message, String typeErreur, Object source, Location location) throws XMLStreamException {
                    logger.debug("Error type : {}, message : {}", typeErreur, message);
                }
            });
        }
        return factory;
    }

    protected void readNextTestCase() throws XMLStreamException {
        this.hasNext = false;
        while (this.reader.hasNext() && !this.hasNext) {
            XMLEvent evt = this.reader.nextEvent();
            switch (evt.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT:
                    logger.trace("Starting document");
                    break;
                case XMLStreamConstants.END_DOCUMENT:
                    logger.trace("Ending document");
                    this.nextTestCase = null;
                    this.endDocument = true;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    logger.trace("Starting element " + evt.toString());
                    handleStartElement(evt.asStartElement());
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    logger.trace("Ending element " + evt.toString());
                    handleEndElement(evt.asEndElement());
                    break;
                case XMLStreamConstants.ATTRIBUTE:
                    logger.trace("Attribute " + evt.toString());
                    break;
                case XMLStreamConstants.CHARACTERS:
                    logger.trace("Characters " + evt.toString());
                    break;
                default:
                    logger.trace("Ignoring XML element: " + evt.toString());
                    break;
            }
        }
    }

    protected abstract void handleStartElement(StartElement elem) throws XMLStreamException;

    protected void handleEndElement(EndElement elem) {
        String elementName = elem.getName().getLocalPart();
        if (elementName.equals(TEST_CASE)) {
            this.hasNext = true;
        }
    }

    @Override
    public boolean hasNext() {
        return this.nextTestCase != null;
    }

    @Override
    public T next() {
        if (this.endDocument) {
            throw new NoSuchElementException("End of the XML document reached");
        }
        T current = this.nextTestCase;
        try {
            readNextTestCase();
        } catch (XMLStreamException e) {
            logger.error("Error while reading next Test Case!", e);
            throw new NoSuchElementException("Error occured while reading next Test Case!");
        }
        return current;
    }

    /**
     * @throws UnsupportedOperationException Always thrown when this method is
     * invoked.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not implemented!");
    }

}
