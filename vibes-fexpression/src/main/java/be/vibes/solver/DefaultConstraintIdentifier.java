package be.vibes.solver;

import be.vibes.fexpression.FExpression;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class DefaultConstraintIdentifier implements ConstraintIdentifier {
    
    private final FExpression constraint;

    DefaultConstraintIdentifier(FExpression constraint) {
        this.constraint = constraint;
    }

    @Override
    public FExpression getConstraint() {
        return constraint;
    }
    
}
