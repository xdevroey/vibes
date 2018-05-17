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
import be.vibes.fexpression.exception.ConfigurationException;

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
