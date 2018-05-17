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

import be.vibes.ts.Execution;
import be.vibes.ts.TestCase;
import be.vibes.ts.TestSet;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.TransitionSystemExecutor;
import be.vibes.ts.exception.TransitionSystenExecutionException;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TestCaseHandler implements XmlEventHandler {
    
    public static final String TEST_SET_TAG = "testset";
    public static final String TEST_CASE_TAG = "testcase";
    public static final String ID_ATTR = TransitionSystemHandler.ID_ATTR;
    public static final String TRANSITION_TAG = TransitionSystemHandler.TRANSITION_TAG;
    public static final String SOURCE_ATTR = "source";
    public static final String TARGET_ATTR = TransitionSystemHandler.TARGET_ATTR;
    public static final String ACTION_ATTR = TransitionSystemHandler.ACTION_ATTR;
    
    private static final Logger LOG = LoggerFactory.getLogger(TestCaseHandler.class);
    
    private TestSet testSet;
    protected String id;
    protected TransitionSystemExecutor currentExecutor;
    
    protected final TransitionSystem ts;
    
    public TestCaseHandler(TransitionSystem ts) {
        this.ts = ts;
    }

    public TestSet getTestSet() {
        return testSet;
    }
    
    @Override
    public void handleStartDocument() {
        LOG.trace("Starting document");
    }
    
    @Override
    public void handleEndDocument() {
        LOG.trace("Ending document");
    }
    
    @Override
    public void handleStartElement(StartElement element) throws XMLStreamException {
        String elementName = element.getName().getLocalPart();
        switch (elementName) {
            case TRANSITION_TAG:
                handleStartTransitionTag(element);
                break;
            case TEST_CASE_TAG:
                handleStartTestCaseTag(element);
                break;
            case TEST_SET_TAG:
                handleStartTestSetTag(element);
                break;
            default:
                handlerStartOtherTag(element);
                break;
        }
    }
    
    protected void handleStartTestSetTag(StartElement element) {
        LOG.trace("Starting test set");
        this.testSet = new TestSet();
    }
    
    protected void handleStartTestCaseTag(StartElement element) {
        LOG.trace("Starting test case");
        id = element.getAttributeByName(QName.valueOf(ID_ATTR)).getValue();
        Preconditions.checkNotNull(id, "Test case identifier may not be null!");
        currentExecutor = new TransitionSystemExecutor(ts);
    }
    
    protected void handleStartTransitionTag(StartElement element) throws XMLStreamException {
        LOG.trace("Starting transition");
        String action = element.getAttributeByName(QName.valueOf(ACTION_ATTR)).getValue();
        Preconditions.checkNotNull(action, "Action attribute may not be null!");
        try {
            currentExecutor.execute(action);
        } catch (TransitionSystenExecutionException ex) {
            LOG.error("Action {} could not be executed!", action, ex);
            throw new XMLStreamException("Action " + action + "could not be executed!", ex);
        }
    }
    
    protected void handlerStartOtherTag(StartElement element) {
        LOG.debug("Unkown starting element {}", element);
    }

    // End tag
    @Override
    public void handleEndElement(EndElement element) throws XMLStreamException {
        String elementName = element.getName().getLocalPart();
        switch (elementName) {
            case TRANSITION_TAG:
                handleEndTransitionTag(element);
                break;
            case TEST_CASE_TAG:
                handleEndTestCaseTag(element);
                break;
            case TEST_SET_TAG:
                handleEndTestSetTag(element);
                break;
            default:
                handlerEndOtherTag(element);
                break;
        }
    }
    
    protected void handleEndTransitionTag(EndElement element) {
        LOG.trace("Ending transition");
    }
    
    protected void handleEndTestCaseTag(EndElement element) throws XMLStreamException {
        LOG.trace("Ending test case");
        Iterator<Execution> it = currentExecutor.getCurrentExecutions();
        Preconditions.checkArgument(it.hasNext(), "No execution possible for test case %s!", id);
        TestCase testcase = new TestCase(id);
        try {
            testcase.enqueueAll(it.next());
        } catch (TransitionSystenExecutionException ex) {
            LOG.error("Could not add transitions to test case!", ex);
            throw new XMLStreamException("Could not add transitions to test case!", ex);
        }
        testSet.add(testcase);
        if (it.hasNext()) {
            LOG.error("More than one execution is possible for test case {}, first one is considered, remaining are discartded!", testcase);
        }
    }
    
    protected void handleEndTestSetTag(EndElement element) {
        LOG.trace("Ending test set");
    }
    
    protected void handlerEndOtherTag(EndElement element) {
        LOG.debug("Unkown ending element {}", element);
    }
    
    protected String charValue;

    // Text
    @Override
    public void handleCharacters(Characters element) throws XMLStreamException {
        this.charValue = element.asCharacters().getData();
    }
    
}
