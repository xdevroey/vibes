
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
class RandomFixedLengthTraceSelector implements TraceSelector {
    
    private final LabelledTransitionSystem lts;
    private final int size;
    protected Random random;

    public RandomFixedLengthTraceSelector(LabelledTransitionSystem lts, int size) {
        checkNotNull(lts, "Argument 'lts' may not be null!");
        checkArgument(size > 0, "Argument 'size' must be > 0!");
        this.lts = lts;
        this.size = size;
        this.random = new Random();
    }

    @Override
    public LabelledTransitionSystem getTransitionSystem() {
        return lts;
    }

    public int getSize() {
        return size;
    }
    
    /**
     * 
     * @return
     * @throws IllegalStateException If the algorithm find a sink state.
     */
    @Override
    public List<Action> select() throws IllegalStateException{
        List<Action> list = Lists.newLinkedList();
        State current = lts.getInitialState();
        for (int i = 0 ; i < this.size ; i ++){
            Transition trans = getRandomOutgoingTransition(current);
            list.add(trans.getAction());
            current = trans.getTo();
        }
        return list;
    }

    protected Transition getRandomOutgoingTransition(State state) {
        Iterator<Transition> out = state.outgoingTransitions();
        checkState(state.outgoingSize() > 0, "Found a sink state!");
        Transition next = out.next();
        for(int i = 0 ;  i < random.nextInt(state.outgoingSize()); i++){
            checkArgument(out.hasNext(), "Inconsistant value for the number of outgoing states %s in %s", state, lts);
            next = out.next();
        }
        return next;
    }
    

}
