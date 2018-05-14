package be.vibes.selection.dissimilar;

import java.util.Set;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @param <T>
 */
public class AntiDiceDissimilarityComputor<T extends Set> extends JaccardDissimilarityComputor<T> {

    public AntiDiceDissimilarityComputor() {
        super(2.0);
    }

}
