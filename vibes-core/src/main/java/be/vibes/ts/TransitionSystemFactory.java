package be.vibes.ts;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TransitionSystemFactory {
    
    protected final DefaultTransitionSystem ts;
    
    protected TransitionSystemFactory(DefaultTransitionSystem ts){
        this.ts = ts;
    }
    
    public TransitionSystemFactory(String initialState){
        this(new DefaultTransitionSystem(initialState));
    }
    
    public void addState(String name){
        ts.addState(name);
    }
    
    public void addStates(String... names){
        for(String name: names){
            addState(name);
        }
    }
    
    public void addProposition(String prop){
        ts.addProposition(prop);
    }
    
    public void addPropositions(String... props){
        for(String p: props){
            addProposition(p);
        }
    }
    
    public void labelState(String state, String prop){
        State s = ts.getState(state);
        AtomicProposition p = ts.getAtomicProposition(prop);
        ts.setLabel(s, p);
    }
    
    public void addAction(String action){
        ts.addAction(action);
    }
    
    public void addActions(String... actions){
        for(String action: actions){
            addAction(action);
        }
    }
    
    public void addTransition(String source, String action, String target){
        State src = ts.addState(source);
        State trg = ts.addState(target);
        Action act = ts.addAction(action);
        ts.addTransition(src, act, trg);
    }
    
    public TransitionSystem build(){
        return ts;
    }
    
    public void validate(){
        // TODO Implement checks for deterministic LTS, connected LTS, and other well formed rules
        throw new UnsupportedOperationException("Not implemented yet!");
    }
    
    
}
