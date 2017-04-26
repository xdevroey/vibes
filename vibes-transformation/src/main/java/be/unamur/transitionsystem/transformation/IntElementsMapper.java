package be.unamur.transitionsystem.transformation;

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
import java.util.Map;

import com.google.common.collect.Maps;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;

/**
 * Maps elements to integers.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class IntElementsMapper implements ActionMapping<Integer>, TransitionMapping<Integer>, StateMapping<Integer> {

    private Map<Action, Integer> actionsMap;
    private Map<State, Integer> statesMap;
    private Map<Transition, Integer> transitionsMap;

    public IntElementsMapper() {
        actionsMap = Maps.newHashMap();
        statesMap = Maps.newHashMap();
        transitionsMap = Maps.newHashMap();
    }

    private int actionId = 1;

    @Override
    public Integer getActionMapping(Action act) {
        Integer val = actionsMap.get(act);
        if (val == null) {
            val = actionId;
            actionsMap.put(act, val);
            actionId++;
        }
        return val;
    }

    private int stateId = 1;

    @Override
    public Integer getStateMapping(State s) {
        Integer val = statesMap.get(s);
        if (val == null) {
            val = stateId;
            statesMap.put(s, val);
            stateId++;
        }
        return val;
    }

    private int transitionId = 1;

    @Override
    public Integer getTransitionMapping(Transition tr) {
        Integer val = transitionsMap.get(tr);
        if (val == null) {
            val = transitionId;
            transitionsMap.put(tr, val);
            transitionId++;
        }
        return val;
    }

}
