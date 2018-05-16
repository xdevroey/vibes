package be.vibes.dsl.transforme;

import be.vibes.dsl.exception.TransformationException;
import be.vibes.fexpression.configuration.Configuration;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.Projection;
import be.vibes.ts.SimpleProjection;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.exception.UnresolvedFExpression;
import static com.google.common.base.Preconditions.*;

public class Project {

    public static TransitionSystem projectProduct(FeaturedTransitionSystem fts, Configuration config) {
        checkNotNull(fts);
        checkNotNull(config);
        Projection proj = SimpleProjection.getInstance();
        try {
            return proj.project(fts, config);
        } catch (UnresolvedFExpression e) {
            throw new TransformationException("Exception while performing projection", e);
        }
    }

}
