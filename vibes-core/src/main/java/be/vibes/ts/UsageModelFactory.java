package be.vibes.ts;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class UsageModelFactory extends TransitionSystemFactory{
    
    public UsageModelFactory(String initialState) {
        super(new DefaultUsageModel(initialState));
    }
    
    @Override
    public void addTransition(String source, String action, String target) {
        addTransition(source, action, 0.0, target);
    }
    
    public void addTransition(String source, String action, double proba, String target){
        DefaultUsageModel um = (DefaultUsageModel) ts;
        State src = um.addState(source);
        State trg = um.addState(target);
        Action act = um.addAction(action);
        Transition tr = um.addTransition(src, act, trg);
        um.setProbability(tr, proba);
    }
    
    @Override
    public DefaultUsageModel build() {
        return (DefaultUsageModel) super.build();
    }
    
}