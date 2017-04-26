
package be.unamur.transitionsystem.test.exception;

/**
 * Represents an error during the execution of a test case.
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class TestCaseExecutionException extends Exception{

    public TestCaseExecutionException(String message, Exception cause) {
        super(message, cause);
    }

    public TestCaseExecutionException(String message) {
        super(message);
    }
}
