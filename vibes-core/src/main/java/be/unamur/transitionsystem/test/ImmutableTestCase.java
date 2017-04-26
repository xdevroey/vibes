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

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.test.exception.TestCaseException;

/**
 * An immutable implementation of {@link AbstractTestCase}. Enqueue and dequeue
 * methods will return new objects.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @param <T>
 * @param <S>
 *
 */
public class ImmutableTestCase<A extends Action, S extends State, T extends Transition>
        extends AbstractTestCase<A, S, T> {

    private Couple last = null;

    public ImmutableTestCase() {
        super();
    }

    private ImmutableTestCase(Couple last) {
        super();
        this.last = last;
    }

    @Override
    public Iterator<Action> iterator() {
        ArrayList<Action> list = new ArrayList<Action>();
        Couple elem = this.last;
        while (elem != null) {
            list.add(0, elem.getAction());
            elem = elem.getPrevious();
        }
        return list.iterator();
    }

    @Override
    public ImmutableTestCase<A, S, T> enqueue(Transition trans) throws TestCaseException {
        return new ImmutableTestCase<A, S, T>(new Couple(this.last, trans.getAction()));
    }

    @Override
    public ImmutableTestCase<A, S, T> enqueue(Action action) throws TestCaseException {
        return new ImmutableTestCase<A, S, T>(new Couple(this.last, action));
    }

    @Override
    public ImmutableTestCase<A, S, T> dequeue() {
        if (this.last != null) {
            return new ImmutableTestCase<A, S, T>(this.last.getPrevious());
        } else {
            return new ImmutableTestCase<A, S, T>();
        }
    }

    private class Couple {

        private Couple previous;
        private Action action;

        public Couple(Couple previous, Action action) {
            super();
            this.previous = previous;
            this.action = action;
        }

        public Couple getPrevious() {
            return previous;
        }

        public Action getAction() {
            return action;
        }
    }

    @Override
    public ImmutableTestCase<A, S, T> copy() {
        return new ImmutableTestCase<A, S, T>(new Couple(this.last.getPrevious(),
                this.last.getAction()));
    }

    @Override
    public int size() {
        int i = 0;
        Couple elem = this.last;
        while (elem != null) {
            i++;
            elem = elem.getPrevious();
        }
        return i;
    }

    @Override
    public boolean contains(Action act) {
        boolean found = false;
        Couple elem = this.last;
        while (elem != null && !found) {
            found = elem.equals(act);
            elem = elem.getPrevious();
        }
        return found;
    }

}
