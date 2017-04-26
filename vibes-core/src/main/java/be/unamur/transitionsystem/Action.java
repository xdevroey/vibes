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
 * Represents an abstract action in a transition system.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class Action extends TransitionSystemElement{

    /**
     * No action name. Transitions with actions having this name will be
     * considered as transitions without action.
     */
    public static final String NO_ACTION_NAME = "NO_ACTION";

    private String name;

    /**
     * Creates a new action.
     */
    public Action() {
        super();
    }

    /**
     * Creates a new action with the given name.
     *
     * @param name The name of the action.
     */
    public Action(String name) {
        this();
        this.name = name;
    }

    /**
     * Returns the anme of this action.
     *
     * @return The name of this action.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this action.
     *
     * @param name the new name of this action.
     */
    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * Compares two actions and return true if obj is an Action instance and the
     * two actions have the same name.
     *
     * @return True if this.getName() == obj.getName()
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Action)) {
            return false;
        }
        Action other = (Action) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Action [name=" + name + "]";
    }

}
