package be.vibes.selection.ec.objective;

import be.vibes.fexpression.DimacsModel;
import be.vibes.fexpression.exception.DimacsFormatException;
import be.vibes.solver.FeatureModel;
import be.vibes.solver.Sat4JSolverFacade;
import be.vibes.solver.exception.SolverInitializationException;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.FeaturedTransitionSystemFactory;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.TransitionSystemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static be.vibes.fexpression.FExpression.featureExpr;

public class TestUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);

    private static TransitionSystem TS;
    private static FeaturedTransitionSystem FTS;
    private static FeatureModel FM;

    public static TransitionSystem getTransitionSystem() {
        if (TS == null) {
            TransitionSystemFactory factory = new TransitionSystemFactory("s0");
            factory.addStates("s1", "s2", "s3");
            factory.addActions("a1", "a2", "a0", "a3", "a4");
            factory.addTransition("s0", "a1", "s1");
            factory.addTransition("s1", "a2", "s2");
            factory.addTransition("s1", "a3", "s0");
            factory.addTransition("s1", "a4", "s3");
            factory.addTransition("s2", "a2", "s3");
            factory.addTransition("s3", "a0", "s0");
            TS = factory.build();
        }
        return TS;
    }

    public static FeaturedTransitionSystem getFeaturedTransitionSystem() {
        if (FTS == null) {
            FeaturedTransitionSystemFactory factory = new FeaturedTransitionSystemFactory("s0");
            factory.addStates("s1", "s2", "s3");
            factory.addActions("a1", "a2", "a0", "a3", "a4");
            factory.addTransition("s0", "a1", featureExpr("f1"), "s1");
            factory.addTransition("s1", "a2", featureExpr("f2"), "s2");
            factory.addTransition("s1", "a3", featureExpr("f1").and(featureExpr("f2")), "s0");
            factory.addTransition("s1", "a4", featureExpr("f3"), "s3");
            factory.addTransition("s2", "a2", "s3");
            factory.addTransition("s3", "a0", "s0");
            FTS = factory.build();
        }
        return FTS;
    }

    public static FeatureModel getFeatureModel() {
        if (FM == null) {
            try {
                DimacsModel model = DimacsModel.createFromFExpression(featureExpr("f1").and(
                        (featureExpr("f2").and(featureExpr("f3").not()))
                                .or(featureExpr("f3").and(featureExpr("f2").not()))));
                FM = new Sat4JSolverFacade(model);
            } catch (SolverInitializationException | DimacsFormatException e) {
                LOG.error("Exception during the initialization of the feature model!", e);
            }
        } else {
            try {
                FM.reset();
            } catch (SolverInitializationException e) {
                LOG.error("Exception during the re-initialization of the feature model!", e);
            }
        }
        return FM;
    }


}
