package be.vibes.ts.io.xml;

import be.vibes.ts.TestCase;
import be.vibes.ts.TestSet;
import be.vibes.ts.Transition;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public interface TestCaseElementPrinter {
    
    public void printElement(XMLStreamWriter xtw, TestSet set) throws XMLStreamException;

    public void printElement(XMLStreamWriter xtw, TestCase testCase) throws XMLStreamException;

    public void printElement(XMLStreamWriter xtw, Transition transition) throws XMLStreamException;
    
}
