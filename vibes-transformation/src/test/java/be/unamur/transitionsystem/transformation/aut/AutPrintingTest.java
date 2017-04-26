/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.transformation.aut;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.transformation.xml.LtsHandler;
import be.unamur.transitionsystem.transformation.xml.XmlReader;
import java.io.File;
import java.io.InputStream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author gperroui
 */
public class AutPrintingTest {
    private static final Logger logger = LoggerFactory
            .getLogger(AutPrintingTest.class);

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
    public void convertLTS2AUTTest() throws Exception {
        File f =  new File(this.getClass().getClassLoader()
                .getResource("aerouc5.ts").getFile());
        
       InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream("aerouc5.ts");
       
        LtsHandler handler = new LtsHandler();
        XmlReader reader = new XmlReader(handler, input);
        reader.readDocument();
        LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
      
        Lts2AutPrinter printer =  new Lts2AutPrinter(System.out, system);
        printer.printAut();
        printer.printMapping(System.out);
    }

}
