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
import be.vibes.fexpression.ParserUtil;
import be.vibes.fexpression.exception.ParserException;
import be.vibes.ts.Action;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.FeaturedTransitionSystemFactory;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeaturedTransitionSystemHandler extends TransitionSystemHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FeaturedTransitionSystemHandler.class);

    public static final String FEXPRESSION_ATTR = "fexpression";

    @Override
    protected void handleStartTransitionTag(StartElement element) throws XMLStreamException {
        LOG.trace("Starting transition");
        String to = element.getAttributeByName(QName.valueOf(TARGET_ATTR)).getValue();
        // Get action
        String actAttr = element.getAttributeByName(QName.valueOf(ACTION_ATTR)).getValue();
        if(actAttr == null){
            actAttr = Action.EPSILON_ACTION;
        }
        // Get feature expression
        String expr = element.getAttributeByName(QName.valueOf(FEXPRESSION_ATTR)).getValue();
        FExpression fexpr = FExpression.trueValue();
        if(expr != null){
            try {
                fexpr = ParserUtil.getInstance().parse(expr);
            } catch (ParserException e) {
                LOG.error("Exception while parsing fexpression {}!", expr, e);
                throw new XMLStreamException("Exception while parsing fexpression " + expr, e);
            }
        }
        // add transition
        getFacotry().addTransition(currentState, actAttr, fexpr, to);
    }

    @Override
    protected void handleEndStartTag(EndElement element) throws XMLStreamException {
        LOG.trace("Ending start");
        this.factory = new FeaturedTransitionSystemFactory(this.charValue);
    }
    
    private FeaturedTransitionSystemFactory getFacotry(){
        return (FeaturedTransitionSystemFactory) this.factory;
    }

    @Override
    public FeaturedTransitionSystem geTransitionSystem() {
        return getFacotry().build();
    }

}
