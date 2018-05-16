package be.vibes.ts;

import be.vibes.fexpression.configuration.Configuration;
import be.vibes.ts.exception.UnresolvedFExpression;

/**
 * Class implementing this interface allow to project a
 * {@link FeaturedTransitionSystem} to a {@link TransitionSystem} according to a
 * given {@link Configuration}
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public interface Projection {

    /**
     * This method performs a total projection of the FTS using the given
     * product.
     *
     * @param fts The FTS to project.
     * @param product The configuration to use for projection.
     * @return A new TS that is the projection of the FTS using the given
     * product.
     * @throws UnresolvedFExpression If an fexpression can not be resolved using
     * the given configuration.
     */
    public TransitionSystem project(FeaturedTransitionSystem fts, Configuration product)
            throws UnresolvedFExpression;

}