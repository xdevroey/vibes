package be.unamur.fts.fexpression.configuration;

/*
 * #%L
 * VIBeS: featured expressions
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
import be.unamur.fts.fexpression.Feature;
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
