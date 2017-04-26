package be.unamur.transitionsystem.test.mutation;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import be.unamur.fts.fexpression.FExpression;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.Visitor;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.test.FtsMutableTestCase;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import static be.unamur.fts.fexpression.ConstraintTransformation.*;
import static com.google.common.base.Preconditions.checkNotNull;

public class FtsMutantVisitor implements Visitor {

    private static final Logger logger = LoggerFactory.getLogger(FtsMutantVisitor.class);

    private FtsMutableTestCase testCase = new FtsMutableTestCase();

    private Deque<Action> actions;

    private String initialState = null;

    //private PrintStream endsInInitialStateOut;
    //private PrintStream doesNotEndInInitialStateOut;
    private List<FExpression> alive = Lists.newArrayList();

    public FtsMutantVisitor(TestCase testCase, String initialStateName /*, PrintStream endsInInitialStateOut, PrintStream doesNotEndInInitialStateOut*/) {
        actions = Queues.newArrayDeque(testCase);
        initialState = initialStateName;
        //this.endsInInitialStateOut = endsInInitialStateOut;
        //this.doesNotEndInInitialStateOut = doesNotEndInInitialStateOut;

    }

    @Override
    public void visit(State state) throws VisitException {
        checkNotNull(state);
        if (initialState.equals(state.getName())) {
            logger.debug("Initial state reached");
            // Check that the exploration is finished
            if (actions.isEmpty()) {
                logger.trace("No more action to execute");
                // Check that the current branch is valid
                endsInInitialState(testCase.getProductConstraint());
                return;
            }
        } else if (actions.isEmpty()) {
            logger.debug("No more action to execute");
            // No more actions to execute and not in the initial State 
            //doesNotEndInInitialState(testCase.getProductConstraint());
            return;
        }
        // Continue the exploration
        Iterator<Transition> it = state.outgoingTransitions();
        Transition tr;
        Action currentAct = actions.peekFirst();
        checkNotNull(currentAct);
        logger.debug("Next action to execute is {}", currentAct);
        while (it.hasNext()) {
            tr = it.next();
            if (tr.getAction().equals(currentAct)) {
                logger.debug("Executing transition {}", tr);
                addTransition(tr);
                actions.poll();
                tr.getTo().accept(this);
                actions.push(currentAct);
                removeTransition();
            } else if (tr.getAction().getName().equals(WrongInitialState.WIS_ACTION_NAME)) {
                logger.debug("Executing WIS transition {}", tr);
                addTransition(tr);
                tr.getTo().accept(this);
                removeTransition();
            } else {
                logger.trace("Transition {} is not executed", tr);
            }
        }
    }

    private void endsInInitialState(FExpression productConstraint) {
        // getDisjunction(endsInInitialState),
        //endsInInitialStateOut.println(productConstraint.toString());
        alive.add(productConstraint);
    }

    private void doesNotEndInInitialState(FExpression productConstraint) {
        // getConjunction(doesNotEndInInitialState));
        //doesNotEndInInitialStateOut.println(new Not(productConstraint).toString());
    }

    protected void addTransition(Transition trans) {
        try {
            testCase.enqueue(trans);
        } catch (TestCaseException e) {
            logger.error("Error while adding transition!", e);
            throw new IllegalStateException(
                    "Error while adding transition, this should not happen but ...", e);
        }
    }

    protected void removeTransition() {
        try {
            testCase.dequeue();
        } catch (TestCaseException e) {
            logger.error("Error while removing transition!", e);
            throw new IllegalStateException(
                    "Error while removing transition, this should not happen but ...", e);
        }
    }

    public FExpression getAlive() {
        return getDisjunction(alive);
    }
    
    public int nbrValidPaths(){
        return alive.size();
    }
}
