package be.unamur.transitionsystem.test.faultinjection;

import java.io.PrintStream;
import java.util.Queue;
import java.util.Set;

import static com.google.common.base.Preconditions.*;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.ExecutionNode;
import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.execution.TestCaseRunner;

public class FaultStatistics {

    private Set<State> errorStates;
    private Set<State> tcDetectedErrorStates;
    private Set<State> globalDetectedErrorStates;

    private Set<Transition> errorTransitions;
    private Set<Transition> tcDetectedErrorTransitions;
    private Set<Transition> globalDetectedErrorTransitions;

    private Set<Action> errorActions;
    private Set<Action> tcDetectedErrorActions;
    private Set<Action> globalDetectedErrorActions;

    private Set<State> visitedStates;
    private Set<Transition> visitedTransitions;
    private Set<Action> visitedActions;

    private int cptComputations = 0;

    public FaultStatistics() {
        this.errorStates = Sets.newHashSet();
        this.errorTransitions = Sets.newHashSet();
        this.errorActions = Sets.newHashSet();
        this.globalDetectedErrorStates = Sets.newHashSet();
        this.globalDetectedErrorTransitions = Sets.newHashSet();
        this.globalDetectedErrorActions = Sets.newHashSet();
        clear();
    }

    public FaultStatistics(TestCaseRunner<? extends TransitionSystem> runner, Set<State> errorStates, Set<Transition> errorTransitions, Set<Action> errorActions) {
        this.errorStates = errorStates;
        this.errorTransitions = errorTransitions;
        this.errorActions = errorActions;
        this.globalDetectedErrorStates = Sets.newHashSet();
        this.globalDetectedErrorTransitions = Sets.newHashSet();
        this.globalDetectedErrorActions = Sets.newHashSet();
        clear();
    }

    public void setErrorStates(Set<State> errorStates) {
        this.errorStates = errorStates;
    }

    public void setErrorTransitions(Set<Transition> errorTransitions) {
        this.errorTransitions = errorTransitions;
    }

    public void setErrorActions(Set<Action> errorActions) {
        this.errorActions = errorActions;
    }

    public Set<State> getErrorStates() {
        return errorStates;
    }

    public Set<Transition> getErrorTransitions() {
        return errorTransitions;
    }

    public Set<Action> getErrorActions() {
        return errorActions;
    }

    public Set<State> getGlobalDetectedErrorStates() {
        return globalDetectedErrorStates;
    }

    public Set<Transition> getGlobalDetectedErrorTransitions() {
        return globalDetectedErrorTransitions;
    }

    public Set<Action> getGlobalDetectedErrorActions() {
        return globalDetectedErrorActions;
    }

    public void clear() {
        this.tcDetectedErrorStates = Sets.newHashSet();
        this.tcDetectedErrorTransitions = Sets.newHashSet();
        this.tcDetectedErrorActions = Sets.newHashSet();
        this.visitedActions = Sets.newHashSet();
        this.visitedTransitions = Sets.newHashSet();
        this.visitedStates = Sets.newHashSet();
    }

    public Set<State> getTcDetectedErrorStates() {
        return tcDetectedErrorStates;
    }

    public Set<Transition> getTcDetectedErrorTransitions() {
        return tcDetectedErrorTransitions;
    }

    public Set<Action> getTcDetectedErrorActions() {
        return tcDetectedErrorActions;
    }

    public Set<State> getVisitedStates() {
        return visitedStates;
    }

    public Set<Transition> getVisitedTransitions() {
        return visitedTransitions;
    }

    public Set<Action> getVisitedActions() {
        return visitedActions;
    }

    public <T extends Transition> void computeStatistics(ExecutionTree<T> tree) {
        checkNotNull(tree);
        Queue<ExecutionNode<T>> queue = Lists.newLinkedList();
        Iterators.addAll(queue, tree.getRoot().sons());
        while (!queue.isEmpty()) {
            ExecutionNode<T> node = queue.poll();
            Transition tr = node.getValue();
            // See if state is in error 
            if (this.getErrorStates().contains(tr.getFrom())) {
                this.globalDetectedErrorStates.add(tr.getFrom());
                this.tcDetectedErrorStates.add(tr.getFrom());
            }
            this.visitedStates.add(tr.getFrom());
            // See if transition in error
            if (this.errorTransitions.contains(tr)) {
                this.globalDetectedErrorTransitions.add(tr);
                this.tcDetectedErrorTransitions.add(tr);
            }
            this.visitedTransitions.add(tr);
            // See if action in error
            if (this.errorActions.contains(tr.getAction())) {
                this.globalDetectedErrorActions.add(tr.getAction());
                this.tcDetectedErrorActions.add(tr.getAction());
            }
            this.visitedActions.add(tr.getAction());
            Iterators.addAll(queue, node.sons());
        }
        cptComputations++;
    }

    public void printCSVHeaders(PrintStream out) {
        out.println("nbrtc,tcdetectederrorstates,globaldetectederrorstates,errorstates,"
                + "tcdetectederrortransitions,globaldetectederrortransitions,errortransitions,"
                + "tcdetectederroractions,globaldetectederroractions,erroractions,"
                + "tcnbrstates,tcnbrtransitions,tcnbractions");
    }

    public void printCurrentValues(PrintStream out) {
        out.print(cptComputations);
        out.print(",");
        out.print(getTcDetectedErrorStates().size());
        out.print(",");
        out.print(getGlobalDetectedErrorStates().size());
        out.print(",");
        out.print(getErrorStates().size());
        out.print(",");
        out.print(getTcDetectedErrorTransitions().size());
        out.print(",");
        out.print(getGlobalDetectedErrorTransitions().size());
        out.print(",");
        out.print(getErrorTransitions().size());
        out.print(",");
        out.print(getTcDetectedErrorActions().size());
        out.print(",");
        out.print(getGlobalDetectedErrorActions().size());
        out.print(",");
        out.print(getErrorActions().size());
        out.print(",");
        out.print(getVisitedStates().size());
        out.print(",");
        out.print(getVisitedTransitions().size());
        out.print(",");
        out.print(getVisitedActions().size());
        out.println();
    }

}
