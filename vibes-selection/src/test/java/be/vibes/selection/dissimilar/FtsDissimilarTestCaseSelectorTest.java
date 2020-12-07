package be.vibes.selection.dissimilar;

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

import be.vibes.fexpression.DimacsModel;
import be.vibes.solver.BDDSolverFacade;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TestCase;
import be.vibes.ts.io.xml.XmlLoaders;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtsDissimilarTestCaseSelectorTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(FtsDissimilarTestCaseSelectorTest.class);

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
    public void testLocalMaximumDistancePrioritization() throws Exception {
        File dimacsModel = new File(FtsDissimilarTestCaseSelectorTest.class.getClassLoader().getResource("svm.splot.dimacs").toURI());
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("fts-sodaVendingMachine.xml");
        FeaturedTransitionSystem fts = XmlLoaders.loadFeaturedTransitionSystem(input);
        DimacsModel model = DimacsModel.createFromDimacsFile(dimacsModel);
        BDDSolverFacade solver = new BDDSolverFacade(model);
        FtsTestCaseDissimilarityComputor comp = new FtsTestCaseDissimilarityComputor(solver, fts);
        LocalMaximumDistancePrioritization prior = new LocalMaximumDistancePrioritization(comp);
        FtsDissimilarTestCaseSelector gen = new FtsDissimilarTestCaseSelector(fts, solver, prior);
        gen.setRunningTime(1000);
        List<TestCase> tests = gen.select(20);
        assertThat(tests, hasSize(20));
    }

    @Test
    public void testGlobalMaximumDistancePrioritization() throws Exception {
        File dimacsModel = new File(FtsDissimilarTestCaseSelectorTest.class.getClassLoader().getResource("svm.splot.dimacs").toURI());
        InputStream input = this.getClass().getClassLoader().getResourceAsStream("fts-sodaVendingMachine.xml");
        FeaturedTransitionSystem fts = XmlLoaders.loadFeaturedTransitionSystem(input);
        DimacsModel model = DimacsModel.createFromDimacsFile(dimacsModel);
        BDDSolverFacade solver = new BDDSolverFacade(model);
        FtsTestCaseDissimilarityComputor comp = new FtsTestCaseDissimilarityComputor(solver, fts);
        GlobalMaximumDistancePrioritization prior = new GlobalMaximumDistancePrioritization(comp);
        FtsDissimilarTestCaseSelector gen = new FtsDissimilarTestCaseSelector(fts, solver, prior);
        gen.setRunningTime(1000);
        List<TestCase> tests = gen.select(20);
        assertThat(tests, hasSize(20));
    }
}
