package be.vibes.selection.dissimilar;

import be.vibes.selection.random.FtsRandomTestCaseSelector;
import be.vibes.selection.random.RandomTestCaseSelector;
import be.vibes.solver.FeatureModel;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TransitionSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class FtsDissimilarTestCaseSelector extends DissimilarTestCaseSelector{
    
    private static final Logger LOG = LoggerFactory.getLogger(DissimilarTestCaseSelector.class);
    private FeatureModel fm;

    public FtsDissimilarTestCaseSelector(TransitionSystem ts, FeatureModel fm, PrioritizationTechnique prioritization) {
        super(ts, prioritization);
        this.fm = fm;
    }
    
    public FtsDissimilarTestCaseSelector(TransitionSystem ts, FeatureModel fm, PrioritizationTechnique prioritization, long runningTime) {
        super(ts, prioritization, runningTime);
        this.fm = fm;
    }

    @Override
    public FeaturedTransitionSystem getTransitionSystem() {
        return (FeaturedTransitionSystem) super.getTransitionSystem();
    }

    @Override
    protected RandomTestCaseSelector getRandomTestCaseSelector() {
        return new FtsRandomTestCaseSelector(getTransitionSystem(), this.fm);
    }
    
}
