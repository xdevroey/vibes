package be.unamur.transitionsystem.transformation.main;

import be.unamur.fts.fexpression.Feature;
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.fts.FeaturedTransition;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.transformation.pml.FtsPromelaPrinter;
import be.unamur.transitionsystem.transformation.pml.LtsPromelaPrinter;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.Option;

/**
 * Transforms the input TS to Promela format.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class FPromelaTransformator implements Transformator {

    private static final String OPTION_NAME = "fpromela";

    public static final FPromelaTransformator FPROMELA = new FPromelaTransformator();

    private FPromelaTransformator() {
    }

    @Override
    public Option getCommandLineOption() {
        return Option.builder(OPTION_NAME)
                .desc("Transform input to Promela (.pml) file.")
                .build();
    }

    @Override
    public void transform(LabelledTransitionSystem lts, OutputStream out, String... cmdArgs) throws IOException {
        LtsPromelaPrinter printer = new LtsPromelaPrinter(out, lts);
        printer.print();
    }

    @Override
    public void transform(FeaturedTransitionSystem fts, OutputStream out, String... cmdArgs) throws IOException {
        Set<Feature> features = Sets.newHashSet();
        Iterator<State> it = fts.states();
        while (it.hasNext()) {
            Iterator<Transition> outgoing = it.next().outgoingTransitions();
            while (outgoing.hasNext()) {
                FeaturedTransition tr = (FeaturedTransition) outgoing.next();
                features.addAll(tr.getFeatureExpression().getFeatures());
            }
        }
        List<String> lst = Lists.newArrayList(features.stream().map(f -> f.getName()).iterator());
        FtsPromelaPrinter printer = new FtsPromelaPrinter(out, fts, lst);
        printer.print();
    }

    @Override
    public void transform(UsageModel um, OutputStream out, String... cmdArgs) throws IOException {
        throw new UnsupportedOperationException("fPromela does not support usage models!");
    }

}
