package be.vibes.fexpression;

import be.vibes.fexpression.exception.FExpressionException;
import java.util.List;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public interface FExpressionVisitorWithReturn<E> {

    public E constant(boolean val) throws FExpressionException;

    public E feature(Feature feature) throws FExpressionException;

    public E not(FExpression expr) throws FExpressionException;

    public E and(List<FExpression> operands) throws FExpressionException;

    public E or(List<FExpression> operands) throws FExpressionException;

}
