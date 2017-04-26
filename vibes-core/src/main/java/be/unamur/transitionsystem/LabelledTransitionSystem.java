package be.unamur.transitionsystem;

/*
 * #%L
 * vibes-core
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

/**
 * This class represents a Labelled Transition System.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class LabelledTransitionSystem extends AbstractTransitionSystem<State, Transition, Action> {

    /**
     * Creates a new LTS.
     */
    public LabelledTransitionSystem() {
        super(new LabelledTransitionSystemElementFactory());
    }

    /**
     * Creates a new copy of the given LTS.
     * @param original The original transition system to copy.
     */
    @SuppressWarnings("unchecked")
    public LabelledTransitionSystem(TransitionSystem original) {
        this();
        importActions((Iterator<Action>) original.actions());
        importStates((Iterator<State>) original.states());
        importTransitions((Iterator<State>) original.states());
        this.setInitialState(getState(original.getInitialState().getName()));
    }

    @Override
    public TransitionSystem copy() {
        return new LabelledTransitionSystem(this);
    }

}
