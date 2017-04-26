/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.unamur.fts.fexpression;

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
