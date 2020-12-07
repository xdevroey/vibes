package be.vibes.ts.coverage;

import be.vibes.solver.FeatureModel;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.Transition;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.execution.Execution;

import java.util.*;

/**
 * Transition pair criteria for transition systems. The coverage criteria considers pairs of transitions
 * incoming and outgoing of the same state. The set of transition pairs to cover is the
 * set of pairs (incoming, outgoing) for each state in the transition system. A pair is considered covered by
 * a test if the execution of the test contains the sequence (incoming, outgoing). Note: if two transitions of a
 * pair to cover are executed but not one after the other, the pair is considered not covered by the test.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TransitionPairCoverage extends StructuralCoverage<TransitionPair> {

    /**
     * Create a new transition pair coverage criteria for the given transition system.
     *
     * @param ts The transition system for which the coverage criteria will be reported.
     */
    public TransitionPairCoverage(TransitionSystem ts) {
        super(ts);
    }

    /**
     * Creates a new transition pair coverage criteria for the given featured transition system and feature model.
     *
     * @param fts The featured transition system for which the coverage criteria will be reported.
     * @param fm  The feature model used during the execution of the tests to compute the coverage criteria.
     */
    public TransitionPairCoverage(FeaturedTransitionSystem fts, FeatureModel fm) {
        super(fts, fm);
    }

    @Override
    public Iterator<TransitionPair> getElementsToBeCovered() {
        Set<TransitionPair> toBeCovered = new HashSet<>();
        getTs().states().forEachRemaining((state) ->{
            getTs().getIncoming(state).forEachRemaining((incoming)->{
                getTs().getOutgoing(state).forEachRemaining((outgoing) ->{
                    TransitionPair pair = new TransitionPair(incoming, outgoing);
                    toBeCovered.add(pair);
                });
            });
        });
        return toBeCovered.iterator();
    }

    @Override
    protected Set<TransitionPair> getCoveredElements(Execution execution) {
        Set<TransitionPair> covered = new HashSet<>();
        if(!execution.isEmpty()){
            Iterator<Transition> transitions = execution.iterator();
            Transition first = transitions.next();
            Transition second;
            TransitionPair pair;
            while(transitions.hasNext()){
                second = transitions.next();
                pair = new TransitionPair(first, second);
                covered.add(pair);
                first = second;
            }
        }
        return covered;
    }

}
