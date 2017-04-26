package be.unamur.transitionsystem.transformation;

/*
 * #%L
 * vibes-transformation
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
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

import be.unamur.transitionsystem.ExecutionNode;
import be.unamur.transitionsystem.ExecutionTree;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.TransitionSystem;

public abstract class AbstractTransitionSystemPruning<T extends Transition> implements TransitionSystemPruning<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTransitionSystemPruning.class);

    protected Set<T> transitionsToKeep(ExecutionTree<T> tree) {
        Set<T> toKeep = Sets.newHashSet();
        ExecutionNode<T> node = (ExecutionNode<T>) tree.getRoot();
        Deque<ExecutionNode<T>> stack = Queues.newArrayDeque();
        stack.push(node);
        while (!stack.isEmpty()) {
            node = stack.pop();
            for (Iterator<ExecutionNode<T>> it = node.sons(); it.hasNext();) {
                ExecutionNode<T> son = it.next();
                if (son.getValue() != null) {
                    toKeep.add(son.getValue());
                }
                stack.push(son);
            }
        }
        return toKeep;
    }

    @Override
    public TransitionSystem prune(TransitionSystem source,
            List<ExecutionTree<T>> trees) {
        Set<T> toKeep = Sets.newHashSet();
        for (ExecutionTree<T> tree : trees) {
            toKeep.addAll(transitionsToKeep(tree));
        }
        logger.debug("Transitions to keepa are {}", toKeep);
        return prune(source, toKeep);
    }

    @Override
    public TransitionSystem prune(TransitionSystem source, Set<T> toKeep) {
        TransitionSystem ts = source.copy();
        Iterator it = ts.states();
        List<Transition> transToRemove = Lists.newArrayList();
        while (it.hasNext()) {
            State s = (State) it.next();
            for (Iterator<Transition> itTrans = s.outgoingTransitions(); itTrans.hasNext();) {
                Transition tr = itTrans.next();
                if (!contains(toKeep, tr)) {
                    logger.debug("Transition {} will be removed", tr);
                    transToRemove.add(tr);
                }
            }
        }
        for (Transition tr : transToRemove) {
            ts.removeTransition(tr);
        }
        // Remove isolated states
        it = ts.states();
        List<State> toRemove = Lists.newArrayList();
        while (it.hasNext()) {
            State s = (State) it.next();
            if (s.incomingSize() == 0 && s.outgoingSize() == 0) {
                toRemove.add(s);
            }
        }
        for (State s : toRemove) {
            if (!(s == ts.getInitialState())) {
                ts.removeState(s);
            }
        }
        return ts;
    }

    private boolean contains(Set<T> toKeep, Transition tr) {
        boolean found = false;
        Iterator<T> it = toKeep.iterator();
        T val;
        while (!found && it.hasNext()) {
            val = it.next();
            found = tr.getFrom().equals(val.getFrom()) && tr.getTo().equals(val.getTo()) && tr.getAction().equals(val.getAction());
        }
        return found;
    }

}
