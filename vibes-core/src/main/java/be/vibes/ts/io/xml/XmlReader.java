package be.vibes.ts.io.xml;

/*-
 * #%L
 * VIBeS: core
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlReader {

    private static final Logger LOG = LoggerFactory.getLogger(XmlReader.class);

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
            factory.setXMLReporter((String message, String typeErreur, Object source, Location location) -> {
                LOG.debug("Error type : " + typeErreur + ", message : "
                        + "message");
            });
        }
        return factory;
    }

    public void readDocument() throws XMLStreamException {
        while (this.reader.hasNext() && !this.endDocument) {
            XMLEvent evt = this.reader.nextEvent();
            switch (evt.getEventType()) {
                case XMLStreamConstants.START_DOCUMENT:
                    LOG.trace("Starting document");
                    this.handler.handleStartDocument();
                    break;
                case XMLStreamConstants.END_DOCUMENT:
                    LOG.trace("Ending document");
                    this.endDocument = true;
                    this.handler.handleEndDocument();
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    LOG.trace("Starting element " + evt.toString());
                    this.handler.handleStartElement(evt.asStartElement());
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    LOG.trace("Ending element " + evt.toString());
                    this.handler.handleEndElement(evt.asEndElement());
                    break;
                case XMLStreamConstants.ATTRIBUTE:
                    LOG.trace("Attribute " + evt.toString());
                    break;
                case XMLStreamConstants.CHARACTERS:
                    LOG.trace("Characters " + evt.toString());
                    this.handler.handleCharacters(evt.asCharacters());
                    break;
                default:
                    LOG.trace("Ignoring XML element: " + evt.toString());
                    break;
            }
        }
    }

}
