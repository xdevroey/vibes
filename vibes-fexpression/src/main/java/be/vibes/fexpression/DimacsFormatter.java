package be.vibes.fexpression;

import be.vibes.fexpression.exception.DimacsFormatException;
import be.vibes.fexpression.exception.FExpressionException;
import com.google.common.collect.BiMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class DimacsFormatter implements FExpressionVisitorWithReturn<Object> {

    private static final Logger logger = LoggerFactory.getLogger(DimacsFormatter.class);

    private BiMap<String, Integer> featureMapping;

    private enum LastCallType {

        and, or, not, feature
    };

    private LastCallType lastCall = null;

    public static int[][] format(FExpression expression, BiMap<String, Integer> featureMapping) throws DimacsFormatException {
        DimacsFormatter form = new DimacsFormatter(featureMapping);
        Object ret = null;
        try {
            FExpression cnf = expression.toCnf();
            logger.debug("CNF version of expression {} is {} ", expression, cnf);
            ret = cnf.accept(form);
            logger.debug("Dimacs format result is {}", ret);
        } catch (FExpressionException ex) {
            throw (DimacsFormatException) ex;
        }
        switch (form.lastCall) {
            case and:
                return (int[][]) ret;
            case or:
                return new int[][]{(int[]) ret};
            case not:
                return new int[][]{new int[]{(int) ret}};
            case feature:
                return new int[][]{new int[]{(int) ret}};
            default:
                throw new DimacsFormatException("Should have called at least one DIMACS format rule!");
        }

    }

    DimacsFormatter(BiMap<String, Integer> featureMapping) {
        this.featureMapping = featureMapping;
    }

    @Override
    public Object constant(boolean val) throws DimacsFormatException {
        throw new DimacsFormatException("Should not have constant value (" + val + ") in CNF for dimacs generation !");
    }

    @Override
    public Object feature(Feature feature) {
        lastCall = LastCallType.feature;
        return featureMapping.get(feature.getName());
    }

    @Override
    public Object not(FExpression expr) throws DimacsFormatException {
        int val = 0;
        try {
            Object featId = expr.accept(this);
            if (!(featId instanceof Integer)) {
                throw new DimacsFormatException("Not may only be applied to feature expression in CNF form!");
            }
            val = -((Integer) featId);
        } catch (FExpressionException ex) {
            throw (DimacsFormatException) ex;
        }
        lastCall = LastCallType.not;
        return val;
    }

    @Override
    public Object and(List<FExpression> operands) throws DimacsFormatException {
        int[][] tab = new int[operands.size()][];
        int i = 0;
        for (FExpression e : operands) {
            try {
                Object ret = e.accept(this);
                if (ret instanceof int[]) {
                    tab[i] = (int[]) ret;
                    i++;
                } else if (ret instanceof Integer) {
                    tab[i] = new int[]{(Integer) ret};
                    i++;
                } else {
                    throw new DimacsFormatException("And may only be applied to feature expression in CNF form!, found " + ret + "!");
                }
            } catch (FExpressionException ex) {
                throw (DimacsFormatException) ex;
            }
        }
        lastCall = LastCallType.and;
        return tab;
    }

    @Override
    public Object or(List<FExpression> operands) throws DimacsFormatException {
        int[] tab = new int[operands.size()];
        int i = 0;
        for (FExpression e : operands) {
            try {
                Object ret = e.accept(this);
                if (!(ret instanceof Integer)) {
                    throw new DimacsFormatException("Or may only be applied to feature expression in CNF form, found " + ret + "!");
                }
                tab[i] = (Integer) ret;
                i++;
            } catch (FExpressionException ex) {
                throw (DimacsFormatException) ex;
            }
        }
        lastCall = LastCallType.or;
        return tab;
    }

}
