package be.unamur.transitionsystem.fts;

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

import be.vibes.fexpression.FExpression;
import be.unamur.transitionsystem.AbstractTransitionSystem;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;

import static com.google.common.base.Preconditions.*;

/**
 * This class represents Featured Transition Systems.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class FeaturedTransitionSystem extends
        AbstractTransitionSystem<State, FeaturedTransition, Action> {

    /**
     * Creates a new FTS.
     */
    public FeaturedTransitionSystem() {
        super(new FeaturedTransitionSystemElementFactory());
    }

    /**
     * Creates a new copy of the given FTS
     * @param original
     */
    public FeaturedTransitionSystem(FeaturedTransitionSystem original) {
        super(original);
    }

    /**
     * Creates a new copy of the given TS with all features expressions set to
     * true.
     * @param original
     */
    @SuppressWarnings("unchecked")
    public FeaturedTransitionSystem(TransitionSystem original) {
        this();
        importActions((Iterator<Action>) original.actions());
        importStates((Iterator<State>) original.states());
        importTransitions((Iterator<State>) original.states());
        this.setInitialState(getState(original.getInitialState().getName()));
    }

//	@SuppressWarnings("unchecked")
//	public FeaturedTransitionSystem(TransitionSystem original, FExpression defaultFExpr) {
//		this();
//		importActions((Iterator<Action>) original.actions());
//		importStates((Iterator<State>) original.states());
//		importTransitions((Iterator<State>) original.states(), defaultFExpr);
//		this.setInitialState(getState(original.getInitialState().getName()));
//	}
    @Override
    protected void importTransitions(Iterator<State> states) {
        importTransitions(states, FExpression.trueValue());
    }

    protected void importTransitions(Iterator<State> states, FExpression defaultFExpr) {
        State state;
        Iterator<Transition> itTrans;
        Transition trans;
        while (states.hasNext()) {
            state = states.next();
            itTrans = state.outgoingTransitions();
            while (itTrans.hasNext()) {
                trans = itTrans.next();
                if (trans instanceof FeaturedTransition) {
                    addTransition(getState(trans.getFrom().getName()), getState(trans
                            .getTo().getName()), getAction(trans.getAction().getName()),
                            ((FeaturedTransition) trans).getFeatureExpression());
                } else {
                    addTransition(getState(trans.getFrom().getName()), getState(trans
                            .getTo().getName()), getAction(trans.getAction().getName()),
                            defaultFExpr);
                }
            }
        }

    }

    /**
     * Creates a new transition in the FTS with the given source, destination,
     * action, and feature expression. The states and the action have to be part
     * of the transition system.
     *
     * @param from The source state. Pre: this.getState(from.getName()) != null
     * @param to The destination state. Pre: this.getState(to.getName()) != null
     * @param action The aciton of the transition. Pre:
     * this.getAction(action.getName()) != null
     * @param fexpression The feature expression associate to the transition.
     * Pre: fexpression != null
     * @return 
     */
    public FeaturedTransition addTransition(State from, State to, Action action,
            FExpression fexpression) {
        checkNotNull(fexpression);
        FeaturedTransition tr = super.addTransition(from, to, action);
        tr.setFeatureExpression(fexpression);
        return tr;
    }

    @Override
    public FeaturedTransition addTransition(State from, State to, Action action) {
        return addTransition(from, to, action, FExpression.trueValue());
    }

    @Override
    public FeaturedTransitionSystem copy() {
        return new FeaturedTransitionSystem(this);
    }

}
