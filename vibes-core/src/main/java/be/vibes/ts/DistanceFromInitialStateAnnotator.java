package be.vibes.ts;

import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class DistanceFromInitialStateAnnotator implements Annotator {

    public static final String PROP_STATE_DFROM = DistanceFromInitialStateAnnotator.class.getName().concat(".dfrom");

    private static DistanceFromInitialStateAnnotator instance;

    private DistanceFromInitialStateAnnotator() {
    }

    public static DistanceFromInitialStateAnnotator getInstance() {
        return instance == null ? instance = new DistanceFromInitialStateAnnotator() : instance;
    }

    @Override
    public void annotate(TransitionSystem ts) {
        Queue<State> queue = Queues.newArrayDeque();
        // Initialization from initial state.
        ts.getInitialState().setProperty(PROP_STATE_DFROM, 0);
        queue.add(ts.getInitialState());
        // Main loop
        while (!queue.isEmpty()) {
            State s = queue.poll();
            checkNotNull(s);
            int dist = s.getProperty(PROP_STATE_DFROM, -1);
            checkState(dist >= 0);
            Iterator<Transition> it = ts.getOutgoing(s);
            while (it.hasNext()) {
                Transition tr = it.next();
                if(tr.getTarget().getProperty(PROP_STATE_DFROM, -1) < 0){
                    tr.getTarget().setProperty(PROP_STATE_DFROM, dist + 1);
                    queue.add(tr.getTarget());
                }
            }
        }
        // Annotate inaccessible states with Integer.MAX_VALUE value
        Iterator<State> it = ts.states();
        while(it.hasNext()){
            State s = it.next();
            if(s.getProperty(PROP_STATE_DFROM, -1) < 0){
                s.setProperty(PROP_STATE_DFROM, Integer.MAX_VALUE);
            }
        }
    }

    @Override
    public boolean isAnnotated(TransitionSystem ts) {
        return ts.getInitialState().getProperty(PROP_STATE_DFROM, false);
    }

}
