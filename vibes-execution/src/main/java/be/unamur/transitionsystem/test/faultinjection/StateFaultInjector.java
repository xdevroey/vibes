package be.unamur.transitionsystem.test.faultinjection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;

public class StateFaultInjector extends FaultInjector<State> {

    private static final Logger logger = LoggerFactory.getLogger(StateFaultInjector.class);

    public StateFaultInjector(TransitionSystem ts) {
        super(ts);
    }

    public StateFaultInjector(TransitionSystem ts, long seed) {
        super(ts, seed);
    }

    @Override
    public Set<State> injectFaults(int maxNbrFaults) {
        Set<State> faults = new HashSet<State>();
        State state;
        @SuppressWarnings("unchecked")
        List<State> states = Lists.newArrayList(this.ts.states());
        for (int i = 0; i < maxNbrFaults; i++) {
            logger.trace("Injecting fault " + i);
            state = states.get(this.random.nextInt(states.size()));
            logger.debug("State {} will be considered as faulty", state);
            faults.add(state);
        }
        return faults;
    }

}
