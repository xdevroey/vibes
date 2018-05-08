package be.vibes.ts;

import com.google.common.base.Preconditions;
import java.util.Objects;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class Action extends TransitionSystemElement{
    
    public static final String EPSILON_ACTION = "epsilon";
    
    static final Action EPSILON = new Action(EPSILON_ACTION);
    
    private final String name;

    Action(String name) {
        super();
        Preconditions.checkNotNull(name, "Name may not be null!");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Action{" + "name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.name);
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
        final Action other = (Action) obj;
        return Objects.equals(this.name, other.name);
    }
    
}
