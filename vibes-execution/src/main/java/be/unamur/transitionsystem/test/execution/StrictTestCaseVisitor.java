package be.unamur.transitionsystem.test.execution;

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

class StrictTestCaseVisitor implements VisitorWithReturn<Boolean> {

    private static final Logger logger = LoggerFactory
            .getLogger(StrictTestCaseVisitor.class);

    private Deque<Action> actions;

    protected State initialState = null;

    @SuppressWarnings("unchecked")
    public StrictTestCaseVisitor(TestCase testCase, ExecutionNode<? extends Transition> father) {
        this.father = (ExecutionNode<Transition>) father;
        actions = Queues.newArrayDeque(testCase);
    }

    private ExecutionNode<Transition> father;

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
        }
        // Continue the exploration
        Iterator<Transition> it = state.outgoingTransitions();
        Transition tr;
        Action currentAct = actions.poll();
        checkNotNull(currentAct);
        boolean validBranch = false;
        logger.debug("Next action to execute is {}", currentAct);
        while (it.hasNext()) {
            tr = it.next();
            if (tr.getAction().equals(currentAct)) {
                logger.trace("Executing transition {}", tr);
                addTransition(tr);
                ExecutionNode<Transition> currentFather = father;
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
        actions.push(currentAct);
        return validBranch;
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
