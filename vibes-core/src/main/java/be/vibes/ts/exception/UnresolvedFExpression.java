package be.vibes.ts.exception;

import be.vibes.fexpression.FExpression;

/**
 * Represents errors during the resolution of feature expressions (
 * {@link FExpression}).
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class UnresolvedFExpression extends Exception {

    public UnresolvedFExpression(String message) {
        super(message);
    }

    public UnresolvedFExpression(String message, Throwable cause) {
        super(message, cause);
    }
    
}
