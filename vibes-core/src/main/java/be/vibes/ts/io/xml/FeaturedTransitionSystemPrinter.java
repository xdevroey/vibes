package be.vibes.ts.io.xml;

import be.vibes.fexpression.FExpression;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.Transition;
import static be.vibes.ts.io.xml.FeaturedTransitionSystemHandler.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeaturedTransitionSystemPrinter extends TransitionSystemPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(FeaturedTransitionSystemPrinter.class);

    @Override
    public void printElement(XMLStreamWriter xtw, Transition transition)
            throws XMLStreamException {
        LOG.trace("Printing transition element");
        xtw.writeStartElement(TRANSITION_TAG);
        xtw.writeAttribute(TARGET_ATTR, transition.getTarget().getName());
        xtw.writeAttribute(ACTION_ATTR, transition.getAction().getName());
        FExpression fexpr = getFTS().getFExpression(transition);
        if (!fexpr.isTrue()) {
            xtw.writeAttribute(FEXPRESSION_ATTR, fexpr.toString());
        }
        xtw.writeEndElement();
    }
    
    private FeaturedTransitionSystem getFTS(){
        return (FeaturedTransitionSystem) ts;
    }

}
