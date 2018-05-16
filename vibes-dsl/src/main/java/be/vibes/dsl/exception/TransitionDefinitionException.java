package be.vibes.dsl.exception;

public class TransitionDefinitionException extends RuntimeException {

    private static final long serialVersionUID = -1487102928041544086L;

    public TransitionDefinitionException(String message) {
        super(message);
    }

    public TransitionDefinitionException(String message, Exception cause) {
        super(message, cause);
    }

}
