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

import be.unamur.transitionsystem.exception.ExecutionNodeVisitException;
import com.google.common.collect.Lists;
import java.util.Objects;

/**
 * This class represents a node in an {@link ExecutionTree}.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 * @param <T> The type of the Transitions fired during an execution.
 */
public class ExecutionNode<T extends Transition> {

    private T value;
    private List<ExecutionNode<T>> sons;
    private ExecutionNode<T> father;

    ExecutionNode(T value, ExecutionNode<T> father) {
        this.value = value;
        this.sons = Lists.newArrayList();
        this.father = father;
    }

    /**
     * Return the transition of this execution node.
     * @return The transition of this execution node.
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the transition of this execution node.
     * @param value The transition of this execution node.
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Returns the father of this execution node.
     * @return the father of this execution node.
     */
    public ExecutionNode<T> getFather() {
        return father;
    }

    /**
     * Removes this from father node's sons if this has a father.
     */
    public void removeFromFather() {
        if (this.father != null) {
            this.father.sons.remove(this);
            this.father = null;
        }
    }

    /**
     * Add a son to this.
     *
     * @param sonValue The value of the son to add.
     * @return The added son.
     */
    public ExecutionNode<T> addSon(T sonValue) {
        ExecutionNode<T> son = new ExecutionNode(sonValue, this);
        this.sons.add(son);
        return son;
    }

    /**
     * Returns an iterator over this's sons.
     * @return An iterator over this's sons.
     */
    public Iterator<ExecutionNode<T>> sons() {
        return this.sons.iterator();
    }

    /**
     * Returns the number of sons.
     * @return The number of sons.
     */
    public int numberOfSons() {
        return this.sons.size();
    }

    @Override
    public String toString() {
        return "ExecutionNode [value=" + value + ", sons=" + sons + "]";
    }

    /**
     * Accept method for visitor pattern.
     *
     * @param visitor The visitor to use on this.
     * @throws ExecutionNodeVisitException If an exception occurs during node
     * visit.
     */
    public void accept(ExecutionNodeVisitor<T> visitor)
            throws ExecutionNodeVisitException {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.value);
        hash = 29 * hash + Objects.hashCode(this.sons);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ExecutionNode)) {
            return false;
        }
        final ExecutionNode<?> other = (ExecutionNode<?>) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return Objects.equals(this.sons, other.sons);
    }
    
    

}
