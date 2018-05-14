package be.vibes.selection.exception;

import be.vibes.ts.State;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class SinkStateReachedException extends Exception {
    
    private final State sinkState;

    public SinkStateReachedException(String message, State sinkState) {
        super(message);
        this.sinkState = sinkState;
    }

    public State getSinkState() {
        return sinkState;
    }
    
}
