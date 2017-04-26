package be.unamur.transitionsystem;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Represents abstract transition system elements (States, Actions, and
 * Transitions)
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public abstract class TransitionSystemElement {

    private Map<String, Object> properties;

    public TransitionSystemElement() {
        this.properties = Maps.newHashMap();
    }

    public void setProperty(String propertyName, Object propertyValue) {
        properties.put(propertyName, propertyValue);
    }

    public Object getProperty(String propertyName) {
        return properties.get(propertyName);
    }
    
    public <T> T getProperty(String propertyName, T defaultValue) {
        T value = (T) properties.get(propertyName);
        return value == null ? defaultValue : value;
    }

}
