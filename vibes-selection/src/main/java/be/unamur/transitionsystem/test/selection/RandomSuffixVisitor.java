
package be.unamur.transitionsystem.test.selection;

import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.annotation.DistanceToInitialStateAnnotator;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import static com.google.common.base.Preconditions.*;
import java.util.Random;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
class RandomSuffixVisitor extends RandomVisitor {

    public RandomSuffixVisitor(Random random, TestCaseFactory testCaseFactory, TestCaseValidator validator) {
        super(random, testCaseFactory, validator);
    }

    @Override
    protected boolean isValidCandidate(Transition candidate) throws TestCaseException {
        boolean isValid = super.isValidCandidate(candidate); 
        int dtoTo = candidate.getTo().getProperty(DistanceToInitialStateAnnotator.PROP_STATE_DTO, -1);
        checkState(dtoTo >= 0, "State %s has not been annotated using distance to initial state (property %s)!", candidate.getTo(), DistanceToInitialStateAnnotator.PROP_STATE_DTO);
        int dtoFrom = candidate.getFrom().getProperty(DistanceToInitialStateAnnotator.PROP_STATE_DTO, -1);
        checkState(dtoFrom >= 0, "State %s has not been annotated using distance to initial state (property %s)!", candidate.getFrom(), DistanceToInitialStateAnnotator.PROP_STATE_DTO);
        isValid = isValid && dtoTo <= dtoFrom; 
        return isValid;
    }
    
    

}
