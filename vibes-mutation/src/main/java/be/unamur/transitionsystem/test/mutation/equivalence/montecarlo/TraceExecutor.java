
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.exception.VisitException;
import static com.google.common.base.Preconditions.*;
import java.util.List;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
class TraceExecutor{
    
    private LabelledTransitionSystem lts;

    public TraceExecutor(LabelledTransitionSystem lts) {
        checkNotNull(lts, "Argument 'lts' may not be null!");
        this.lts = lts;
    }

    public LabelledTransitionSystem getLts() {
        return lts;
    }
    
    
    
    public boolean execute(List<Action> trace){
        TraceExecutorVisitor visitor = new TraceExecutorVisitor(trace);
        try {
            return lts.getInitialState().accept(visitor);
        } catch (VisitException ex) {
            throw new IllegalStateException("Exception occured using TraceExecutorVisitor, should not happen!", ex);
        }
    }

}
