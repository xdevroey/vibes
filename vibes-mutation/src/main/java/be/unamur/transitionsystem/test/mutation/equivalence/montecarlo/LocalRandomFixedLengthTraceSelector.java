package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.annotation.DistanceFromInitialStateAnnotator;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
class LocalRandomFixedLengthTraceSelector extends RandomFixedLengthTraceSelector {
    
    private static final Logger LOG = LoggerFactory.getLogger(LocalRandomFixedLengthTraceSelector.class);

    private final State[] localStates;

    public LocalRandomFixedLengthTraceSelector(LabelledTransitionSystem lts, int size, List<State> localStates) {
        super(lts, size);
        // Annotate LTS with distances to/from initial state
        if (!lts.getProperty(DistanceFromInitialStateAnnotator.PROP_TS_IS_ANNOTATED_DFROM, false)) {
            DistanceFromInitialStateAnnotator.getInstance().annotate(lts);
        }
        // Get local states from the LTS
        List<State> lst = Lists.newArrayList();
        for (State s : localStates) {
            State toAdd = lts.getState(s.getName());
            // Consider only states which can be accessed from the initial 
            // state and that have at least one sucessor
            //checkNotNull(toAdd, "State %s not found in given LTS!", s);
            if (toAdd != null && toAdd.getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, Integer.MAX_VALUE) < Integer.MAX_VALUE && toAdd.outgoingSize() > 0) {
                lst.add(toAdd);
            }
        }
        this.localStates = lst.toArray(new State[lst.size()]);
        LOG.debug("Number local states considered: {}", this.localStates, null);
    }

    @Override
    public List<Action> select() throws IllegalStateException {
        if (localStates.length == 0) {
            LOG.debug("No local states found, will proceed with full random trace selection.");
            return super.select();
        }
        LinkedList<Action> list = Lists.newLinkedList();
        // Select a state from which to start
        State start = localStates[random.nextInt(localStates.length)];
        // Select a prefix for the selected state
        State state = start;
        while(!state.equals(getTransitionSystem().getInitialState())){
            Transition tr = getIncomingTransition(state);
            list.addFirst(tr.getAction());
            state = tr.getFrom();
        }
        // Select a postfix for the selected state
        state = start;
        for (int i = list.size() ; i < getSize() ; i ++){
            Transition trans = getRandomOutgoingTransition(state);
            list.add(trans.getAction());
            state = trans.getTo();
        }
        return list;
    }
    
    private Transition getIncomingTransition(State state) {
        checkArgument(state.incomingSize() > 0, "Found an inaccessible state, should be prevented by class invariant!");
        int dfrom = state.getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1);
        Iterator<Transition> it = state.incomingTransitions();
        List<Transition> previous = Lists.newArrayList();
        while(it.hasNext()){
            Transition candidate = it.next();
            int dfromPrevious = candidate.getFrom().getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1);
            checkState(dfromPrevious >= 0, "State %s has not been annotated using distance from initial state!", candidate.getFrom());
            if(dfromPrevious <= dfrom){
                previous.add(candidate);
            }
        }
        checkState(previous.size() > 0, "No previous state with lesser dfrom value found from state %s!", state);
        return previous.get(random.nextInt(previous.size()));
    }

}
