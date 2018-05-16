package be.vibes.ts.io.xml;

import be.vibes.ts.State;
import be.vibes.ts.Transition;
import be.vibes.ts.TransitionSystem;
import static be.vibes.ts.io.xml.TransitionSystemHandler.*;

import java.util.Iterator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransitionSystemPrinter implements TransitionSystemElementPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(TransitionSystemPrinter.class);

    protected TransitionSystem ts;
    
    @Override
    public void printElement(XMLStreamWriter xtw, TransitionSystem ts) throws XMLStreamException {
        LOG.trace("Printing TS element");
        xtw.writeStartElement(TS_TAG);
        
        this.ts = ts;
        
        // Starting state
        xtw.writeStartElement(START_TAG);
        xtw.writeCharacters(ts.getInitialState().getName());
        xtw.writeEndElement();

        // States
        xtw.writeStartElement(STATES_TAG);
        printElement(xtw, ts.states());
        xtw.writeEndElement();

        xtw.writeEndElement();
        
        this.ts = null;
    }

    @Override
    public void printElement(XMLStreamWriter xtw, Iterator<State> states) throws XMLStreamException {
        State state;
        while (states.hasNext()) {
            state = states.next();
            printElement(xtw, state);
        }
    }

    @Override
    public void printElement(XMLStreamWriter xtw, State state) throws XMLStreamException {
        LOG.trace("Printing state element");
        xtw.writeStartElement(STATE_TAG);
        xtw.writeAttribute(ID_ATTR, state.getName());
        Iterator<Transition> transitions = ts.getOutgoing(state);
        Transition transition;
        while (transitions.hasNext()) {
            transition = transitions.next();
            printElement(xtw, transition);
        }
        xtw.writeEndElement();
    }

    @Override
    public void printElement(XMLStreamWriter xtw, Transition transition)
            throws XMLStreamException {
        LOG.trace("Printing transition element");
        xtw.writeStartElement(TRANSITION_TAG);
        xtw.writeAttribute(TARGET_ATTR, transition.getTarget().getName());
        xtw.writeAttribute(ACTION_ATTR, transition.getAction().getName());
        xtw.writeEndElement();
    }

}
