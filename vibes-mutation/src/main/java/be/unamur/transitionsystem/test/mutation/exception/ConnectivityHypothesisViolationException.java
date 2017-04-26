
package be.unamur.transitionsystem.test.mutation.exception;

import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.mutation.equivalence.montecarlo.EquivalenceResults;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class ConnectivityHypothesisViolationException extends EquivalenceComputationException{
    
    public ConnectivityHypothesisViolationException(String message, TransitionSystem original, TransitionSystem mutant, EquivalenceResults results) {
        super(message, original, mutant, results);
    }
    
}
