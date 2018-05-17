package be.vibes.ts;

/*-
 * #%L
 * VIBeS: core
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

import java.util.Iterator;

/**
 * A TransitionSystem is a tuple M = (S, Act, trans, I, AP, L). Where S is a set
 * of states; Act a set of actions; trans, included in S x Act x S, is the
 * transition relation; i, from in S, is the initial state; AP is a set of atomic
 * propositions; and L : S -&gt; 2^AP is a labeling function.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public interface TransitionSystem {
    
    public Iterator<State> states();
    
    public State getState(String name);
    
    public Iterator<Action> actions();
    
    public Action getAction(String name);
    
    public Iterator<Transition> transitions();
        
    public Iterator<Transition> getTransitions(State source, Action action, State target);
    
    public Iterator<Transition> getOutgoing(State source);
    
    public int getOutgoingCount(State source);
    
    public Iterator<Transition> getIncoming(State target);
    
    public int getIncomingCount(State target);
    
    public Iterator<Transition> getTransitions(Action action);
    
    public Iterator<AtomicProposition> atomicPropositions();
    
    public AtomicProposition getAtomicProposition(String name);
    
    public AtomicProposition getLabel(State state);
    
    public State getInitialState();
    
    public int getTransitionsCount();
    
    public int getActionsCount();
    
    public int getStatesCount();
    
    public int getPropositionsCount();
    
}
