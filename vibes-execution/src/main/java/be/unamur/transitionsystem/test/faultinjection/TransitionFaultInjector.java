package be.unamur.transitionsystem.test.faultinjection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;

public class TransitionFaultInjector extends FaultInjector<Transition> {

    private static final Logger logger = LoggerFactory.getLogger(TransitionFaultInjector.class);

    public TransitionFaultInjector(TransitionSystem ts) {
        super(ts);
    }

    public TransitionFaultInjector(TransitionSystem ts, long seed) {
        super(ts, seed);
    }

    @Override
    public Set<Transition> injectFaults(int maxNbrFaults) {
        Set<Transition> faults = new HashSet<Transition>();
        State state;
        Transition tr;
        @SuppressWarnings("unchecked")
        List<State> states = Lists.newArrayList(this.ts.states());
        for (int i = 0; i < maxNbrFaults; i++) {
            logger.trace("Injecting fault " + i);
            state = states.get(this.random.nextInt(states.size()));
            List<Transition> transitions = Lists.newArrayList(state.outgoingTransitions());
            tr = transitions.get(this.random.nextInt(transitions.size()));
            logger.debug("Transition {} in state {} will be considered as faulty", tr, state);
            faults.add(tr);
        }
        return faults;
    }

}
