package be.vibes.selection.random;

/*-
 * #%L
 * VIBeS: test case selection
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

import be.vibes.selection.exception.SinkStateReachedException;
import be.vibes.ts.State;
import be.vibes.ts.TestCase;
import be.vibes.ts.Transition;
import be.vibes.ts.UsageModel;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class UsageDrivenRandomSelector extends RandomTestCaseSelector {

    public UsageDrivenRandomSelector(UsageModel um, int maxNbrTry, int maxLength) {
        super(um, maxNbrTry, maxLength);
    }

    public UsageDrivenRandomSelector(UsageModel um) {
        super(um);
    }

    public UsageDrivenRandomSelector(UsageModel um, int maxLength) {
        super(um, maxLength);
    }

    @Override
    public UsageModel getTransitionSystem() {
        return (UsageModel) super.getTransitionSystem();
    }

    @Override
    protected Transition getRandomTransition(TestCase tc) throws SinkStateReachedException {
        UsageModel um = getTransitionSystem();
        State state = tc == null ? um.getInitialState() : tc.getLast().getTarget();
        List<Transition> outgoings = Lists.newArrayList(um.getOutgoing(state));
        if (outgoings.isEmpty()) {
            throw new SinkStateReachedException("Sink state " + state + " reached, could not select next transition!", state);
        } else {
            return getUsageBasedTransition(outgoings);
        }
    }

    private Transition getUsageBasedTransition(List<Transition> outgoings) {
        UsageModel um = getTransitionSystem();
        // Get Maximum proba for remaining transitions
        double maxProba = 0;
        for (Transition tr : outgoings) {
            maxProba += um.getProbability(tr);
        }
        // Get the element with the proba'
        double randomNbr = random.nextDouble() * maxProba;
        int elemIdx = -1;
        maxProba = 0;
        Iterator<Transition> it = outgoings.iterator();
        while (it.hasNext() && randomNbr >= maxProba) {
            Transition tr = it.next();
            maxProba = maxProba + um.getProbability(tr);
            elemIdx++;
        }
        // Return the designated element 
        return outgoings.get(elemIdx);
    }

}
