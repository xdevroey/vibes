package be.unamur.transitionsystem.test.execution.exception;

/**
 * Represents an error for illegal transitions fired in a
 * {@link be.unamur.transitionsystem.test.execution.TransitionSystemExecutor}.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class IllegalFiredTransition extends Exception {

    public IllegalFiredTransition(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalFiredTransition(String message) {
        super(message);
    }
}
