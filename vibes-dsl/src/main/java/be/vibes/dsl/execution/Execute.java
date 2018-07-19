package be.vibes.dsl.execution;

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

import be.vibes.dsl.exception.ExecutionException;
import be.vibes.solver.FeatureModel;
import be.vibes.ts.Action;
import be.vibes.ts.Execution;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.FeaturedTransitionSystemExecutor;
import be.vibes.ts.TestCase;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.TransitionSystemExecutor;
import be.vibes.ts.exception.TransitionSystenExecutionException;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains methods to execute test cases on transition systems. The
 * execution returns a list of executions representing the possibles paths
 * followed in the transition system.
 *
 * @see Execution
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class Execute {

    private static final Logger LOG = LoggerFactory.getLogger(Execute.class);

    /**
     * Executes the given actions on the given transition system.
     *
     * @param actions The sequence of actions to execute.
     * @param ts The transition system on which the test case must be executed.
     * @return The execution tree, representing the different sequences of
     * transitions fired when executing the test case on the transition system.
     */
    public static List<Execution> execute(TransitionSystem ts, List<Action> actions) {
        TransitionSystemExecutor exec = new TransitionSystemExecutor(ts);
        actions.forEach((action) -> {
            try {
                exec.execute(action);
            } catch (TransitionSystenExecutionException ex) {
                LOG.error("Could not execute action {}", action, ex);
                throw new ExecutionException("Could not execute action " + action + "!", ex);
            }
        });
        return Lists.newArrayList(exec.getCurrentExecutions());
    }

    /**
     * Executes the actions in the given test case on the given transition
     * system. The resulting Executions may diverge from the execution
     * represented by the test case.
     *
     * @param testCase The sequence of actions to execute.
     * @param ts The transition system on which the test case must be executed.
     * @return The execution tree, representing the different sequences of
     * transitions fired when executing the test case on the transition system.
     */
    public static List<Execution> execute(TransitionSystem ts, TestCase testCase) {
        List<Action> actions = new ArrayList<>();
        testCase.iterator().forEachRemaining((transition) -> {
            actions.add(transition.getAction());
        });
        return execute(ts, actions);
    }
    
    /**
     * Executes the given actions on the given featured transition system.
     *
     * @param actions The sequence of actions to execute.
     * @param fm The feature model of the transition system.
     * @param fts The transition system on which the test case must be executed.
     * @return The execution tree, representing the different sequences of
     * transitions fired when executing the test case on the transition system.
     */
    public static List<Execution> execute(FeaturedTransitionSystem fts, FeatureModel fm, List<Action> actions) {
        TransitionSystemExecutor exec = new FeaturedTransitionSystemExecutor(fts, fm);
        actions.forEach((action) -> {
            try {
                exec.execute(action);
            } catch (TransitionSystenExecutionException ex) {
                LOG.error("Could not execute action {}", action, ex);
                throw new ExecutionException("Could not execute action " + action + "!", ex);
            }
        });
        return Lists.newArrayList(exec.getCurrentExecutions());
    }

    /**
     * Executes the actions in the given test case on the given transition
     * system. The resulting Executions may diverge from the execution
     * represented by the test case.
     *
     * @param testCase The sequence of actions to execute.
     * @param fm The feature model of the transition system.
     * @param fts The transition system on which the test case must be executed.
     * @return The execution tree, representing the different sequences of
     * transitions fired when executing the test case on the transition system.
     */
    public static List<Execution> execute(FeaturedTransitionSystem fts, FeatureModel fm, TestCase testCase) {
        List<Action> actions = new ArrayList<>();
        testCase.iterator().forEachRemaining((transition) -> {
            actions.add(transition.getAction());
        });
        return execute(fts, fm, actions);
    }

}
