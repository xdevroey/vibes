package be.unamur.transitionsystem.test.mutation;

import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import static be.unamur.transitionsystem.TransitionSystemCleaner.removeIsolatedStates;
import static be.unamur.transitionsystem.TransitionSystemCleaner.removeUnusedActions;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.transformation.fts.SimpleProjection;

/**
 * This singleton class may be used to project a mutant using a given
 * configuration. The difference with the SimpleProjection is that this
 * implementation removes artifical transitions added by mutation operators.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class MutantProjection extends SimpleProjection {

    private static MutantProjection instance = null;

    public static MutantProjection getInstance() {
        return instance == null ? instance = new MutantProjection() : instance;
    }

    protected MutantProjection() {
    }

    @Override
    public LabelledTransitionSystem project(FeaturedTransitionSystem mutantFts, Configuration product) {
        LabelledTransitionSystem lts = super.project(mutantFts, product);
        // Remove artificial transitions added by WIS operator
        State initiState = lts.getInitialState();
        if (initiState.outgoingSize() == 1) {
            Transition tr = initiState.outgoingTransitions().next();
            while (initiState.outgoingSize() == 1
                    && tr.getAction().getName().equals(WrongInitialState.WIS_ACTION_NAME)) {
                // Remove wrong initial state
                lts.setInitialState(tr.getTo());
                lts.removeState(initiState);
                // Set initial state to next state
                initiState = tr.getTo();
                if (initiState.outgoingSize() == 1) {
                    tr = initiState.outgoingTransitions().next();
                }
            }
        }
        lts.setInitialState(initiState);
        removeIsolatedStates(lts);
        removeUnusedActions(lts);
        return lts;
    }

}
