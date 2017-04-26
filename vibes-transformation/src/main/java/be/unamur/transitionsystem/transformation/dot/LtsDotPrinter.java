package be.unamur.transitionsystem.transformation.dot;

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
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class LtsDotPrinter {

    protected TransitionSystem ts;
    protected PrintStream out;
    private Map<State, String> stateIds;

    public LtsDotPrinter(PrintStream output, TransitionSystem ts) {
        this.out = output;
        this.ts = ts;
        this.stateIds = Maps.newHashMap();
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

    @SuppressWarnings("unchecked")
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
        for (Iterator<Transition> it = s.outgoingTransitions(); it.hasNext();) {
            Transition tr = it.next();
            printTransition(tr);
        }

    }

    protected void printTransition(Transition tr) {
        out.println(getStateId(tr.getFrom()) + " -> " + getStateId(tr.getTo())
                + " [ label=\" " + tr.getAction().getName() + " \" ];");
    }
    
    public void flush(){
        out.flush();
    }

}
