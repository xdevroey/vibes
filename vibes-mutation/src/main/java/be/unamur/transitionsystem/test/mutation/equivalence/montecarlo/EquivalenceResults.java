package be.unamur.transitionsystem.test.mutation.equivalence.montecarlo;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.test.TestCase;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class EquivalenceResults {

    private int succeededRuns;
    private List<TestCase> counterExamples;

    EquivalenceResults() {
        this.succeededRuns = 0;
        this.counterExamples = Lists.newArrayList();
    }

    public int getSucceededRuns() {
        return succeededRuns;
    }

    public int getFailedRuns() {
        return counterExamples.size();
    }

    public int getTotalRuns() {
        return succeededRuns + counterExamples.size();
    }

    public double getEquivalenceDegree() {
        return ((double) getSucceededRuns()) / getTotalRuns();
    }

    /**
     * Returns the list of (possibly non unique) counter examples for
     * equivalence computation. If the equivalence computation was configured to
     * fail first, this list will contain at most one element.
     *
     * @return A list of non unique counter examples for the equivalence
     * computation.
     */
    public List<TestCase> getCounterExamples() {
        return counterExamples;
    }

    void incrementSucceededRuns() {
        this.succeededRuns++;
    }

    void incrementFailedRuns(TestCase counterExample) {
        this.counterExamples.add(counterExample);
    }

    /**
     * Merge the values of the other results with the current results and
     * returns this.
     *
     * @param other
     * @return This EquivalenceResults with succeededRuns = succeededRuns +
     * other.succeededRuns and counterExamples = counterExamples ++
     * other.counterExamples.
     */
    EquivalenceResults merge(EquivalenceResults other) {
        this.succeededRuns = this.succeededRuns + other.getSucceededRuns();
        for (TestCase tc : other.getCounterExamples()) {
            incrementFailedRuns(tc);
        }
        return this;
    }

    @Override
    public String toString() {
        return "EquivalenceResults{" + "succeededRuns=" + succeededRuns + ", counterExamples=" + counterExamplesToCsvString() + '}';
    }

    public String counterExamplesToCsvString() {
        StringBuilder str = new StringBuilder();
        str.append('{');
        for (Iterator<TestCase> iterator = counterExamples.iterator(); iterator.hasNext();) {
            TestCase next = iterator.next();
            str.append('(');
            for (Iterator<Action> itActions = next.iterator(); itActions.hasNext();) {
                Action act = itActions.next();
                str.append(act.getName().replace("\"", "\\\""));
                if (itActions.hasNext()) {
                    str.append(';');
                }
            }
            str.append(')');
            if (iterator.hasNext()) {
                str.append(';');
            }
        }
        str.append('}');
        return str.toString();
    }

}
