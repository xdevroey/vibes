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
import java.util.Iterator;

import be.vibes.fexpression.FExpression;
import be.vibes.fexpression.configuration.Configuration;
import be.vibes.solver.exception.ConstraintNotFoundException;
import be.vibes.solver.exception.ConstraintSolvingException;
import be.vibes.solver.exception.SolverInitializationException;

public interface FeatureModel {

    /**
     *
     * @param constraint
     * @return
     * @throws SolverInitializationException If the constraint could not be
     * added to the solver. Exact reason depends on implementation.
     * @throws SolverFatalErrorException If the solver encounter an error he
     * could not recover from. Solver should be reset when this exception is
     * launched
     */
    public ConstraintIdentifier addConstraint(FExpression constraint)
            throws SolverInitializationException, SolverFatalErrorException;

    /**
     *
     * @param id
     * @throws SolverFatalErrorException If the solver encounter an error it
     * could not recover from. Solver should be reset when this exception is
     * launched.
     * @throws be.vibes.solver.exception.ConstraintNotFoundException
     */
    public void removeConstraint(ConstraintIdentifier id)
            throws SolverFatalErrorException, ConstraintNotFoundException;

    /**
     *
     * @return @throws ConstraintSolvingException
     */
    public boolean isSatisfiable() throws ConstraintSolvingException;

    /**
     *
     * @return @throws ConstraintSolvingException
     */
    public Iterator<Configuration> getSolutions() throws ConstraintSolvingException;

    /**
     * Reset the state of the solver. This method should be used only if the
     * solver is in an inconsistant state.
     *
     * @throws SolverInitializationException If an error occurs during the
     * reseting of the solver.
     */
    public void reset() throws SolverInitializationException;

    /**
     * Returns the number of solutions.
     *
     * @return
     * @throws be.vibes.solver.exception.ConstraintSolvingException
     */
    public double getNumberOfSolutions() throws ConstraintSolvingException;

}
