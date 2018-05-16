package be.vibes.ts.io.xml;

import be.vibes.solver.FeatureModel;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.FeaturedTransitionSystemExecutor;
import static be.vibes.ts.io.xml.TestCaseHandler.ID_ATTR;
import com.google.common.base.Preconditions;
import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class FtsTestCaseHandler extends TestCaseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FtsTestCaseHandler.class);

    private final FeatureModel fm;

    public FtsTestCaseHandler(FeaturedTransitionSystem fts, FeatureModel fm) {
        super(fts);
        this.fm = fm;
    }

    @Override
    protected void handleStartTestCaseTag(StartElement element) {
        LOG.trace("Starting test case");
        id = element.getAttributeByName(QName.valueOf(ID_ATTR)).getValue();
        Preconditions.checkNotNull(id, "Test case identifier may not be null!");
        currentExecutor = new FeaturedTransitionSystemExecutor((FeaturedTransitionSystem) ts, fm);
    }

}
