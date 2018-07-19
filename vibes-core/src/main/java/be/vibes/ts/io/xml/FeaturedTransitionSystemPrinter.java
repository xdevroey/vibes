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
