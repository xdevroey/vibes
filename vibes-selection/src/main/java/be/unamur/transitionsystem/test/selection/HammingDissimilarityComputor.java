package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.test.selection.exception.DissimilarityComputationException;
import java.util.Set;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @param <T>
 */
public class HammingDissimilarityComputor<T extends Set> implements SetBasedDissimilarityComputor<T> {

    private T allElements;

    /**
     * Build a new Hamming dissimilarity computor.
     *
     * @param allElements The input domain of the sets to compare. All the
     * elements of the sets used with the dissimilarity method must be included
     * in allElements.
     */
    public HammingDissimilarityComputor(T allElements) {
        this.allElements = allElements;
    }

    @Override
    public double dissimilarity(T s1, T s2) throws DissimilarityComputationException {
        return 1 - getDistance(s1, s2);
    }

    public double getDistance(T s1, T s2) {
        int cptIdentical = 0;
        for (Object o : allElements) {
            boolean inS1 = s1.contains(o);
            boolean inS2 = s2.contains(o);
            if ((inS1 && inS2) || (!inS1 && !inS2)) {
                cptIdentical++;
            }
        }
        return ((double) cptIdentical) / allPossibleElementsCount();
    }
    
    public int allPossibleElementsCount(){
        return allElements.size();
    }
    
}
