package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.test.selection.exception.DissimilarityComputationException;

public interface DissimilarityComputor<T> {

    /**
     * Returns a value between 0.0 (dissimilar) and 1.0 (similar) indicating the
     * similarity degree between o1 and o2. If similarity(o1,o2) == 1.0, then
     * o1.equals(o2) is true.
     *
     * @param o1 The first object to compare.
     * @param o2 The second object to compare.
     * @return a Value between 0.0 (dissimilar) and 1.0 (similar).
     * @throws DissimilarityComputationException
     */
    public double dissimilarity(T o1, T o2) throws DissimilarityComputationException;

}
