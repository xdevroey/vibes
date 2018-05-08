package be.vibes.solver;

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
import be.vibes.fexpression.DimacsFormatter;
import be.vibes.fexpression.DimacsModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.minisat.core.Solver;
import org.sat4j.minisat.orders.RandomLiteralSelectionStrategy;
import org.sat4j.minisat.orders.RandomWalkDecorator;
import org.sat4j.minisat.orders.VarOrderHeap;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterators;

import static com.google.common.base.Preconditions.*;
import be.vibes.fexpression.FExpression;
import be.vibes.fexpression.Feature;
import be.vibes.fexpression.configuration.Configuration;
import be.vibes.fexpression.configuration.SimpleConfiguration;
import be.vibes.fexpression.exception.DimacsFormatException;
import be.vibes.solver.exception.ConstraintNotFoundException;
import be.vibes.solver.exception.ConstraintSolvingException;
import be.vibes.solver.exception.SolverInitializationException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Sat4JSolverFacade implements FeatureModel, Iterator<Configuration> {

    private static final Logger logger = LoggerFactory.getLogger(Sat4JSolverFacade.class);

    private ISolver solver;
    private DimacsModel model;
    private Set<ConstraintIdentifier> deliveredIds = Sets.newHashSet();
    
    public Sat4JSolverFacade(DimacsModel model) throws SolverInitializationException {
        this.model = model;
        // Initialize solver
        initSolver();
        // Load dimacs model into the solver
        loadDimacsFdModel();
    }

    public Sat4JSolverFacade(File featureMapping) throws SolverInitializationException {
        // Read mapping to get the number of variables
        try {
            model = DimacsModel.createFromTvlParserMappingFile(featureMapping);
        } catch (IOException e) {
            logger.debug("IOException while creating Sat4JSolverFacade", e);
            throw new SolverInitializationException(
                    "Could not load feature mapping file!", e);
        }
        initSolver();
    }

    public Sat4JSolverFacade(String dimacsModel, String featureMapping)
            throws SolverInitializationException {
        this(new File(dimacsModel), new File(featureMapping));
    }

    public Sat4JSolverFacade(File dimacsModel, File featureMapping)
            throws SolverInitializationException {
        checkArgument(featureMapping.exists() && featureMapping.isFile(), "Mapping File %s does not exists!",
                featureMapping.getPath());
        checkArgument(dimacsModel.exists() && dimacsModel.isFile(), "Dimacs File %s does not exists!",
                dimacsModel.getPath());

        // Load the dimacs model
        try {
            model = DimacsModel.createFromTvlParserGeneratedFiles(featureMapping, dimacsModel);
        } catch (IOException e) {
            logger.debug("IOException while creating Sat4JSolverFacade", e);
            throw new SolverInitializationException(
                    "Could not load feature mapping file!", e);
        }

        // Initialize solver
        initSolver();

        // Load dimacs model into the solver
        loadDimacsFdModel();
    }

    private void initSolver() {
        // Enable random SAT solutions
        Solver<?> solv = SolverFactory.newSAT();
        solv.setOrder(new RandomWalkDecorator(new VarOrderHeap(new RandomLiteralSelectionStrategy()), 1));
        solver = new ModelIterator(solv);
        solver.newVar(this.model.getFeaturesCount());
    }

    private void loadDimacsFdModel() throws SolverInitializationException {
        logger.debug("Loading the model into the solver");
        try {
            for (int[] clause : model.getDimacsFD()) {
                logger.debug("Adding clause {} to solver", Arrays.toString(clause));
                this.solver.addClause(new VecInt(clause));
            }
        } catch (ContradictionException e) {
            // If one of the clauses is empty or if one of the clauses contains
            // only falsified literals after unit propagation
            logger.debug("ContradictionException while creating Sat4JSolverFacade", e);
            throw new SolverInitializationException(
                    "Could not initialize solver due to empty clause or "
                    + "unsatisfyable problem!", e);
        }

        logger.debug("{} clauses added to the solver", model.getDimacsFD().size());
    }

    @Override
    public ConstraintIdentifier addConstraint(FExpression constraint)
            throws SolverInitializationException, SolverFatalErrorException {
        logger.trace("Adding expression {} to solver", constraint);
        Sat4JContraintIdentifier id = new Sat4JContraintIdentifier(constraint);
        // If the node is true, stop
        if (constraint.equals(FExpression.trueValue())) {
            return id;
        }
        // else compute DIMACS
        int[][] constraints;
        try {
            constraints = DimacsFormatter.format(constraint, model.getFeatureMapping());
            logger.trace("CNF version of expression {} has {} clauses", constraint,
                    constraints.length);
        } catch (DimacsFormatException ex) {
            logger.error("Exception while formatting contraint {}!", constraint, ex);
            throw new SolverInitializationException("Could not format constraint to DIMACS to add to solver", ex);
        }
        // and add them to the solver
        for (int[] constr : constraints) {
            try {
                logger.trace("Adding {} clause to solver", Arrays.toString(constr));
                id.addSat4JConstraint(this.solver.addClause(new VecInt(constr)), constr);
            } catch (ContradictionException e) {
                // If one of the clauses is empty or if one of the clauses
                // contains only falsified literals after unit propagation
                // remove already added constraints
                try {
                    logger.trace(
                            "Clauses is empty or one of the clauses contains only falsified literals after unit propagation",
                            e);
                    removeConstraint(id);
                } catch (ConstraintNotFoundException e1) {
                    logger.error("Constraint not found!", e);
                    throw new SolverFatalErrorException(
                            "Could not completely remove constraint from solver. "
                            + "Solver in inconsistant state, should be reset!",
                            e1);
                }
                throw new SolverInitializationException(
                        "Could not add constraint to the solver due to empty clause ("
                        + Arrays.toString(constr) + ") or "
                        + "unsatisfyable problem!", e);
            }
        }
        this.deliveredIds.add(id);
        return id;
    }

    @Override
    public void removeConstraint(ConstraintIdentifier id)
            throws ConstraintNotFoundException, SolverFatalErrorException {
        checkArgument(id instanceof Sat4JContraintIdentifier,
                "Argument id must be instance of Sat4JContraintIdentifier!");
        if (!this.deliveredIds.contains(id)) {
            return;
        }
        Iterator<IConstr> it = ((Sat4JContraintIdentifier) id).iteratorIConstr();
        IConstr constr;
        while (it.hasNext()) {
            constr = it.next();
            logger.trace("Removing IConstr '{}' from solver", constr);
            try {
                if (constr != null && !this.solver.removeConstr(constr)) {
                    throw new SolverFatalErrorException(
                            "Failed to remove constraint's clause "
                            + constr
                            + " from solver. Solver in incosistant state, should be reset!");
                }
            } catch (NoSuchElementException e) {
                logger.error(
                        "Unexpected exception occured ... SAT4J implementation ? :-/ ", e);
            }
        }
    }

    @Override
    public boolean isSatisfiable() throws ConstraintSolvingException {
        IProblem problem = this.solver;
        try {
            return problem.isSatisfiable();
        } catch (TimeoutException e) {
            throw new ConstraintSolvingException("Timeout reached for solving !", e);
        }
    }

    /**
     * Creates a new Sat4J solver and load the feature model in it (all added
     * constraints will be lost).
     *
     * @throws be.vibes.solver.exception.SolverInitializationException
     */
    @Override
    public void reset() throws SolverInitializationException {
        deliveredIds.clear();
        initSolver();
        loadDimacsFdModel();
    }

    @Override
    public boolean hasNext() {
        try {
            return this.isSatisfiable();
        } catch (ConstraintSolvingException e) {
            logger.warn("Exception while solving problem !", e);
            return false;
        }
    }

    @Override
    public Configuration next() {
        String featureName;
        ArrayList<Feature> sol = Lists.newArrayList();
        int[] m;
        m = this.solver.model();
        for (int i = 0; i < m.length; i++) {
            if (m[i] > 0) {
                featureName = model.getFeatureMapping().inverse().get(m[i]);
                if (featureName == null) {
                    logger.error(
                            "Feature number {} not found in mapping, will be replaces by null!",
                            m[i]);
                }
                sol.add(Feature.feature(featureName));
            }
        }
        return new SimpleConfiguration(sol);
    }

    /**
     * @throws UnsupportedOperationException Always thrown when the method is
     * invoked
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can not remove element from iteration!");
    }

    /**
     * Returns the current object (this).
     *
     * @throws be.vibes.solver.exception.ConstraintSolvingException
     */
    @Override
    public Iterator<Configuration> getSolutions() throws ConstraintSolvingException {
        return this;
    }

    /*
     * Setting the reuse of the state of the solver between calls 
     */
    public void setKeepSolverHot(boolean hot) {
        solver.setKeepSolverHot(hot);
    }

    /**
     * forces the solver to explore configurations randomly
     */
    public void setRandomExploration() {
        ((Solver<?>) this.solver).setOrder(new RandomWalkDecorator(new VarOrderHeap(new RandomLiteralSelectionStrategy()), 1));
    }

    @Override
    public double getNumberOfSolutions() throws ConstraintSolvingException {
        return Iterators.size(getSolutions());
    }

}
