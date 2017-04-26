package be.unamur.transitionsystem.test.mutation.equivalence.exact;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;


public class Determinism {
    
    
   private static HashSet<String> nfas;
     private static HashSet<String> dfas;
    
     private static Logger logger = LoggerFactory.getLogger(Determinism.class);

	public static boolean  isDeterministic(LabelledTransitionSystem  lts) {
		
		boolean det = true;
                Iterator<State> it=  lts.states(); 
                while (it.hasNext()) {
                    //logger.info(it.toString());
                     State s = it.next();
                     Iterator<Transition> trans = s.outgoingTransitions();
                     HashMap<String,HashSet<State>> map   =  new HashMap();
                     while (trans.hasNext()) {
                         Transition t = trans.next();
                         String act  =  t.getAction().getName();
                         if (map.containsKey(act)) {
                            // logger.info("entering if for a action already seen...");
                             if (map.get(act).add(t.getTo())) {
                                 // a new output state form the same action linked to the same input state => non-determinism
                                 // TODO code for logging
                                 
                                 return false;
                             } else {
                                 //We are duplicating the transition, should not happen !!!!!!";
                                 return false;
                             }
                         } else {
                             
                             HashSet<State> tmp = new HashSet<>();
                             tmp.add(t.getTo());
                             map.put(act, tmp);
                         }
                     }
                }
                
                return det;
	}
	
        
        public static void computeDeterminism(HashMap<String,LabelledTransitionSystem> map) {
            
            nfas = new HashSet<>();
            dfas = new HashSet<>();
          
            for (String tsName:  map.keySet()) {
                if (isDeterministic(map.get(tsName))) {
                    dfas.add(tsName);
                } else {
                    nfas.add(tsName);
                }
            }
            
            
        }
        
	public static void outputDeterminismStats(File out) throws IOException {
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(out));
            
            writer.append("nfas/dfas ratio "+ nfas.size() + " / " + dfas.size());
            writer.newLine();
            int total = nfas.size() + dfas.size();
            writer.append("Total: " + total);
            writer.newLine();
            writer.append("NFAS:");
            writer.newLine();
            writer.append("[");
            for (String nfa: nfas) {
                writer.append(nfa+" ");
            }
            writer.append("]");
            
            writer.append("DFAS:");
            writer.newLine();
            writer.append("[");
            for (String dfa: dfas) {
                writer.append(dfa+" ");
            }
            writer.append("]");
            
            writer.flush();
            writer.close();
            
        }
}
