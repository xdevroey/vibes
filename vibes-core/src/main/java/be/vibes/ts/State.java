package be.vibes.ts;

import com.google.common.base.Preconditions;
import java.util.Objects;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class State extends TransitionSystemElement{
    
    private final String name;

    State(String name) {
        super();
        Preconditions.checkNotNull(name, "Name may not be null!");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "State{" + "name=" + name + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final State other = (State) obj;
        return Objects.equals(this.name, other.name);
    }
    
    
}
