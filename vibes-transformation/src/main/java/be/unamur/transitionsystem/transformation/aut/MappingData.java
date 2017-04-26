/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.transitionsystem.transformation.aut;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author gperroui
 */
public class MappingData  implements Serializable {
    
    private Map<String, String> stateIds;
    private Map<String, String> idStates;
    private Map<String, String> actionsIds;
    private Map<String, String> idActions;
    
  public MappingData() {
      this.stateIds = Maps.newHashMap();
      this.idStates =  Maps.newHashMap();
      this.actionsIds = Maps.newHashMap();
      this.idActions = Maps.newHashMap();
  }

    /**
     * @return the stateIds
     */
    public Map<String, String> getStateIds() {
        return stateIds;
    }

    /**
     * @return the idStates
     */
    public Map<String, String> getIdStates() {
        return idStates;
    }

    /**
     * @return the actionsIds
     */
    public Map<String, String> getActionsIds() {
        return actionsIds;
    }

    /**
     * @return the idActions
     */
    public Map<String, String> getIdActions() {
        return idActions;
    }
    
    
 
    
}
