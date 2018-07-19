package be.vibes.ts;

/*-
 * #%L
 * VIBeS: core
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Represents abstract transition system elements
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public abstract class TransitionSystemElement {

    private final Map<String, Object> properties;

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
