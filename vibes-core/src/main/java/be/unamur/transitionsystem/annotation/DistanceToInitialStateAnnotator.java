package be.unamur.transitionsystem.annotation;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class DistanceToInitialStateAnnotator implements Annotator {

    public static final String PROP_TS_IS_ANNOTATED_DTO = DistanceFromInitialStateAnnotator.class.getName().concat(".isannotateddto");
    public static final String PROP_STATE_DTO = DistanceToInitialStateAnnotator.class.getName().concat(".dto");

    private static DistanceToInitialStateAnnotator instance;

    private DistanceToInitialStateAnnotator() {
    }

    public static DistanceToInitialStateAnnotator getInstance() {
        return instance == null ? instance = new DistanceToInitialStateAnnotator() : instance;
    }

    @Override
    public void annotate(TransitionSystem ts) {
        Queue<State> queue = Queues.newArrayDeque();
        // Initialization from initial state.
        ts.getInitialState().setProperty(PROP_STATE_DTO, 0);
        queue.add(ts.getInitialState());
        // Main loop
        while (!queue.isEmpty()) {
            State s = queue.poll();
            checkNotNull(s);
            int dist = s.getProperty(PROP_STATE_DTO, -1);
            checkState(dist >= 0);
            Iterator<Transition> it = s.incomingTransitions();
            while (it.hasNext()) {
                Transition tr = it.next();
                if(tr.getFrom().getProperty(PROP_STATE_DTO, -1) < 0){
                    tr.getFrom().setProperty(PROP_STATE_DTO, dist + 1);
                    queue.add(tr.getFrom());
                }
            }
        }
        // Annotate inaccessible states with Integer.MAX_VALUE value
        Iterator<State> it = ts.states();
        while(it.hasNext()){
            State s = it.next();
            if(s.getProperty(PROP_STATE_DTO, -1) < 0){
                s.setProperty(PROP_STATE_DTO, Integer.MAX_VALUE);
            }
        }
        ts.setProperty(PROP_TS_IS_ANNOTATED_DTO, true);
    }

    @Override
    public boolean isAnnotated(TransitionSystem ts) {
        return ts.getProperty(PROP_TS_IS_ANNOTATED_DTO, false);
    }

}
