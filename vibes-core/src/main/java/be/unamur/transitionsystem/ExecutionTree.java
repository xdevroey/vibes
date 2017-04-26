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
import be.unamur.transitionsystem.exception.ExecutionNodeVisitException;
import java.util.Objects;

/**
 * An execution tree represents the possible executions in a
 * {@link TransitionSystem}. Each path in the tree to a leaf node will
 * correspond to a sequence of transitions executed in a TS.
 *
 * <br>
 * <b>Inv</b>: ExecutionTree.getRoot().getValue() is always null, root has no
 * values, only sons corresponding to the transitions that may be fired from the
 * initial state. This.getRoot() forms a tree structure, no loops are allowed.
 * All the transitions in the tree belongs to the same TS.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 * @param <T> The type of transitions retained in the tree.
 */
public class ExecutionTree<T extends Transition> {

    private ExecutionNode<T> root;

    /**
     * Creates a new tree.
     */
    public ExecutionTree() {
        root = new ExecutionNode<T>(null, null);
    }

    /**
     * Returns the root execution node.
     *
     * @return the root execution node.
     */
    public ExecutionNode<T> getRoot() {
        return this.root;
    }

    @Override
    public String toString() {
        return "ExecutionTree [root=" + root + "]";
    }

    /**
     * Returns true if there exists at least one path in the TS.
     * @return True if there exists at least one path in the TS.
     */
    public boolean hasPath() {
        return root.numberOfSons() > 0;
    }

    /**
     * Accept method, used to visit this.getRoot().
     *
     * @param visitor The execution node visitor used on this.getRoot().
     * @throws ExecutionNodeVisitException
     */
    public void accept(ExecutionNodeVisitor<T> visitor)
            throws ExecutionNodeVisitException {
        if (this.root != null) {
            this.root.accept(visitor);
        }
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof ExecutionTree){
            ExecutionTree e = (ExecutionTree) other;
            return getRoot().equals(e.getRoot());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.root);
        return hash;
    }
    
}
