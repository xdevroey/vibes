package be.vibes.ts.coverage;

import be.vibes.ts.Transition;

import java.util.Objects;

/**
 * Utility class representing a pair of transitions. Used to compute transition pair coverage.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 * @see TransitionPairCoverage
 */
public class TransitionPair {

    private Transition first;
    private Transition second;

    public TransitionPair(Transition first, Transition second) {
        this.first = first;
        this.second = second;
    }

    public Transition getFirst() {
        return first;
    }

    public Transition getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransitionPair that = (TransitionPair) o;
        return Objects.equals(first, that.first) &&
                Objects.equals(second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
