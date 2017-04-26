
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.VisitorWithReturn;
import be.unamur.transitionsystem.exception.VisitException;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
class TraceExecutorVisitor implements VisitorWithReturn<Boolean>{
    
    private LinkedList<Action> queue;

    public TraceExecutorVisitor(List<Action> actions) {
        this.queue = Lists.newLinkedList(actions);
    }

    @Override
    public Boolean visit(State state) throws VisitException {
        if(queue.isEmpty()){
            return true;
        }
        Action act = queue.poll();
        Iterator<Transition> it = state.outgoingTransitions();
        Transition tr;
        boolean executed = false;
        while(it.hasNext() && !executed){
            tr = it.next();
            executed = tr.getAction().equals(act) && tr.getTo().accept(this);
        }
        queue.addFirst(act);
        return executed;
    }
    
    

}
