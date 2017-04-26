/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.transformation.aut;

import com.google.common.collect.Maps;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gperroui
 */
public class Aut2Lts {

    private LabelledTransitionSystem lts;
    private File f;
   // private Map<String, String> idStates;
    MappingData data;
    private boolean mapEnabled = false;

    public Aut2Lts(File f, File mappingFile) throws IOException, ClassNotFoundException {

        lts = new LabelledTransitionSystem();
        this.f = f;
        initMap(mappingFile);

    }

    public LabelledTransitionSystem getLabelledTransitionSystem() throws IOException {

        BufferedInputStream bf = new BufferedInputStream(new FileInputStream(f));

        BufferedReader br = new BufferedReader(
                new InputStreamReader(bf, StandardCharsets.UTF_8));

        String header = br.readLine();
        State initialState = null;
        String initName = header.substring(header.indexOf('(') + 1, header.indexOf(',')).trim(); 
       if (mapEnabled) {
           // initialState = new State("toto");
            initialState = new State(getStateName(initName));
        }
        else
            initialState = new State(initName);
       
        lts.setInitialState(initialState);

        String line;

        while ((line = br.readLine()) != null) {

            line = line.substring(line.indexOf("(")+1, line.lastIndexOf(")"));
            String[] tokens = line.split(",");

            String fromStateID;
            String toStateID;
            String actionName =null;

            if (mapEnabled) {
                fromStateID = getStateName(tokens[0].trim());
                actionName = getActionName(tokens[1].trim());
                toStateID = getStateName(tokens[2].trim());
            } else {
                fromStateID = tokens[0].trim();
                toStateID = tokens[2].trim();
                actionName =  tokens[1].trim();
            }

           // String actionName = tokens[1].replaceAll("\"","");
            if (lts.getState(fromStateID) == null) {
                lts.addState(fromStateID);
            }

            if (lts.getState(toStateID) == null) {
                lts.addState(toStateID);
            }

            if (lts.getAction(actionName) == null) {
                lts.addAction(actionName);
            }

            lts.addTransition(lts.getState(fromStateID), lts.getState(toStateID), lts.getAction(actionName));

        }

        return lts;
    }

    private String getStateName(String id) {
        return data.getIdStates().get(id);
//return idStates.get(id);
    }
    
    private String getActionName(String id) {
        return data.getIdActions().get(id);
    }

    private void initMap(File mappingFile) {

        try {
            if (mappingFile != null) {

                //idStates = Maps.newHashMap();

                FileInputStream fis = new FileInputStream(mappingFile);
                ObjectInputStream ois = new ObjectInputStream(fis);

                data = (MappingData) ois.readObject();

                ois.close();
                fis.close();
                mapEnabled = true;
            }
        } catch (IOException | ClassNotFoundException e) {

        }

    }
}
