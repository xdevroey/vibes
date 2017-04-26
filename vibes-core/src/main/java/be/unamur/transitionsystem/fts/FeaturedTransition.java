package be.unamur.transitionsystem.fts;

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
import be.unamur.fts.fexpression.FExpression;
import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;

import static com.google.common.base.Preconditions.*;

/**
 * This class represent a Featured Transition used in
 * {@link FeaturedTransitionSystem} models. A featured transitions has an action
 * and a feature expression representing products able to execute the
 * transitions. In this implementation, featured expressions are represented
 * using boolean expressions over features ({@link FExpression}) .
 *
 * @see FExpression
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class FeaturedTransition extends Transition {

    private FExpression featureExpression;

    /**
     * Builds a new featured transition.
     */
    public FeaturedTransition() {
        super();
    }

    /**
     * Builds a new featured transition with a feature expression set to true.
     * @param action
     * @param to
     */
    public FeaturedTransition(State from, State to, Action action) {
        this(from, to, action, FExpression.trueValue());
    }

    /**
     * Builds a new featured transition with the given values.
     * @param featureExpression
     * @param action
     */
    public FeaturedTransition(State from, State to, Action action,
            FExpression featureExpression) {
        super(from, to, action);
        setFeatureExpression(featureExpression);
    }

    /**
     * Sets the transition's feature expression to the given featureExpression.
     *
     * @param featureExpression The new feature expression. Pre:
     * featureExpression !=null
     */
    protected void setFeatureExpression(FExpression featureExpression) {
        checkNotNull(featureExpression);
        this.featureExpression = featureExpression;
    }

    /**
     * Returns the transition's feature expression.
     * @return 
     */
    public FExpression getFeatureExpression() {
        return featureExpression;
    }

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (!super.equals(obj))
//			return false;
//		if (!(obj instanceof FeaturedTransition))
//			return false;
//		FeaturedTransition other = (FeaturedTransition) obj;
//		if (featureExpression == null) {
//			if (other.featureExpression != null)
//				return false;
//		} else if (!featureExpression.equals(other.featureExpression))
//			return false;
//		return true;
//	}
    @Override
    public String toString() {
        return "FTSTransition [from=" + getFrom().getName() + ", to=" + getTo().getName()
                + ", action=" + getAction().getName() + ", featureExpression="
                + featureExpression + "]";
    }

}
