/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.collections.CollectionUtils;

/**
 * @deprecated
 * @author Gilles Perrouin
 */

public class MutantDifference {

    private static final Logger logger = LoggerFactory.getLogger(MutantDifference.class);

    public static ArrayList<State> getLTSDiffStates(LabelledTransitionSystem original, LabelledTransitionSystem mutant) {

        logger.debug("Starting");
        ArrayList<State> res;
        res = new ArrayList<>(); // list of interesintg states on original
        //res[1] = new ArrayList<>(); // list of interesting states on mutant

        //if (original.numberOfStates() >= mutant.numberOfStates()) {
        if (!original.getInitialState().getName().equals(mutant.getInitialState().getName())) {
            res.add(mutant.getInitialState());
        }

        Iterator<State> it = original.states();
        while (it.hasNext()) {
            State s = it.next();
            if (mutant.getState(s.getName()) == null) {
                logger.debug("did not find {} in mutant !!!", s.getName());
                res.add(s);
            } else {
                if ((s.incomingSize() != mutant.getState(s.getName()).incomingSize())
                        || (s.outgoingSize() != mutant.getState(s.getName()).outgoingSize())) {
                    logger.debug("orig: {} incoming {} outgoing {}" , s.getName(), s.incomingSize(), s.outgoingSize());
                    logger.debug("mutant: {} incoming {} outgoing {}", s.getName(), mutant.getState(s.getName()).incomingSize(), mutant.getState(s.getName()).outgoingSize());
                    res.add(s);
                 

                } else {
                    Iterator<Transition> oInTrans = s.incomingTransitions();
                    Iterator<Transition> mInTrans = mutant.getState(s.getName()).incomingTransitions();
                    Iterator<Transition> oOutTrans = s.outgoingTransitions();
                    Iterator<Transition> mOutTrans = mutant.getState(s.getName()).outgoingTransitions();

                    ArrayList<String> oInActions = Lists.newArrayList();
                    ArrayList<String> mInActions = Lists.newArrayList();

                    while (oInTrans.hasNext() && mInTrans.hasNext()) {

                        oInActions.add(oInTrans.next().getAction().getName());
                        mInActions.add(mInTrans.next().getAction().getName());

                    }

                    ArrayList<String> oOutActions = Lists.newArrayList();
                    ArrayList<String> mOutActions = Lists.newArrayList();

                    while (oOutTrans.hasNext() && mOutTrans.hasNext()) {

                        oOutActions.add(oOutTrans.next().getAction().getName());
                        mOutActions.add(mOutTrans.next().getAction().getName());

                    }

                    java.util.Collection diffIn = CollectionUtils.disjunction(mInActions, oInActions);
                    java.util.Collection diffOut = CollectionUtils.disjunction(mOutActions, oOutActions);

                    if (!diffIn.isEmpty() || !diffOut.isEmpty()) {
                        if (!diffIn.isEmpty()) {
                            logger.debug("in actions may differ for state {} o:  {} m: {}", s.getName(), mInActions, oInActions);
                        }
                        if (!diffOut.isEmpty()) {
                            logger.debug("out actions may differ for state {} o:  {} m: {}" , s.getName(), mOutActions, oOutActions);
                        }
                        res.add(s);
                    
                    }

                }
            }
        }
        logger.debug("States orig {}", res);
        logger.debug("States mutant {}", res);
        return res;
    }
}
