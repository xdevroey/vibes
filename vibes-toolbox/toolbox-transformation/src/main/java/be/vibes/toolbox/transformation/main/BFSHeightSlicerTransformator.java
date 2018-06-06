package be.vibes.toolbox.transformation.main;

import static be.vibes.dsl.io.Xml.*;
import be.vibes.ts.DistanceFromInitialStateAnnotator;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.State;
import be.vibes.ts.Transition;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.UsageModel;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.cli.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Slices a given TS according to a given BFS height in order to keep only
 * states accessible by paths less or equal to this BFS height.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class BFSHeightSlicerTransformator implements Transformator {

    private static final Logger LOG = LoggerFactory.getLogger(BFSHeightSlicerTransformator.class);

    public static final String OPTION_NAME = "bfshslicer";

    public static final BFSHeightSlicerTransformator BFS_SLICER = new BFSHeightSlicerTransformator();

    private BFSHeightSlicerTransformator() {
    }

    @Override
    public Option getCommandLineOption() {
        return Option.builder(OPTION_NAME)
                .desc("Slices a given TS accordign to a given BFS height in "
                        + "order to keep only states accessible by paths less "
                        + "or equal to this BFS height.")
                .hasArg()
                .optionalArg(false)
                .argName("height")
                .build();
    }

    @Override
    public void transform(TransitionSystem lts, OutputStream out, String... cmdArgs) throws IOException {
        slice(lts, Integer.parseInt(cmdArgs[0]));
        print(lts, out);
        printSinkStates(lts, out);
    }

    @Override
    public void transform(FeaturedTransitionSystem fts, OutputStream out, String... cmdArgs) throws IOException {
        slice(fts, Integer.parseInt(cmdArgs[0]));
        print(fts, out);
        printSinkStates(fts, out);
    }

    @Override
    public void transform(UsageModel um, OutputStream out, String... cmdArgs) throws IOException {
        slice(um, Integer.parseInt(cmdArgs[0]));
        print(um, out);
        printSinkStates(um, out);
    }

    private void slice(TransitionSystem ts, int bfsHeight) {
        throw new UnsupportedOperationException("Slicing not supported yet!");
        /*
        DistanceFromInitialStateAnnotator.getInstance().annotate(ts);
        Iterator<State> it = ts.states();
        // Remove transitions that are after the accessible states 
        while (it.hasNext()) {
            State s = it.next();
            int distance = s.getProperty(DistanceFromInitialStateAnnotator.PROP_STATE_DFROM, -1);
            if (distance < 0) {
                LOG.error("State {} was not annotated, is given TS strongly connected?", s);
            } else if (distance >= bfsHeight) {
                List<Transition> toRemove = Lists.newArrayList(ts.getOutgoing(s));
                for (Transition tr : toRemove) {
                    ts.removeTransition(tr);
                }
            }
        }
        // Remove isolated states
        List<State> toRemove = Lists.newArrayList(Iterators.filter(ts.states(), (State s) -> ts.getIncomingCount(s) == 0 && !(s == ts.getInitialState())));
        for (State s : toRemove) {
            ts.removeState(s);
        }
        */
    }

    private void printSinkStates(TransitionSystem ts, OutputStream out) throws IOException {
        List<State> sinks = Lists.newArrayList(Iterators.filter(ts.states(), (State s) -> ts.getOutgoingCount(s) == 0));
        sinks.add(ts.getInitialState());
        String statesList = sinks.stream().map((State s) -> s.getName()).reduce("", (String s1, String s2) -> s1.length() == 0 ? s2 : s1 + "," + s2);
        out.flush();
        PrintStream printer = new PrintStream(out);
        printer.println();
        printer.printf("<!-- %s -->", statesList).println();
        printer.flush();
    }

}
