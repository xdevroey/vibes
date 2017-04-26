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

import javax.xml.stream.XMLStreamException;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.test.TestCase;

public class LtsTestCaseXmlGenerator extends TestCaseXmlGenerator {

    public LtsTestCaseXmlGenerator(PrintStream out) throws XMLStreamException {
        super(out);
    }

    @Override
    protected void printTestCaseContent(TestCase atc) throws XMLStreamException {
        // write actions
        xtw.writeStartElement(ACTIONS);
        for (Action act : atc) {
            printActionContent(act);
        }
        xtw.writeEndElement();
    }

    @Override
    protected void printActionContent(Action action) throws XMLStreamException {
        xtw.writeStartElement(ACTION);
        xtw.writeCharacters(action.getName());
        xtw.writeEndElement();
    }

}
