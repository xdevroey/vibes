package be.vibes.toolbox.transformation.main;

import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class MainTest {

    private static final Logger LOG = LoggerFactory.getLogger(MainTest.class);

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOG.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

    @Test
    public void testMain() throws Exception {
        String input = getClass().getClassLoader().getResource("minepump.ts").getFile();
        File output = testFolder.newFile();
        output.delete();
        File mapping = testFolder.newFile();
        mapping.delete();
        String[] args = new String[]{"-lts", input, "-out", output.getAbsolutePath(), "-" + DOTTransformator.OPTION_NAME};
        Main main = new Main();
        main.execute(args);
        assertTrue("No output file generated!", output.exists());
    }

}
