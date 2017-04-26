package be.unamur.transitionsystem;

import static be.unamur.transitionsystem.dsl.TransitionSystemBuilder.define;
import com.google.common.collect.Sets;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class TransitionSystemCleanerTest {

    private static final Logger LOG = LoggerFactory.getLogger(TransitionSystemCleanerTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOG.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

    @Test
    public void testRemoveIsolatedStatesAllConnected() {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s0").action("act1").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s2").action("act3").to("s0")
                .build();
        LabelledTransitionSystem result = (LabelledTransitionSystem) lts.copy();
        TransitionSystemCleaner.removeIsolatedStates(result);
        Set<State> resultStates = Sets.newHashSet(result.states());
        Set<State> originalStates = Sets.newHashSet(lts.states());
        assertThat(resultStates, equalTo(originalStates));
    }
        
    @Test
    public void testRemoveIsolatedStatesOneStateDisconnected() {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s0").action("act1").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s2").action("act3").to("s0")
                .build();
        lts.addState("isolated");
        LabelledTransitionSystem result = (LabelledTransitionSystem) lts.copy();
        TransitionSystemCleaner.removeIsolatedStates(result);
        Set<State> resultStates = Sets.newHashSet(result.states());
        Set<State> originalStates = Sets.newHashSet(lts.states());
        assertThat(resultStates, not(contains(lts.getState("isolated"))));
        originalStates.remove(lts.getState("isolated"));
        assertThat(resultStates, equalTo(originalStates));
    }
    
    @Test
    public void testRemoveIsolatedStatesDisconnectedComponent() {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s0").action("act1").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s2").action("act3").to("s0")
                .from("iso1").action("i1").to("iso2")
                .from("iso2").action("i2").to("iso3")
                .from("iso3").action("i3").to("iso2")
                .build();
        LOG.debug("Original LTS is {}", lts);
        LabelledTransitionSystem result = (LabelledTransitionSystem) lts.copy();
        TransitionSystemCleaner.removeIsolatedStates(result);
        LOG.debug("Pruned LTS is {}", result);
        Set<State> resultStates = Sets.newHashSet(result.states());
        Set<State> originalStates = Sets.newHashSet(lts.states());
        assertThat(resultStates, not(contains(lts.getState("iso1"))));
        assertThat(resultStates, not(contains(lts.getState("iso2"))));
        assertThat(resultStates, not(contains(lts.getState("iso3"))));
        originalStates.remove(lts.getState("iso1"));
        originalStates.remove(lts.getState("iso2"));
        originalStates.remove(lts.getState("iso3"));
        assertThat(resultStates, equalTo(originalStates));
    }
    
    
    @Test
    public void testRemoveUnusedActionsAllConnected() {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s0").action("act1").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s2").action("act3").to("s0")
                .build();
        LabelledTransitionSystem result = (LabelledTransitionSystem) lts.copy();
        TransitionSystemCleaner.removeUnusedActions(result);
        Set<Action> resultActions = Sets.newHashSet(result.actions());
        Set<Action> originalActions = Sets.newHashSet(lts.actions());
        originalActions.remove(lts.getAction(Action.NO_ACTION_NAME));
        assertThat(resultActions, equalTo(originalActions));
    }
    
    @Test
    public void testRemoveUnusedActionsOneUnusedAction() {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s0").action("act1").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s2").action("act3").to("s0")
                .build();
        lts.addAction("unused");
        LabelledTransitionSystem result = (LabelledTransitionSystem) lts.copy();
        TransitionSystemCleaner.removeUnusedActions(result);
        Set<Action> resultActions = Sets.newHashSet(result.actions());
        Set<Action> originalActions = Sets.newHashSet(lts.actions());
        originalActions.remove(lts.getAction("unused"));
        originalActions.remove(lts.getAction(Action.NO_ACTION_NAME));
        assertThat(resultActions, equalTo(originalActions));
    }
    
    
    @Test
    public void testRemoveUnusedActionsDisconnectedComponent() {
        LabelledTransitionSystem lts = define().init("s0")
                .from("s0").action("act1").to("s1")
                .from("s0").action("act1").to("s2")
                .from("s1").action("act2").to("s2")
                .from("s2").action("act3").to("s0")
                .from("iso1").action("i1").to("iso2")
                .from("iso2").action("i2").to("iso3")
                .from("iso3").action("i3").to("iso2")
                .build();
        LabelledTransitionSystem result = (LabelledTransitionSystem) lts.copy();
        TransitionSystemCleaner.removeIsolatedStates(result);
        TransitionSystemCleaner.removeUnusedActions(result);
        Set<Action> resultActions = Sets.newHashSet(result.actions());
        Set<Action> originalActions = Sets.newHashSet(lts.actions());
        assertThat(resultActions, not(contains(lts.getAction("i1"))));
        assertThat(resultActions, not(contains(lts.getAction("i2"))));
        assertThat(resultActions, not(contains(lts.getAction("i3"))));
        originalActions.remove(lts.getAction("i1"));
        originalActions.remove(lts.getAction("i2"));
        originalActions.remove(lts.getAction("i3"));
        originalActions.remove(lts.getAction(Action.NO_ACTION_NAME));
        assertThat(resultActions, equalTo(originalActions));
    }
        

}