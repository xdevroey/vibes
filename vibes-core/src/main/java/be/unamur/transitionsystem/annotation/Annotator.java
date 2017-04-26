
package be.unamur.transitionsystem.annotation;

import be.unamur.transitionsystem.TransitionSystem;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public interface Annotator {
    
    public void annotate(TransitionSystem ts);
    
    public boolean isAnnotated(TransitionSystem ts);

}
