/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.info.toolbox.determinism;


/**
 *
 * @author gperroui
 */
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.test.mutation.equivalence.exact.Determinism;
import be.unamur.transitionsystem.transformation.xml.LtsHandler;
import be.unamur.transitionsystem.transformation.xml.XmlReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Main {
 
     private static final Logger logger = LoggerFactory.getLogger(Main.class);

    
    public static void main(String[] args) throws XMLStreamException {
        
        HashMap<String,LabelledTransitionSystem> map = new HashMap<>();
        
           FilenameFilter textFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String lowercaseName = name.toLowerCase();
                    return lowercaseName.endsWith(".ts");
                }
            };

           File directory = new File(args[0]);
           if (!directory.isDirectory()) {
               logger.info("not a directory, exiting...");
               System.exit(0);
           } else {
               for (File tsFile: directory.listFiles(textFilter)) {
                   
                    FileInputStream input = null;
                   try {
                       input = new FileInputStream(tsFile);
                   } catch (FileNotFoundException ex) {
                       java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                   }
		LtsHandler handler = new LtsHandler();
		XmlReader reader = new XmlReader(handler, input);
                
                reader.readDocument();
		LabelledTransitionSystem system = (LabelledTransitionSystem) handler.geTransitionSystem();
                   
                map.put(tsFile.getName(), system);
               }
           }
                
        Determinism.computeDeterminism(map);
         try {
             Determinism.outputDeterminismStats(new File(directory.getPath()+"/DeterminismStats.txt"));
         } catch (IOException ex) {
             java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    
}
