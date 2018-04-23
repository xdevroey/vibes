package be.vibes.ts;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Transition{" + "source=" + source + ", action=" + action + ", target=" + target + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.source);
        hash = 23 * hash + Objects.hashCode(this.action);
        hash = 23 * hash + Objects.hashCode(this.target);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transition other = (Transition) obj;
        if (!Objects.equals(this.source, other.source)) {
            return false;
        }
        if (!Objects.equals(this.action, other.action)) {
            return false;
        }
        return Objects.equals(this.target, other.target);
    }
    
    
    
}
