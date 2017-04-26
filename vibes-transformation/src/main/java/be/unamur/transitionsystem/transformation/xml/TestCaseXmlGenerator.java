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
import java.io.PrintStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.TestSet;

public abstract class TestCaseXmlGenerator implements TestCaseXmlConstants {

    protected XMLStreamWriter xtw;

    public TestCaseXmlGenerator(PrintStream out) throws XMLStreamException {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        this.xtw = xof.createXMLStreamWriter(out);
    }

    public void startDocumen() throws XMLStreamException {
        xtw.writeStartDocument("1.0");
        xtw.writeStartElement(DOCUMENT);
    }

    public void endDocument() throws XMLStreamException {
        xtw.writeEndDocument();
        xtw.flush();
        xtw.close();
    }

    private int testCaseId = 1;

    public void printTestCase(TestCase atc)
            throws XMLStreamException {
        xtw.writeStartElement(TEST_CASE);
        xtw.writeAttribute(TEST_CASE_ID, Integer.toString(testCaseId++));
        printTestCaseContent(atc);
        xtw.writeEndElement();
    }

    protected abstract void printTestCaseContent(TestCase atc) throws XMLStreamException;

    protected abstract void printActionContent(Action action) throws XMLStreamException;

    public void printTestSet(TestSet set) throws XMLStreamException {
        for (TestCase atc : set) {
            printTestCase(atc);
        }
    }

}

/*
 //Write product expression
 xtw.writeStartElement(FEXPRESSION);
 try {
 if (atc.getProductConstraint() != null) {
 xtw.writeCharacters(atc.getProductConstraint().toString());
 } else {
 xtw.writeCharacters(ConstantNode.TRUE.toString());
 }
 } catch (ParserException e) {
 throw new XMLStreamException("Exception while parsing contraint!", e);
 }
 xtw.writeEndElement();

 */
