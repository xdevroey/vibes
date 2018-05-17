package be.vibes.fexpression.configuration;

/*
 * #%L
 * VIBeS: featured expressions
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
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
import be.vibes.fexpression.Feature;
import java.util.Set;

import com.google.common.collect.Sets;
import java.util.Iterator;

/**
 * A simple {@link Configuration} implementation. This implementation
 * encapsulate a String {@link Set}. No validity check is done for the
 * configuration when features are selected.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class SimpleConfiguration implements Configuration {

    private Set<Feature> features;

    /**
     * Creates a new simple configuration.
     */
    public SimpleConfiguration() {
        this.features = Sets.newHashSet();
    }

    /**
     * Creates a new simple configuration with the given features.
     *
     * @param features The initial set of selected features.
     */
    public SimpleConfiguration(Iterable<Feature> features) {
        this.features = Sets.newHashSet(features);
    }

    public SimpleConfiguration(Feature... features) {
        this.features = Sets.newHashSet(features);
    }

    @Override
    public void selectFeature(Feature feature) {
        features.add(feature);
    }

    @Override
    public void deselectFeature(Feature feature) {
        features.remove(feature);
    }

    @Override
    public boolean isSelected(Feature feature) {
        return features.contains(feature);
    }

    @Override
    public String toString() {
        return "SimpleConfiguration [features=" + features + "]";
    }

    @Override
    public Feature[] getFeatures() {
        return features.toArray(new Feature[features.size()]);
    }

    @Override
    public Iterator<Feature> iterator() {
        return this.features.iterator();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((features == null) ? 0 : features.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SimpleConfiguration other = (SimpleConfiguration) obj;
        if (features == null) {
            if (other.features != null) {
                return false;
            }
        } else if (!features.equals(other.features)) {
            return false;
        }
        return true;
    }

}
