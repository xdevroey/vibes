
package be.vibes.selection.dissimilar;

import be.vibes.selection.exception.DissimilarityComputationException;
import com.google.common.collect.Multiset;
import java.util.Iterator;
import static java.lang.Integer.min;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @param <T>
 */
public class CountingDissimilarityComputor<T extends Multiset> implements DissimilarityComputor<T>{

    public CountingDissimilarityComputor() {
    }

    @Override
    public double dissimilarity(T s1, T s2) throws DissimilarityComputationException {
        int countIdentical = 0;
        Iterator it = s1.entrySet().iterator();
        while(it.hasNext()){
            Multiset.Entry e = (Multiset.Entry) it.next();
            countIdentical = countIdentical + min(e.getCount(), s2.count(e.getElement()));
        }
        return 1.0 - (countIdentical / ((s1.size() + s2.size()) / 2.0));
    }
    
    

}
