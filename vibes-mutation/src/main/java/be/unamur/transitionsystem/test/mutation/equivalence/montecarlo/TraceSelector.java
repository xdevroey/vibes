
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.TransitionSystem;
import java.util.List;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
interface TraceSelector {

    /**
     *
     * @return
     * @throws IllegalStateException If the algorithm find a sink state.
     */
    List<Action> select() throws IllegalStateException;
    
    TransitionSystem getTransitionSystem();

}
