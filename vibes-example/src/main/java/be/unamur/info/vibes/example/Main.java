package be.unamur.info.vibes.example;

import be.unamur.fts.fexpression.DimacsModel;
import be.unamur.fts.fexpression.FExpression;
import be.unamur.fts.fexpression.configuration.Configuration;
import be.unamur.fts.solver.Sat4JSolverFacade;
import be.unamur.fts.solver.SolverFacade;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import static be.unamur.transitionsystem.dsl.selection.AllStates.*;
import static be.unamur.transitionsystem.dsl.selection.Random.*;
import static be.unamur.transitionsystem.dsl.selection.Dissimilar.*;
import static be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders.*;
import be.unamur.transitionsystem.dsl.test.mutation.ConfiguredMutagen;
import be.unamur.transitionsystem.dsl.test.mutation.FeaturedMutantsModels;
import be.unamur.transitionsystem.dsl.test.mutation.Mutagen;
import be.unamur.transitionsystem.dsl.transforme.Dot;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.TestSet;
import be.unamur.transitionsystem.test.mutation.FeaturedMutantsModel;
import be.unamur.transitionsystem.test.mutation.MutationOperator;
import java.util.Iterator;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Main {

    public static void main(String[] args) throws Exception {
        /* To be reworked
        
        FeaturedTransitionSystem svm = new SodaVendingMachineModel().getTransitionSystem();
        System.out.println(Dot.format(svm));

        // Manual test suite selection
        TestSet testSuite = new ManualTestSuite().getTestSet();

        // Feature model loading
        String dimacsFile = Main.class.getClassLoader().getResource("svm.splot.dimacs").getFile();
        DimacsModel featureModel = DimacsModel.createFromDimacsFile(dimacsFile);
        SolverFacade solver = new Sat4JSolverFacade(featureModel);

        // All-states test suite selection
        TestSet allStatesSuite = allStatesSelection(svm, solver);

        // Random test suite selection
        TestSet randomSuite = randomSelection(svm, solver);

        // Dissimilar selection
        from(svm, solver)
                .during(30000)
                .withLocalMaxDistance(ftsDissimilarity(solver, levenshtein(), avg()))
                .generate(5);

        MutationOperator op = Mutagen.stateMissing(svm)
                .stateSelectionStrategy(svm.getState("s4"), svm.getState("s8"), svm.getState("s9"))
                .done();

        LabelledTransitionSystem lts = loadLabelledTransitionSystem("product.xml");

        FeaturedMutantsModel fmm = ConfiguredMutagen.configure("").mutate(lts);

        FExpression alive = FeaturedMutantsModels.getAliveMutants(testSuite.get(0), fmm);
        solver.addConstraint(alive);
        Iterator<Configuration> solutions = solver.getSolutions();
        while(solutions.hasNext()) {
            System.out.println(solutions.next());
        }
    */

    }

}
