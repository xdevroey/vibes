package be.unamur.transitionsystem.test.faultinjection;

import java.util.Random;
import java.util.Set;

import be.unamur.transitionsystem.TransitionSystem;

public abstract class FaultInjector<T> {

    protected Random random;
    protected TransitionSystem ts;

    public FaultInjector(TransitionSystem fts) {
        this.ts = fts;
        this.random = new Random();
    }

    public FaultInjector(TransitionSystem fts, long seed) {
        this(fts);
        this.random.setSeed(seed);
    }

    public abstract Set<T> injectFaults(int maxNbrFaults);
}
