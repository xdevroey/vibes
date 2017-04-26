package be.unamur.transitionsystem.test.selection.fts;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.selection.SortedList;

public class DummyAllTransitionsGenerator {

    private Map<State, SortedList<ScoredPair<Transition, FExpression>>> nextsCloseToInit;
    private FeaturedTransitionSystem fts;

    public DummyAllTransitionsGenerator(FeaturedTransitionSystem fts) {
        this.fts = fts;
        nextsCloseToInit = Maps.newHashMap();
        backtrack(fts.getInitialState(), 0, FExpression.trueValue());
    }

    private void backtrack(State state, int score, FExpression constr) {
        if (state == fts.getInitialState()) {
            return;
        }
        Iterator<Transition> in = state.incomingTransitions();
        score = score + 1;
        while (in.hasNext()) {
            Transition tr = in.next();
            backtrack(tr.getFrom(), score, addTransition((FeaturedTransition) tr, score, constr));
        }
    }

    private FExpression addTransition(FeaturedTransition tr, int score, FExpression constr) {
        State from = tr.getFrom();
        // Get the list
        SortedList<ScoredPair<Transition, FExpression>> list = nextsCloseToInit.get(from);
        if (list == null) {
            list = new SortedList<ScoredPair<Transition, FExpression>>();
            nextsCloseToInit.put(from, list);
        }
        // Get the FExpression if any
        FExpression fexpr;
        if (tr.getFeatureExpression() == null || tr.getFeatureExpression().equals(FExpression.trueValue())) {
            fexpr = constr;
        } else {
            fexpr = constr.and(tr.getFeatureExpression());
        }
        // add to the list
        list.add(new ScoredPair<Transition, FExpression>(score, tr, fexpr));
        return fexpr;
    }

}
