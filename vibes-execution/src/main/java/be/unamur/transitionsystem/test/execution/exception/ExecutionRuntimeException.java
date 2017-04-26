package be.unamur.transitionsystem.test.execution.exception;

import be.unamur.fts.solver.exception.ConstraintSolvingException;

public class ExecutionRuntimeException extends RuntimeException {

    public ExecutionRuntimeException(String s, Exception e) {
        super(s, e);
    }

    public ExecutionRuntimeException(String s) {
        super(s);
    }

}
