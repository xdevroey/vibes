package be.unamur.fexpression.analyze.main;

import be.unamur.fts.fexpression.DimacsModel;
import be.unamur.fts.fexpression.ParserUtil;
import static com.google.common.base.Preconditions.*;

import be.unamur.fts.solver.BDDSolverFacade;
import be.unamur.fts.solver.SolverFacade;

public class Main {

    public static void main(String[] args) throws Exception {
        checkArgument(args.length >= 2, "Wrong number of arguments: java -jar toolbox-product-analyze.jar <fd.dimacs> <fexpression>");
        SolverFacade solver = new BDDSolverFacade(DimacsModel.createFromDimacsFile(args[0]));
        solver.addConstraint(ParserUtil.getInstance().parse(args[1]));
        System.out.println(solver.getNumberOfSolutions());
    }

}
