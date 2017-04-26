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
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;

public class LtsHandler implements XmlEventHandler {

    public static final String TS_TAG = "ts";
    public static final String FTS_TAG = "ts";
    public static final String START_TAG = "start";
    public static final String STATES_TAG = "states";
    public static final String STATE_TAG = "state";
    public static final String TRANSITION_TAG = "transition";

    public static final String TARGET_ATTR = "target";
    public static final String ID_ATTR = "id";
    public static final String ACTION_ATTR = "action";

    private static final Logger logger = LoggerFactory.getLogger(LtsHandler.class);

    protected TransitionSystem transitionSystem;

    public LtsHandler() {
    }

    public TransitionSystem geTransitionSystem() {
        return this.transitionSystem;
    }

    @Override
    public void handleStartDocument() {
        this.transitionSystem = new LabelledTransitionSystem();
    }

    @Override
    public void handleEndDocument() {
    }

    // Start tag
    @Override
    public void handleStartElement(StartElement element) throws XMLStreamException {
        String elementName = element.getName().getLocalPart();
        if (elementName.equals(TRANSITION_TAG)) {
            handleStartTransitionTag(element);
        } else if (elementName.equals(STATE_TAG)) {
            handleStartStateTag(element);
        } else if (elementName.equals(STATES_TAG)) {
            handleStartStatesTag(element);
        } else if (elementName.equals(START_TAG)) {
            handleStartStartTag(element);
        } else if (elementName.equals(TS_TAG)) {
            handleStartTsTag(element);
        } else {
            handlerStartOtherTag(element);
        }
    }

    protected void handlerStartOtherTag(StartElement element) throws XMLStreamException {
        logger.debug("Unkown element {}", element);
    }

    protected State currentState;

    protected Transition transition;

    protected void handleStartTransitionTag(StartElement element) throws XMLStreamException {
        logger.trace("Starting transition");
        String to = element.getAttributeByName(QName.valueOf(TARGET_ATTR)).getValue();
        logger.trace("to attribute = {}", to);
        Attribute actAttr = element.getAttributeByName(QName.valueOf(ACTION_ATTR));
        logger.trace("action attribute = {}", actAttr);
        if (actAttr != null) {
            this.transitionSystem.addTransition(this.currentState, this.transitionSystem.addState(to), this.transitionSystem.addAction(actAttr.getValue()));
        } else {
            this.transitionSystem.addTransition(this.currentState, this.transitionSystem.addState(to), this.transitionSystem.addAction(Action.NO_ACTION_NAME));
        }
    }

    protected void handleStartStateTag(StartElement element) throws XMLStreamException {
        logger.trace("Starting state");
        this.currentState = this.transitionSystem.addState(element.getAttributeByName(QName.valueOf(ID_ATTR)).getValue());
    }

    protected void handleStartStatesTag(StartElement element) throws XMLStreamException {
        logger.trace("Starting states");
    }

    protected void handleStartStartTag(StartElement element) throws XMLStreamException {
        logger.trace("Starting start");
    }

    protected void handleStartTsTag(StartElement element) throws XMLStreamException {
        logger.trace("Starting ts");
    }

    // End tag
    @Override
    public void handleEndElement(EndElement element) throws XMLStreamException {
        String elementName = element.getName().getLocalPart();
        if (elementName.equals(TRANSITION_TAG)) {
            handleEndTransitionTag(element);
        } else if (elementName.equals(STATE_TAG)) {
            handleEndStateTag(element);
        } else if (elementName.equals(STATES_TAG)) {
            handleEndStatesTag(element);
        } else if (elementName.equals(START_TAG)) {
            handleEndStartTag(element);
        } else if (elementName.equals(TS_TAG)) {
            handleEndTsTag(element);
        }
    }

    protected void handleEndStartTag(EndElement element) throws XMLStreamException {
        logger.trace("Ending start");
        this.transitionSystem.setInitialState(this.transitionSystem.addState(this.charValue));
    }

    protected void handleEndTsTag(EndElement element) throws XMLStreamException {
        logger.trace("Ending ts");
    }

    protected void handleEndStatesTag(EndElement element) throws XMLStreamException {
        logger.trace("Ending states");
    }

    protected void handleEndStateTag(EndElement element) throws XMLStreamException {
        logger.trace("Ending state");
    }

    protected void handleEndTransitionTag(EndElement element) throws XMLStreamException {
        logger.trace("Ending transition");
    }

    protected String charValue;

    // Text
    @Override
    public void handleCharacters(Characters element) throws XMLStreamException {
        this.charValue = element.asCharacters().getData();
    }
}
