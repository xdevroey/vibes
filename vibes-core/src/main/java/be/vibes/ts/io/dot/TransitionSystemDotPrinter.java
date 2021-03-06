package be.vibes.ts.io.dot;

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

import be.vibes.ts.State;
import be.vibes.ts.Transition;
import be.vibes.ts.TransitionSystem;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Prints transition systems in DOT format (to be used with Graphviz).
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TransitionSystemDotPrinter {
    
    protected final TransitionSystem ts;
    protected final PrintStream out;
    private final Map<State, String> stateIds;

    public TransitionSystemDotPrinter(TransitionSystem ts, PrintStream output) {
        this.out = output;
        this.ts = ts;
        this.stateIds = new HashMap<>();
    }

    private int cpt = 0;

    protected String getStateId(State state) {
        String id = stateIds.get(state);
        if (id == null) {
            id = "state" + cpt;
            stateIds.put(state, id);
            cpt++;
        }
        return id;
    }

    public void printDot() {
        State start = ts.getInitialState();
        out.println("digraph G {");
        out.println("rankdir=LR;");
        out.println(getStateId(start) + "[ label = \"" + start.getName()
                + "\", style=filled, color=green ];");
        for (Iterator<State> it = ts.states(); it.hasNext();) {
            State s = it.next();
            printState(s);
        }
        out.println("}");
    }

    protected void printState(State s) {
        if (s != ts.getInitialState()) {
            out.println(getStateId(s) + " [ label = \"" + s.getName() + "\" ];");
        }
        for (Iterator<Transition> it = ts.getOutgoing(s); it.hasNext();) {
            Transition tr = it.next();
            printTransition(tr);
        }

    }

    protected void printTransition(Transition tr) {
        out.println(getStateId(tr.getSource()) + " -> " + getStateId(tr.getTarget())
                + " [ label=\" " + tr.getAction().getName() + " \" ];");
    }
    
    public void flush(){
        out.flush();
    }
    
}
