package be.vibes.ts;

import be.vibes.fexpression.FExpression;
import be.vibes.fexpression.configuration.Configuration;
import java.util.Iterator;

/**
 * This class implements a simple projection operator. For each transition, if
 * the transition has a feature expression evaluate to true for the given
 * configuration, the transition is kept in the projected
 * {@link TransitionSystem}. In this implementation, if the feature expression
 * is false or not completely evaluated, the transition is discarded from the
 * new transition system. States that do not appear in at least one transition
 * evaluated to true are discarded. Actions that dot not appear in at least one
 * transition evaluated to true are discarded.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class SimpleProjection implements Projection {

    private static SimpleProjection instance = null;

    public static SimpleProjection getInstance() {
        return instance == null ? instance = new SimpleProjection() : instance;
    }

    protected SimpleProjection() {
    }

    @Override
    public TransitionSystem project(FeaturedTransitionSystem fts,
            Configuration product) {
        TransitionSystemFactory factory = new TransitionSystemFactory(fts.getInitialState().getName());
        Iterator<State> it = fts.states();
        while (it.hasNext()) {
            State s = it.next();
            Iterator<Transition> itTr = fts.getOutgoing(s);
            while (itTr.hasNext()) {
                Transition tr = itTr.next();
                FExpression expr = fts.getFExpression(tr).assign(product);
                if (expr.isTrue()) {
                    String source = tr.getSource().getName();
                    String action = tr.getAction().getName();
                    String target = tr.getTarget().getName();
                    factory.addState(source);
                    factory.addAction(action);
                    factory.addState(target);
                    factory.addTransition(source, action, target);
                }
            }
        }
        return factory.build();
    }

}
