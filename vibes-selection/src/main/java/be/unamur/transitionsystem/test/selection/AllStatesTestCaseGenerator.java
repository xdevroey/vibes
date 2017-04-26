package be.unamur.transitionsystem.test.selection;

/*
 * #%L
 * vibes-selection
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.test.TestCaseFactory;
import be.unamur.transitionsystem.test.exception.TestCaseException;
import be.unamur.transitionsystem.test.selection.exception.TestCaseSelectionException;

public class AllStatesTestCaseGenerator extends AbstractTestCaseGenerator {

    private static final Logger LOG = LoggerFactory
            .getLogger(AllStatesTestCaseGenerator.class);

    private ScoreComputor scoreComputor;

    // Processing fields
    private Set<State> toVisit;
    private State initialState;

    public AllStatesTestCaseGenerator(TestCaseFactory testCaseFactory,
            TestCaseValidator validator, TestCaseWrapUp wrapUp,
            ScoreComputor scoreComputor) {
        super(testCaseFactory, validator, wrapUp);
        this.scoreComputor = scoreComputor;
    }

    public ScoreComputor getScoreComputor() {
        return scoreComputor;
    }

    public void setScoreComputor(ScoreComputor scoreComputor) {
        this.scoreComputor = scoreComputor;
    }

    /**
     *
     * @param ts The transition system to use in order to generate test cases.
     * We assume that the provided ts is connected and all states are accessible
     * from the initial state.
     * @param wrapUp
     * @param validator
     * @throws TestCaseSelectionException
     */
    @SuppressWarnings("unchecked")
    @Override
    public void generateAbstractTestSet(TransitionSystem ts, TestCaseValidator validator,
            TestCaseWrapUp wrapUp) throws TestCaseSelectionException {
        this.toVisit = Sets.newHashSet(ts.states());
        this.initialState = ts.getInitialState();
        toVisit.remove(initialState);
        PartialTestCase bestCandidate;
        SortedList<PartialTestCase> candidates = new SortedList<>();
        PartialTestCase testCase = new PartialTestCase(0, getTestCaseFactory()
                .buildTestCase());
        addCandidateTestCases(candidates, testCase, ts.getInitialState(), validator,
                wrapUp, scoreComputor);
        while (!toVisit.isEmpty() && !candidates.isEmpty()) {
            bestCandidate = candidates.remove(0);
            addCandidateTestCases(candidates, bestCandidate,
                    bestCandidate.getLastVisitedState(), validator, wrapUp, scoreComputor);
        }
        if (toVisit.size() > 0) {
            LOG.error("Some states were not visited, remaining are {}", this.toVisit);
        }
    }

    private void addCandidateTestCases(SortedList<PartialTestCase> candidates,
            PartialTestCase testCase, State state, TestCaseValidator validator,
            TestCaseWrapUp wrapUp, ScoreComputor scoreComputor) {
        int score;
        PartialTestCase tc;
        Iterator<Transition> it = state.outgoingTransitions();
        Transition tr;
        while (it.hasNext()) {
            tr = it.next();
            try {
                tc = (PartialTestCase) testCase.copy();
                tc.enqueue(tr);
                if (tr.getTo() == initialState) {
                    LOG.debug("Initial State reached");
                    if (validator.isValid(tc)) {
                        LOG.debug("Test Case generated");
                        if (this.toVisit.removeAll(tc.getVisitedStates())) {
                            wrapUp.wrapUp(tc.getTestCase());
                        } else {
                            LOG.debug("Did not progress...");
                        }
                    } else {
                        LOG.debug("Invalid Test Case generated");
                    }
                } else if (validator.isValid(tc)) {
                    score = computeScore(tc, tr);
                    tc.setScore(score);
                    candidates.add(tc);
                    LOG.debug("Adding candidate test-case {}", tc);
                    LOG.debug("Number of candidates={}", candidates.size());
                    LOG.trace("Candidates ares {}", candidates);
                } else {
                    LOG.debug("Invalid test case detected: {}", tc);
                }
            } catch (TestCaseException e) {
                LOG.error("Unable to enqueue transition transition {}!", tr, e);
            }
        }
    }

    private int computeScore(PartialTestCase tc, Transition tr) {
        Set<State> visited = new HashSet<>(tc.getVisitedStates());
        int score = scoreComputor.computeScore(tr,
                Sets.difference(this.toVisit, visited));
        /*
        // Decrease score if the number of loop is too high to avoid infinite looping.
        List<State> visited = Lists.newArrayList(tc.getVisitedStates());
        int uniqueStates = 0;
        if (visited.size() > 0) {
            Collections.sort(visited, (State left, State right) -> left.getName().compareTo(right.getName()));
            int count = 0;
            State candidate = visited.get(0);
            int candidateCount = 1;
            uniqueStates = 1;
            for (State s : visited) {
                if (s == candidate) {
                    candidateCount++;
                } else {
                    candidate = s;
                    candidateCount = 1;
                    uniqueStates++;
                }
                if (candidateCount > count) {
                    count = candidateCount;
                }
            }
            if (count > 2) {
                score = score - count;
            }
        }
        // Add number of unique states already visited 
        score  = score + uniqueStates;
         */
        Set<State> covered = Sets.intersection(visited, this.toVisit);
        score = score + covered.size();
        return score; // score equals the number of states covered by the test case + the potential number of states covered if the given transition is taken.
    }

}
