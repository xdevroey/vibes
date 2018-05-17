package be.vibes.solver;

/*
 * #%L
 * VIBeS: featured expressions
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
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
import be.vibes.fexpression.FExpression;
import java.util.ArrayList;
import java.util.Iterator;

import org.sat4j.specs.IConstr;

public class Sat4JContraintIdentifier extends DefaultConstraintIdentifier {

    private final ArrayList<IConstr> sat4JConstraints;

    private final ArrayList<int[]> dimacsConstraints;

    Sat4JContraintIdentifier(FExpression constraint) {
        super(constraint);
        this.sat4JConstraints = new ArrayList<>();
        this.dimacsConstraints = new ArrayList<>();
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
