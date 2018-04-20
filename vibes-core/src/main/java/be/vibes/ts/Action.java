package be.vibes.ts;

import com.google.common.base.Preconditions;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class Action extends TransitionSystemElement{
    
    static final Action EPSILON = new Action("epsilon");
    
    private final String name;

    Action(String name) {
        super();
        Preconditions.checkNotNull(name, "Name may not be null!");
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
