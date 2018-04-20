package be.vibes.ts;

import be.vibes.fexpression.FExpression;

/**
 * An FTS is a tuple (S, Act, trans, i, AP, L, d, gamma). Where S, Act, trans,
 * i, AP, L are a TransitionSystem; d is a feature model; gamma : trans -&gt;
 * B(N) is a total function, labeling each transition with a feature expression,
 * i.e., a Boolean expression over the features.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public interface FeaturedTransitionSystem extends TransitionSystem {
    
    public FExpression getFExpression(Transition transition);

}
