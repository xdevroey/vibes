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

import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlReader {

    private static final Logger logger = LoggerFactory.getLogger(XmlReader.class);

    protected static XMLInputFactory factory;

    protected XMLEventReader reader;
    protected boolean endDocument = false;
    protected XmlEventHandler handler;

    public XmlReader(XmlEventHandler handler, File input) throws FileNotFoundException, XMLStreamException {
        this(handler, new FileInputStream(input));
    }

    public XmlReader(XmlEventHandler handler, InputStream inputStream) throws XMLStreamException {
        this.reader = getXMLInputFactory().createXMLEventReader(inputStream);
        this.handler = handler;
    }

    public XmlReader(XmlEventHandler handler, Reader reader) throws XMLStreamException {
        this.reader = getXMLInputFactory().createXMLEventReader(reader);
        this.handler = handler;
    }

    private static XMLInputFactory getXMLInputFactory() {
        if (factory == null) {
            factory = XMLInputFactory.newInstance();
            //factory.setProperty("javax.xml.stream.isValidating", Boolean.FALSE);

            factory.setProperty("javax.xml.stream.isCoalescing", true);
            factory.setXMLReporter(new XMLReporter() {
                public void report(String message, String typeErreur, Object source,
                        Location location) throws XMLStreamException {
                    logger.debug("Error type : " + typeErreur + ", message : "
                            + "message");
                }
            });
        }
        return factory;
    }

    public void readDocument() throws XMLStreamException {
        while (this.reader.hasNext() && !this.endDocument) {
            XMLEvent evt = this.reader.nextEvent();
            switch (evt.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT:
                    logger.trace("Starting document");
                    this.handler.handleStartDocument();
                    break;
                case XMLStreamConstants.END_DOCUMENT:
                    logger.trace("Ending document");
                    this.endDocument = true;
                    this.handler.handleEndDocument();
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    logger.trace("Starting element " + evt.toString());
                    this.handler.handleStartElement(evt.asStartElement());
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    logger.trace("Ending element " + evt.toString());
                    this.handler.handleEndElement(evt.asEndElement());
                    break;
                case XMLStreamConstants.ATTRIBUTE:
                    logger.trace("Attribute " + evt.toString());
                    break;
                case XMLStreamConstants.CHARACTERS:
                    logger.trace("Characters " + evt.toString());
                    this.handler.handleCharacters(evt.asCharacters());
                    break;
                default:
                    logger.trace("Ignoring XML element: " + evt.toString());
                    break;
            }
        }
    }

}
