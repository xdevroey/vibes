package be.unamur.transitionsystem.test.selection;

/*
 * #%L
 * vibes-selection
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;

import com.google.common.collect.Sets;

public class WarshallScoreComputor implements ScoreComputor {

    private static final Logger LOG = LoggerFactory.getLogger(WarshallScoreComputor.class);

    private Map<State, Set<State>> matrix = null;
    private int level = 1;

    @SuppressWarnings("rawtypes")
    @Override
    public void initilise(TransitionSystem ts) {
        this.matrix = new HashMap<>();
        Iterator it = ts.states();
        State s;
        Iterator itTr;
        Transition tr;
        while (it.hasNext()) {
            s = (State) it.next();
            itTr = s.outgoingTransitions();
            while (itTr.hasNext()) {
                tr = (Transition) itTr.next();
                if (tr.getTo() != ts.getInitialState()) {
                    putEntry(s, tr.getTo(), true);
                }
            }
        }
    }

    /**
     * Returns the accessibility level considered for the strategy. The
     * accessibility level gives the number of prevision states accessible from
     * another state of the FTS without considering feature expressions (for
     * performance reasons).
     *
     * @return The accessibility level considered by the strategy.
     */
    public int getLevel() {
        return this.level;
    }

    public void warshall(int times) {
        for (int i = 0; i < times; i++) {
            warshall();
        }
    }

    /**
     * Compute the accessibility Matrix using the Warshall algorithm.
     */
    public void warshall() {
        LOG.info("Computing accessibility matrix using Warshall algorithm");
        boolean a_ik, a_kj, a_ij;
        Set<State> states = Sets.newHashSet(this.matrix.keySet());
        for (State k : states) {
            for (State i : states) {
                a_ik = getEntry(i, k);
                for (State j : states) {
                    a_ij = getEntry(i, j);
                    a_kj = getEntry(k, j);
                    a_ij = a_ij || (a_ik && a_kj);
                    putEntry(i, j, a_ij);
                }
            }
        }
        LOG.info("Done computing accessibility matrix");
        level++;
    }

    @Override
    public int computeScore(Transition transition, Set<State> prior) {
        int score = 0;
        boolean accessible;
        for (State possiblyNext : prior) {
            accessible = getEntry(transition.getTo(), possiblyNext);
            if (accessible) {
                score++;
            }
        }
        return score;
    }

    public boolean getEntry(State from, State to) {
        Set<State> set = this.matrix.get(from);
        if (set == null) {
            return false;
        }
        return set.contains(to);
    }

    public void putEntry(State from, State to, boolean accessible) {
        LOG.trace("Putting entry for states({} , {})  to true", from.getName(),
                to.getName());
        Set<State> set = this.matrix.get(from);
        if (set == null) {
            if (!accessible) {
                return;
            }
            set = new HashSet<>();
            this.matrix.put(from, set);
        }
        if (accessible) {
            set.add(to);
        } else {
            set.remove(to);
        }
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        Set<State> states = Sets.newHashSet(this.matrix.keySet());
        buff.append("[");
        for (State i : states) {
            buff.append(i.getName());
            buff.append("[");
            for (State j : states) {
                buff.append(j.getName());
                buff.append(":");
                buff.append(getEntry(i, j));
                buff.append(", ");
            }
            buff.append("], ");
        }
        buff.append("]");
        return buff.toString();
    }

}
