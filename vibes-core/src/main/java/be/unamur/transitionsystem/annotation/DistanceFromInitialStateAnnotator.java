package be.unamur.transitionsystem.annotation;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class DistanceFromInitialStateAnnotator implements Annotator {

    public static final String PROP_TS_IS_ANNOTATED_DFROM = DistanceFromInitialStateAnnotator.class.getName().concat(".isannotateddfrom");
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
            Iterator<Transition> it = s.outgoingTransitions();
            while (it.hasNext()) {
                Transition tr = it.next();
                if(tr.getTo().getProperty(PROP_STATE_DFROM, -1) < 0){
                    tr.getTo().setProperty(PROP_STATE_DFROM, dist + 1);
                    queue.add(tr.getTo());
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
        ts.setProperty(PROP_TS_IS_ANNOTATED_DFROM, true);
    }

    @Override
    public boolean isAnnotated(TransitionSystem ts) {
        return ts.getProperty(PROP_TS_IS_ANNOTATED_DFROM, false);
    }

}
