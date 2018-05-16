package be.vibes.ts.io.dot;

import be.vibes.ts.Transition;
import be.vibes.ts.UsageModel;
import java.io.PrintStream;

/**
 * Prints usage models in DOT format (to be used with Graphviz).
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class UsageModelDotPrinter extends TransitionSystemDotPrinter{
    
    public UsageModelDotPrinter(UsageModel um, PrintStream output) {
        super(um, output);
    }

    @Override
    protected void printTransition(Transition tr) {
        double proba = ((UsageModel)ts).getProbability(tr);
        out.println(getStateId(tr.getSource()) + " -> "
                + getStateId(tr.getTarget()) + " [ label=\" "
                + tr.getAction().getName() + "/" + proba + " \" ];");
    }
}
