package be.vibes.fexpression;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.vibes.solver.Sat4JSolverFacadeTest;

public class DimacsModelTest {

    private static final Logger logger = LoggerFactory.getLogger(DimacsModelTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	@Test
    public void test() throws Exception {
        File dimacsModel = new File(Sat4JSolverFacadeTest.class.getClassLoader().getResource("vending-machine.dimacs").toURI());
        File featureMapping = new File(Sat4JSolverFacadeTest.class.getClassLoader().getResource("vending-machine.map").toURI());
        assertTrue("Test file not found: vending-machine.dimacs!", dimacsModel.exists());
        assertTrue("Test file not found: vending-machine.map!", featureMapping.exists());
        DimacsModel model = DimacsModel.createFromTvlParserGeneratedFiles(featureMapping, dimacsModel);
        assertNotNull(model);
        assertNotNull(model.getDimacsFD());
        assertEquals(20, model.getDimacsFD().size());
        assertNotNull(model.getFd());
        assertNotNull(model.getFeatureMapping());
        assertEquals(13, model.getFeatureMapping().size());
    }

}
