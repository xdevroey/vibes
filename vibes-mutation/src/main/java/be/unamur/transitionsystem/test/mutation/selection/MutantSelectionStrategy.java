/**
 *
 */
package be.unamur.transitionsystem.test.mutation.selection;

import java.util.ArrayList;

import org.apache.commons.configuration.XMLConfiguration;

import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.fts.solver.Sat4JSolverFacade;

/**
 * @author gilles.perrouin
 *
 */
@Deprecated
public interface MutantSelectionStrategy {

    public ArrayList<Configuration> getMutants(XMLConfiguration mutantsConfig, Sat4JSolverFacade facade);

}
