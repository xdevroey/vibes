package be.unamur.transitionsystem.transformation.pml;

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
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Iterator;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.transformation.ActionMapping;
import be.unamur.transitionsystem.transformation.IntElementsMapper;
import be.unamur.transitionsystem.transformation.StateMapping;

/**
 * Cf. Classen, A. (2011). Modelling and Model Checking Variability-Intensive
 * Systems. PReCISE Research Center, Faculty of Computer Science, University of
 * Namur (FUNDP). page 153.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class LtsPromelaPrinter {

    protected PrintStream out;

    protected TransitionSystem ts;

    protected StateMapping<Integer> stateMap;

    protected ActionMapping<Integer> actionMap;

    public LtsPromelaPrinter(OutputStream output, TransitionSystem ts) {
        out = new PrintStream(output);
        this.ts = ts;
        IntElementsMapper mapper = new IntElementsMapper();
        stateMap = mapper;
        actionMap = mapper;
    }

    public void setStateMap(StateMapping<Integer> stateMap) {
        this.stateMap = stateMap;
    }

    public void setActionMap(ActionMapping<Integer> actionMap) {
        this.actionMap = actionMap;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }

    public StateMapping<Integer> getStateMap() {
        return stateMap;
    }

    public ActionMapping<Integer> getActionMap() {
        return actionMap;
    }

    public void print() {
        out.println("/*");
        out.println("  Generated on " + new Date());
        out.println("*/");
        // Define clauses
        printDefineClauses();
        // Variables
        printVariables();
        // Print state machine
        out.println("active proctype main(){");
        out.println();
        printMain();
        out.println("}");
        out.flush();
    }

    protected void printVariables() {
        out.println("  int previousAction = -1;");
        out.println();
    }

    @SuppressWarnings("unchecked")
    protected void printDefineClauses() {
        int id;
//		for (Iterator<State> it = ts.states(); it.hasNext();) {
//			State s = it.next();
//			id = stateMap.getStateMapping(s);
//			out.println("/* State : " + s.getName() + " */");
//			out.print("#define state" + id + " (");
//			out.print("current == " + id);
//			out.println(")");
//		}
        out.println();
        for (Iterator<Action> it = ts.actions(); it.hasNext();) {
            Action act = it.next();
            id = actionMap.getActionMapping(act);
            out.println("/* Action : " + act.getName() + " */");
            out.print("#define action" + id + " (");
            out.print("previousAction == " + id);
            out.println(")");
        }
        out.println();
    }

    /*
     * @SuppressWarnings("unchecked") protected void printMain() {
     * out.println("    do "); for (Iterator<State> it = ts.states();
     * it.hasNext();) { State s = it.next(); printState(s); }
     * out.println("        :: else -> skip; "); out.println("    od "); }
     */
    @SuppressWarnings("unchecked")
    protected void printMain() {
        out.println("    goto init_state;");
        for (Iterator<State> it = ts.states(); it.hasNext();) {
            State s = it.next();
            printState(s);
        }
    }

//	protected void printState(State s) {
//		int id = stateMap.getStateMapping(s);
//		out.println("        /* " + s.getName() + " */");
//		out.println("        :: state" + id + " -> if");
//		for (Iterator<Transition> it = s.outgoingTransitions(); it.hasNext();) {
//			Transition tr = it.next();
//			printTransition(tr);
//		}
//		out.println("               :: else -> skip; ");
//		out.println("           fi;");
//	}
    protected void printState(State s) {
        int id = stateMap.getStateMapping(s);
        out.println("    /* " + s.getName() + " */");
        out.println("    state" + id + ":");
        if (s.equals(ts.getInitialState())) {
            out.println("    init_state:");
        }
        out.println("        gd");
        for (Iterator<Transition> it = s.outgoingTransitions(); it.hasNext();) {
            Transition tr = it.next();
            printTransition(tr);
        }
        out.println("        dg;");
    }

//	protected void printTransition(Transition tr) {
//		out.println("               /* to  "	+ tr.getTo().getName() + "  action = " + tr.getAction().getName() + "*/");
//		out.println("               :: atomic{ current = "
//				+ stateMap.getStateMapping(tr.getTo()).intValue() + "; "
//				+ " previousAction = " + actionMap.getActionMapping(tr.getAction()) + "; }");
//	}
    protected void printTransition(Transition tr) {
        int id = stateMap.getStateMapping(tr.getTo());
        out.println("            /* " + tr.getTo().getName() + " */");
        out.println("              :: previousAction = " + actionMap.getActionMapping(tr.getAction()) + ";");
        out.println("                 goto state" + id + ";");
    }

}
