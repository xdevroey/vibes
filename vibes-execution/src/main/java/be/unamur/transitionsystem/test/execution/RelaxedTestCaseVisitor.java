package be.unamur.transitionsystem.test.execution;

/*
 * #%L
 * vibes-execution
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
import java.util.Deque;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Queues;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.ExecutionNode;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.VisitorWithReturn;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.TestCase;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Maps;
import java.util.Map;

class RelaxedTestCaseVisitor implements VisitorWithReturn<Boolean> {

    private static final Logger logger = LoggerFactory.getLogger(RelaxedTestCaseVisitor.class);

    private Deque<Action> actions;

    private ExecutionNode<Transition> father;

    private State initialState;

    @SuppressWarnings("unchecked")
    RelaxedTestCaseVisitor(TestCase testCase, ExecutionNode<? extends Transition> fatherNode) {
        actions = Queues.newArrayDeque(testCase);
        this.father = (ExecutionNode<Transition>) fatherNode;
    }

    @Override
    public Boolean visit(State state) throws VisitException {
        checkNotNull(state);
        checkState(father.getValue() == null
                || father.getValue().getTo().equals(state));
        if (initialState == null) {
            logger.debug("Initial state set to {}", state);
            initialState = state;
        } else if (initialState == state) {
            logger.debug("Initial state reached");
            // Check that the exploration is finished
            if (actions.isEmpty()) {
                logger.trace("No more action to execute");
                // Check that the current branch is valid
                if (isCurrentBranchValid()) {
                    logger.trace("Current branch is valid");
                    return true;
                } else {
                    logger.trace("Current branch is invalid");
                    return false;
                }
            }
        } else if (actions.isEmpty()) {
            logger.debug("No more action to execute");
            // No more actions to execute and not in the initial State 
            return false;
        } else if (sameStateVisitedAgain(state, actions)) {
            // If the same state is visited again with the same action
            // cycle detected, abort exploration
            logger.debug("Cycle detected, abort exploration of this branch");
            return false;
        }
        Iterator<Transition> it = state.outgoingTransitions();
        Transition tr;
        Action currentAct = actions.peekFirst();
        checkNotNull(currentAct);
        checkState(it.hasNext(), "Graph has to be connected!");
        boolean validBranch = false;
        while (it.hasNext()) {
            tr = it.next();
            // If the transition corresponds to the actual action
            // compute its sons and add it to the nodes list
            if (tr.getAction().equals(currentAct)) {
                logger.trace("Executing transition {}", tr);
                addTransition(tr);
                ExecutionNode<Transition> currentFather = (ExecutionNode<Transition>) father;
                father = father.addSon(tr);
                actions.poll(); // Action has been executed
                if (tr.getTo().accept(this)) {
                    validBranch = true;
                } else {
                    logger.trace("Son branch ({}) is invalid, will be pruned", father);
                    father.removeFromFather();
                }
                father = currentFather;
                removeTransition();
                actions.push(currentAct);
            } // If the transition has no action
            // do not remove the action from this.actions, compute its sons and add it to the nodes list.
            else if (tr.getAction() == null || tr.getAction().getName().equals(Action.NO_ACTION_NAME)) {
                logger.trace("Executing transition {}", tr);
                addTransition(tr);
                ExecutionNode<Transition> currentFather = (ExecutionNode<Transition>) father;
                father = father.addSon(tr);
                if (tr.getTo().accept(this)) {
                    validBranch = true;
                } else {
                    logger.trace("Son branch ({}) is invalid, will be pruned", father);
                    father.removeFromFather();
                }
                father = currentFather;
                removeTransition();
            } else {
                logger.trace("Transition {} is not executed", tr);
            }
        }
        return validBranch;
    }

    private Map<State, Integer> stackSize = Maps.newHashMap();

    /**
     * Returns true if a state has already been visited with the given actions
     * stack. Actions stack is identified by its size at the moment of the call.
     *
     * @param state The visited state.
     * @param actions The stack of actions at the moment of the visit of the
     * state.
     * @return True if the state has already been visited with a stack with the
     * same size.
     */
    private boolean sameStateVisitedAgain(State state, Deque<Action> actions) {
        logger.debug("Calling sameStateVisitedAgain with parameters state={}, actions={}", state, actions);
        Integer size = stackSize.get(state);
        boolean res = size != null && size == actions.size();
        stackSize.put(state, actions.size());
        return res;
    }

    protected void addTransition(Transition trans) {
        checkNotNull(trans);
    }

    protected void removeTransition() {
    }

    protected boolean isCurrentBranchValid() {
        return true;
    }
}
