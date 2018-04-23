package be.vibes.ts;

import be.vibes.ts.exception.TransitionSystenExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
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

    public boolean canExecute(String action) {
        checkNotNull(action, "Action may not be null!");
        Action act = ts.getAction(action);
        checkNotNull(act, "Action {} does not belong to the transition system!", action);
        return canExecute(act);
    }
    
    public boolean canExecute(Action action) {
        checkNotNull(action, "Action may not be null!");
        boolean canExecute = false;
        if (executions.isEmpty()) {
            LOG.trace("No execution found, will start from initial state!");
            canExecute = canExecuteFromState(ts.getInitialState(), action);
        } else {
            Iterator<Execution> it = executions.iterator();
            State s;
            while (!canExecute && it.hasNext()) {
                s = it.next().getLast().getTarget();
                LOG.trace("Executions found, will start from state {}!", s);
                canExecute = canExecuteFromState(s, action);
            }
        }
        return canExecute;
    }

    private boolean canExecuteFromState(State state, Action action) {
        checkNotNull(state, "State may not be null!");
        checkNotNull(action, "Action may not be null!");
        boolean canExecute = false;
        Iterator<Transition> it = ts.getOutgoing(state);
        while (!canExecute && it.hasNext()) {
            Transition tr = it.next();
            canExecute = tr.getAction().equals(action);
        }
        return canExecute;
    }
    
    public void execute(String action) throws TransitionSystenExecutionException {
        checkNotNull(action, "Action may not be null!");
        checkNotNull(ts.getAction(action), "Action {} does not belong to the transition system!", action);
        execute(ts.getAction(action));
    }

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

    private boolean startsFromInitialState(Action action) {
        Iterator<Transition> it = ts.getOutgoing(ts.getInitialState());
        boolean executed = false;
        while (it.hasNext()) {
            Transition tr = it.next();
            if (executed = tr.getAction().equals(action)) {
                try {
                    this.executions.add(new Execution().enqueue(tr));
                } catch (TransitionSystenExecutionException ex) {
                    // This should not happen given Execution class invariant 
                    LOG.error("Transition could not be added to execution!", ex);
                }
            }
        }
        return executed;
    }

    private boolean startsFromLastsTargets(Action action) {
        List<Execution> newExecutions = new ArrayList<>();
        boolean executed = false;
        for (Execution exec : this.executions) {
            List<Transition> nextTransitions = getNextTransitions(exec.getLast().getTarget(), action);
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

    private List<Transition> getNextTransitions(State source, Action action) {
        List<Transition> next = new ArrayList<>();
        Iterator<Transition> it = ts.getOutgoing(source);
        while (it.hasNext()) {
            Transition tr = it.next();
            if (tr.getAction().equals(action)) {
                next.add(tr);
            }
        }
        return next;
    }

    public Iterator<Execution> getCurrentExecutions() {
        return this.executions.iterator();
    }

}
