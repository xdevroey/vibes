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
import java.util.Iterator;
import java.util.List;

import be.unamur.transitionsystem.exception.VisitException;
import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Lists;

/**
 * This class represents a State in different kinds of Transition Systems.
 * States cannot (should not!) be directly manipulated, please use
 * {@link AbstractTransitionSystem} implementations to add/remove states and
 * actions.
 *
 * @see AbstractTransitionSystem
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 *
 */
public class State extends TransitionSystemElement {

    private String name;

    private List<Transition> outgoing = Lists.newArrayList();
    private List<Transition> incoming = Lists.newArrayList();

    /**
     * Createsa new state.
     */
    public State() {
        super();
    }

    /**
     * Creates a new state with the given name.
     *
     * @param name A new state with the given name.
     */
    public State(String name) {
        this();
        this.name = name;
    }

    // Accessors
    /**
     * Returns the Name of the state.
     *
     * @return The name of the state.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the state.
     *
     * @param name The name of the state.
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Returns outgoing transitions.
     *
     * @return The list of outgoing transitions.
     */
    protected List<Transition> getOutgoing() {
        return this.outgoing;
    }

    /**
     * Returns incoming transitions.
     *
     * @return The list of incoming transitions.
     */
    protected List<Transition> getIncoming() {
        return incoming;
    }

    /**
     * Returns true if this state contains the given incoming transition.
     *
     * @param transition The given incoming transition.
     * @return True if the givent transition is an incoming transition of this
     * state.
     */
    public boolean containsIncoming(Transition transition) {
        return this.incoming.contains(transition);
    }

    /**
     * Returns true if this state contains the given outgoing transition.
     *
     * @param transition The given outgoing transition.
     * @return True if the givent transition is an outgoing transition of this
     * state.
     */
    public boolean containsOutgoing(Transition transition) {
        return this.outgoing.contains(transition);
    }

    /**
     * Returns the incoming transition equals to the given transition. This
     * method should only be used between a TS and its clone. See
     * {@link Transition}.equals method for more details about transition
     * equality.
     *
     * @param transition The transition to compare to.
     * @return The incoming transition equals to the given transition
     */
    public Transition getIncoming(Transition transition) {
        for (Transition tr : this.incoming) {
            if (tr.equals(transition)) {
                return tr;
            }
        }
        return null;
    }

    /**
     * Returns the outgoing transition equals to the given transition. This
     * method should only be used between a TS and its clone. See
     * {@link Transition}.equals method for more details about transition
     * equality.
     *
     * @param transition
     * @return
     */
    public Transition getOutTransition(Transition transition) {
        for (Transition tr : this.outgoing) {
            if (tr.equals(transition)) {
                return tr;
            }
        }
        return null;
    }

    /**
     * Returns the number of incoming transitions.
     *
     * @return
     */
    public int incomingSize() {
        return this.incoming.size();
    }

    /**
     * Returns the number of outgoing transitions.
     *
     * @return
     */
    public int outgoingSize() {
        return this.outgoing.size();
    }

    // Adding elements
    /**
     * Add the incoming transition to this. If this.containsIncoming(tr), the
     * method has no effet.
     *
     * @param tr the transition to add. Pre: tr.getTo().equals(this)
     */
    public void addIncoming(Transition tr) {
        checkArgument(tr.getTo().equals(this));
        if (!this.incoming.contains(tr)) {
            this.incoming.add(tr);
        }
    }

    /**
     * Add the outgoing transition to this. If this.containsOutgoing(tr), the
     * method has no effet.
     *
     * @param tr the transition to add. Pre: tr.getFrom().equals(this)
     */
    public void addOutgoing(Transition tr) {
        checkArgument(tr.getFrom().equals(this));
        if (!this.outgoing.contains(tr)) {
            this.outgoing.add(tr);
        }
    }

    // Removing elements
    /**
     * Removes the given incoming transition and returns true or returns false
     * if !containsIncoming(tr)
     *
     * @param tr
     * @return
     */
    protected boolean removeIncoming(Transition tr) {
        return this.incoming.remove(tr);
    }

    /**
     * Removes the given outgoing transition and returns true or returns false
     * if !containsOutgoing(tr)
     *
     * @param tr
     * @return
     */
    protected boolean removeOutgoing(Transition tr) {
        return this.outgoing.remove(tr);
    }

    // Iterating over elements
    /**
     * Returns an iterator over incoming transitions.
     *
     * @return
     */
    public Iterator<Transition> incomingTransitions() {
        return this.incoming.iterator();
    }

    /**
     * Returns an iterator over outgoing transitions.
     *
     * @return
     */
    public Iterator<Transition> outgoingTransitions() {
        return this.outgoing.iterator();
    }

    // Inherited method redefinitions
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * State equivalence is based on the name. If two states have the same name,
     * they are equal.
     *
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
        if (!(obj instanceof State)) {
            return false;
        }
        State other = (State) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    // Visitor pattern
    /**
     * Used in the visitor pattern.
     *
     * @param visitor
     * @throws be.unamur.transitionsystem.exception.VisitException
     * @see Visitor
     */
    public void accept(Visitor visitor) throws VisitException {
        visitor.visit(this);
    }

    /**
     * Used in the visitor pattern.
     *
     * @param <T>
     * @param visitor
     * @return
     * @throws be.unamur.transitionsystem.exception.VisitException
     * @see VisitorWithReturn
     */
    public <T> T accept(VisitorWithReturn<T> visitor) throws VisitException {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "State [name=" + name + ", outgoing=" + outgoing + ", incoming="
                + incoming + "]";
    }

}
