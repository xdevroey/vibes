
package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.TransitionSystem;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public abstract class AbstractTestCaseSelector<T extends TransitionSystem> implements TestCaseSelector {
    
    private final T transitionSystem;
    
    public AbstractTestCaseSelector(T transitionSystem){            
        this.transitionSystem = transitionSystem;
    }

    @Override
    public T getTransitionSystem() {
        return transitionSystem;
    }
    
}
