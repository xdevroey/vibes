package be.unamur.transitionsystem.transformation.fts;

/*
 * #%L
 * vibes-transformation
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
import java.util.Iterator;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.fexpression.Feature;
import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import static be.unamur.transitionsystem.TransitionSystemCleaner.*;

/**
 * This class implements a simple projection operator. For each transition, if
 * the transition has a feature expression evaluate to true for the given
 * configuration, the transition is kept in the projected
 * {@link TransitionSystem}.
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
    public LabelledTransitionSystem project(FeaturedTransitionSystem fts,
            Configuration product) {
        LabelledTransitionSystem ts = new LabelledTransitionSystem();
        State s = ts.addState(fts.getInitialState().getName());
        ts.setInitialState(s);
        Iterator<State> it = fts.states();
        while (it.hasNext()) {
            s = it.next();
            Iterator<Transition> itTr = s.outgoingTransitions();
            FeaturedTransition tr;
            while (itTr.hasNext()) {
                tr = (FeaturedTransition) itTr.next();
                FExpression expr = tr.getFeatureExpression().assign(product);
                if (expr.isTrue()) {
                    State from = ts.addState(tr.getFrom().getName());
                    State to = ts.addState(tr.getTo().getName());
                    Action act = ts.addAction(tr.getAction().getName());
                    ts.addTransition(from, to, act);
                }
            }
        }
        removeIsolatedStates(ts);
        removeUnusedActions(ts);
        return ts;
    }

}
