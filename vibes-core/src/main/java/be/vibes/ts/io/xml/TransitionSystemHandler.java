package be.vibes.ts.io.xml;

/*-
 * #%L
 * VIBeS: core
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import be.vibes.ts.Action;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.TransitionSystemFactory;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransitionSystemHandler implements XmlEventHandler {

    public static final String TS_TAG = "ts";
    public static final String START_TAG = "start";
    public static final String STATES_TAG = "states";
    public static final String STATE_TAG = "state";
    public static final String TRANSITION_TAG = "transition";

    public static final String TARGET_ATTR = "target";
    public static final String ID_ATTR = "id";
    public static final String ACTION_ATTR = "action";

    private static final Logger LOG = LoggerFactory.getLogger(TransitionSystemHandler.class);

    protected TransitionSystemFactory factory;

    public TransitionSystem geTransitionSystem() {
        return this.factory.build();
    }

    @Override
    public void handleStartDocument() {
        LOG.trace("Starting document");
    }

    @Override
    public void handleEndDocument() {
        LOG.trace("Ending document");
    }

    // Start tag
    @Override
    public void handleStartElement(StartElement element) throws XMLStreamException {
        String elementName = element.getName().getLocalPart();
        switch (elementName) {
            case TRANSITION_TAG:
                handleStartTransitionTag(element);
                break;
            case STATE_TAG:
                handleStartStateTag(element);
                break;
            case STATES_TAG:
                handleStartStatesTag(element);
                break;
            case START_TAG:
                handleStartStartTag(element);
                break;
            case TS_TAG:
                handleStartTsTag(element);
                break;
            default:
                handlerStartOtherTag(element);
                break;
        }
    }

    protected void handlerStartOtherTag(StartElement element) throws XMLStreamException {
        LOG.debug("Unkown element {}", element);
    }

    protected String currentState;

    protected void handleStartTransitionTag(StartElement element) throws XMLStreamException {
        LOG.trace("Starting transition");
        String to = element.getAttributeByName(QName.valueOf(TARGET_ATTR)).getValue();
        String actAttr = element.getAttributeByName(QName.valueOf(ACTION_ATTR)).getValue();
        if (actAttr != null) {
            this.factory.addTransition(currentState, actAttr, to);
        } else {
            this.factory.addTransition(currentState, Action.EPSILON_ACTION, to);
        }
    }

    protected void handleStartStateTag(StartElement element) throws XMLStreamException {
        LOG.trace("Starting state");
        this.currentState = element.getAttributeByName(QName.valueOf(ID_ATTR)).getValue();
    }

    protected void handleStartStatesTag(StartElement element) throws XMLStreamException {
        LOG.trace("Starting states");
    }

    protected void handleStartStartTag(StartElement element) throws XMLStreamException {
        LOG.trace("Starting start");
    }

    protected void handleStartTsTag(StartElement element) throws XMLStreamException {
        LOG.trace("Starting ts");
    }

    // End tag
    @Override
    public void handleEndElement(EndElement element) throws XMLStreamException {
        String elementName = element.getName().getLocalPart();
        switch (elementName) {
            case TRANSITION_TAG:
                handleEndTransitionTag(element);
                break;
            case STATE_TAG:
                handleEndStateTag(element);
                break;
            case STATES_TAG:
                handleEndStatesTag(element);
                break;
            case START_TAG:
                handleEndStartTag(element);
                break;
            case TS_TAG:
                handleEndTsTag(element);
                break;
            default:
                break;
        }
    }

    protected void handleEndStartTag(EndElement element) throws XMLStreamException {
        LOG.trace("Ending start");
        this.factory = new TransitionSystemFactory(this.charValue);
    }

    protected void handleEndTsTag(EndElement element) throws XMLStreamException {
        LOG.trace("Ending ts");
    }

    protected void handleEndStatesTag(EndElement element) throws XMLStreamException {
        LOG.trace("Ending states");
    }

    protected void handleEndStateTag(EndElement element) throws XMLStreamException {
        LOG.trace("Ending state");
    }

    protected void handleEndTransitionTag(EndElement element) throws XMLStreamException {
        LOG.trace("Ending transition");
    }

    protected String charValue;

    // Text
    @Override
    public void handleCharacters(Characters element) throws XMLStreamException {
        this.charValue = element.asCharacters().getData();
    }
}
