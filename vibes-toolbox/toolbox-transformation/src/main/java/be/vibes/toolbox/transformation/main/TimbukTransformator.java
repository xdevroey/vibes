package be.vibes.toolbox.transformation.main;

import be.vibes.ts.Action;
import be.vibes.ts.FeaturedTransitionSystem;
import be.vibes.ts.State;
import be.vibes.ts.TransitionSystem;
import be.vibes.ts.UsageModel;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.Option;

/**
 * Transforms the input TS to Timbuk Tree automaton.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class TimbukTransformator implements Transformator {

    private static final String OPTION_NAME = "timbuk";

    private static final String ARGUMENTS_NAMES = "accepting> <alphabet> <statesmap> <actionsmap";

    private static final String ALL_ARG_VALUE = "all";
    private static final String INITIAL_ARG_VALUE = "initial";

    public static final TimbukTransformator TIMBUK = new TimbukTransformator();

    private TimbukTransformator() {
    }

    @Override
    public Option getCommandLineOption() {
        return Option.builder(OPTION_NAME)
                .desc(String.format("Transforms input to Timbuk Tree automaton. "
                        + "The first argument (<accepting>) defines the "
                        + "states to consider as accepting states: if the value "
                        + "is '%s', only the initial state is considered as an "
                        + "accepting state; if the value is '%s', all the states are "
                        + "considered as accepting states; if the value is a "
                        + "comma separated list of states, all the provided states "
                        + "are considered as accepting states. The second argument "
                        + "(<alphabet>), indicates which actions are part of the "
                        + "alphabet of the produced automata: if the value is '%s', "
                        + "all the actions of the original LTS are considered"
                        + "for the alphabet; if the value is a comma separated "
                        + "list of actions, all the provided actions are part of "
                        + "the alphabet; if the value is a LTS file (ending by "
                        + ".ts or .lts), all the actions of the given LTS are "
                        + "considered for the alphabet.",
                        INITIAL_ARG_VALUE, ALL_ARG_VALUE, ALL_ARG_VALUE))
                .argName(ARGUMENTS_NAMES)
                .numberOfArgs(4)
                .build();
    }

    @Override
    public void transform(TransitionSystem lts, OutputStream out, String... cmdArgs) throws IOException {
        throw new UnsupportedOperationException("Timbuk Tree automaton not supported yet!");
        /*
        PrintStream output = new PrintStream(out);
        LtsTimbukPrinter printer = new LtsTimbukPrinter(output, lts);
        // Get accepting states
        String[] states = cmdArgs[0].trim().split(",");
        Preconditions.checkArgument(states.length > 0, "No valid state provided as accepting-states argument of the %s option!", OPTION_NAME);
        Iterator<State> accepting;
        switch (states[0]) {
            case ALL_ARG_VALUE:
                accepting = lts.states();
                break;
            case INITIAL_ARG_VALUE:
                accepting = Arrays.asList(lts.getInitialState()).iterator();
                break;
            default:
                List<State> acceptingStates = Lists.newArrayList();
                for (String name : states) {
                    State s = lts.getState(name);
                    Preconditions.checkArgument(s != null, "State %s is not part of given LTS!", name);
                    acceptingStates.add(s);
                }
                accepting = acceptingStates.iterator();
                break;
        }
        // Get alhpabet
        String[] actions = cmdArgs[1].trim().split(",");
        Preconditions.checkArgument(actions.length > 0, "No valid actions provided as alphabet argument of the %s option!", OPTION_NAME);
        Set<Action> alphabet;
        if (actions[0].equals(ALL_ARG_VALUE)) {
            alphabet = Sets.newHashSet(lts.actions());
        } else if (actions[0].endsWith(".lts") || actions[0].endsWith(".ts")) {
            TransitionSystem actionsSrc = TransitionSystemXmlLoaders.loadTransitionSystem(actions[0]);
            alphabet = Sets.newHashSet(actionsSrc.actions());
        } else {
            alphabet = Sets.newHashSet();
            for (String name : actions) {
                alphabet.add(new Action(name));
            }
        }
        alphabet.add(new Action(Action.NO_ACTION_NAME));
        // Print automata
        printer.printLTSTimbuk(accepting, alphabet.iterator());
        // Print states mapping
        File statesMapFile = new File(cmdArgs[2].trim());
        if (statesMapFile.exists()) {
            statesMapFile.delete();
        }
        statesMapFile.createNewFile();
        try(PrintStream statesMapping = new PrintStream(statesMapFile); ) {
            printer.printStateMapping(statesMapping);
            statesMapping.flush();
        }
        // Print actions mapping
        File actionsMapFile = new File(cmdArgs[3].trim());
        if (actionsMapFile.exists()) {
            actionsMapFile.delete();
        }
        actionsMapFile.createNewFile();
        try(PrintStream actionsMapping = new PrintStream(actionsMapFile); ) {
            printer.printActionMapping(actionsMapping);
            actionsMapping.flush();
        }
        output.flush();
        */
    }

    @Override
    public void transform(FeaturedTransitionSystem fts, OutputStream out, String... cmdArgs) throws IOException {
        throw new UnsupportedOperationException("Timbuk Tree automaton does not support featured transition systems!");
    }

    @Override
    public void transform(UsageModel um, OutputStream out, String... cmdArgs) throws IOException {
        throw new UnsupportedOperationException("Timbuk Tree automaton does not support featured transition systems!");
    }

}
