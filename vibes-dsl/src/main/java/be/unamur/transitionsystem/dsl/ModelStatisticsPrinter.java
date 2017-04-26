package be.unamur.transitionsystem.dsl;

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
import java.io.PrintStream;
import java.util.Map;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.stat.ModelStatistics;
import be.unamur.transitionsystem.stat.ModelStatisticsBuilder;
import be.unamur.transitionsystem.usagemodel.UsageModel;

/**
 * This class defines static methods used to get/print models statistics. 
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class ModelStatisticsPrinter {

    public static void printModelStatistics(LabelledTransitionSystem ts) {
        printModelStatistics(ts, System.out);
    }

    public static void printModelStatistics(FeaturedTransitionSystem fts) {
        printModelStatistics(fts, System.out);
    }

    public static void printModelStatistics(UsageModel usageModel) {
        printModelStatistics(usageModel, System.out);
    }

    public static void printModelStatistics(LabelledTransitionSystem ts, PrintStream out) {
        ModelStatistics stats = ModelStatisticsBuilder.INSTANCE.build(ts);
        out.print(stats.getStatistics());
    }

    public static void printModelStatistics(FeaturedTransitionSystem fts, PrintStream out) {
        ModelStatistics stats = ModelStatisticsBuilder.INSTANCE.build(fts);
        out.print(stats.getStatistics());
    }

    public static void printModelStatistics(UsageModel usageModel, PrintStream out) {
        ModelStatistics stats = ModelStatisticsBuilder.INSTANCE.build(usageModel);
        out.print(stats.getStatistics());
    }

    public static Map<String, Object> getModelStatistics(LabelledTransitionSystem ts) {
        return ModelStatisticsBuilder.INSTANCE.build(ts).getStatisticsValues();
    }

    public static Map<String, Object> getModelStatistics(FeaturedTransitionSystem fts) {
        return ModelStatisticsBuilder.INSTANCE.build(fts).getStatisticsValues();
    }

    public static Map<String, Object> getModelStatistics(UsageModel usageModel) {
        return ModelStatisticsBuilder.INSTANCE.build(usageModel).getStatisticsValues();
    }

}
