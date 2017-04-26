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
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransition;

/**
 * Cf. Classen, A. (2011). Modelling and Model Checking Variability-Intensive
 * Systems. PReCISE Research Center, Faculty of Computer Science, University of
 * Namur (FUNDP). page 153.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class FtsPromelaPrinter extends LtsPromelaPrinter {

    public static final String ROOT_FEATURE = "rootFeature";

    private List<String> features;

    public FtsPromelaPrinter(OutputStream output, TransitionSystem ts,
            List<String> features) {
        super(output, ts);
        this.features = features;
    }

    @Override
    protected void printVariables() {
        // Print features
        out.println("typedef features {");
        for (Iterator<String> itFeatures = features.iterator(); itFeatures.hasNext();) {
            String f = (String) itFeatures.next();
            out.print("    bool " + f);
            if (itFeatures.hasNext()) {
                out.println(";");
            } else {
                out.println();
            }
        }
        out.println("}");
        out.println();
        out.println("  features f;");
        super.printVariables();
    }

//	@Override
//	protected void printTransition(Transition trans) {
//		FtsTransition tr = (FtsTransition) trans;
//		String fexpr;
//		if (tr.getFeatureExpression() != null) {
//			fexpr = printFeatureExpression(tr.getFeatureExpression());
//		} else {
//			fexpr = printFeatureExpression(ConstantNode.TRUE);
//		}
//		out.println("               /* to  " + tr.getTo().getName() + "  action = "
//				+ tr.getAction().getName() + "  fExpression= "
//				+ tr.getFeatureExpression().toString() + "*/");
//		out.println("               :: " + fexpr + "; if :: atomic{ current = "
//				+ stateMap.getStateMapping(tr.getTo()).intValue() + "; "
//				+ " previousAction = " + actionMap.getActionMapping(tr.getAction())
//				+ "; } fi;");
//	}
    protected void printTransition(Transition trans) {
        FeaturedTransition tr = (FeaturedTransition) trans;
        String fexpr;
        if (tr.getFeatureExpression() != null) {
            fexpr = printFeatureExpression(tr.getFeatureExpression());
        } else {
            fexpr = printFeatureExpression(FExpression.trueValue());
        }
        int id = stateMap.getStateMapping(tr.getTo());
        out.println("            /* " + tr.getTo().getName() + " */");
        out.println("              :: " + fexpr + ";");
        out.println("                 previousAction = " + actionMap.getActionMapping(tr.getAction()) + ";");
        out.println("                 goto state" + id + ";");
    }

    private String printFeatureExpression(FExpression featureExpression) {
        StringBuilder str = new StringBuilder();
        str.append(PromelaFexpressionRepr.getPromelaRepr(featureExpression));
        return str.toString();
    }

}
