package be.vibes.ts;

import be.vibes.fexpression.FExpression;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class FeaturedTransitionSystemFactory extends TransitionSystemFactory{
    
    public FeaturedTransitionSystemFactory(String initialState) {
        super(new DefaultFeaturedTransitionSystem(initialState));
    }

    @Override
    public void addTransition(String source, String action, String target) {
        addTransition(source, action, FExpression.trueValue(), target);
    }
    
    public void addTransition(String source, String action, FExpression fexpr, String target){
        DefaultFeaturedTransitionSystem fts = (DefaultFeaturedTransitionSystem) ts;
        State src = fts.addState(source);
        State trg = fts.addState(target);
        Action act = fts.addAction(action);
        Transition tr = fts.addTransition(src, act, trg);
        fts.setFExpression(tr, fexpr);
    }

    @Override
    public FeaturedTransitionSystem build() {
        return (DefaultFeaturedTransitionSystem) super.build();
    }
    
}
