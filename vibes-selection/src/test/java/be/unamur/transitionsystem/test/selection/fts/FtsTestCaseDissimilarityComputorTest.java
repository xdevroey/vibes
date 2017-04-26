package be.unamur.transitionsystem.test.selection.fts;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.solver.BDDConstraintIdentifier;
import be.unamur.fts.solver.SolverFacade;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.test.FtsTestCase;
import static org.mockito.Mockito.*;

public class FtsTestCaseDissimilarityComputorTest {

    private static final Logger logger = LoggerFactory
            .getLogger(FtsTestCaseDissimilarityComputorTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	@Test
    public void testSimpleDissimilarity() throws Exception {
        SolverFacade mockSolver = mock(SolverFacade.class);
        when(mockSolver.addConstraint(any(FExpression.class))).thenReturn(
                new BDDConstraintIdentifier());
        when(mockSolver.getNumberOfSolutions()).thenReturn(1.0).thenReturn(2.0);

        FtsTestCase tc1 = mock(FtsTestCase.class);
        when(tc1.iterator()).thenReturn(
                Lists.newArrayList(new Action("act1"), new Action("act2"),
                        new Action("act3")).iterator());
        FtsTestCase tc2 = mock(FtsTestCase.class);
        when(tc2.iterator()).thenReturn(
                Lists.newArrayList(new Action("act1"), new Action("act2"),
                        new Action("act4")).iterator());

        FtsTestCaseDissimilarityComputor comp = new FtsTestCaseDissimilarityComputor(
                mockSolver);

        double diss = comp.dissimilarity(tc1, tc2);

        assertEquals((1.0 - 0.5) * (1.0 - 0.5), diss, 0.00001);
    }

}
