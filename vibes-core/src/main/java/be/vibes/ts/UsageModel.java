package be.vibes.ts;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public interface UsageModel extends TransitionSystem{
    
    public double getProbability(Transition transition);
    
    public void setProbability(Transition transition, double proba);
    
}
