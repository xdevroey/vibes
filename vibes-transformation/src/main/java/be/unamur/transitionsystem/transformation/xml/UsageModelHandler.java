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
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.usagemodel.UsageModel;

public class UsageModelHandler extends LtsHandler {

    private static final Logger logger = LoggerFactory.getLogger(UsageModelHandler.class);

    public static final String PROBA_ATTR = "proba";

    public UsageModelHandler() {
        super();
    }

    @Override
    public void handleStartDocument() {
        this.transitionSystem = new UsageModel();
    }

    @Override
    protected void handleStartTransitionTag(StartElement element) throws XMLStreamException {
        logger.trace("Starting transition");
        String to = element.getAttributeByName(QName.valueOf(TARGET_ATTR)).getValue();
        Attribute actAttr = element.getAttributeByName(QName.valueOf(ACTION_ATTR));
        Action act = this.transitionSystem.addAction(Action.NO_ACTION_NAME);
        if (actAttr != null) {
            act = this.transitionSystem.addAction(actAttr.getValue());
        }
        Attribute attr = element.getAttributeByName(QName.valueOf(PROBA_ATTR));
        if (attr != null) {
            try {
                ((UsageModel) this.transitionSystem).addTransition(this.currentState, this.transitionSystem.addState(to), act, Double.parseDouble(attr.getValue()));
            } catch (NumberFormatException e) {
                logger.error("Exception while parsing proba " + attr.getValue(), e);
                throw new XMLStreamException("Exception while parsing proba " + attr.getValue(), e);
            }
        } else {
            this.transitionSystem.addTransition(this.currentState, this.transitionSystem.addState(to), act);
        }
    }

    @Override
    public UsageModel geTransitionSystem() {
        return (UsageModel) super.geTransitionSystem();
    }

}
