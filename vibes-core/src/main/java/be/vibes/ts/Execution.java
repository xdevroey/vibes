package be.vibes.ts;

import be.vibes.ts.exception.TransitionSystenExecutionException;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * An execution (also called behavior) of M is a non-empty, infinite sequence
 * seq = s0 a1 s1 a2 . . . With s0 = i such that (si -- ai+1 --> si+1) for all 0
 * &le; i.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class Execution implements Iterable<Transition> {
    
    private final Deque<Transition> transitions;
    
    public Execution() {
        this.transitions = new ArrayDeque<>();
    }
    
    @Override
    public Iterator iterator() {
        return this.transitions.iterator();
    }
    
    public Execution enqueue(Transition transition) throws TransitionSystenExecutionException {
        if (transitions.isEmpty()) {
            transitions.add(transition);
        } else {
            // Check that the last state is the source state of the current transition 
            Transition last = transitions.getLast();
            if (!last.getTarget().equals(transition.getSource())) {
                throw new TransitionSystenExecutionException("Last transition (" + transition + ") does not start from last state (" + last.toString() + ")!");
            } else {
                this.transitions.addLast(transition);
            }
        }
        return this;
    }
    
    public Execution enqueueAll(Iterable<Transition> transitions) throws TransitionSystenExecutionException {
        for (Transition tr : transitions) {
            enqueue(tr);
        }
        return this;
    }
    
    public Execution dequeue() {
        Preconditions.checkElementIndex(0, this.transitions.size(), "Execution is empty!");
        this.transitions.removeLast();
        return this;
    }
    
    public Transition getFirst(){
        Preconditions.checkElementIndex(0, this.transitions.size(), "Execution is empty!");
        return this.transitions.getFirst();
    }
    
    public Transition getLast(){
        Preconditions.checkElementIndex(0, this.transitions.size(), "Execution is empty!");
        return this.transitions.getLast();
    }
    
    public int getSize(){
        return this.transitions.size();
    }
    
    public Execution copy() {
        Execution copy = new Execution();
        try {
            copy.enqueueAll(this);
        } catch (TransitionSystenExecutionException ex) {
            throw new IllegalStateException("Copy of an inconsistent Execution!", ex);
        }
        return copy;
    }
    
}
