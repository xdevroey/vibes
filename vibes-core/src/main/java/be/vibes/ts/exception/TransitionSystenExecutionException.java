package be.vibes.ts.exception;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class TransitionSystenExecutionException extends Exception{

    public TransitionSystenExecutionException(String message) {
        super(message);
    }
    
    public TransitionSystenExecutionException(String message, Exception cause) {
        super(message, cause);
    }
    
}
