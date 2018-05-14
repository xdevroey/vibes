
package be.vibes.selection;

import be.vibes.ts.TransitionSystem;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public abstract class AbstractTestCaseSelector implements TestCaseSelector {
    
    private final TransitionSystem transitionSystem;
   
    
    public AbstractTestCaseSelector(TransitionSystem transitionSystem){
        this.transitionSystem = transitionSystem;
    }

    @Override
    public TransitionSystem getTransitionSystem() {
        return transitionSystem;
    }
    
}
