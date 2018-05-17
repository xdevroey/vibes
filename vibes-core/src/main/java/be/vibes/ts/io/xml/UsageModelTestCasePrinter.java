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

import be.vibes.ts.Transition;
import be.vibes.ts.UsageModel;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static be.vibes.ts.io.xml.TestCaseHandler.ACTION_ATTR;
import static be.vibes.ts.io.xml.TestCaseHandler.SOURCE_ATTR;
import static be.vibes.ts.io.xml.TestCaseHandler.TARGET_ATTR;
import static be.vibes.ts.io.xml.TestCaseHandler.TRANSITION_TAG;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class UsageModelTestCasePrinter extends TestCasePrinter {
    
    private static final Logger LOG = LoggerFactory.getLogger(FtsTestCasePrinter.class);
    
    public static final String PROBA_ATTR = UsageModelTestCasePrinter.PROBA_ATTR;
    
    private final UsageModel um;

    public UsageModelTestCasePrinter(UsageModel um) {
        super();
        this.um = um;
    }
    
    @Override
    public void printElement(XMLStreamWriter xtw, Transition transition) throws XMLStreamException {
        LOG.trace("Printing transition element");
        xtw.writeStartElement(TRANSITION_TAG);
        xtw.writeAttribute(SOURCE_ATTR, transition.getSource().getName());
        xtw.writeAttribute(ACTION_ATTR, transition.getAction().getName());
        xtw.writeAttribute(PROBA_ATTR, Double.toString(um.getProbability(transition)));
        xtw.writeAttribute(TARGET_ATTR, transition.getTarget().getName());
        xtw.writeEndElement();
    }
}
