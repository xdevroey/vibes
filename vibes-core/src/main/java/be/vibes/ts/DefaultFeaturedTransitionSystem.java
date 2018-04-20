package be.vibes.ts;

import be.vibes.fexpression.FExpression;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class DefaultFeaturedTransitionSystem extends DefaultTransitionSystem implements FeaturedTransitionSystem{
    
    private final Map<Transition, FExpression> fexpression;

    public DefaultFeaturedTransitionSystem(String initialState) {
        super(initialState);
        this.fexpression = new HashMap<>();
    }    

    @Override
    public FExpression getFExpression(Transition transition) {
        Preconditions.checkNotNull(transition, "Transition may not be null!");
        FExpression fexpr = fexpression.get(transition);
        if(fexpr == null){
            fexpr = FExpression.trueValue();
        }
        return fexpr;
    }
    
    void setFExpression(Transition transition, FExpression fexpr){
        Preconditions.checkNotNull(transition, "Transition may not be null!");
        Preconditions.checkNotNull(fexpr, "Fexpr may not be null!");
        this.fexpression.put(transition, fexpr);
    }
    
}
