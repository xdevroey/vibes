package be.vibes.dsl.io;

/*-
 * #%L
 * VIBeS: dsl
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

import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.UsageModel;
import be.vibes.ts.io.dot.FeaturedTransitionSystemDotPrinter;
import be.vibes.ts.io.dot.TransitionSystemDotPrinter;
import be.vibes.ts.io.dot.UsageModelDotPrinter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Prints Models in Dot format. To be used with Graphviz tools.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Dot {

    public static String format(FeaturedTransitionSystem fts) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FeaturedTransitionSystemDotPrinter printer = new FeaturedTransitionSystemDotPrinter(fts, new PrintStream(out));
        printer.printDot();
        printer.flush();
        return out.toString();
    }

    public static String format(TransitionSystem lts) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransitionSystemDotPrinter printer = new TransitionSystemDotPrinter(lts, new PrintStream(out));
        printer.printDot();
        printer.flush();
        return out.toString();
    }

    public static String format(UsageModel um) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UsageModelDotPrinter printer = new UsageModelDotPrinter(um, new PrintStream(out));
        printer.printDot();
        printer.flush();
        return out.toString();
    }

}
