package be.vibes.ts.io.xml;

import be.vibes.ts.Transition;
import be.vibes.ts.UsageModel;
import static be.vibes.ts.io.xml.UsageModelHandler.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsageModelPrinter extends TransitionSystemPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(UsageModelPrinter.class);

    @Override
    public void printElement(XMLStreamWriter xtw, Transition transition)
            throws XMLStreamException {
        LOG.trace("Printing transition element");
        xtw.writeStartElement(TRANSITION_TAG);
        xtw.writeAttribute(TARGET_ATTR, transition.getTarget().getName());
        xtw.writeAttribute(ACTION_ATTR, transition.getAction().getName());
        xtw.writeAttribute(PROBA_ATTR, Double.toString(getUsageModel().getProbability(transition)));
        xtw.writeEndElement();
    }
    
    private UsageModel getUsageModel(){
        return (UsageModel) ts;
    }


}
