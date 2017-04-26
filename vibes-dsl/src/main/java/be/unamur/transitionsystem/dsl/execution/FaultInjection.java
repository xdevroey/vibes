package be.unamur.transitionsystem.dsl.execution;

import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.faultinjection.ActionFaultInjector;
import be.unamur.transitionsystem.test.faultinjection.StateFaultInjector;
import be.unamur.transitionsystem.test.faultinjection.FaultStatistics;
import be.unamur.transitionsystem.test.faultinjection.TransitionFaultInjector;

/**
 * This class contains utility methods to perform fault injection on a given
 * transition system. The injection returns a FaultStatistics object describing
 * the faulty states, actions, and transitions. Here is a simple usage scenario:
 * <pre>
 * FaultStatistics faults = injectIn(ts)
 *      .faultyActions(20)
 *      .faultyStates(5)
 *      .faultyTransitions(30)
 *      .done();
 * </pre>
 *
 * @see FaultStatistics
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class FaultInjection {

    private FaultStatistics exec;
    private TransitionSystem ts;

    private FaultInjection(TransitionSystem ts) {
        this.exec = new FaultStatistics();
        this.ts = ts;
    }

    /**
     * Creates a new fault injection.
     *
     * @param ts The transition system in which the fault will be injected.
     * @return A new fault injection object to parametrize.
     */
    public static FaultInjection injectIn(TransitionSystem ts) {
        return new FaultInjection(ts);
    }

    /**
     * Specify the maximum number of faulty actions. Actions are selected in a
     * random way.
     *
     * @param nbrActions The maximum number of faulty actions.
     * @return this
     */
    public FaultInjection faultyActions(int nbrActions) {
        exec.setErrorActions(new ActionFaultInjector(this.ts).injectFaults(nbrActions));
        return this;
    }

    /**
     * Specify the maximum number of faulty actions. Actions are selected in a
     * random way.
     *
     * @param nbrActions The maximum number of faulty actions.
     * @param seed The seed to use for the random selection of the actions.
     * @return this
     */
    public FaultInjection faultyActions(int nbrActions, long seed) {
        exec.setErrorActions(new ActionFaultInjector(this.ts, seed).injectFaults(nbrActions));
        return this;
    }

    /**
     * Specify the maximum number of faulty states. States are selected in a
     * random way.
     *
     * @param nbrStates The maximum number of faulty states.
     * @return this
     */
    public FaultInjection faultyStates(int nbrStates) {
        exec.setErrorStates(new StateFaultInjector(this.ts).injectFaults(nbrStates));
        return this;
    }

    /**
     * Specify the maximum number of faulty states. States are selected in a
     * random way.
     *
     * @param nbrStates The maximum number of faulty states.
     * @param seed The seed to use for the random selection of the states.
     * @return this
     */
    public FaultInjection faultyStates(int nbrStates, long seed) {
        exec.setErrorStates(new StateFaultInjector(this.ts, seed).injectFaults(nbrStates));
        return this;
    }

    /**
     * Specify the maximum number of faulty transitions. Transitions are
     * selected in a random way.
     *
     * @param nbrTransitions The maximum number of faulty transitions.
     * @return this
     */
    public FaultInjection faultyTransitions(int nbrTransitions) {
        exec.setErrorTransitions(new TransitionFaultInjector(this.ts).injectFaults(nbrTransitions));
        return this;
    }

    /**
     * Specify the maximum number of faulty transitions. Transitions are
     * selected in a random way.
     *
     * @param nbrTransitions The maximum number of faulty transitions.
     * @param seed The seed to use for the random selection of the transitions.
     * @return this
     */
    public FaultInjection faultyTransitions(int nbrTransitions, long seed) {
        exec.setErrorTransitions(new TransitionFaultInjector(this.ts, seed).injectFaults(nbrTransitions));
        return this;
    }

    /**
     * Achieve the configuration to perform the injection and return a
     * FaultStatistics object.
     *
     * @return The description of the injected fault according to this
     * configuration.
     */
    public FaultStatistics done() {
        return exec;
    }

}
