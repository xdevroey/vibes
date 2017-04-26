package be.unamur.transitionsystem.test.selection;

/*
 * #%L vibes-selection %% Copyright (C) 2014 PReCISE, University of Namur %%
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SortedList<T extends Comparable<T>> implements List<T> {

    protected class Element {

        private T element;
        private Element next = null;

        public Element(T element) {
            super();
            this.element = element;
        }

        public Element(T element, Element next) {
            super();
            this.element = element;
            this.next = next;
        }

        public T getElement() {
            return element;
        }

        public void setElement(T element) {
            this.element = element;
        }

        public Element getNext() {
            return next;
        }

        public void setNext(Element next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return "Element [element=" + element + ", next=" + next + "]";
        }

    }

    private int size = 0;
    private Element head = null;

    @Override
    public boolean add(T e) {
        if (this.head == null) {
            this.head = new Element(e);
        } else if (this.head.getNext() == null) {
            if (e.compareTo(this.head.getElement()) < 0) {
                this.head.setNext(new Element(e));
            } else {
                this.head = new Element(e, this.head);
            }
        } else if (e.compareTo(this.head.getElement()) > 0) {
            this.head = new Element(e, this.head);
        } else {
            Element current = this.head;
            while (current.getNext() != null
                    && e.compareTo(current.getNext().getElement()) < 0) {
                current = current.getNext();
            }
            current.setNext(new Element(e, current.getNext()));
        }
        this.size++;
        return true;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException(
                "Does not make sens for a sorted list (you idiot)!");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException(
                "Does not make sens for a sorted list (you idiot)!");
    }

    @Override
    public void clear() {
        this.size = 0;
        this.head = null;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean contains = false;
        Iterator<?> it = c.iterator();
        while (!contains && it.hasNext()) {
            contains = contains(it.next());
        }
        return contains;
    }

    @Override
    public T get(int index) {
        if (index >= this.size) {
            return null;
        }
        Element current = this.head;
        int currentIdx = 0;
        while (currentIdx < index) {
            current = current.getNext();
            currentIdx++;
        }
        return current.getElement();
    }

    @Override
    public int indexOf(Object o) {
        // TODO improve implementation using comparator method
        Element current = this.head;
        int index = 0;
        while (current != null && !(current.getElement().equals(o))) {
            current = current.getNext();
            index++;
        }
        if (index < size()) {
            return index;
        } else {
            return -1;
        }
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    class SortedListIterator implements Iterator<T> {

        private Element elem;

        public SortedListIterator(SortedList<T>.Element elem) {
            this.elem = elem;
        }

        @Override
        public boolean hasNext() {
            return elem != null;
        }

        @Override
        public T next() {
            Element e = elem;
            elem = elem.getNext();
            return e.getElement();
        }

    }

    @Override
    public Iterator<T> iterator() {
        return new SortedListIterator(head);
    }

    @Override
    public int lastIndexOf(Object o) {
        // TODO Implement method
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public ListIterator<T> listIterator() {
        // TODO Implement method
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        // TODO Implement method
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public boolean remove(Object o) {
        // TODO Implement method
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public T remove(int index) {
        Element current = this.head;
        Element next;
        if (index == 0) {
            this.head = current.next;
            this.size--;
            return current.getElement();
        }
        int i = 1;
        while (i < index) {
            current = current.getNext();
            i++;
        }
        next = current.getNext();
        current.setNext(next.getNext());
        this.size--;
        return next.getElement();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Implement method
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public T set(int index, T element) {
        // TODO Implement method
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        // TODO Implement method
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public Object[] toArray() {
        // TODO Implement method
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public <T2> T2[] toArray(T2[] a) {
        // TODO Implement method
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public String toString() {
        return "SortedList [size=" + size + ", head=" + head + "]";
    }

}
