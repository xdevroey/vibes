package be.vibes.solver;

/*
 * #%L
 * VIBeS: featured expressions
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
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
import be.vibes.fexpression.Feature;
import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import org.junit.Test;

import be.vibes.fexpression.ParserUtil;
import be.vibes.solver.exception.SolverInitializationException;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sat4JSolverFacadeTest {

    private static final Logger logger = LoggerFactory.getLogger(Sat4JSolverFacadeTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    };

    @Test
    public void testInitialiseSolver() throws Exception {
        File dimacsModel = new File(Sat4JSolverFacadeTest.class.getClassLoader().getResource("vending-machine.dimacs").toURI());
        File featureMapping = new File(Sat4JSolverFacadeTest.class.getClassLoader().getResource("vending-machine.map").toURI());
        assertTrue("Test file not found: vending-machine.dimacs!", dimacsModel.exists());
        assertTrue("Test file not found: vending-machine.map!", featureMapping.exists());
        Sat4JSolverFacade solver = new Sat4JSolverFacade(dimacsModel, featureMapping);
        assertTrue("Model should be SAT!", solver.isSatisfiable());

        for (; solver.hasNext();) {
            Feature[] str = (Feature[]) solver.next().getFeatures();
            System.out.println(Arrays.toString(str));
        }

    }

    @Test(expected = SolverInitializationException.class)
    public void testInitialiseSolverWithoutDimacs() throws Exception {
        File featureMapping = new File(Sat4JSolverFacadeTest.class.getClassLoader().getResource("vending-machine.map").toURI());
        assertTrue("Test file not found: vending-machine.map!", featureMapping.exists());
        Sat4JSolverFacade solver = new Sat4JSolverFacade(featureMapping);
        assertTrue("Model should be SAT!", solver.isSatisfiable());
        solver.addConstraint(ParserUtil.getInstance().parse("FreeDrinks && CancelPurchase"));
        assertTrue("Model should be SAT!", solver.isSatisfiable());
        solver.addConstraint(ParserUtil.getInstance().parse("!FreeDrinks"));
        solver.isSatisfiable();
        fail("Model should not be SAT!");
    }

}
