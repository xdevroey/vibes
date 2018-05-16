package be.vibes.dsl.ts;

/**
 * This abstract class allows to define transition systems (TS) by
 * extending it and implementing the define() method.
 *
 * @see AbstractTransitionSystemDefinition
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public abstract class TransitionSystemDefinition extends AbstractTransitionSystemDefinition {

    /**
     * Creates a new LTS definition corresponding to the declaration made in
     * this.define().
     */
    public TransitionSystemDefinition() {
        super();
    }

}
