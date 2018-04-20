package be.vibes.ts;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class DefaultUsageModel extends DefaultTransitionSystem implements UsageModel{
    
    private final Map<Transition, Double> proba;

    public DefaultUsageModel(String initialState) {
        super(initialState);
        this.proba = new HashMap<>();
    }

    @Override
    public double getProbability(Transition transition) {
        Preconditions.checkNotNull(transition, "Transition may not be null!");
        Double pr = proba.get(transition);
        if(pr == null){
            pr = 0.0;
        }
        return pr;
    }

    @Override
    public void setProbability(Transition transition, double proba){
        Preconditions.checkNotNull(transition, "Transition may not be null!");
        Preconditions.checkArgument(proba >= 0 && proba <= 1.0, "Proba must have a value between 0 and 1!");
        this.proba.put(transition, proba);
    }
    
}
