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
 * This class represents a Transition in a TS.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class Transition extends TransitionSystemElement {

    private State from;
    private State to;
    private Action action;

    /**
     * Creates a new Transition
     */
    public Transition() {
        super();
    }

    /**
     * *
     * Creates a new Transition with the given parameters (pre: all parameters
     * belongs to the same TS).
     * @param from
     * @param to
     * @param action
     */
    public Transition(State from, State to, Action action) {
        this();
        this.from = from;
        this.to = to;
        this.action = action;
    }

    /**
     * Return the action associated to this transition.
     * @return 
     */
    public Action getAction() {
        return action;
    }

    /**
     * Sets the action associated to this transition.
     *
     * @param action
     */
    protected void setAction(Action action) {
        this.action = action;
    }

    /**
     * Returns the starting state of this transition.
     * @return 
     */
    public State getFrom() {
        return from;
    }

    /**
     * Sets the starting state of this transition.
     * @param from
     */
    protected void setFrom(State from) {
        this.from = from;
    }

    /**
     * Returns the ending state of this transition.
     * @return 
     */
    public State getTo() {
        return to;
    }

    /**
     * Sets the ending state of this transition.
     * @param to
     */
    protected void setTo(State to) {
        this.to = to;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result + ((from == null) ? 0 : from.hashCode());
        result = prime * result + ((to == null) ? 0 : to.hashCode());
        return result;
    }

    /**
     * Returns true if the two transitions are equal. Two transitions are equal
     * if the action, origin and ending states are equal. this.equals(tr)
     * implies (this.getAction.equals(tr.getAction) and
     * this.getFrom.equals(tr.getFrom) and this.getTo.equals(tr.getTo)).
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Transition)) {
            return false;
        }
        Transition other = (Transition) obj;
        if (action == null) {
            if (other.action != null) {
                return false;
            }
        } else if (!action.equals(other.action)) {
            return false;
        }
        if (from == null) {
            if (other.from != null) {
                return false;
            }
        } else if (!from.equals(other.from)) {
            return false;
        }
        if (to == null) {
            if (other.to != null) {
                return false;
            }
        } else if (!to.equals(other.to)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Transition [from=" + from.getName() + ", to=" + to.getName()
                + ", action=" + action.getName() + "]";
    }

}
