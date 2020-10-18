package be.vibes.dsl.transforme;

/*-
 * #%L
 * VIBeS: dsl
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

import be.vibes.dsl.exception.TransformationException;
import be.vibes.dsl.execution.Execute;
import be.vibes.solver.FeatureModel;
import be.vibes.ts.execution.Execution;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.FeaturedTransitionSystemFactory;
import be.vibes.ts.TestCase;
import be.vibes.ts.TestSet;
import be.vibes.ts.Transition;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.TransitionSystemFactory;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class Prune {

    /**
     * Executes the test cases on the given transition system and returns a
     * fresh transition system that contains only the transition triggered
     * during the execution.
     *
     * @param ts The transition on which the test cases are executed.
     * @param testSet The set of test cases to execute.
     * @return A fresh transition system that contains only the transition
     * triggered during the execution.
     */
    public static TransitionSystem prune(TransitionSystem ts, TestSet testSet) {
        List<Execution> executions = Lists.newArrayList();
        for (TestCase tc : testSet) {
            executions.addAll(Execute.execute(ts, tc));
        }
        return prune(ts, executions);
    }

    public static TransitionSystem prune(TransitionSystem ts, TestCase testcase) {
        return prune(ts, Execute.execute(ts, testcase));
    }

    public static TransitionSystem prune(TransitionSystem ts, Execution execution) {
        List<Execution> executions = new ArrayList<>();
        executions.add(execution);
        return prune(ts, executions);
    }

    public static TransitionSystem prune(TransitionSystem ts, List<Execution> executions) {
        TransitionSystemFactory factory = new TransitionSystemFactory(ts.getInitialState().getName());
        executions.forEach((exec) -> {
            exec.forEach((tr) -> {
                addTransition(factory, ts, tr);
            });
        });
        return factory.build();
    }

    private static void addTransition(TransitionSystemFactory factory, TransitionSystem ts, Transition tr) {
        CheckTsContainsTransition(ts, tr);
        factory.addState(tr.getSource().getName());
        factory.addState(tr.getTarget().getName());
        factory.addAction(tr.getAction().getName());
        factory.addTransition(tr.getSource().getName(), tr.getAction().getName(), tr.getTarget().getName());
    }
    
    private static void CheckTsContainsTransition(TransitionSystem ts, Transition tr) throws TransformationException{
        if(ts.getState(tr.getSource().getName()) == null){
            throw new TransformationException("Transition " + tr + " does not belong to the given transition system, source state not found!");
        }
        if(ts.getState(tr.getTarget().getName()) == null){
            throw new TransformationException("Transition " + tr + " does not belong to the given transition system, target state not found!");
        }
        if(ts.getAction(tr.getAction().getName()) == null){
            throw new TransformationException("Transition " + tr + " does not belong to the given transition system, action not found!");
        }
    }

    
    public static FeaturedTransitionSystem prune(FeaturedTransitionSystem fts, FeatureModel fm, TestSet testSet) {
        List<Execution> executions = Lists.newArrayList();
        for (TestCase tc : testSet) {
            executions.addAll(Execute.execute(fts, fm, tc));
        }
        return prune(fts, executions);
    }

    public static TransitionSystem prune(FeaturedTransitionSystem fts, FeatureModel fm, TestCase testcase) {
        return prune(fts, Execute.execute(fts, fm, testcase));
    }

    public static FeaturedTransitionSystem prune(FeaturedTransitionSystem fts, Execution execution) {
        List<Execution> executions = new ArrayList<>();
        executions.add(execution);
        return prune(fts, executions);
    }

    public static FeaturedTransitionSystem prune(FeaturedTransitionSystem fts, List<Execution> executions) {
        FeaturedTransitionSystemFactory factory = new FeaturedTransitionSystemFactory(fts.getInitialState().getName());
        executions.forEach((exec) -> {
            exec.forEach((tr) -> {
                addFeaturedTransition(factory, fts, tr);
            });
        });
        return factory.build();
    }

    private static void addFeaturedTransition(FeaturedTransitionSystemFactory factory, FeaturedTransitionSystem fts, Transition tr) {
        CheckTsContainsTransition(fts, tr);
        factory.addState(tr.getSource().getName());
        factory.addState(tr.getTarget().getName());
        factory.addAction(tr.getAction().getName());
        factory.addTransition(tr.getSource().getName(), tr.getAction().getName(), fts.getFExpression(tr), tr.getTarget().getName());
    }

}
