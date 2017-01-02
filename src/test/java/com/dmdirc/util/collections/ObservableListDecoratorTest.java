/*
 * Copyright (c) 2006-2017 DMDirc Developers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.dmdirc.util.collections;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ObservableListDecoratorTest {

    private List<String> list;
    private ObservableList<String> obslist;
    private ListObserver observer;

    @Before
    public void setup() {
        list = new LinkedList<>();
        obslist = new ObservableListDecorator<>(list);
        observer = mock(ListObserver.class);
        obslist.addListListener(observer);
    }

    @Test
    public void testAddingSingleEntryFiresListener() {
        obslist.add("test");
        verify(observer).onItemsAdded(obslist, 0, 0);
        obslist.add("test");
        verify(observer).onItemsAdded(obslist, 1, 1);
    }

    @Test
    public void testAddingSingleEntryAtIndexFiresListener() {
        list.addAll(Arrays.asList("one", "two", "three", "four"));
        obslist.add(1, "one point five");
        verify(observer).onItemsAdded(obslist, 1, 1);
    }

    @Test
    public void testAddingRangeFiresListener() {
        obslist.addAll(Arrays.asList("one", "two", "three", "four"));
        verify(observer).onItemsAdded(obslist, 0, 3);
        obslist.addAll(Arrays.asList("one", "two", "three", "four"));
        verify(observer).onItemsAdded(obslist, 4, 7);
    }

    @Test
    public void testAddingRangeAtIndexFiresListener() {
        list.addAll(Arrays.asList("one", "two", "three", "four"));
        obslist.addAll(1, Arrays.asList("one", "two", "three", "four"));
        verify(observer).onItemsAdded(obslist, 1, 5);
    }

    @Test
    public void testClearingFiresListener() {
        list.addAll(Arrays.asList("one", "two", "three", "four"));
        obslist.clear();
        verify(observer).onItemsRemoved(obslist, 0, 3);
    }

    @Test
    public void testClearingEmptyListDoesntFireListener() {
        obslist.clear();
        verify(observer, never()).onItemsRemoved(any(), anyInt(), anyInt());
    }

    @Test
    public void testRemovingByIndexFiresListener() {
        list.addAll(Arrays.asList("one", "two", "three", "four"));
        obslist.remove(2);
        verify(observer).onItemsRemoved(obslist, 2, 2);
    }

    @Test
    public void testRemovingByObjectFiresListener() {
        list.addAll(Arrays.asList("one", "two", "three", "four"));
        obslist.remove("three");
        verify(observer).onItemsRemoved(obslist, 2, 2);
    }

    @Test
    public void testRemovingListenerStopsFutureCalls() {
        obslist.removeListListener(observer);
        obslist.add("test");
        verify(observer, never()).onItemsAdded(obslist, 0, 0);
    }

    @Test
    public void testSize() {
        assertEquals(0, obslist.size());
        obslist.add("test");
        assertEquals(1, obslist.size());
        obslist.add("test");
        assertEquals(2, obslist.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(obslist.isEmpty());
        obslist.add("test");
        assertFalse(obslist.isEmpty());
        obslist.remove("test");
        assertTrue(obslist.isEmpty());
    }

    @Test
    public void testContains() {
        assertFalse(obslist.contains("test"));
        obslist.add("test");
        assertTrue(obslist.contains("test"));
        obslist.remove("test");
        assertFalse(obslist.contains("test"));
    }

}
