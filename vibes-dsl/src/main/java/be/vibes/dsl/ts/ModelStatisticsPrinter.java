package be.vibes.dsl.ts;

/*
 * #%L
 * vibes-dsl
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
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.ModelStatistics;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.TransitionSystemModelStatistics;
import be.vibes.ts.UsageModel;
import java.io.PrintStream;
import java.util.Map;

/**
 * This class defines static methods used to get/print models statistics. 
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class ModelStatisticsPrinter {

    public static void printModelStatistics(TransitionSystem ts) {
        printModelStatistics(ts, System.out);
    }

    public static void printModelStatistics(FeaturedTransitionSystem fts) {
        printModelStatistics(fts, System.out);
    }

    public static void printModelStatistics(UsageModel usageModel) {
        printModelStatistics(usageModel, System.out);
    }

    public static void printModelStatistics(TransitionSystem ts, PrintStream out) {
        ModelStatistics stats = TransitionSystemModelStatistics.getStatistics(ts);
        out.print(stats.getStatistics());
    }

    public static void printModelStatistics(FeaturedTransitionSystem fts, PrintStream out) {
        ModelStatistics stats = TransitionSystemModelStatistics.getStatistics(fts);
        out.print(stats.getStatistics());
    }

    public static void printModelStatistics(UsageModel usageModel, PrintStream out) {
        ModelStatistics stats = TransitionSystemModelStatistics.getStatistics(usageModel);
        out.print(stats.getStatistics());
    }

    public static Map<String, Object> getModelStatistics(TransitionSystem ts) {
        return TransitionSystemModelStatistics.getStatistics(ts).getStatisticsValues();
    }

    public static Map<String, Object> getModelStatistics(FeaturedTransitionSystem fts) {
        return TransitionSystemModelStatistics.getStatistics(fts).getStatisticsValues();
    }

    public static Map<String, Object> getModelStatistics(UsageModel usageModel) {
        return TransitionSystemModelStatistics.getStatistics(usageModel).getStatisticsValues();
    }

}
