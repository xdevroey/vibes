package be.unamur.fts.solver;

/*
 * #%L
 * VIBeS: featured expressions
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
import java.util.ArrayList;
import java.util.Iterator;

import org.sat4j.specs.IConstr;

public class Sat4JContraintIdentifier implements ConstraintIdentifier {

    private ArrayList<IConstr> sat4JConstraints;

    private ArrayList<int[]> dimacsConstraints;

    Sat4JContraintIdentifier() {
        this.sat4JConstraints = new ArrayList<IConstr>();
        this.dimacsConstraints = new ArrayList<int[]>();
    }

    void addSat4JConstraint(IConstr constr, int[] dimacsRepr) {
        this.sat4JConstraints.add(constr);
        this.dimacsConstraints.add(dimacsRepr);
    }

    void removeSat4JConstraint(IConstr constr) {
        this.dimacsConstraints.remove(this.sat4JConstraints.indexOf(constr));
        this.sat4JConstraints.remove(constr);
    }

    Iterator<IConstr> iteratorIConstr() {
        return this.sat4JConstraints.iterator();
    }

    Iterator<int[]> iteratorDimacs() {
        return this.dimacsConstraints.iterator();
    }

    @Override
    public String toString() {
        return "Sat4JContraintIdentifier [sat4JConstraints=" + sat4JConstraints
                + "]";
    }

}
