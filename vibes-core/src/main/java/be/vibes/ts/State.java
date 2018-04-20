package be.vibes.ts;

import com.google.common.base.Preconditions;

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
    
}
