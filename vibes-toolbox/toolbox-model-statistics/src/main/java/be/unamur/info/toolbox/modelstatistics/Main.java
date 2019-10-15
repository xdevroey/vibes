package be.unamur.info.toolbox.modelstatistics;

import static com.google.common.base.Preconditions.checkArgument;

import static be.vibes.dsl.ts.ModelStatisticsPrinter.*;
import static be.vibes.dsl.io.Xml.*;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Main {

    public static void main(String[] args) {
        checkArgument(args.length >= 2, "Wrong number of arguments: java -jar toolbox-model-statistics.jar (-ts <model.ts> | -fts <model.fts> | -um <model.um>)");
        if (args[0].equals("-ts")) {
            printModelStatistics(loadTransitionSystem(args[1]));
        } else if (args[0].equals("-fts")) {
            printModelStatistics(loadFeaturedTransitionSystem(args[1]));
        } else if (args[0].equals("-um")) {
            printModelStatistics(loadUsageModel(args[1]));
        } else {
            throw new IllegalArgumentException("Wrong arguments: java -jar toolbox-model-statistics.jar (-ts <model.ts> | -fts <model.fts> | -um <model.um>)");
        }
    }
    
}
