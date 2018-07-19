package be.vibes.ts.io.dot;

/*-
 * #%L
 * VIBeS: core
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
