package be.vibes.selection.random;

/*-
 * #%L
 * VIBeS: test case selection
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

import be.vibes.fexpression.FExpression;
import be.vibes.selection.exception.SinkStateReachedException;
import be.vibes.solver.ConstraintIdentifier;
import be.vibes.solver.FeatureModel;
import be.vibes.solver.exception.ConstraintSolvingException;
import be.vibes.solver.exception.SolverInitializationException;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.FeaturedTransitionSystemExecutor;
import be.vibes.ts.State;
import be.vibes.ts.TestCase;
import be.vibes.ts.Transition;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class FtsRandomTestCaseSelector extends RandomTestCaseSelector {

    private static final Logger LOG = LoggerFactory.getLogger(FtsRandomTestCaseSelector.class);

    private final FeatureModel fm;
    private final FeaturedTransitionSystemExecutor exec;

    public FtsRandomTestCaseSelector(FeaturedTransitionSystem fts, FeatureModel fm, int maxNbrTry, int maxLength) {
        super(fts, maxNbrTry, maxLength);
        this.fm = fm;
        this.exec = new FeaturedTransitionSystemExecutor(fts, fm);
    }

    public FtsRandomTestCaseSelector(FeaturedTransitionSystem fts, FeatureModel fm) {
        this(fts, fm, DEFAULT_MAX_NUMBER_TRY, DEFAULT_MAX_LENGTH);
    }
    
    public FtsRandomTestCaseSelector(FeaturedTransitionSystem fts, FeatureModel fm, int maxLength) {
        this(fts, fm, DEFAULT_MAX_NUMBER_TRY, maxLength);
    }

    @Override
    protected Transition getRandomTransition(TestCase tc) throws SinkStateReachedException {
        FeaturedTransitionSystem fts = getTransitionSystem();
        State state = tc == null ? fts.getInitialState() : tc.getLast().getTarget();
        FExpression prods = tc == null ? FExpression.trueValue() : exec.getFexpression(tc);
        List<Transition> outgoings = Lists.newArrayList();
        Iterator<Transition> it = fts.getOutgoing(state);
        ConstraintIdentifier idProds = null;
        ConstraintIdentifier idFexpr = null;
        try {
            idProds = fm.addConstraint(prods);
            while (it.hasNext()) {
                Transition tr = it.next();
                FExpression fexpr = fts.getFExpression(tr);
                idFexpr = fm.addConstraint(fexpr);
                if(fm.isSatisfiable()){
                    outgoings.add(tr);
                }
                fm.removeConstraint(idFexpr);
            }
            fm.removeConstraint(idProds);
        } catch (ConstraintSolvingException ex) {
            LOG.error("Exception while processing constraints {} and {}, will reset solver!", idProds, idFexpr, ex);
            try {
                fm.reset();
            } catch (SolverInitializationException ex1) {
                LOG.error("Exception while resetting solver!", ex1);
                throw new IllegalStateException("Could not reset solver after failure!", ex1);
            }
        }
        // Return random exception
        if (outgoings.isEmpty()) {
            throw new SinkStateReachedException("Sink state " + state + " reached, could not select next transition!", state);
        } else {
            return outgoings.get(this.random.nextInt(outgoings.size()));
        }
    }

    @Override
    public FeaturedTransitionSystem getTransitionSystem() {
        return (FeaturedTransitionSystem) super.getTransitionSystem();
    }

}
