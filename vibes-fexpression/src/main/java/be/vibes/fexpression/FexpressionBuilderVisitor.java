package be.vibes.fexpression;

/*-
 * #%L
 * VIBeS: featured expressions
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class FexpressionBuilderVisitor extends FexpressionBaseVisitor<FExpression> {

    @Override
    public FExpression visitNotExpr(FexpressionParser.NotExprContext ctx) {
        FExpression expr = ctx.op.accept(this);
        expr.notWith();
        return expr;
    }

    @Override
    public FExpression visitFeatureName(FexpressionParser.FeatureNameContext ctx) {
        return new FExpression(ctx.FEATURE().toString());
    }

    @Override
    public FExpression visitParenthesis(FexpressionParser.ParenthesisContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override
    public FExpression visitConstantTrue(FexpressionParser.ConstantTrueContext ctx) {
        return FExpression.trueValue();
    }

    @Override
    public FExpression visitConstantFalse(FexpressionParser.ConstantFalseContext ctx) {
        return FExpression.falseValue();
    }

    @Override
    public FExpression visitAndOrExpr(FexpressionParser.AndOrExprContext ctx) {
        FExpression left = ctx.opLeft.accept(this);
        FExpression right = ctx.opRight.accept(this);
        if (ctx.operator.getType() == FexpressionParser.AND) {
            left.andWith(right);
        } else {
            left.orWith(right);
        }
        return left;
    }

}
