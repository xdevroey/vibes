
package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import java.util.List;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public interface TestCaseSelector {
    
    public TransitionSystem getTransitionSystem();
    
    public TestCase select() throws TestCaseSelectionException;
    
    public List<TestCase> select(int nbr) throws TestCaseSelectionException;
    
    public void select(int nbr, TestCaseWrapUp wrapUp) throws TestCaseSelectionException;

}
