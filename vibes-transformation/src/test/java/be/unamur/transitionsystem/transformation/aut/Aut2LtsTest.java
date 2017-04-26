/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.transformation.aut;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.transformation.xml.LtsHandler;
import be.unamur.transitionsystem.transformation.xml.LtsPrinter;
import be.unamur.transitionsystem.transformation.xml.XmlPrinter;
import be.unamur.transitionsystem.transformation.xml.XmlReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import javax.xml.stream.XMLStreamException;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author gperroui
 */
public class Aut2LtsTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(Aut2LtsTest.class);

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
    public void checkBidirectionalTransformationTest() throws FileNotFoundException, XMLStreamException, IOException, ClassNotFoundException {
        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("aerouc5.ts");

        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();

        File fAut = testFolder.newFile("aerouc5.aut");

        PrintStream out = new PrintStream(fAut);
        assertNotNull(out);
        Lts2AutPrinter printer = new Lts2AutPrinter(out, system);
        printer.printAut();
        File mapf = testFolder.newFile("aerouc5-mapping.map");
        FileOutputStream fos = new FileOutputStream(mapf);
        printer.printMapping(fos);

        out.flush();
        out.close();

        fos.flush();
        fos.close();

        Aut2Lts aut2lts = new Aut2Lts(fAut, mapf);
        LabelledTransitionSystem lts = aut2lts.getLabelledTransitionSystem();

        File convMappingFile = testFolder.newFile("aerouc5-conv-mapping.ts");
        XmlPrinter xml = new XmlPrinter(convMappingFile, new LtsPrinter());
        xml.print(lts);

        // checking sizes 
        Assert.assertEquals(lts.numberOfStates(), system.numberOfStates());
        Assert.assertEquals(lts.numberOfActions(), system.numberOfActions());

        //checking intial state
        Assert.assertEquals(lts.getInitialState().getName(), system.getInitialState().getName());

        // checking states 
        Iterator<State> stateIt = system.states();
        while (stateIt.hasNext()) {
            String sName = stateIt.next().getName();
            //logger.info(lts.getState(sName).getName() +" == "+sName);
            Assert.assertEquals(lts.getState(sName).getName(), sName);
        }

        // checking actions
        Iterator<Action> actionIt = system.actions();
        while (actionIt.hasNext()) {
            String actName = actionIt.next().getName();
            Assert.assertEquals(lts.getAction(actName).getName(), actName);
        }

    }

    @Test
    public void checkBidirectionalTransformationLargeTest() throws FileNotFoundException, XMLStreamException, IOException, ClassNotFoundException {
        File f = new File(this.getClass().getClassLoader()
                .getResource("largeRandom.ts").getFile());

        InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("largeRandom.ts");

        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();

        File fAut = testFolder.newFile("largeRandom.aut");

        PrintStream out = new PrintStream(fAut);
        assertNotNull(out);
        Lts2AutPrinter printer = new Lts2AutPrinter(out, system);
        printer.printAut();
        File mapf = testFolder.newFile("largeRandom-mapping.map");
        FileOutputStream fos = new FileOutputStream(mapf);
        printer.printMapping(fos);

        out.flush();
        out.close();

        fos.flush();
        fos.close();

        Aut2Lts aut2lts = new Aut2Lts(fAut, mapf);
        LabelledTransitionSystem lts = aut2lts.getLabelledTransitionSystem();

        File largeRandomConvFile = testFolder.newFile("largeRandom-conv.ts");
        if (largeRandomConvFile.exists()) {
            largeRandomConvFile.delete();
        }
        largeRandomConvFile.createNewFile();

        XmlPrinter xml = new XmlPrinter(largeRandomConvFile, new LtsPrinter());
        xml.print(lts);

        //checking intial state
        Assert.assertEquals(lts.getInitialState().getName(), system.getInitialState().getName());

        // checking sizes 
        Assert.assertEquals(lts.numberOfStates(), system.numberOfStates());
        Assert.assertEquals(lts.numberOfActions(), system.numberOfActions());

        // checking states 
        Iterator<State> stateIt = system.states();
        while (stateIt.hasNext()) {
            String sName = stateIt.next().getName();

            Assert.assertEquals(lts.getState(sName).getName(), sName);
        }

        // checking actions
        Iterator<Action> actionIt = system.actions();
        while (actionIt.hasNext()) {
            String actName = actionIt.next().getName();
            Assert.assertEquals(lts.getAction(actName).getName(), actName);
        }

    }

    protected int getNumberofTransitions(LabelledTransitionSystem lts) {
        int transNum = 0;
        Iterator<State> stateIt = lts.states();
        while (stateIt.hasNext()) {
            State s = stateIt.next();
            Iterator<Transition> transIt = s.outgoingTransitions();
            while (transIt.hasNext()) {
                transNum++;
                transIt.next();

            }

        }
        return transNum;
    }

}
