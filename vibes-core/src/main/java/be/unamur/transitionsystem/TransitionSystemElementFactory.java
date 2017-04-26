package be.unamur.transitionsystem;

/*
 * #%L vibes-core %% Copyright (C) 2014 PReCISE, University of Namur %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. #L%
 */
/**
 * A Factory class for TS elements.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 * @param <S> The type of states of the TS.
 * @param <T> The type of transitions of the TS.
 * @param <A> The type of actions of the TS.
 */
public interface TransitionSystemElementFactory<S extends State, T extends Transition, A extends Action> {

    /**
     * Builds a new state.
     * @return 
     */
    public S buildState();

    /**
     * Builds a new state with the given name.
     * @param name
     * @return 
     */
    public S buildState(String name);

    /**
     * Builds a new transition.
     * @return 
     */
    public T buildTransition();

    /**
     * Builds a new transition with the given values.
     * @param action
     * @return 
     */
    public T buildTransition(State from, State to, Action action);

    /**
     * Builds a new action.
     * @return 
     */
    public A buildAction();

    /**
     * Builds a new action with the given name.
     * @param name
     * @return 
     */
    public A buildAction(String name);
}
