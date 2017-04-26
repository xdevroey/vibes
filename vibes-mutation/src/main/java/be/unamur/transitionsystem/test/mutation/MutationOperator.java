package be.unamur.transitionsystem.test.mutation;

/*
 * #%L
 * vibes-mutation
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.mutation.exception.CounterExampleFoundException;
import be.unamur.transitionsystem.test.mutation.exception.MutationException;
import com.google.common.collect.Sets;
import java.util.Set;

/**
 * Abstract mutation operator on TransitionSystem. <br>
 * <b>Representation invariant:</b>
 * <ul>
 * <li>Call to apply() implies getMutationUniqueKey() returns a non null
 * value</li>
 * <li>Call to apply() implies transpose() returns a non null value</li>
 * <li>Call to apply() implies result() returns a non null value</li>
 * </ul>
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public abstract class MutationOperator {

    private TransitionSystem ts;

    private String mutationUniqueKey = null;

    private Set<State> modifiedStates;

    public MutationOperator(TransitionSystem ts) {
        this.ts = ts;
        this.modifiedStates = Sets.newHashSet();
    }

    /**
     * Returns the original transition system.
     *
     * @return The original transition system on which this operator is applied.
     */
    protected TransitionSystem getTransitionSystem() {
        return this.ts;
    }

    /**
     * Apply the mutation operator to a copy of the transition system provided
     * at the construction of the object. The original transition system is
     * never modified.
     *
     * @throws MutationException If the mutation is not possible
     */
    public abstract void apply() throws MutationException;

    /**
     * Returns the transposition of the mutation operator into a newly create
     * FTS if the argument is null or in the provided FTS. Structure on which
     * the mutation has occured has to be present in the FTS.
     *
     * @param ftsToModify The FTS representing the previous mutations applied on
     * this.getTransitionSystem() or null if there is no previous mutation.
     * @return The FTS representing the previous mutations and this mutation
     * applied on this.getTransitionSystem()
     * @throws MutationException If the apply() method has not been called first
     * or if a required structure is not present in the FTS.
     * @throws CounterExampleFoundException If an equivalent mutatnt is detected.
     */
    public abstract FeaturedTransitionSystem transpose(
            FeaturedTransitionSystem ftsToModify) throws MutationException,
            CounterExampleFoundException;

    /**
     * Returns the result of the application of the mutation operator as a copy
     * of the original transition system.
     *
     * @return The result of the application of the mutation operator on the
     * original system.
     */
    public abstract TransitionSystem result();

    /**
     * Returns a unique key identifying the operator and its operands once it
     * has been applied. Two different MutationOperator instances of the same
     * class applied to the same operands have the same mutation unique key
     * (e.g., if they are removing the same state, they have the same key).
     * Returns null if the apply() method has not been called yet.
     *
     * @return A unique key identifying the operator and its operands once it
     * has been applied.
     */
    public String getMutationUniqueKey() {
        return mutationUniqueKey;
    }

    /**
     * Returns true if the operator has been applied.
     *
     * @return True if the operator has been applied.
     */
    public boolean hasBeenApplied() {
        return getMutationUniqueKey() != null;
    }

    /**
     * Must be called at the end of apply() method.
     *
     * @param mutationUniqueKey The mutation unique key.
     */
    protected void setMutationUniqueKey(String mutationUniqueKey) {
        this.mutationUniqueKey = mutationUniqueKey;
        setFeatureId();
    }

    public Set<State> getModifiedStates() {
        return modifiedStates;
    }

    protected void addModifiedState(State s) {
        modifiedStates.add(s);
    }

    protected void clearModifiedStates() {
        this.modifiedStates = Sets.newHashSet();
    }

    /**
     * Returns a TLA (three letters acronym) representing the operator.
     *
     * @return A TLA representing the operator.
     */
    public abstract String getSymbol();

    public void setActionSelectionStrategy(ActionSelectionStrategy strategy) {
    }

    public void setStateSelectionStrategy(StateSelectionStrategy strategy) {
    }

    public void setTransitionSelectionStrategy(TransitionSelectionStrategy strategy) {
    }

    private static int cptMutation = 0;

    private String featureId = null;

    public String getFeatureId() {
        return featureId;
    }

    protected void setFeatureId() {
        cptMutation++;
        featureId = getSymbol() + "_" + cptMutation;
    }

}
