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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.fexpression.exception.ParserException;
import be.unamur.fts.fexpression.ParserUtil;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.test.FtsMutableTestCase;
import be.unamur.transitionsystem.test.FtsTestCase;
import be.unamur.transitionsystem.test.exception.TestCaseException;

public class FtsTestCaseXmlReader extends TestCaseXmlReader<FtsTestCase> {

    public FtsTestCaseXmlReader(File input) throws FileNotFoundException,
            XMLStreamException {
        super(input);
    }

    public FtsTestCaseXmlReader(InputStream inputStream)
            throws XMLStreamException {
        super(inputStream);
    }

    public FtsTestCaseXmlReader(Reader reader) throws XMLStreamException {
        super(reader);
    }

    private FExpression constraint = null;

    @Override
    protected void handleStartElement(StartElement elem) throws XMLStreamException {
        String elementName = elem.getName().getLocalPart();
        if (elementName.equals(TEST_CASE)) {
            this.nextTestCase = new FtsMutableTestCase();
            this.nextTestCase.setId(elem.getAttributeByName(QName.valueOf(TEST_CASE_ID))
                    .getValue());
        } else if (elementName.equals(FEXPRESSION)) {
            try {
                constraint = ParserUtil.getInstance().parse(
                        this.reader.nextEvent().asCharacters().getData());
            } catch (ParserException e) {
                throw new XMLStreamException("Invalid feature expression in test case "
                        + this.nextTestCase.getId() + "!", e);
            }
        } else if (elementName.equals(ACTIONS)) {
            // Do nothing
        } else if (elementName.equals(ACTION)) {
            Action act = new Action(this.reader.nextEvent().asCharacters().getData());
            try {
                ((FtsMutableTestCase) nextTestCase).enqueue(act, constraint);
                constraint = null;
            } catch (TestCaseException e) {
                throw new XMLStreamException("Invalid feature expression in transition "
                        + act.getName() + " of test case " + this.nextTestCase.getId()
                        + "!", e);
            }
        }
    }

}
