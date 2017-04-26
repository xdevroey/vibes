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
import static be.unamur.transitionsystem.transformation.xml.UsageModelHandler.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.usagemodel.UsageModelTransition;

public class UsageModelPrinter extends LtsPrinter {

    private static final Logger logger = LoggerFactory.getLogger(UsageModelPrinter.class);

    @Override
    public void printElement(XMLStreamWriter xtw, be.unamur.transitionsystem.Transition transition)
            throws XMLStreamException {
        logger.trace("Printing transition element");
        xtw.writeStartElement(TRANSITION_TAG);
        xtw.writeAttribute(TARGET_ATTR, transition.getTo().getName());
        if (transition.getAction().getName() != Action.NO_ACTION_NAME) {
            xtw.writeAttribute(ACTION_ATTR, transition.getAction().getName());
        }
        double proba = ((UsageModelTransition) transition).getProbability();
        if (proba != 1.0) {
            xtw.writeAttribute(PROBA_ATTR, Double.toString(proba));
        }
        xtw.writeEndElement();
    }

}
