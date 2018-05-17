package be.vibes.ts.io.xml;

import be.vibes.solver.FeatureModel;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TestSet;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.exception.TransitionSystemDefinitionException;
import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TestSetXmlLoader {
    
    private static final Logger LOG = LoggerFactory.getLogger(TestSetXmlLoader.class);
    
    public static TestSet loadTestSet(InputStream in, TransitionSystem ts) throws TransitionSystemDefinitionException {
        TestCaseHandler handler = new TestCaseHandler(ts);
        try {
            XmlReader reader = new XmlReader(handler, in);
            reader.readDocument();
        } catch (XMLStreamException e) {
            LOG.error("Error while reading TS input!", e);
            throw new TransitionSystemDefinitionException("Error while reading TS!", e);
        }
        return handler.getTestSet();
    }

    public static TestSet loadTestSet(InputStream in, FeaturedTransitionSystem fts, FeatureModel fm) throws TransitionSystemDefinitionException {
        FtsTestCaseHandler handler = new FtsTestCaseHandler(fts, fm);
        try {
            XmlReader reader = new XmlReader(handler, in);
            reader.readDocument();
        } catch (XMLStreamException e) {
            LOG.error("Error while reading TS input!", e);
            throw new TransitionSystemDefinitionException("Error while reading TS!", e);
        }
        return handler.getTestSet();
    }

}
