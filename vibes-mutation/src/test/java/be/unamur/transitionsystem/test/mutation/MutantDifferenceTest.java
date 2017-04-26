/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.test.mutation;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.LtsMutableTestCase;
import be.unamur.transitionsystem.test.TestCase;
import be.unamur.transitionsystem.test.execution.StrictTestCaseRunner;
import be.unamur.transitionsystem.test.execution.TestCaseRunner;
import be.unamur.transitionsystem.test.mutation.equivalence.montecarlo.MutantDifference;
import be.unamur.transitionsystem.test.mutation.exception.MutationException;
import be.unamur.transitionsystem.test.selection.AccumulatorWrapUp;
import be.unamur.transitionsystem.test.selection.AlwaysTrueValidator;
import be.unamur.transitionsystem.test.selection.LocalRandomTestCaseGenerator;
import be.unamur.transitionsystem.transformation.xml.LtsHandler;
import be.unamur.transitionsystem.transformation.xml.XmlReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.xml.stream.XMLStreamException;
import static org.hamcrest.Matchers.hasItem;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gperroui
 */
public class MutantDifferenceTest {

    private Logger logger = LoggerFactory.getLogger(MutantDifferenceTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }

    };

    private LabelledTransitionSystem getLts(File resource) throws Exception {
        InputStream input = new FileInputStream(resource);
        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        return (LabelledTransitionSystem) handler.geTransitionSystem();
    }

    @Test
    public void getLtsDiffStatesTestSame() throws Exception {

        LabelledTransitionSystem original = getLts(new File(this.getClass().getClassLoader().getResource("ElsaRR.ts").getFile()));
        LabelledTransitionSystem mutant = getLts(new File(this.getClass().getClassLoader().getResource("ElsaRR.ts").getFile()));
        Assert.assertNotNull(mutant);
        Assert.assertNotNull(original);

        ArrayList<State> res = MutantDifference.getLTSDiffStates(original, mutant);

        Assert.assertTrue(res.isEmpty());
        //Assert.assertTrue(res.isEmpty());

    }

      @Test
    public void getLtsDiffStatesTestNotSameLarge() throws Exception {

        LabelledTransitionSystem original = getLts(new File(this.getClass().getClassLoader().getResource("ts-6cc2cfa6-f302-44da-8482-8ecb045fd8d9.ts").getFile()));
        LabelledTransitionSystem mutant = getLts(new File(this.getClass().getClassLoader().getResource("AEX_2.ts").getFile()));
        Assert.assertNotNull(mutant);
        Assert.assertNotNull(original);

        ArrayList<State> res = MutantDifference.getLTSDiffStates(original, mutant);
        System.out.println(res.toString());
        Assert.assertFalse(res.isEmpty());
       

    }
    
    @Test
    public void getLtsDiffStatesTestNotSame() throws Exception {

        LabelledTransitionSystem original = getLts(new File(this.getClass().getClassLoader().getResource("svm.ts").getFile()));
        LabelledTransitionSystem mutant = getLts(new File(this.getClass().getClassLoader().getResource("SVM_TAD.ts").getFile()));
        Assert.assertNotNull(mutant);
        Assert.assertNotNull(original);

        ArrayList<State> res = MutantDifference.getLTSDiffStates(original, mutant);

        Assert.assertFalse(res.isEmpty());
        Assert.assertFalse(res.isEmpty());

    }

    @Test
    public void getLtsDiffStatesTestAEX() throws Exception {
        try {
            InputStream input = this.getClass().getClassLoader()
                    .getResourceAsStream("ts-sodaVendingMachine.xml");
            LtsHandler handler = new LtsHandler();
            XmlReader reader = new XmlReader(handler, input);
            reader.readDocument();
            LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
            assertNotNull(system);
            logger.debug("Transition System {}", system);
            final Transition selectedTransition = system.getState("state2")
                    .outgoingTransitions().next();
            logger.debug("Selected transition = {}", selectedTransition);
            final Action selectedAction = system.getAction("return");
            logger.debug("Selected Action = {}", selectedAction);
            ActionExchange op = new ActionExchange(system, new TransitionSelectionStrategy() {
                @Override
                public Transition selectTransition(MutationOperator op, TransitionSystem ts) {
                    return selectedTransition;
                }
            }, new ActionSelectionStrategy() {
                @Override
                public Action selectAction(MutationOperator op, TransitionSystem ts) {
                    return selectedAction;
                }
            });
            op.apply();

            LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();

            ArrayList<State> res = MutantDifference.getLTSDiffStates(system, mutant);

            Assert.assertThat(res, hasItem(system.getState("state2")));

        } catch (XMLStreamException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MutationException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void getLtsDiffStatesTestTDE() throws Exception {

        try {
            InputStream input = this.getClass().getClassLoader()
                    .getResourceAsStream("ts-sodaVendingMachine.xml");
            LtsHandler handler = new LtsHandler();
            XmlReader reader = new XmlReader(handler, input);
            reader.readDocument();
            LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
            assertNotNull(system);
            logger.info("Transition System {}", system);
            final Transition tr = system.getState("state2").outgoingTransitions().next();
            final State to = system.getState("state6");
            TransitionDestinationExchange op = new TransitionDestinationExchange(system, new TransitionSelectionStrategy() {
                @Override
                public Transition selectTransition(MutationOperator op, TransitionSystem ts) {
                    return tr;
                }
            }, new StateSelectionStrategy() {
                @Override
                public State selectState(MutationOperator op, TransitionSystem ts) {
                    return to;
                }
            });
            op.apply();

            LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();

            ArrayList<State> res = MutantDifference.getLTSDiffStates(system, mutant);

            //Assert.assertThat(res, hasItem("state2"));
            Assert.assertThat(res, hasItem(system.getState("state6")));

        } catch (XMLStreamException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MutationException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void getLtsDiffStatesTestSMI() throws Exception {
        try {
            InputStream input = this.getClass().getClassLoader()
                    .getResourceAsStream("ts-sodaVendingMachine.xml");
            LtsHandler handler = new LtsHandler();
            XmlReader reader = new XmlReader(handler, input);
            reader.readDocument();
            LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
            assertNotNull(system);
            logger.info("Transition System {}", system);
            final State selectedState = system.getState("state2");
            logger.info("Selected state is {}", selectedState);
            StateMissing op = new StateMissing(system, new StateSelectionStrategy() {
                @Override
                public State selectState(MutationOperator op, TransitionSystem ts) {
                    return selectedState;
                }
            });
            op.apply();

            LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();

            ArrayList<State> res = MutantDifference.getLTSDiffStates(system, mutant);

            Assert.assertThat(res, hasItem(system.getState("state2")));

        } catch (XMLStreamException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MutationException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void getLtsDiffStatesTestTMI() throws Exception {

        try {
            InputStream input = this.getClass().getClassLoader()
                    .getResourceAsStream("ts-sodaVendingMachine.xml");
            LtsHandler handler = new LtsHandler();
            XmlReader reader = new XmlReader(handler, input);
            reader.readDocument();
            LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
            assertNotNull(system);
            logger.info("Transition System {}", system);
            final Transition tr = system.getState("state2").outgoingTransitions().next();
            TransitionMissing op = new TransitionMissing(system, new TransitionSelectionStrategy() {
                @Override
                public Transition selectTransition(MutationOperator op, TransitionSystem ts) {
                    return tr;
                }
            });
            op.apply();

            LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();

            ArrayList<State> res = MutantDifference.getLTSDiffStates(system, mutant);

            Assert.assertThat(res, hasItem(system.getState("state2")));

        } catch (XMLStreamException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MutationException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void getLtsDiffStatesTestAMI() throws Exception {

        try {
            InputStream input = this.getClass().getClassLoader()
                    .getResourceAsStream("ts-sodaVendingMachine.xml");
            LtsHandler handler = new LtsHandler();
            XmlReader reader = new XmlReader(handler, input);
            reader.readDocument();
            LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
            assertNotNull(system);
            logger.info("Transition System {}", system);
            final Transition selectedTransition = system.getInitialState()
                    .outgoingTransitions().next();
            logger.debug("Selected transition = {}", selectedTransition);
            ActionMissing op = new ActionMissing(system, new TransitionSelectionStrategy() {
                @Override
                public Transition selectTransition(MutationOperator op, TransitionSystem ts) {
                    return selectedTransition;
                }
            });
            op.apply();

            LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();

            ArrayList<State> res = MutantDifference.getLTSDiffStates(system, mutant);

            Assert.assertThat(res, hasItem(selectedTransition.getFrom()));
            Assert.assertThat(res, hasItem(selectedTransition.getTo()));

        } catch (XMLStreamException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MutationException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void getLtsDiffSatesTestTAD() throws Exception {
        try {

            InputStream input = this.getClass().getClassLoader()
                    .getResourceAsStream("ts-sodaVendingMachine.xml");
            LtsHandler handler = new LtsHandler();
            XmlReader reader = new XmlReader(handler, input);
            reader.readDocument();
            LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
            assertNotNull(system);
            logger.info("Transition System {}", system);
            final State from = system.getState("state2");
            final State to = system.getState("state6");
            final Action action = system.addAction("serveSoda");
            TransitionAdd op = new TransitionAdd(system, new StateSelectionStrategy() {
                private boolean used = false;

                @Override
                public State selectState(MutationOperator op, TransitionSystem ts) {
                    if (!used) {
                        used = true;
                        return from;
                    }
                    return to;
                }
            }, new ActionSelectionStrategy() {
                @Override
                public Action selectAction(MutationOperator op, TransitionSystem ts) {
                    return action;
                }
            });
            op.apply();

            LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();

            ArrayList<State> res = MutantDifference.getLTSDiffStates(system, mutant);

            Assert.assertThat(res, hasItem(system.getState("state2")));
            Assert.assertThat(res, hasItem(system.getState("state6")));

        } catch (XMLStreamException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MutationException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Test
    public void getLTSDiffStatesTestWIS() throws Exception {

        try {
            InputStream input = this.getClass().getClassLoader()
                    .getResourceAsStream("ts-sodaVendingMachine.xml");
            LtsHandler handler = new LtsHandler();
            XmlReader reader = new XmlReader(handler, input);
            reader.readDocument();
            LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
            assertNotNull(system);
            logger.info("Transition System {}", system);
            final State state = system.getState("state2");
            WrongInitialState op = new WrongInitialState(system, new StateSelectionStrategy() {
                @Override
                public State selectState(MutationOperator op, TransitionSystem ts) {
                    return state;
                }
            });
            op.apply();

            LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();

            ArrayList<State> res = MutantDifference.getLTSDiffStates(system, mutant);

            Assert.assertThat(res, hasItem(system.getState("state2")));

        } catch (XMLStreamException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MutationException ex) {
            java.util.logging.Logger.getLogger(MutantDifferenceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testGenerateTC_TAD() {
        try {

            InputStream input = this.getClass().getClassLoader()
                    .getResourceAsStream("ts-sodaVendingMachine.xml");
            LtsHandler handler = new LtsHandler();
            XmlReader reader = new XmlReader(handler, input);
            reader.readDocument();
            LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
            assertNotNull(system);
            logger.info("Transition System {}", system);
            final State from = system.getState("state2");
            final State to = system.getState("state6");
            final Action action = system.addAction("serveSoda");
            TransitionAdd op = new TransitionAdd(system, new StateSelectionStrategy() {
                private boolean used = false;

                @Override
                public State selectState(MutationOperator op, TransitionSystem ts) {
                    if (!used) {
                        used = true;
                        return from;
                    }
                    return to;
                }
            }, new ActionSelectionStrategy() {
                @Override
                public Action selectAction(MutationOperator op, TransitionSystem ts) {
                    return action;
                }
            });
            op.apply();

            LabelledTransitionSystem mutant = (LabelledTransitionSystem) op.result();

            ArrayList<State> states = MutantDifference.getLTSDiffStates(system, mutant);

            AccumulatorWrapUp wrap = new AccumulatorWrapUp();
            LocalRandomTestCaseGenerator gen = new LocalRandomTestCaseGenerator(LtsMutableTestCase.FACTORY, AlwaysTrueValidator.TRUE_VALIDATOR, wrap, states);

            TestCaseRunner runner = new StrictTestCaseRunner();
            boolean isok = false;
            for (int i = 0; i < 100; i++) {
                TestCase tc = gen.generateTestCase(mutant);
                ExecutionTree tree = runner.run(system, tc);
                if (!tree.hasPath()) {
                    logger.debug("found non-executing test case" + tc.toString());
                    isok = true;
                } else {
                    logger.debug("found executing test case" + tc.toString());
                }
            }
             Assert.assertTrue(isok);
        } catch (Exception ex) {

            ex.printStackTrace();
        }
       
    }

    @Test
    public void getLtsStatesBadTDE() throws Exception  {
         
        LabelledTransitionSystem original = getLts(new File(this.getClass().getClassLoader().getResource("elsaRR.ts").getFile()));
        LabelledTransitionSystem mutant = getLts(new File(this.getClass().getClassLoader().getResource("BadTDE/TDE_543.ts").getFile())); 
        
           ArrayList<State> res = MutantDifference.getLTSDiffStates(original, mutant);
           logger.debug(res.toString());
           
            Assert.assertNotNull(res);
        
    }
    
    
}
