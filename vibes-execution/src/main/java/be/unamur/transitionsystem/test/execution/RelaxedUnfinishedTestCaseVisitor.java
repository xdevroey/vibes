package be.unamur.transitionsystem.test.execution;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.ExecutionNode;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.VisitorWithReturn;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.TestCase;

public class RelaxedUnfinishedTestCaseVisitor implements VisitorWithReturn<Boolean> {

    private static final Logger logger = LoggerFactory
            .getLogger(RelaxedTestCaseVisitor.class);

    private Deque<Action> actions;

    private ExecutionNode<Transition> father;

    protected State initialState;

    @SuppressWarnings("unchecked")
    RelaxedUnfinishedTestCaseVisitor(TestCase testCase,
            ExecutionNode<? extends Transition> fatherNode) {
        actions = Queues.newArrayDeque(testCase);
        this.father = (ExecutionNode<Transition>) fatherNode;
    }

    @Override
    public Boolean visit(State state) throws VisitException {
        checkNotNull(state);
        checkState(father.getValue() == null || father.getValue().getTo().equals(state));
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
            if (isCurrentBranchValid()) {
                logger.trace("Current branch is valid");
                return true;
            } else {
                logger.trace("Current branch is invalid");
                return false;
            }
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
            // put back the action in this.actions, compute its sons and add it to the nodes list.
            else if (tr.getAction() == null
                    || tr.getAction().getName().equals(Action.NO_ACTION_NAME)) {
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

    private HashSet<State> lastVisited = Sets.newHashSet();
    private int lastActionStackSize;

    private boolean sameStateVisitedAgain(State state, Deque<Action> actions) {
        logger.trace("Last visited is {}", lastVisited);
        logger.trace("Stack size is {}", lastActionStackSize);
        logger.trace("State is {}", state);
        logger.trace("New Stack size is {}", actions.size());
        boolean res = false;
        if (lastVisited == null) {
            lastVisited.add(state);
            lastActionStackSize = actions.size();
        } else if (actions.size() == lastActionStackSize) {
            // actions has not progressed
            res = lastVisited.contains(state);
            lastVisited.add(state);
        } else {
            lastVisited.clear();
            lastActionStackSize = actions.size();
        }
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
