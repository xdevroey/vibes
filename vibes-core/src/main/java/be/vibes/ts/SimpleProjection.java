package be.vibes.ts;

/*-
 * #%L
 * VIBeS: core
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
