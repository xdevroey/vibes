package be.unamur.transitionsystem.test.faultinjection;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;

public class ActionFaultInjector extends FaultInjector<Action> {

    private static final Logger logger = LoggerFactory.getLogger(ActionFaultInjector.class);

    public ActionFaultInjector(TransitionSystem ts) {
        super(ts);
    }

    public ActionFaultInjector(TransitionSystem ts, long seed) {
        super(ts, seed);
    }

    @Override
    public Set<Action> injectFaults(int maxNbrFaults) {
        Set<Action> faults = Sets.newHashSet();
        State state;
        Transition tr;
        @SuppressWarnings("unchecked")
        List<State> states = Lists.newArrayList(this.ts.states());
        for (int i = 0; i < maxNbrFaults; i++) {
            logger.trace("Injecting fault " + i);
            state = states.get(this.random.nextInt(states.size()));
            List<Transition> transitions = Lists.newArrayList(state.outgoingTransitions());
            tr = transitions.get(this.random.nextInt(transitions.size()));
            logger.debug("Action {} of transition {} in state {} will be considered as faulty", tr.getAction(), tr, state);
            faults.add(tr.getAction());
        }
        return faults;
    }
}
