package be.vibes.ts.io.dot;

import be.vibes.fexpression.FExpression;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.Transition;
import java.io.PrintStream;

/**
 * Prints featured transition systems in DOT format (to be used with Graphviz).
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class FeaturedTransitionSystemDotPrinter extends TransitionSystemDotPrinter {
    
    public FeaturedTransitionSystemDotPrinter(FeaturedTransitionSystem fts, PrintStream output) {
        super(fts, output);
    }
    
    @Override
    protected void printTransition(Transition tr) {
        FExpression fexpr = ((FeaturedTransitionSystem)ts).getFExpression(tr);
        out.println(getStateId(tr.getSource()) + " -> "
                + getStateId(tr.getTarget()) + " [ label=\" "
                + tr.getAction().getName() + "/" + fexpr.toString() + " \" ];");
    }
    
    
}
