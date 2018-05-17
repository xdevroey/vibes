package be.vibes.dsl.test;

/*-
 * #%L
 * VIBeS: dsl
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

import be.vibes.dsl.exception.TestCaseDefinitionException;
import be.vibes.solver.FeatureModel;
import be.vibes.ts.Execution;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.FeaturedTransitionSystemExecutor;
import be.vibes.ts.TestCase;
import be.vibes.ts.TestSet;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.TransitionSystemExecutor;
import be.vibes.ts.exception.TransitionSystenExecutionException;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TestSetDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(TestSetDefinition.class);

    private boolean defined = false;
    protected TestSet testSet = new TestSet();
    private final TransitionSystem ts;
    private final FeatureModel fm;

    protected TestSetDefinition(TransitionSystem ts) {
        this.ts = ts;
        this.fm = null;
    }

    protected TestSetDefinition(FeaturedTransitionSystem fts, FeatureModel fm) {
        this.ts = fts;
        this.fm = fm;
    }

    protected TestCaseDefinition test(String identifier) {
        return new TestCaseDefinition(identifier, this);
    }

    void notifyTestCaseDefinitionComplete(TestCaseDefinition def) {
        TestCase testCase = new TestCase(def.getId());
        TransitionSystemExecutor exec = fm == null ? new TransitionSystemExecutor(ts)
                : new FeaturedTransitionSystemExecutor((FeaturedTransitionSystem) ts, fm);
        def.actions().forEach((str) -> {
            try {
                exec.execute(str);
            } catch (TransitionSystenExecutionException ex) {
                LOG.error("Error in test case {}: action {} could not be executed on the transition system!", def.getId(), str);
                throw new TestCaseDefinitionException("Error in test case " + def.getId() + ": action " + str + " could not be executed on the transition system!", ex);
            }
        });
        Iterator<Execution> execsIt = exec.getCurrentExecutions();
        if (!execsIt.hasNext()) {
            throw new TestCaseDefinitionException("No valid execution in the transition system for the given sequence of action in test case " + def.getId() + "!");
        }
        try {
            testCase.enqueueAll(execsIt.next());
        } catch (TransitionSystenExecutionException ex) {
            throw new TestCaseDefinitionException("Could not build test case" + def.getId() + " from the given sequence of actions!", ex);
        }
        if (!execsIt.hasNext()) {
            LOG.warn("More than one possible execution for the given sequence of actions, only the first one is considered for test cases!");
        }
        testSet.add(testCase);
    }

    /**
     * The method that has to be implemented by sub-classes in order to define
     * the test-case
     */
    protected abstract void define();

    public TestSet getTestSet() {
        if (!defined) {
            defined = true;
            define();
        }
        return testSet;
    }

}
