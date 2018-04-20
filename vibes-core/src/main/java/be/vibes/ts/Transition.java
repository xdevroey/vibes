package be.vibes.ts;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class Transition {
    
    private final State source;
    private final Action action;
    private final State target;

    Transition(State source, Action action, State target) {
        super();
        this.source = source;
        this.action = action;
        this.target = target;
    }

    public Action getAction() {
        return action;
    }

    public State getSource() {
        return source;
    }

    public State getTarget() {
        return target;
    }
    
}
