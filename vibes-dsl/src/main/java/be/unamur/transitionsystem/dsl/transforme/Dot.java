
package be.unamur.transitionsystem.dsl.transforme;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.transformation.dot.FtsDotPrinter;
import be.unamur.transitionsystem.transformation.dot.LtsDotPrinter;
import be.unamur.transitionsystem.transformation.dot.UsageModelDotPrinter;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Dot {
    
    public static String format(FeaturedTransitionSystem fts){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FtsDotPrinter printer = new FtsDotPrinter(new PrintStream(out), fts);
        printer.printDot();
        printer.flush();
        return out.toString();
    }
    
    public static String format(LabelledTransitionSystem lts){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        LtsDotPrinter printer = new LtsDotPrinter(new PrintStream(out), lts);
        printer.printDot();
        printer.flush();
        return out.toString();
    }
      
    public static String format(UsageModel um){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UsageModelDotPrinter printer = new UsageModelDotPrinter(new PrintStream(out), um);
        printer.printDot();
        printer.flush();
        return out.toString();
    }
    
}
