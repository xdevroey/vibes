/**
 *
 */
package be.unamur.transitionsystem.test.mutation.selection;

import be.unamur.fts.fexpression.Feature;
import java.util.ArrayList;

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
public class AllMutantSelectionStrategy implements MutantSelectionStrategy {

    private static final Logger logger = LoggerFactory.getLogger(AllMutantSelectionStrategy.class);

    /* (non-Javadoc)
     * @see be.unamur.transitionsystem.test.mutation.equivalence.selection.MutantSelectionStrategy#getMutants(org.apache.commons.configuration.XMLConfiguration)
     */
    @Override
    public ArrayList<Configuration> getMutants(XMLConfiguration mutantsConfig, Sat4JSolverFacade facade) {
        ArrayList<Configuration> configs = Lists.newArrayListWithCapacity(mutantsConfig.getInt("mutantsSize"));
        int nbMutants = 0;
        facade.setKeepSolverHot(false);
        while (facade.hasNext()) {
            logger.debug("Creating configuration: " + nbMutants);
            Iterable<Feature> features = facade.next();
            Configuration c = new SimpleConfiguration(features);
            configs.add(c);
            nbMutants++;
        }
        return configs;
    }

}
