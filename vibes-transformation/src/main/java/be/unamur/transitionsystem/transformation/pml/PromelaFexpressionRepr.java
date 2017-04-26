package be.unamur.transitionsystem.transformation.pml;

/*
 * #%L
 * vibes-transformation
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.fexpression.FExpressionVisitorWithReturn;
import be.unamur.fts.fexpression.Feature;
import be.unamur.fts.fexpression.exception.FExpressionException;
import java.util.Iterator;
import java.util.List;

public class PromelaFexpressionRepr implements FExpressionVisitorWithReturn<String> {

    public static String getPromelaRepr(FExpression exp) {
        try {
            PromelaFexpressionRepr visitor = new PromelaFexpressionRepr();
            return exp.accept(visitor);
        } catch (FExpressionException ex) {
            throw new IllegalStateException("No exception should be launched by PromelaFexpressionVisitor class!", ex);
        }
    }

    private PromelaFexpressionRepr() {
    }

    @Override
    public String constant(boolean val) {
        return Boolean.toString(val);
    }

    @Override
    public String feature(Feature feature) {
        return "f." + feature.getName();
    }

    @Override
    public String not(FExpression expr) {
        try {
            return "!(" + expr.accept(this) + ")";
        } catch (FExpressionException ex) {
            throw new IllegalStateException("No exception should be launched by PromelaFexpressionVisitor class!", ex);
        }
    }

    @Override
    public String and(List<FExpression> operands) {
        switch (operands.size()) {
            case 0:
                return "";
            case 1: {
                try {
                    return operands.get(0).accept(this);
                } catch (FExpressionException ex) {
                    throw new IllegalStateException("No exception should be launched by PromelaFexpressionVisitor class!", ex);
                }
            }
            default:
                StringBuilder builder = new StringBuilder();
                Iterator<FExpression> it = operands.iterator();
                while (it.hasNext()) {
                    try {
                        builder.append("(");
                        builder.append(it.next().accept(this));
                        builder.append(")");
                        if (it.hasNext()) {
                            builder.append("&&");
                        }
                    } catch (FExpressionException ex) {
                        throw new IllegalStateException("No exception should be launched by PromelaFexpressionVisitor class!", ex);
                    }
                }
                return builder.toString();
        }
    }

    @Override
    public String or(List<FExpression> operands) {
        switch (operands.size()) {
            case 0:
                return "";
            case 1: {
                try {
                    return operands.get(0).accept(this);
                } catch (FExpressionException ex) {
                    throw new IllegalStateException("No exception should be launched by PromelaFexpressionVisitor class!", ex);
                }
            }
            default:
                StringBuilder builder = new StringBuilder();
                Iterator<FExpression> it = operands.iterator();
                while (it.hasNext()) {
                    try {
                        builder.append("(");
                        builder.append(it.next().accept(this));
                        builder.append(")");
                        if (it.hasNext()) {
                            builder.append("||");
                        }
                    } catch (FExpressionException ex) {
                        throw new IllegalStateException("No exception should be launched by PromelaFexpressionVisitor class!", ex);
                    }
                }
                return builder.toString();
        }
    }

}
