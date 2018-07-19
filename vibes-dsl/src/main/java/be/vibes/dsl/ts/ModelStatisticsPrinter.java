package be.vibes.dsl.ts;

/*
 * #%L
 * vibes-dsl
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
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
