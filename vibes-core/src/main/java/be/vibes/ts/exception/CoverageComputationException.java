package be.vibes.ts.exception;


/**
 * Represents error happening during the computation of a coverage criteria.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class CoverageComputationException extends Exception {

    public CoverageComputationException(String error, Exception cause) {
        super(error, cause);
    }
}
