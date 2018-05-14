
package be.vibes.selection;

import be.vibes.selection.exception.TestCaseSelectionException;
import be.vibes.ts.TestCase;
import be.vibes.ts.TransitionSystem;
import java.util.List;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public interface TestCaseSelector {
    
    public TransitionSystem getTransitionSystem();
    
    public List<TestCase> select(int nbr) throws TestCaseSelectionException;
    
}
