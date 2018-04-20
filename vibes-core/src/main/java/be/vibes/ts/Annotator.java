package be.vibes.ts;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public interface Annotator {

    public void annotate(TransitionSystem ts);

    public boolean isAnnotated(TransitionSystem ts);

}
