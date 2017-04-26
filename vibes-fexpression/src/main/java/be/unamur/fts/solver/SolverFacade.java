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
import java.util.Iterator;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.fts.solver.exception.ConstraintNotFoundException;
import be.unamur.fts.solver.exception.ConstraintSolvingException;
import be.unamur.fts.solver.exception.SolverInitializationException;

public interface SolverFacade {

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
     * @throws be.unamur.fts.solver.exception.ConstraintNotFoundException
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
     * @throws be.unamur.fts.solver.exception.ConstraintSolvingException
     */
    public double getNumberOfSolutions() throws ConstraintSolvingException;

}
