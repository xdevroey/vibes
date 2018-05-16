package be.vibes.ts.io.xml;

import be.vibes.ts.TestCase;
import be.vibes.ts.TestSet;
import be.vibes.ts.Transition;
import static be.vibes.ts.io.xml.TestCaseHandler.*;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TestCasePrinter implements TestCaseElementPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(TestCasePrinter.class);

    @Override
    public void printElement(XMLStreamWriter xtw, TestSet set) throws XMLStreamException {
        LOG.trace("Printing test set element");
        xtw.writeStartElement(TEST_SET_TAG);
        for (TestCase testCase : set) {
            printElement(xtw, testCase);
        }
        xtw.writeEndElement();
    }

    @Override
    public void printElement(XMLStreamWriter xtw, TestCase testCase) throws XMLStreamException {
        LOG.trace("Printing test case element");
        xtw.writeStartElement(TEST_CASE_TAG);
        xtw.writeAttribute(ID_ATTR, testCase.getId());
        for(Transition tr: testCase){
            printElement(xtw, tr);
        }
        xtw.writeEndElement();
    }

    @Override
    public void printElement(XMLStreamWriter xtw, Transition transition) throws XMLStreamException {
        LOG.trace("Printing transition element");
        xtw.writeStartElement(TRANSITION_TAG);
        xtw.writeAttribute(SOURCE_ATTR, transition.getSource().getName());
        xtw.writeAttribute(ACTION_ATTR, transition.getAction().getName());
        xtw.writeAttribute(TARGET_ATTR, transition.getTarget().getName());
        xtw.writeEndElement();
    }

}
