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
import be.unamur.fts.fexpression.exception.ConfigurationException;

/**
 * Represents a configuration of a feature model. A configuration is a set of
 * selected features. Other features are considered as deselected. A feature is
 * identified by its name.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public interface Configuration extends Iterable<Feature> {

    /**
     * Add the feature to the selected features. A feature is identified by its
     * name.
     *
     * @param feature The feature to add.
     * @throws ConfigurationException If the feature can not be added to the
     * current configuration.
     */
    public abstract void selectFeature(Feature feature) throws ConfigurationException;

    /**
     * Deselect the given feature.
     *
     * @param feature The feature to deselect
     */
    public abstract void deselectFeature(Feature feature);

    /**
     * Returns true if the given feature is selected.
     *
     * @param feature The feature to test.
     * @return True if the given feature is selected.
     */
    public abstract boolean isSelected(Feature feature);

    /**
     * Returns the list of feature in an array of Strings
     *
     * @return the list of (selected) features in the configuration
     */
    public abstract Feature[] getFeatures();

}
