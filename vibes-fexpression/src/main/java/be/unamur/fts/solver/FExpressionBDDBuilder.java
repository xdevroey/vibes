package be.unamur.fts.solver;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.fexpression.FExpressionVisitorWithReturn;
import java.util.Map;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import be.unamur.fts.fexpression.Feature;
import be.unamur.fts.fexpression.exception.FExpressionException;
import static com.google.common.base.Preconditions.*;
import java.util.List;

public class FExpressionBDDBuilder implements FExpressionVisitorWithReturn<BDD> {

    private BDDFactory factory;
    private Map<String, BDD> featureMapping;

    public FExpressionBDDBuilder(BDDFactory factory, Map<String, BDD> featureMapping) {
        this.factory = factory;
        this.featureMapping = featureMapping;
    }

    @Override
    public BDD constant(boolean val) {
        if (val) {
            return this.factory.one();
        } else {
            return this.factory.zero();
        }
    }

    @Override
    public BDD feature(Feature feature) {
        BDD featBdd = this.featureMapping.get(feature.getName());
        checkNotNull(featBdd, "Feature %s not found in feature to BDD mapping!", feature);
        return featBdd;
    }

    @Override
    public BDD not(FExpression expr) {
        try {
            BDD operand = expr.accept(this);
            return operand.not();
        } catch (FExpressionException ex) {
            throw new IllegalStateException("No exception should happen while using this visitor!", ex);
        }
    }

    @Override
    public BDD and(List<FExpression> operands) {
        try {
            BDD conj = factory.one();
            for (FExpression e : operands) {
                conj = conj.and(e.accept(this));
            }
            return conj;
        } catch (FExpressionException ex) {
            throw new IllegalStateException("No exception should happen while using this visitor!", ex);
        }
    }

    @Override
    public BDD or(List<FExpression> operands) {
        try {
            BDD disj = factory.zero();
            for (FExpression e : operands) {
                disj = disj.or(e.accept(this));
            }
            return disj;
        } catch (FExpressionException ex) {
            throw new IllegalStateException("No exception should happen while using this visitor!", ex);
        }
    }

}
