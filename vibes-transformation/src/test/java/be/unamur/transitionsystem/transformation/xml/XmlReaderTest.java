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
import static org.junit.Assert.*;

import java.io.InputStream;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.transformation.xml.XmlEventHandler;
import be.unamur.transitionsystem.transformation.xml.XmlReader;

public class XmlReaderTest {

    private static final Logger logger = LoggerFactory.getLogger(XmlReaderTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	@Test
    public void test() throws Exception {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("fts-sodaVendingMachine.xml");
        assertNotNull("Test file not found!", input);
        XmlReader reader = new XmlReader(new XmlEventHandler() {
            @Override
            public void handleStartElement(StartElement asStartElement) throws XMLStreamException {
                logger.info("Starting element {}", asStartElement.asStartElement().getName());
            }

            @Override
            public void handleStartDocument() {
                logger.info("Starting Document");
            }

            @Override
            public void handleEndElement(EndElement asEndElement) throws XMLStreamException {
                logger.info("Ending element {}", asEndElement.asEndElement().getName());
            }

            @Override
            public void handleEndDocument() {
                logger.info("Ending Document");
            }

            @Override
            public void handleCharacters(Characters asCharacters) throws XMLStreamException {
                logger.info("Reading characters {}", asCharacters.asCharacters().getData());
            }
        }, input);
        reader.readDocument();
    }

}
