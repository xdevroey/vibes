package be.unamur.fts.solver;

/*
 * #%L
 * VIBeS: featured expressions
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
import be.unamur.fts.fexpression.Feature;
import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import org.junit.Test;

import be.unamur.fts.fexpression.ParserUtil;
import be.unamur.fts.solver.exception.SolverInitializationException;
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
