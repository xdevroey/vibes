
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.selection.TestCaseSelector;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;
import com.google.common.collect.Lists;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class TestCaseSelectorWrapper implements TraceSelector {
    
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TestCaseSelectorWrapper.class);
    
    private TestCaseSelector selector;

    public TestCaseSelectorWrapper(TestCaseSelector selector) {
        this.selector = selector;
    }
    
    @Override
    public List<Action> select() throws IllegalStateException {
        List<Action> lst = null;
        try {
            lst = Lists.newArrayList(selector.select());
        } catch (TestCaseSelectionException ex) {
            LOG.debug("Exception occured while selecting test-case!", ex);
            throw new IllegalStateException("Exception occured while selecting test-case!", ex);
        }
        return lst;
    }

    @Override
    public TransitionSystem getTransitionSystem() {
        return selector.getTransitionSystem();
    }
    
    

}
