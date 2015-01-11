/*
 * Copyright (c) 2006-2015 DMDirc Developers
 *
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
 * SOFTWARE.
 */

package com.dmdirc.util.collections;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class QueuedLinkedHashSetTest {

    private QueuedLinkedHashSet<String> set;

    @Before
    public void setup() {
        set = new QueuedLinkedHashSet<>();
    }

    @Test(expected = NoSuchElementException.class)
    public void testElementWhenNewlyCreated() {
        set.element();
    }

    @Test(expected = NoSuchElementException.class)
    public void testElementWhenEmpty() {
        set.offer("foo");
        set.remove();
        set.element();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveWhenNewlyCreated() {
        set.remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveWhenEmpty() {
        set.offer("foo");
        set.remove();
        set.remove();
    }

    @Test
    public void testOfferMultipleItems() {
        assertTrue(set.offer("one"));
        assertEquals(1, set.size());
        assertFalse(set.offer("one"));
        assertEquals(1, set.size());
    }

    @Test
    public void testRemove() {
        set.offer("one");
        set.offer("two");
        assertEquals("two", set.remove());
        assertEquals("one", set.remove());
        assertTrue(set.isEmpty());
    }

    @Test
    public void testPoll() {
        set.offer("one");
        set.offer("two");
        assertEquals("two", set.poll());
        assertEquals("one", set.poll());
        assertTrue(set.isEmpty());
    }

    @Test
    public void testPeekWhenNewlyCreated() {
        assertNull(set.peek());
    }

    @Test
    public void testPeekWhenEmpty() {
        set.offer("foo");
        set.remove();
        assertNull(set.peek());
    }

    @Test
    public void testPeek() {
        set.offer("one");
        set.offer("two");
        assertEquals("two", set.peek());
        assertEquals("two", set.peek());
        assertEquals(2, set.size());
    }

    @Test
    public void testOfferAndMoveExisting() {
        set.offer("one");
        set.offer("two");
        set.offerAndMove("one");
        assertEquals("one", set.peek());
        assertEquals(2, set.size());
    }

    @Test
    public void testOfferAndMoveNew() {
        set.offer("one");
        set.offer("two");
        set.offerAndMove("three");
        assertEquals("three", set.peek());
        assertEquals(3, set.size());
    }

}