package be.vibes.dsl.exception;

public class ExecutionException extends RuntimeException {

    private static final long serialVersionUID = -3684120337851112981L;

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutionException(String message) {
        super(message);
    }

}
