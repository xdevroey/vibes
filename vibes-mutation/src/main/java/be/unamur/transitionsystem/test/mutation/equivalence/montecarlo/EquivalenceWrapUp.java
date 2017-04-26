/**
 *
 */
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.selection.TestCaseWrapUp;

/**
 * @author gilles.perrouin
 *
 */
@Deprecated
public class EquivalenceWrapUp implements TestCaseWrapUp {

    private int nbEquiv = 0;
    private TransitionSystem source;

    public EquivalenceWrapUp(TransitionSystem source) {
        this.source = source;
    }

    /* (non-Javadoc)
     * @see be.unamur.transitionsystem.test.selection.TestCaseWrapUp#wrapUp(be.unamur.transitionsystem.test.TestCase)
     */
    @Override
    public void wrapUp(TestCase testCase) {
        // TODO Auto-generated method stub

        if (MonteCarloEquivalenceFromFile.checkValidity(source, testCase)) {
            nbEquiv++;
        }
    }

    public int getEquivalentsNb() {
        return nbEquiv;
    }

}
