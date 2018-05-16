package be.vibes.ts.io.xml;

import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.Transition;
import static be.vibes.ts.io.xml.TestCaseHandler.ACTION_ATTR;
import static be.vibes.ts.io.xml.TestCaseHandler.SOURCE_ATTR;
import static be.vibes.ts.io.xml.TestCaseHandler.TARGET_ATTR;
import static be.vibes.ts.io.xml.TestCaseHandler.TRANSITION_TAG;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class FtsTestCasePrinter extends TestCasePrinter {

    private static final Logger LOG = LoggerFactory.getLogger(FtsTestCasePrinter.class);
    
    public static final String FEXPRESSION_ATTR = FeaturedTransitionSystemHandler.FEXPRESSION_ATTR;
    
    private final FeaturedTransitionSystem fts;

    public FtsTestCasePrinter(FeaturedTransitionSystem fts) {
        super();
        this.fts = fts;
    }

    @Override
    public void printElement(XMLStreamWriter xtw, Transition transition) throws XMLStreamException {
        LOG.trace("Printing transition element");
        xtw.writeStartElement(TRANSITION_TAG);
        xtw.writeAttribute(SOURCE_ATTR, transition.getSource().getName());
        xtw.writeAttribute(ACTION_ATTR, transition.getAction().getName());
        xtw.writeAttribute(FEXPRESSION_ATTR, fts.getFExpression(transition).toString());
        xtw.writeAttribute(TARGET_ATTR, transition.getTarget().getName());
        xtw.writeEndElement();
    }

}
