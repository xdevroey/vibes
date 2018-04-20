package be.vibes.ts;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;

/**
 * A General class to compute statistics over transition systems.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class TransitionSystemModelStatistics implements ModelStatistics {

    /**
     * Key value used for the number of states in the model.
     */
    public static final String NUMBER_OF_STATES = "NUMBER_OF_STATES";

    /**
     * Key value used for the number of transitions in the model.
     */
    public static final String NUMBER_OF_TRANSITIONS = "NUMBER_OF_TRANSITIONS";

    /**
     * Key value used for the number of actions in the model.
     */
    public static final String NUMBER_OF_ACTIONS = "NUMBER_OF_ACTIONS";

    /**
     * Key value used for the number of atomic propositions in the model.
     */
    public static final String NUMBER_OF_PROPOSITIONS = "NUMBER_OF_PROPOSITIONS";

    public static final String AVG_DEGREE = "AVG_DEGREE";

    public static final String BFS_HEIGHT = "BFS_HEIGHT";

    public static final String NB_BACK_LEVEL_TRANSITIONS = "NB_BACK_LEVEL_TRANSITIONS";

    private final Map<String, Object> values;

    
    private TransitionSystemModelStatistics() {
        this.values = new HashMap<>();
    }
    
    /**
     * Computes statistics on the given TS.
     *
     * @param ts The TS on which computing statistics.
     * @return The statistics.
     */
    public static TransitionSystemModelStatistics getStatistics(TransitionSystem ts){
        TransitionSystemModelStatistics stats = new TransitionSystemModelStatistics();
        stats.put(NUMBER_OF_STATES, ts.getStatesCount());
        stats.put(NUMBER_OF_ACTIONS, ts.getActionsCount());
        stats.put(NUMBER_OF_TRANSITIONS, ts.getTransitionsCount());
        stats.put(NUMBER_OF_PROPOSITIONS, ts.getPropositionsCount());
        stats.put(AVG_DEGREE, ((double) ts.getTransitionsCount()) / ts.getStatesCount());
        Map<State, Integer> stateLevel = Maps.newHashMap();
        stats.put(BFS_HEIGHT, computeBfsHeight(ts, stateLevel));
        stats.put(NB_BACK_LEVEL_TRANSITIONS, computeNbBackLevels(ts, stateLevel));
        return stats;
    }

    private static int computeBfsHeight(TransitionSystem ts, Map<State, Integer> stateLevel) {
        Set<State> visited = Sets.newHashSet(ts.getInitialState());
        List<State> toVisit = Lists.newArrayList(ts.getInitialState());
        int height = 0;
        stateLevel.put(ts.getInitialState(), height);
        while (!toVisit.isEmpty()) {
            State s = toVisit.remove(0);
            Iterator<Transition> it = ts.getOutgoing(s);
            boolean levelAdd = false;
            while (it.hasNext()) {
                Transition t = it.next();
                if (!visited.contains(t.getTarget())) {
                    levelAdd = true;
                    visited.add(t.getTarget());
                    toVisit.add(t.getTarget());
                    stateLevel.put(t.getTarget(), height + 1);
                }
            }
            if (levelAdd) {
                height++;
            }
        }
        return height;
    }

    private static Object computeNbBackLevels(TransitionSystem ts, Map<State, Integer> stateLevel) {
        if (stateLevel.size() != ts.getStatesCount()) {
            Set<State> allStates = Sets.newHashSet(ts.states());
            throw new IllegalStateException("Number of states in ts and statelevel does not match, missing states " + Sets.difference(allStates, stateLevel.keySet()) + "!");
        }
        int nb = 0;
        for (Entry<State, Integer> e : stateLevel.entrySet()) {
            Iterator<Transition> it = ts.getOutgoing(e.getKey());
            while (it.hasNext()) {
                Transition t = it.next();
                if (e.getValue() > stateLevel.get(t.getTarget())) {
                    nb++;
                }
            }
        }
        return nb;
    }

    protected void put(String key, Object value) {
        values.put(key, value);
    }

    protected Object get(String key) {
        return values.get(key);
    }

    /**
     * Returns the number of states in the model.
     *
     * @return
     */
    public int getNumberOfState() {
        return (Integer) get(NUMBER_OF_STATES);
    }

    /**
     * Returns the number of actions in the model.
     *
     * @return
     */
    public int getNumberOfActions() {
        return (Integer) get(NUMBER_OF_ACTIONS);
    }

    /**
     * Returns the number of transitions in the model.
     *
     * @return
     */
    public int getNumberOfTransitions() {
        return (Integer) get(NUMBER_OF_TRANSITIONS);
    }

    public double getAvgDegree() {
        return (Double) get(AVG_DEGREE);
    }

    public int getBfsHeight() {
        return (Integer) get(BFS_HEIGHT);
    }

    public int getNbBackLevelTransitions() {
        return (Integer) get(NB_BACK_LEVEL_TRANSITIONS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see be.unamur.transitionsystem.stat.ModelStatistics#getStatistics()
     */
    @Override
    public String getStatistics() {
        StringBuilder builder = new StringBuilder();
        values.entrySet().forEach((e) -> {
            builder.append(e.getKey())
                    .append(" = ")
                    .append(e.getValue())
                    .append("\n");
        });
        return builder.toString();
    }

    @Override
    public Map<String, Object> getStatisticsValues() {
        return values;
    }

}
