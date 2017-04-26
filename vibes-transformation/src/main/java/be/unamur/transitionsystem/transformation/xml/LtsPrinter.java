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
import static be.unamur.transitionsystem.transformation.xml.LtsHandler.*;

import java.util.Iterator;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;

public class LtsPrinter implements ElementPrinter {

    private static final Logger logger = LoggerFactory.getLogger(LtsPrinter.class);

    @Override
    public void printElement(XMLStreamWriter xtw, TransitionSystem ts) throws XMLStreamException {
        logger.trace("Printing TS element");
        xtw.writeStartElement(TS_TAG);

        // Starting state
        xtw.writeStartElement(START_TAG);
        xtw.writeCharacters(ts.getInitialState().getName());
        xtw.writeEndElement();

        // States
        xtw.writeStartElement(STATES_TAG);
        printElement(xtw, ts.states());
        xtw.writeEndElement();

        xtw.writeEndElement();
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
        logger.trace("Printing state element");
        xtw.writeStartElement(STATE_TAG);
        xtw.writeAttribute(ID_ATTR, state.getName());
        Iterator<be.unamur.transitionsystem.Transition> transitions = state.outgoingTransitions();
        be.unamur.transitionsystem.Transition transition;
        while (transitions.hasNext()) {
            transition = transitions.next();
            printElement(xtw, transition);
        }
        xtw.writeEndElement();
    }

    @Override
    public void printElement(XMLStreamWriter xtw, be.unamur.transitionsystem.Transition transition)
            throws XMLStreamException {
        logger.trace("Printing transition element");
        xtw.writeStartElement(TRANSITION_TAG);
        xtw.writeAttribute(TARGET_ATTR, transition.getTo().getName());
        xtw.writeAttribute(ACTION_ATTR, transition.getAction().getName());
        xtw.writeEndElement();
    }

}
