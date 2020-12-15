package be.vibes.selection.ec.objective;

import be.vibes.ts.coverage.StructuralCoverage;
import be.vibes.ts.execution.Execution;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.mockito.Mockito.*;

public class CoverStructureTest {

    private static final Logger LOG = LoggerFactory.getLogger(CoverStructureTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOG.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    };

    @Test
    public void testEvaluateEmptyExecutions() {
        StructuralCoverage criteria = mock(StructuralCoverage.class);
        when(criteria.coverage(any(Iterator.class))).thenReturn(0.42);
        CoverStructure cov = new CoverStructure(criteria);
        List<Execution> executions = new ArrayList<>();
        double objValue = cov.evaluate(executions);
        assertThat(objValue, closeTo(1.0 - 0.42, 0.00001));
    }

}