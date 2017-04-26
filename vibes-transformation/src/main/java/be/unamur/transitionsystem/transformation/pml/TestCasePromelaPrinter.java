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
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.transformation.ActionMapping;
import be.unamur.transitionsystem.transformation.IntElementsMapper;

public class TestCasePromelaPrinter {

    protected PrintStream out;

    protected ActionMapping<Integer> actionMap;

    protected TestCase testCase;

    public TestCasePromelaPrinter(OutputStream out, TestCase testCase) {
        this.out = new PrintStream(out);
        this.testCase = testCase;
        this.actionMap = new IntElementsMapper();
    }

    public TestCasePromelaPrinter(OutputStream out, ActionMapping<Integer> actionMap,
            TestCase testCase) {
        this(out, testCase);
        this.actionMap = actionMap;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }

    public void setActionMap(ActionMapping<Integer> actionMap) {
        this.actionMap = actionMap;
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
        printMainLoop();
        out.println("}");
        out.flush();
    }

    private void printVariables() {
        out.println("  int previousAction = -1;");
        out.println();
    }

    private void printMainLoop() {
        int idCurrent;
        for (Iterator<Action> it = testCase.iterator(); it.hasNext();) {
            Action act = it.next();
            idCurrent = actionMap.getActionMapping(act);
            out.println("      /* Action " + act.getName() + "*/");
            out.println("    previousAction = " + idCurrent + ";");
            out.println();
        }
    }

    private void printDefineClauses() {
        int id;
        out.println();
        for (Iterator<Action> it = testCase.iterator(); it.hasNext();) {
            Action act = it.next();
            id = actionMap.getActionMapping(act);
            out.println("/* Action : " + act.getName() + " */");
            out.print("#define action" + id + " (");
            out.print("previousAction == " + id);
            out.println(")");
        }
        out.println();
    }

    public void printProperties() {
        int id;
        for (Iterator<Action> it = testCase.iterator(); it.hasNext();) {
            Action act = it.next();
            id = actionMap.getActionMapping(act);
            out.print("previousAction" + id);
            if (it.hasNext()) {
                out.print(",");
            }
        }
        out.println();
    }

}
