package be.vibes.solver;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDD.AllSatIterator;
import net.sf.javabdd.BDDFactory;
import be.vibes.fexpression.FExpression;
import be.vibes.fexpression.configuration.Configuration;
import be.vibes.fexpression.configuration.SimpleConfiguration;
import be.vibes.fexpression.DimacsModel;
import be.vibes.fexpression.Feature;
import be.vibes.fexpression.exception.FExpressionException;
import be.vibes.solver.exception.ConstraintNotFoundException;
import be.vibes.solver.exception.ConstraintSolvingException;
import be.vibes.solver.exception.SolverInitializationException;
import static com.google.common.base.Preconditions.*;

public class BDDSolverFacade implements FeatureModel {

    private static final Logger logger = LoggerFactory.getLogger(BDDSolverFacade.class);

    private BDDFactory factory;
    private Map<String, BDD> featureMapping;
    private BDD model;
    private Map<ConstraintIdentifier, BDD> constraints;
    private String[] featureNames;

    private BDD modelAndContraints = null;

    public BDDSolverFacade(DimacsModel model) {
        this(model.getFeatures(), model.getFd());
    }

    public BDDSolverFacade(List<String> features, FExpression featureDiagram) {
        this.featureMapping = Maps.newHashMap();
        this.constraints = Maps.newHashMap();
        /* Initialize with reasonable nodes and cache size and NxN variables */
        String numOfNodes = System.getProperty("bddnodes");
        int numberOfNodes;
        if (numOfNodes == null) {
            numberOfNodes = (int) features.size() * 1000000 + 1;
        } else {
            numberOfNodes = Integer.parseInt(numOfNodes);
        }
        String cache = System.getProperty("bddcache");
        int cacheSize;
        if (cache == null) {
            cacheSize = 1000;
        } else {
            cacheSize = Integer.parseInt(cache);
        }
        numberOfNodes = Math.max(1000, numberOfNodes);
        this.factory = BDDFactory.init("java", numberOfNodes, cacheSize);
        if (this.factory.varNum() < features.size()) {
            this.factory.setVarNum(features.size());
        }
        // Initialize feature variables
        int i = 0;
        featureNames = features.toArray(new String[features.size()]);
        for (String name : featureNames) {
            this.featureMapping.put(name, this.factory.ithVar(i));
            i++;
        }
        // Build BDD representing the feature model
        try {
            this.model = featureDiagram.applySimplification().accept(new FExpressionBDDBuilder(this.factory,
                    this.featureMapping));
        } catch (FExpressionException ex) {
            throw new IllegalStateException("No exception should happen while using FExpressionBDDBuilder visitor!", ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.factory.done();
        super.finalize();
    }

    @Override
    public ConstraintIdentifier addConstraint(FExpression constraint)
            throws SolverInitializationException, SolverFatalErrorException {
        try {
            ConstraintIdentifier id = new BDDConstraintIdentifier();
            BDD c = constraint.accept(new FExpressionBDDBuilder(this.factory, this.featureMapping));
            constraints.put(id, c);
            if(modelAndContraints != null){
                modelAndContraints = modelAndContraints.and(c);
            }
            return id;
        } catch (FExpressionException ex) {
            throw new IllegalStateException("No exception should happen while using FExpressionBDDBuilder visitor!", ex);
        }
    }

    @Override
    public void removeConstraint(ConstraintIdentifier id)
            throws SolverFatalErrorException, ConstraintNotFoundException {
        if (constraints.remove(id) == null) {
            throw new ConstraintNotFoundException("Constraint not found: " + id);
        }
        modelAndContraints = null;
    }

    @Override
    public boolean isSatisfiable() throws ConstraintSolvingException {
        if (modelAndContraints == null) {
            buildModelAndConstraints();
        }
        return this.modelAndContraints.satCount() > 0;
    }

    @Override
    public Iterator<Configuration> getSolutions() throws ConstraintSolvingException {
        if (modelAndContraints == null) {
            buildModelAndConstraints();
        }
        AllSatIterator it = this.modelAndContraints.allsat();
        List<Configuration> res = Lists.newArrayList();
        while (it.hasNext()) {
            byte[] assignation = it.nextSat();
            checkState(assignation.length == featureNames.length);
            addConfigurations(res, assignation, 0, new SimpleConfiguration());
        }
        logger.trace("BDDSolver: {} solutions found", res.size());
        return res.iterator();
    }

    private void addConfigurations(List<Configuration> res, byte[] assignation, int i, SimpleConfiguration conf) {
        if (i >= assignation.length) {
            res.add(conf);
        } else if (assignation[i] > 0) {
            conf.selectFeature(Feature.feature(featureNames[i]));
            addConfigurations(res, assignation, i + 1, conf);
        } else if (assignation[i] < 0) {
            SimpleConfiguration conf2 = new SimpleConfiguration(conf.getFeatures());
            conf.selectFeature(Feature.feature(featureNames[i]));
            // Add one conf without feature selected
            addConfigurations(res, assignation, i + 1, conf2);
            // Add one conf with feature selected
            addConfigurations(res, assignation, i + 1, conf);
        } else {
            addConfigurations(res, assignation, i + 1, conf);
        }
    }

    @Override
    public void reset() throws SolverInitializationException {
        this.constraints.clear();

    }

    @Override
    public double getNumberOfSolutions() throws ConstraintSolvingException {
        if (modelAndContraints == null) {
            buildModelAndConstraints();
        }
        return modelAndContraints.satCount();
    }

    private void buildModelAndConstraints() {
        modelAndContraints = factory.one();
        modelAndContraints = modelAndContraints.and(model);
        for (BDD c : constraints.values()) {
            modelAndContraints = modelAndContraints.and(c);
        }
    }

}
