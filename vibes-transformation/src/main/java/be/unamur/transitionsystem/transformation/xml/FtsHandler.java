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

import be.unamur.fts.fexpression.exception.ParserException;
import be.unamur.fts.fexpression.ParserUtil;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;

public class FtsHandler extends LtsHandler {

    private static final Logger logger = LoggerFactory.getLogger(FtsHandler.class);

    public static final String FEXPRESSION_ATTR = "fexpression";
    public static final String FTS_TAG = "fts";

    public FtsHandler() {
        super();
    }

    @Override
    public void handleStartDocument() {
        this.transitionSystem = new FeaturedTransitionSystem();
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
        Attribute attr = element.getAttributeByName(QName.valueOf(FEXPRESSION_ATTR));
        if (attr != null) {
            try {
                ((FeaturedTransitionSystem) this.transitionSystem).addTransition(this.currentState, this.transitionSystem.addState(to), act, ParserUtil.getInstance().parse(attr.getValue()));
            } catch (ParserException e) {
                logger.error("Exception while parsing fexpression " + attr.getValue(), e);
                throw new XMLStreamException("Exception while parsing fexpression " + attr.getValue(), e);
            }
        } else {
            this.transitionSystem.addTransition(this.currentState, this.transitionSystem.addState(to), act);
        }
    }

    protected void handlerStartOtherTag(StartElement element) throws XMLStreamException {
        String elementName = element.getName().getLocalPart();
        if (elementName.equals(FTS_TAG)) {
            logger.debug("Starting fts tag");
        } else {
            super.handlerStartOtherTag(element);
        }
    }

    @Override
    public FeaturedTransitionSystem geTransitionSystem() {
        return (FeaturedTransitionSystem) super.geTransitionSystem();
    }

}
