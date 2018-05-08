package be.vibes.ts.io.xml;

import be.vibes.ts.Action;
import be.vibes.ts.UsageModel;
import be.vibes.ts.UsageModelFactory;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsageModelHandler extends TransitionSystemHandler {

    private static final Logger LOG = LoggerFactory.getLogger(UsageModelHandler.class);

    public static final String PROBA_ATTR = "proba";

    @Override
    protected void handleStartTransitionTag(StartElement element) throws XMLStreamException {
        LOG.trace("Starting transition");
        String to = element.getAttributeByName(QName.valueOf(TARGET_ATTR)).getValue();
        // Get action
        String actAttr = element.getAttributeByName(QName.valueOf(ACTION_ATTR)).getValue();
        if(actAttr == null){
            actAttr = Action.EPSILON_ACTION;
        }
        // Get probability and create transition
        Attribute attr = element.getAttributeByName(QName.valueOf(PROBA_ATTR));
        if (attr != null) {
            try {
                getFactory().addTransition(currentState, actAttr, Double.parseDouble(attr.getValue()), to);
            } catch (NumberFormatException e) {
                LOG.error("Exception while parsing proba {}", attr.getValue(), e);
                throw new XMLStreamException("Exception while parsing proba " + attr.getValue(), e);
            }
        } else {
            getFactory().addTransition(currentState, actAttr, to);
        }
    }

    @Override
    protected void handleEndStartTag(EndElement element) throws XMLStreamException {
        LOG.trace("Ending start");
        this.factory = new UsageModelFactory(this.charValue);
    }

    @Override
    public UsageModel geTransitionSystem() {
        return getFactory().build();
    }

    private UsageModelFactory getFactory() {
        return (UsageModelFactory) this.factory;
    }

}
