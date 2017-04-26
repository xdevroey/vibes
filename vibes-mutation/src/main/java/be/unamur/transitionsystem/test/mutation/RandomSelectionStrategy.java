package be.unamur.transitionsystem.test.mutation;

/*
 * #%L
 * vibes-mutation
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
import java.util.Random;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;

public class RandomSelectionStrategy implements ActionSelectionStrategy, TransitionSelectionStrategy, StateSelectionStrategy {

    private Random random;

    public RandomSelectionStrategy() {
        this.random = new Random();
    }

    public RandomSelectionStrategy(long seed) {
        this();
        this.random.setSeed(seed);
    }

    @Override
    public Action selectAction(MutationOperator operator, TransitionSystem ts) {
        int elementIndex = this.random.nextInt(ts.numberOfActions());
        int i = -1;
        Action act = null;
        @SuppressWarnings("rawtypes")
        Iterator actIt = ts.actions();
        while (actIt.hasNext() && i < elementIndex) {
            act = (Action) actIt.next();
            i++;
        }
        return act;
    }

    @Override
    public Transition selectTransition(MutationOperator operator, TransitionSystem ts) {
        State randomState = selectState(operator, ts);
        int elementIndex;
        Iterator<Transition> itTrans;
        if (randomState.outgoingSize() == 0) {
            elementIndex = this.random.nextInt(randomState.incomingSize());
            itTrans = randomState.incomingTransitions();
        } else {
            elementIndex = this.random.nextInt(randomState.outgoingSize());
            itTrans = randomState.outgoingTransitions();
        }
        int i = -1;
        Transition tr = null;
        while (itTrans.hasNext() && i < elementIndex) {
            tr = itTrans.next();
            i++;
        }
        return tr;
    }

    @Override
    public State selectState(MutationOperator operator, TransitionSystem ts) {
        int elementIndex = this.random.nextInt(ts.numberOfStates());
        int i = -1;
        State state = null;
        State previous = null;
        @SuppressWarnings("rawtypes")
        Iterator stateIt = ts.states();
        while (stateIt.hasNext() && i < elementIndex) {
            previous = state;
            state = (State) stateIt.next();
            i++;
        }
        if (ts.getInitialState().equals(state)) {
            if (stateIt.hasNext()) {
                return (State) stateIt.next();
            } else {
                return previous;
            }
        } else {
            return state;
        }
    }

}
