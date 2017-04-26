
package be.unamur.transitionsystem.test.mutation.exception;

import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.mutation.equivalence.montecarlo.EquivalenceResults;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class EquivalenceComputationException extends Exception {

    private TransitionSystem original;
    private TransitionSystem mutant;
    private EquivalenceResults results;

    public EquivalenceComputationException(String message, TransitionSystem original, TransitionSystem mutant, EquivalenceResults results) {
        super(message);
        this.original = original;
        this.mutant = mutant;
        this.results = results;
    }

    public TransitionSystem getOriginal() {
        return original;
    }

    public TransitionSystem getMutant() {
        return mutant;
    }

    public EquivalenceResults getPartialResults() {
        return results;
    }

}
