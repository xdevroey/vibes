package be.vibes.solver;

/*-
 * #%L
 * VIBeS: featured expressions
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

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

import be.vibes.fexpression.configuration.Configuration;
import be.vibes.fexpression.configuration.SimpleConfiguration;
import be.vibes.fexpression.DimacsModel;
import be.vibes.fexpression.FExpression;
import be.vibes.fexpression.Feature;
import be.vibes.fexpression.ParserUtil;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BDDSolverFacadeTest {

    private static final Logger logger = LoggerFactory.getLogger(BDDSolverFacadeTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    };

    @Test
    public void testTrue() throws Exception {
        FExpression expr = ParserUtil.getInstance().parse("true");
        logger.debug("Building BDD for expression {}", expr);
        assertThat(expr, equalTo(FExpression.trueValue()));
        BDDSolverFacade solver = new BDDSolverFacade(new ArrayList<String>(), expr);
        assertTrue(solver.isSatisfiable());
    }

    @Test
    public void testFalse() throws Exception {
        FExpression expr = ParserUtil.getInstance().parse("false");
        logger.debug("Building BDD for expression {}", expr);
        assertThat(expr, equalTo(FExpression.falseValue()));
        BDDSolverFacade solver = new BDDSolverFacade(new ArrayList<String>(), expr);
        assertFalse(solver.isSatisfiable());
    }

    @Test
    public void testTrueAndTrue() throws Exception {
        FExpression expr = ParserUtil.getInstance().parse("true&&true");
        logger.debug("Building BDD for expression {}", expr);
        assertThat(expr, equalTo(FExpression.trueValue().and(FExpression.trueValue())));
        BDDSolverFacade solver = new BDDSolverFacade(new ArrayList<String>(), expr);
        assertTrue(solver.isSatisfiable());
    }

    @Test
    public void testTrueAndFalse() throws Exception {
        FExpression expr = ParserUtil.getInstance().parse("true&&false");
        logger.debug("Building BDD for expression {}", expr);
        assertThat(expr, equalTo(FExpression.trueValue().and(FExpression.falseValue())));
        BDDSolverFacade solver = new BDDSolverFacade(new ArrayList<String>(), expr);
        assertFalse(solver.isSatisfiable());
    }

    @Test
    public void testSVM() throws Exception {
        File dimacsModel = new File(Sat4JSolverFacadeTest.class.getClassLoader().getResource("svm.splot.dimacs").toURI());
        assertTrue("Test file not found: vending-machine.dimacs!", dimacsModel.exists());
        DimacsModel model = DimacsModel.createFromDimacsFile(dimacsModel);
        logger.debug("Building BDD for expression {}", model.getFd());
        BDDSolverFacade solver = new BDDSolverFacade(model.getFeatures(), model.getFd());
        assertTrue(solver.isSatisfiable());
        assertEquals(24.00, solver.getNumberOfSolutions(), 0);
        Iterator<Configuration> it = solver.getSolutions();
        Configuration conf;
        int cpt = 0;
        Set<Configuration> result = Sets.newHashSet();
        while (it.hasNext()) {
            conf = it.next();
            cpt++;
            result.add(conf);
        }
        assertEquals(24, cpt);
        // Check configurations
        Set<SimpleConfiguration> expected = Sets.newHashSet();
        File results = new File(Sat4JSolverFacadeTest.class.getClassLoader().getResource("svm.splot.solutions.txt").toURI());
        for (String line : Splitter.on(CharMatcher.anyOf("\n\r")).omitEmptyStrings()
                .split(Files.toString(results, Charsets.UTF_8))) {
            String[] tabLine = line.split(",");
            if (tabLine.length > 0) {
                Feature[] features = new Feature[tabLine.length];
                for (int i = 0; i < features.length; i++) {
                    features[i] = Feature.feature(tabLine[i]);
                }
                expected.add(new SimpleConfiguration(features));
            }
        }
        assertEquals(expected, result);
    }

    @Test
    public void testFeatures1() throws Exception {
        BDDSolverFacade solver = new BDDSolverFacade(Lists.newArrayList("f1", "f2"), ParserUtil.getInstance().parse("f1&&f2"));
        assertTrue(solver.isSatisfiable());
        List<Configuration> results = Lists.newArrayList(solver.getSolutions());
        assertEquals("Wrong number of solutions!", 1, results.size());
        assertTrue(results.get(0).isSelected(Feature.feature("f1")));
        assertTrue(results.get(0).isSelected(Feature.feature("f2")));
    }

}
