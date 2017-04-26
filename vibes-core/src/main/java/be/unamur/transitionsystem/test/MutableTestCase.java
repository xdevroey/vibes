package be.unamur.transitionsystem.test;

/*
 * #%L vibes-core %% Copyright (C) 2014 PReCISE, University of Namur %%
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. #L%
 */
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.test.exception.TestCaseException;

/**
 * A mutable implementation of test case. Enqueue and Dequeue methods will
 * return this.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @param <T>
 * @param <S>
 */
@SuppressWarnings("unchecked")
public class MutableTestCase<A extends Action, S extends State, T extends Transition>
        extends AbstractTestCase<A, S, T> {

    private static final Logger logger = LoggerFactory.getLogger(MutableTestCase.class);

    protected Multiset<A> actionSet;
    protected ArrayList<A> actions;

    public MutableTestCase() {
        super();
        this.actionSet = HashMultiset.create();
        this.actions = new ArrayList<A>();
    }

    @Override
    public Iterator<Action> iterator() {
        return (Iterator<Action>) this.actions.iterator();
    }

    @Override
    public MutableTestCase<A, S, T> enqueue(Transition trans) throws TestCaseException {
        this.actions.add((A) trans.getAction());
        this.actionSet.add((A) trans.getAction());
        return this;
    }

    @Override
    public MutableTestCase<A, S, T> enqueue(Action action) throws TestCaseException {
        this.actions.add((A) action);
        this.actionSet.add((A) action);
        return this;
    }

    @Override
    public MutableTestCase<A, S, T> dequeue() throws TestCaseException {
        if (this.actions.size() > 0) {
            A act = this.actions.remove(this.actions.size() - 1);
            this.actionSet.remove(act);
        } else {
            logger.warn("Call to dequeue on an empty MutableAbstractTestCase!");
        }
        return this;
    }

    @Override
    public MutableTestCase<A, S, T> copy() throws TestCaseException {
        MutableTestCase<A, S, T> tc = new MutableTestCase<A, S, T>();
        tc.actions = new ArrayList<A>(actions);
        tc.actionSet = HashMultiset.create(actionSet);
        return tc;
    }

    @Override
    public boolean contains(Action act) {
        return actionSet.contains(act);
    }

    @Override
    public int size() {
        return actions.size();
    }

}
