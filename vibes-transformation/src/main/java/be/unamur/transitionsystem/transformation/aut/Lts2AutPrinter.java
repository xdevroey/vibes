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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;

/**
 *
 * @author gperroui
 */
public class Lts2AutPrinter {

    protected LabelledTransitionSystem lts;
    protected PrintStream out;

    //private Map<State, String> stateIds;
    //private Map<String, String> idStates;
    private int stateCpt = 0;
    private int actionCpt =0;
    
    private MappingData data;
    // private XMLConfiguration config;

    public Lts2AutPrinter(PrintStream output, LabelledTransitionSystem lts) {
        this.out = output;
        this.lts = lts;
        this.data = new MappingData();
        
         //this.stateIds = Maps.newHashMap();
        //this.idStates = Maps.newHashMap();
    }

    protected String getStateId(State state) {
        
        String id = data.getStateIds().get(state.getName());
        //String id = stateIds.get(state);
        if (id == null) {
            id = (new Integer(stateCpt)).toString();
            data.getStateIds().put(state.getName(), id);
             //stateIds.put(state, id);
            data.getIdStates().put(id, state.getName());
            //idStates.put(id, state.getName());
            stateCpt++;
        }
        return id;
    }
    
    
    protected String getActionId(Action action) {
        
        String id =  data.getActionsIds().get(action.getName());
        if (id == null) {
            id =  "act"+ (new Integer(actionCpt)).toString();
            data.getActionsIds().put(action.getName(), id);
            data.getIdActions().put(id, action.getName());
            actionCpt++;
        }
        return id;
    } 
    

    protected int getNumberofTransitions() {
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

    public void printAut() {

        // printing initial state, number of transitions and states, header line...    
        out.println("des(" + getStateId(lts.getInitialState()) + "," + getNumberofTransitions() + "," + lts.numberOfStates() + ")");
        // printing transition according to AUT format
        Iterator<State> stateIt = lts.states();
        while (stateIt.hasNext()) {
            State s = stateIt.next();
            Iterator<Transition> transIt = s.outgoingTransitions();
            while (transIt.hasNext()) {
                Transition tr = transIt.next();

                out.println("(" + getStateId(s) + "," + getActionId(tr.getAction()) + "," + getStateId(tr.getTo()) + ")");
            }

        }

    }

    /*protected void printMapping(String file) throws ConfigurationException, IOException {
        
        File f= new File(file);
        f.delete(); // deleting any existing configuration file of the same name...
        
        config = new XMLConfiguration(f);
        config.setRootElementName("mapping");
        int cpt =0;
         config.addProperty("pairs", null);
        for (String autID : stateIds.values()) {
            //config.
            config.addProperty("pairs.pair("+cpt+")", null);
            config.addProperty("pairs.pair("+cpt+")"+"." +autID, autID);
            config.addProperty("pairs.pair("+cpt+").ltsStateName", idStates.get(autID));
            cpt++;
        }
        config.save();
    }*/
    
    public void printMapping(OutputStream out) throws FileNotFoundException, IOException {
        ObjectOutputStream oos=new ObjectOutputStream(out);
        oos.writeObject(data);
        oos.flush();
    }
    
    

}
