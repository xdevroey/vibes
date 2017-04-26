package be.unamur.transitionsystem;

/*
 * #%L vibes-core %% Copyright (C) 2014 PReCISE, University of Namur %%
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. #L%
 */
import be.unamur.transitionsystem.annotation.DistanceFromInitialStateAnnotator;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * Utility class used to clean a model by removing unused or inaccessible
 * elements.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class TransitionSystemCleaner {

    private static final Logger logger = LoggerFactory
            .getLogger(TransitionSystemCleaner.class);

    /**
     * Remove inaccessible states from the {@link TransitionSystem}.
     *
     * @param ts The transition system to clean.
     */
    public static void removeIsolatedStates(TransitionSystem ts) {
        Set<State> toRemove = Sets.newHashSet();
        DistanceFromInitialStateAnnotator.getInstance().annotate(ts);
        Iterator<State> it = ts.states();
        while(it.hasNext()){
            State s = it.next();
            if(s.getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1) == Integer.MAX_VALUE){
                toRemove.add(s);
            }
        }
        for (State s : toRemove) {
            if (!(s == ts.getInitialState())) {
                ts.removeState(s);
            }
        }
    }

    /**
     * Try to remove unused actions from the transition system. If the remove
     * method is not implemented in the {@link TransitionSystem}.actions()
     * iterator, this method has no effect.
     *
     * @param ts The transition system to clean.
     */
    @SuppressWarnings("unchecked")
    public static void removeUnusedActions(TransitionSystem ts) {
        Set<Action> toKeep = Sets.newHashSet();
        for (Iterator<State> itState = ts.states(); itState.hasNext();) {
            State s = (State) itState.next();
            for (Iterator<Transition> it = s.outgoingTransitions(); it.hasNext();) {
                Transition tr = (Transition) it.next();
                toKeep.add(tr.getAction());
            }
        }
        for (Iterator<Action> itAct = ts.actions(); itAct.hasNext();) {
            Action act = itAct.next();
            if (!toKeep.contains(act)) {
                try {
                    itAct.remove();
                } catch (UnsupportedOperationException ex) {
                    logger.debug("Impossible to remove actions from system", ex);
                    break;
                }
            }
        }
    }

}
