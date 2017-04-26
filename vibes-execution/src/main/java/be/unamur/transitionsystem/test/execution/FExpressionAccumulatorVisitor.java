package be.unamur.transitionsystem.test.execution;

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
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.fexpression.ConstraintTransformation;
import be.unamur.transitionsystem.ExecutionNode;
import be.unamur.transitionsystem.ExecutionNodeVisitor;
import be.unamur.transitionsystem.exception.ExecutionNodeVisitException;
import be.unamur.transitionsystem.fts.FeaturedTransition;

/**
 * {@link ExecutionNode} visitor used to compute the feature expression of
 * products able to execute the execution tree formed by the node.
 *
 * Correct sequence of calls should be:<br>
 *
 * <pre>
 * FExpressionAccumulatorVisitor visitor = new FExpressionAccumulatorVisitor();
 * tree.getRoot().accept(visitor);
 * visitor.getExpression(); // Returns products feature expression
 * </pre>
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class FExpressionAccumulatorVisitor implements
        ExecutionNodeVisitor<FeaturedTransition> {

    private FExpression expression = null;

    private Set<FExpression> set = Sets.newHashSet((FExpression) FExpression.trueValue());

    private List<FExpression> disjunction = Lists.newArrayList();

    @Override
    public void visit(ExecutionNode<FeaturedTransition> node)
            throws ExecutionNodeVisitException {
        if (node.getValue() != null) {
            set.add(node.getValue().getFeatureExpression());
        }
        if (node.numberOfSons() > 1) {
            ExecutionNode<FeaturedTransition> son;
            Set<FExpression> savSet;
            Iterator<ExecutionNode<FeaturedTransition>> it = node.sons();
            while (it.hasNext()) {
                son = it.next();
                savSet = Sets.newHashSet(set);
                son.accept(this);
                set = savSet;
            }
        } else if (node.numberOfSons() == 1) {
            node.sons().next().accept(this);
        } else {
            disjunction.add(ConstraintTransformation.getConjunction(set));
        }

    }

    /**
     * After calling visit(), returns the feature expression of products able to
     * execute the tree formed by the execution node or TRUE if the visit method
     * has not been called.
     */
    public FExpression getExpression() {
        if (expression == null) {
            expression = ConstraintTransformation.getDisjunction(disjunction);
        }
        return expression;
    }
}
