package be.unamur.info.toolbox.wordpressts2fts.main;

import be.unamur.fts.fexpression.FExpression;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders;
import be.unamur.transitionsystem.dsl.TransitionSystemXmlPrinter;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.abego.treelayout.internal.util.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class with code to tag a LTS generated from a Wordpress web log and
 * produce a FTS (plugins, themes, and administrator access are considered as
 * features)
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static final String PLUGIN_REGEXP = ".*/wp-content/plugins/([^/]+)/.*";
    public static final String THEME_REGEXP = ".*/wp-content/themes/([^/]+)/.*";
    public static final String ADMIN_REGEXP = ".*/wp-admin/.*";

    public static final String ADMIN_FEATURE_EXPRESSION = "Admin_user";

    private final Set<String> plugins;
    private final Set<String> themes;

    private final FeaturedTransitionSystem fts;

    private final Pattern pluginPattern;
    private final Pattern themePattern;
    private final Pattern adminPattern;

    public Main(LabelledTransitionSystem lts) {
        plugins = Sets.newHashSet();
        themes = Sets.newHashSet();
        pluginPattern = Pattern.compile(PLUGIN_REGEXP);
        themePattern = Pattern.compile(THEME_REGEXP);
        adminPattern = Pattern.compile(ADMIN_REGEXP);
        fts = new FeaturedTransitionSystem(lts);
    }

    private void process() {
        Iterator<State> itStates = fts.states();
        while (itStates.hasNext()) {
            State state = itStates.next();
            for (Transition tr : Lists.newArrayList(state.outgoingTransitions())) {
                tagTransition((FeaturedTransition) tr);
            }
        }
    }

    private void tagTransition(FeaturedTransition tr) {
        LOG.debug("Processing transition {}", tr);
        String target = tr.getTo().getName();
        Matcher m = pluginPattern.matcher(target);
        if (m.matches()) {
            // tag with plugin name as feature expression and add plugin name to features
            String plugin = m.group(1);
            Preconditions.checkNotNull(plugin);
            plugin = formatFeatureName(plugin.replaceAll("-", "_") + "_plugin");
            FExpression expr = tr.getFeatureExpression().and(FExpression.featureExpr(plugin));
            changeFeatureExpression(tr, expr.applySimplification());
            plugins.add(plugin);
        } else if ((m = themePattern.matcher(target)).matches()) {
            // tag with theme name as feature expression and add theme name to features
            String theme = m.group(1);
            Preconditions.checkNotNull(theme);
            theme = formatFeatureName(theme.replaceAll("-", "_") + "_theme");
            FExpression expr = tr.getFeatureExpression().and(FExpression.featureExpr(theme));
            changeFeatureExpression(tr, expr.applySimplification());
            themes.add(theme);
        } else if (adminPattern.matcher(target).matches()) {
            // tag with administrator privilege feature
            FExpression expr = tr.getFeatureExpression().and(FExpression.featureExpr(ADMIN_FEATURE_EXPRESSION));
            changeFeatureExpression(tr, expr.applySimplification());
        }
    }

    private void changeFeatureExpression(FeaturedTransition tr, FExpression fexpr) {
        fts.removeTransition(tr);
        fts.addTransition(tr.getFrom(), tr.getTo(), tr.getAction(), fexpr);
    }

    private void printFTS(String outputFile) {
        TransitionSystemXmlPrinter.print(fts, outputFile);
    }

    private void printTVL(String outputFile) throws IOException {
        File file = new File(outputFile);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        PrintStream out = new PrintStream(file);
        out.println("/*");
        out.println(" * Generated on " + new GregorianCalendar().getTime());
        out.println(" * using VIBeS (https://projects.info.unamur.be/vibes/)");
        out.println(" */");
        out.println();
        out.printf("root %s {", formatFeatureName(outputFile.replace(".tvl", ""))).println();
        out.println("    group someof {");
        out.println("        Plugins,");
        out.println("        Themes,");
        out.printf("        %s", ADMIN_FEATURE_EXPRESSION).println();
        out.println("    }");
        out.println("}");
        out.println();
        out.println("Plugins {");
        out.println("    group [1..*] {");
        Iterator<String> it = plugins.iterator();
        while (it.hasNext()) {
            out.printf("        %s", formatFeatureName(it.next()));
            if (it.hasNext()) {
                out.print(",");
            }
            out.println();
        }
        out.println("    }");
        out.println('}');
        out.println();
        out.println("Themes {");
        out.println("    group [1..*] {");
        it = themes.iterator();
        while (it.hasNext()) {
            out.printf("        %s", formatFeatureName(it.next()));
            if (it.hasNext()) {
                out.print(",");
            }
            out.println();
        }
        out.println("    }");
        out.println('}');
    }

    private String formatFeatureName(String name) {
        if (!Character.isLetter(name.charAt(0))) {
            name = "A" + name;
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
    }

    private void printSXFM(String outputFile) throws IOException {
        File file = new File(outputFile);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        PrintStream out = new PrintStream(file);
        Date now = new GregorianCalendar().getTime();
        out.println("<!--");
        out.println(" Generated on " + now);
        out.println(" using VIBeS (https://projects.info.unamur.be/vibes/)");
        out.println(" -->");
        String rootFeature = formatFeatureName(outputFile.replace(".sxfm", ""));
        out.printf("<feature_model name=\"%s\">", rootFeature).println();
        out.println("<meta>");
        out.println("<data name=\"description\"></data>");
        out.println("<data name=\"creator\">Xavier Devroey</data>");
        out.println("<data name=\"address\"></data>");
        out.println("<data name=\"email\">xavier.devroey@unamur.be</data>");
        out.println("<data name=\"phone\"></data>");
        out.println("<data name=\"website\">https://sites.google.com/site/xdevroey/home</data>");
        out.println("<data name=\"organization\">PReCISE</data>");
        out.println("<data name=\"department\">University of Namur</data>");
        out.printf("<data name=\"date\">%td/%tm/%tY</data>", now, now, now).println();
        out.println("<data name=\"reference\"></data>");
        out.println("</meta>");
        out.println("<feature_tree>");
        out.printf(":r %s (%s)", rootFeature, rootFeature).println();
        if (plugins.size() > 0) {
            out.println("\t:o Plugins (Plugins)");
            out.println("\t\t:g (plugins_group) [1,*] ");
            for (String plugin : plugins) {
                plugin = formatFeatureName(plugin);
                out.printf("\t\t\t: %s (%s)", plugin, plugin).println();
            }
        }
        if (themes.size() > 0) {
            out.println("\t:o Themes (Themes)");
            out.println("\t\t:g (themes_group) [1,*] ");
            for (String theme : themes) {
                theme = formatFeatureName(theme);
                out.printf("\t\t\t: %s (%s)", theme, theme).println();
            }
        }
        out.printf("\t:o %s (%s)", ADMIN_FEATURE_EXPRESSION, ADMIN_FEATURE_EXPRESSION).println();
        out.println("</feature_tree>");
        out.println("<constraints>");
        out.println("</constraints>");
        out.println("</feature_model>");
    }

    public static void main(String[] args) throws Exception {
        Contract.checkArg(args.length >= 3, "Wrong number of arguments! Usage: java -jar toolbox-wordpress-ts2fts.jar <input-model.ts> <output-model.fts> <output-model.tvl>");
        LOG.info("Starting");
        LabelledTransitionSystem lts = TransitionSystemXmlLoaders.loadLabelledTransitionSystem(args[0]);
        LOG.info("Input TS loaded");
        Main main = new Main(lts);
        LOG.info("Starting tagging transitions with features!");
        main.process();
        LOG.info("Printing FTS");
        main.printFTS(args[1]);
        LOG.info("Printing TVL");
        main.printTVL(args[2]);
        LOG.info("Printing SXFM");
        main.printSXFM(args[2].replace(".tvl", ".splot.xml"));
        LOG.info("Done");
    }

}
