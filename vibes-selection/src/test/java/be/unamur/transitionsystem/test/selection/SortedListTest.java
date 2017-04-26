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
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.test.selection.SortedList;

public class SortedListTest {

    private static final Logger logger = LoggerFactory.getLogger(SortedListTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            logger.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	@Test
    public void testEmptyList() {
        SortedList<Integer> list = new SortedList<Integer>();
        assertTrue("New list should be empty!", list.isEmpty());
        assertEquals("New list should be empty!", 0, list.size());
    }

    @Test
    public void testAdd() {
        SortedList<Integer> list = new SortedList<Integer>();
        assertTrue("New list should be empty!", list.isEmpty());
        list.add(1);
        logger.debug("List is {}", list);
        assertFalse("List should not be empty after add!", list.isEmpty());
        assertEquals("Wrong size!", 1, list.size());
        list.add(2);
        logger.debug("List is {}", list);
        assertEquals("Wrong size!", 2, list.size());
        list.add(-1);
        logger.debug("List is {}", list);
        assertEquals("Wrong size!", 3, list.size());
        list.add(2);
        logger.debug("List is {}", list);
        assertEquals("Wrong size (list may contain duplicates)!", 4, list.size());
    }

    @Test
    public void testClear() {
        SortedList<Integer> list = new SortedList<Integer>();
        assertTrue("New list should be empty!", list.isEmpty());
        list.add(1);
        logger.debug("List is {}", list);
        assertFalse("List should not be empty after add!", list.isEmpty());
        assertEquals("Wrong size!", 1, list.size());
        list.add(2);
        logger.debug("List is {}", list);
        assertEquals("Wrong size!", 2, list.size());
        list.add(-1);
        logger.debug("List is {}", list);
        assertEquals("Wrong size!", 3, list.size());
        list.add(2);
        logger.debug("List is {}", list);
        assertEquals("Wrong size (list may contain duplicates)!", 4, list.size());
        list.clear();
        logger.debug("List is {}", list);
        assertTrue("List should be empty after clear!", list.isEmpty());
        assertEquals("Llist should be empty after clear!", 0, list.size());
    }

    @Test
    public void testGet() {
        SortedList<Integer> list = new SortedList<Integer>();
        assertTrue("New list should be empty!", list.isEmpty());

        list.add(1);
        logger.debug("List is {}", list);
        assertFalse("List should not be empty after add!", list.isEmpty());
        assertEquals("Wrong size!", 1, list.size());
        assertEquals("Wrong Value!", Integer.valueOf(1), list.get(0));

        list.add(2);
        logger.debug("List is {}", list);
        assertEquals("Wrong size!", 2, list.size());
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(0));
        assertEquals("Wrong Value!", Integer.valueOf(1), list.get(1));

        list.add(-1);
        assertEquals("Wrong size!", 3, list.size());
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(0));
        assertEquals("Wrong Value!", Integer.valueOf(1), list.get(1));
        assertEquals("Wrong Value!", Integer.valueOf(-1), list.get(2));

        list.add(2);
        logger.debug("List is {}", list);
        assertEquals("Wrong size (list may contain duplicates)!", 4, list.size());
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(0));
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(1));
        assertEquals("Wrong Value!", Integer.valueOf(1), list.get(2));

        list.add(0);
        logger.debug("List is {}", list);
        assertEquals("Wrong size (list may contain duplicates)!", 5, list.size());
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(0));
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(1));
        assertEquals("Wrong Value!", Integer.valueOf(1), list.get(2));
        assertEquals("Wrong Value!", Integer.valueOf(0), list.get(3));
        assertEquals("Wrong Value!", Integer.valueOf(-1), list.get(4));

        list.add(10);
        logger.debug("List is {}", list);
        assertEquals("Wrong size (list may contain duplicates)!", 6, list.size());
        assertEquals("Wrong Value!", Integer.valueOf(10), list.get(0));
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(1));
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(2));
        assertEquals("Wrong Value!", Integer.valueOf(1), list.get(3));
        assertEquals("Wrong Value!", Integer.valueOf(0), list.get(4));
        assertEquals("Wrong Value!", Integer.valueOf(-1), list.get(5));
    }

    @Test
    public void testRemove() {
        SortedList<Integer> list = new SortedList<Integer>();
        assertTrue("New list should be empty!", list.isEmpty());

        list.add(1);
        list.add(2);
        list.add(-1);
        list.add(2);
        logger.debug("List is {}", list);
        assertEquals("Wrong size (list may contain duplicates)!", 4, list.size());
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(0));
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(1));
        assertEquals("Wrong Value!", Integer.valueOf(1), list.get(2));
        assertEquals("Wrong Value!", Integer.valueOf(-1), list.get(3));

        assertEquals("Wrong value removed !", Integer.valueOf(2), list.remove(0));
        logger.debug("List is {}", list);
        assertEquals("Wrong size (list may contain duplicates)!", 3, list.size());
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(0));
        assertEquals("Wrong Value!", Integer.valueOf(1), list.get(1));
        assertEquals("Wrong Value!", Integer.valueOf(-1), list.get(2));

        list.add(0);
        list.add(10);
        logger.debug("List is {}", list);
        assertEquals("Wrong Value!", Integer.valueOf(10), list.get(0));
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(1));
        assertEquals("Wrong Value!", Integer.valueOf(1), list.get(2));
        assertEquals("Wrong Value!", Integer.valueOf(0), list.get(3));
        assertEquals("Wrong Value!", Integer.valueOf(-1), list.get(4));

        assertEquals("Wrong value removed !", Integer.valueOf(1), list.remove(2));
        logger.debug("List is {}", list);
        assertEquals("Wrong size (list may contain duplicates)!", 4, list.size());
        assertEquals("Wrong Value!", Integer.valueOf(10), list.get(0));
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(1));
        assertEquals("Wrong Value!", Integer.valueOf(0), list.get(2));
        assertEquals("Wrong Value!", Integer.valueOf(-1), list.get(3));

        assertEquals("Wrong value removed !", Integer.valueOf(-1), list.remove(3));
        logger.debug("List is {}", list);
        assertEquals("Wrong size (list may contain duplicates)!", 3, list.size());
        assertEquals("Wrong Value!", Integer.valueOf(10), list.get(0));
        assertEquals("Wrong Value!", Integer.valueOf(2), list.get(1));
        assertEquals("Wrong Value!", Integer.valueOf(0), list.get(2));
    }

}
