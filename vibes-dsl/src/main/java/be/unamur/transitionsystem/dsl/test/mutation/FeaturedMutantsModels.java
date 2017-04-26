package be.unamur.transitionsystem.dsl.test.mutation;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.fexpression.Feature;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.dsl.exception.MutantExecutionException;
import be.unamur.transitionsystem.exception.VisitException;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.mutation.FeaturedMutantsModel;
import be.unamur.transitionsystem.test.mutation.FtsMutantVisitor;
import be.unamur.transitionsystem.test.mutation.WrongInitialState;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;

/**
 * This class contains static methods to manipulate a Featured Mutants Model
 * (FMM). A FMM is composed of a FTS and a variability model representing all
 * the possible mutations.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class FeaturedMutantsModels {

    /**
     * This method returns the featured expression representing the alive
     * mutants after the execution of the given test case.
     *
     * @param testCase The test case to execute.
     * @param mutantFts The FMM's FTS representing the mutations performed on
     * the original TS.
     * @param originalInitialStateName The name of the initial state in the
     * original TS. This information is mandatory since the execution of the
     * test case is considered as valid if it ends in the initial state
     * (equivalent to the accepting state) of the original TS.
     * @return A featured expression representing the mutants alive, i.e., the
     * mutants that were able to execute the given test case.
     */
    public static FExpression getAliveMutants(TestCase testCase, FeaturedTransitionSystem mutantFts, String originalInitialStateName) {
        FtsMutantVisitor visitor = new FtsMutantVisitor(testCase, originalInitialStateName);
        try {
            mutantFts.getInitialState().accept(visitor);
        } catch (VisitException e) {
            throw new MutantExecutionException("Error while executing mutant!", e);
        }
        return visitor.getAlive();
    }

    public static FExpression getAliveMutants(TestCase testCase, FeaturedTransitionSystem mutantFts, State originalInitialState) {
        return getAliveMutants(testCase, mutantFts, originalInitialState.getName());
    }

    public static FExpression getAliveMutants(TestCase testCase, FeaturedTransitionSystem mutantFts) {
        return getAliveMutants(testCase, mutantFts, getOriginalInitialState(mutantFts).getName());
    }
    
    public static FExpression getAliveMutants(TestCase testCase, FeaturedMutantsModel fmm) {
        return getAliveMutants(testCase, fmm.getFts());
    }

    public static State getOriginalInitialState(FeaturedTransitionSystem mutantFts) {
        State previousInitState = mutantFts.getInitialState();
        State newInitState = mutantFts.getInitialState();
        boolean continueExploration = true;
        while (continueExploration) {
            Iterator<Transition> it = newInitState.outgoingTransitions();
            while ((previousInitState == newInitState) && it.hasNext()) {
                FeaturedTransition tr = (FeaturedTransition) it.next();
                // Only consider transition added by wrong initial state mutations
                if (tr.getAction().getName().equals(WrongInitialState.WIS_ACTION_NAME)) {
                    // Assign feature expression with FALSE to WIS features
                    FExpression expr = tr.getFeatureExpression().copy();
                    Map<Feature, Boolean> assignements = Maps.newHashMap();
                    for (Feature f : expr.getFeatures()) {
                        if (f.getName().startsWith(WrongInitialState.WIS_SYMBOL)) {
                            assignements.put(f, Boolean.FALSE);
                        } else {
                            assignements.put(f, Boolean.TRUE);
                        }
                    }
                    expr = expr.assign(assignements);
                    // If the transition is true (not WIS transition => leading to initial state), take it
                    if (expr.isTrue()) {
                        newInitState = tr.getTo();
                    }
                }
            }
            if (newInitState != previousInitState) {
                previousInitState = newInitState;
                continueExploration = true;
            } else {
                continueExploration = false;
            }
        }
        return previousInitState;
    }

}
