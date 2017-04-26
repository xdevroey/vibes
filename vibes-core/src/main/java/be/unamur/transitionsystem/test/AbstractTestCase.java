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
import java.util.Iterator;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.test.exception.TestCaseException;

/**
 * This class represents an abstract test case.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 * @param <A> The type of actions of the TS (will be the type of actions
 * considered in the test cases).
 * @param <S> The type of states of the TS.
 * @param <T> The type of transitions of the TS.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractTestCase<A extends Action, S extends State, T extends Transition>
        implements TestCase {

    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Two test cases are equals if they have the same sequences of actions.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof AbstractTestCase)) {
            return false;
        }
        AbstractTestCase<A, S, T> tr = (AbstractTestCase<A, S, T>) obj;
        Iterator<A> itTrans1 = (Iterator<A>) this.iterator();
        Iterator<A> itTrans2 = (Iterator<A>) tr.iterator();
        boolean equals = true;
        while (itTrans1.hasNext() && itTrans2.hasNext() && equals) {
            equals = itTrans1.next().equals(itTrans2.next());
        }
        return equals && !itTrans1.hasNext() && !itTrans2.hasNext();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        Iterator<A> it = (Iterator<A>) this.iterator();
        A act;
        while (it.hasNext()) {
            act = it.next();
            builder.append('(');
            builder.append(act.getName());
            builder.append(')');
            if (it.hasNext()) {
                builder.append(',');
            }
        }
        builder.append("]");
        return builder.toString();
    }

    public abstract AbstractTestCase<A, S, T> copy() throws TestCaseException;

    @Override
    public abstract int size();

}
