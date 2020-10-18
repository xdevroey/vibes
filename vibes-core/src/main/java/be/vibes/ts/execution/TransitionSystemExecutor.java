package be.vibes.ts.execution;

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

import be.vibes.ts.*;
import be.vibes.ts.exception.TransitionSystenExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Allows to execute a TransitionSystem.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TransitionSystemExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(TransitionSystemExecutor.class);

    private final TransitionSystem ts;
    private List<Execution> executions;

    public TransitionSystemExecutor(TransitionSystem ts) {
        this.ts = ts;
        this.executions = new ArrayList<>();
    }

    public boolean canExecute(String action) throws TransitionSystenExecutionException {
        checkNotNull(action, "Action may not be null!");
        Action act = getTransitionSystem().getAction(action);
        checkArgument(act != null, "Action {} does not belong to the transition system!", action);
        return TransitionSystemExecutor.this.canExecute(act);
    }

    public boolean canExecute(Action action) throws TransitionSystenExecutionException {
        checkNotNull(action, "Action may not be null!");
        boolean canExecute = false;
        if (executions.isEmpty()) {
            LOG.trace("No execution found, will start from initial state!");
            canExecute = canExecute(null, action);
        } else {
            Iterator<Execution> it = executions.iterator();
            State s;
            while (!canExecute && it.hasNext()) {
                Execution current = it.next();
                LOG.trace("Executions found, will start from state {}!", current.getLast().getTarget());
                canExecute = canExecute(current, action);
            }
        }
        return canExecute;
    }

    private boolean canExecute(Execution current, Action action) throws TransitionSystenExecutionException {
        checkNotNull(action, "Action may not be null!");
        return !getNextTransitions(current, action).isEmpty();
    }

    /**
     * Execute the action that has the given name on the transition system or
     * throws a TransitionSystenExecutionException if the action cannot be
     * executed.
     *
     * @param action The action to execute.
     * @throws NullPointerException               If the action name is null.
     * @throws IllegalArgumentException           If the action name does not corresponds
     *                                            to an action of the transition system.
     * @throws TransitionSystenExecutionException If the action cannot be
     *                                            executed.
     */
    public void execute(String action) throws TransitionSystenExecutionException {
        checkNotNull(action, "Action may not be null!");
        checkArgument(getTransitionSystem().getAction(action) != null, "Action {} does not belong to the transition system!", action);
        execute(getTransitionSystem().getAction(action));
    }

    /**
     * Execute the given action on the transition system or throws a
     * TransitionSystenExecutionException if the action cannot be executed.
     *
     * @param action The action to execute.
     * @throws NullPointerException               If the action is null.
     * @throws TransitionSystenExecutionException If the action cannot be executed.
     */
    public void execute(Action action) throws TransitionSystenExecutionException {
        checkNotNull(action, "Action may not be null!");
        boolean executed;
        if (this.executions.isEmpty()) {
            executed = startsFromInitialState(action);
        } else {
            executed = startsFromLastsTargets(action);
        }
        if (!executed) {
            throw new TransitionSystenExecutionException("Could not execute action " + action + " from executions " + Arrays.toString(this.executions.toArray()) + "!");
        }
    }

    /**
     * Executes the test case on the transition system or throws a TransitionSystenExecutionException if the test
     * case cannot be executed.
     *
     * @param testCase The test case to execute.
     * @throws NullPointerException               If the testCase or one of its actions are null.
     * @throws TransitionSystenExecutionException If the test case cannot be executed.
     */
    public void execute(TestCase testCase) throws TransitionSystenExecutionException {
        checkNotNull(testCase, "TestCase may not be null!");
        for (Transition transition : testCase) {
            execute(transition.getAction());
        }
    }

    private boolean startsFromInitialState(Action action) throws TransitionSystenExecutionException {
        List<Transition> nextTransitions = getNextTransitions(null, action);
        boolean executed = !nextTransitions.isEmpty();
        for (Transition tr : nextTransitions) {
            try {
                this.executions.add(new Execution().enqueue(tr));
            } catch (TransitionSystenExecutionException ex) {
                // This should not happen given Execution class invariant 
                LOG.error("Transition could not be added to execution (Execution class invariant violated)!", ex);
            }
        }
        return executed;
    }

    private boolean startsFromLastsTargets(Action action) throws TransitionSystenExecutionException {
        List<Execution> newExecutions = new ArrayList<>();
        boolean executed = false;
        for (Execution exec : this.executions) {
            List<Transition> nextTransitions = getNextTransitions(exec, action);
            if (executed = !nextTransitions.isEmpty()) {
                for (Transition tr : nextTransitions) {
                    try {
                        newExecutions.add(exec.copy().enqueue(tr));
                    } catch (TransitionSystenExecutionException ex) {
                        // This should not happen given this class invariant 
                        LOG.error("Transition " + tr + " could not be added to execution " + exec + "!", ex);
                    }
                }
            }
        }
        this.executions = newExecutions;
        return executed;
    }

    protected List<Transition> getNextTransitions(Execution current, Action action) throws TransitionSystenExecutionException {
        checkNotNull(action, "Action may not be null!");
        State source = current == null ? getTransitionSystem().getInitialState() : current.getLast().getTarget();
        List<Transition> next = new ArrayList<>();
        Iterator<Transition> it = getTransitionSystem().getOutgoing(source);
        while (it.hasNext()) {
            Transition tr = it.next();
            if (tr.getAction().equals(action)) {
                next.add(tr);
            }
        }
        return next;
    }

    /**
     * Returns the current executions. Each execution is a path in the
     * transition system that has been followed by execution the previous
     * actions.
     *
     * @return The current paths followed in the transition system.
     */
    public Iterator<Execution> getCurrentExecutions() {
        return this.executions.iterator();
    }

    protected TransitionSystem getTransitionSystem() {
        return ts;
    }

    /**
     * Resets the status of the executor and clears the previous executionss
     *
     * @throws TransitionSystenExecutionException If an error happens during the re-initialisation of the executor.
     */
    public void reset() throws TransitionSystenExecutionException {
        this.executions.clear();
    }

}
