/*
 * #%L
 * vibes-mutation
 * %%
 * Copyright (C) 2015 PReCISE, University of Namur
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



package be.unamur.transitionsystem.test.mutation.equivalence.exact;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

public class NFANaive {


	private ArrayDeque<NFAPair> R = new ArrayDeque<NFAPair>();
	private ArrayDeque<NFAPair> todo = new ArrayDeque<NFAPair>();

	private Logger logger = LoggerFactory.getLogger(NFANaive.class);

	@SuppressWarnings("unchecked")
	public boolean computeHKEquivalence(LabelledTransitionSystem a, LabelledTransitionSystem b) {

		// both lists should be empty at the start of the algorithm...
		R.clear(); 
		todo.clear();

		// we need to merge actions (equivalent to alphabet) from automata:  A mutation may introduce new actions in automaton b...
		//Iterator<Action> itact =  Iterators.concat(a.actions(),b.actions());
		Iterator<Action> itact = a.actions();

		// there are various strategies for choosing a pair of states to start, it seems that initial states is a good start...
		ArrayList<State> initx =  new ArrayList<State>();
		initx.add(a.getInitialState());
		ArrayList<State> inity = new ArrayList<State>();
		inity.add(b.getInitialState());

		NFAPair init =  new NFAPair(initx,inity);
		todo.add(init);

		while (!todo.isEmpty()) {
			NFAPair p =  todo.removeLast();
			logger.debug("processing pair: "+ p.toString()+" from todo");
			if (inR(p)) {
				//p.printNFAPair();
				logger.debug("already in R, " + p.toString());
				;
			} else {
				if (oSharp(p.x,a) != oSharp(p.y,b)) {
					System.out.println("Counter-example: " +p.toString());
					logger.debug("R size:"+R.size());
					logger.debug("R:" +  getRString());
					return false;
				}
				while (itact.hasNext()) {
					Action tmp = itact.next();

					NFAPair  p_prime =  new NFAPair(tSharp(p.x, tmp),tSharp(p.y, tmp));
					
					logger.debug("adding pair: " + p_prime.toString()+" to todo");
					todo.addLast(p_prime);
					
				}
				logger.debug("adding pair: " + p.toString()+" to R");
				R.offer(p);
				itact =a.actions(); // resetting the iterator...
			}
		}
		printR();
		printRStats();
		return true;
	}



	private boolean inR(NFAPair p) {
		for (NFAPair pair:  R) {
			if (pair.equals(p))
				return true;
		}

		return false;
	}



	private boolean oSharp(ArrayList<State> x, LabelledTransitionSystem lts) {

		if (x.contains(lts.getInitialState()))
			return true;
		else
			return false;

	}


	private ArrayList<State> tSharp(ArrayList<State> s, Action a) {

		ArrayList<State> states =  new ArrayList<State>();


		for (State st: s) {
			//logger.debug("processing state: "+ st.getName()+" action "+ a);
			Iterator<Transition> it = st.outgoingTransitions();
			while ( it.hasNext()) {
				Transition tr = it.next();
				if (tr.getAction().getName().equals(a.getName())) {
					if (tr.getTo()!= null ) {
						logger.debug("adding succ state: "+ tr.getTo().getName());
						states.add(tr.getTo());
					} 
				}
			}

		}
		if (states.isEmpty()) {
			logger.debug("no target states found for this action "+ a.getName());
			states.addAll(s);
		} else if (s.containsAll(states) && states.containsAll(s)) {
			return s;
		}
		return states;
	}

	public void printR()  {
			System.out.println(getRString());
	}
	
	public String getRString() {
		String res = new String();
		
		res += "R={";
		for (NFAPair p : R) {
			res= res + p.toString()+"\n";
		}
		res+="}";
		return res;
	}
	
	public void printRStats()  {
		System.out.println("Size of the relation R:"+ R.size());
	}
}
