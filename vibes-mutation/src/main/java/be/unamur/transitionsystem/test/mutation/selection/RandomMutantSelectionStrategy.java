/**
 *
 */
package be.unamur.transitionsystem.test.mutation.selection;

import be.unamur.fts.fexpression.Feature;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.fts.fexpression.configuration.SimpleConfiguration;
import be.unamur.fts.solver.Sat4JSolverFacade;
import com.google.common.collect.Lists;

/**
 * @author gilles.perrouin
 *
 */
@Deprecated
public class RandomMutantSelectionStrategy implements MutantSelectionStrategy {

    private static final Logger logger = LoggerFactory.getLogger(RandomMutantSelectionStrategy.class);

    /* (non-Javadoc)
     * @see be.unamur.transitionsystem.test.mutation.equivalence.selection.MutantSelectionStrategy#getMutants(org.apache.commons.configuration.XMLConfiguration)
     */
    @Override
    public ArrayList<Configuration> getMutants(XMLConfiguration mutantsConfig, Sat4JSolverFacade facade) {
        ArrayList<Configuration> configs = Lists.newArrayListWithCapacity(mutantsConfig.getInt("mutantsSize"));
        int size = mutantsConfig.getInt("mutantsSize");
        logger.info("Sampling " + size + " mutants for equivalence ");
        int nbMutants = 0;
        facade.setKeepSolverHot(false);
        Feature[] tmp = null;
        while (facade.hasNext() && (nbMutants < size)) {
            Feature[] features = facade.next().getFeatures();
            logger.debug("Creating configuration: " + nbMutants);

            if (!Arrays.equals(tmp, features)) {
                Configuration c = new SimpleConfiguration(features);
                configs.add(c);
            }
            tmp = features;
            nbMutants++;
        }
        return configs;
    }

}
