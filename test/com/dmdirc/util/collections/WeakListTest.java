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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

import static org.junit.Assert.*;

public class WeakListTest {

    @Test
    public void testIsEmpty() {
        final List<String> instance = new WeakList<>();
        assertTrue(instance.isEmpty());
        instance.add("test1");
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testAdd() {
        final List<String> instance = new WeakList<>();
        assertTrue(instance.add("test1"));
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testAddInt() {
        final List<String> instance = new WeakList<>();
        assertTrue(instance.add("test1"));
        instance.add(0, "test2");
        assertEquals("test2", instance.get(0));
    }

    @Test
    public void testRemove() {
        final List<String> instance = new WeakList<>();
        instance.add("test1");
        assertFalse(instance.isEmpty());
        assertTrue(instance.remove("test1"));
        assertFalse(instance.remove("test1"));
    }

    @Test
    public void testRemoveInt() {
        final List<String> instance = new WeakList<>();
        assertTrue(instance.isEmpty());
        instance.add("test1");
        instance.add("test2");
        assertEquals(2, instance.size());
        instance.remove(1);
        assertTrue(instance.contains("test1"));
        assertFalse(instance.contains("test2"));
    }

    @Test
    public void testGet() {
        final List<String> instance = new WeakList<>();
        instance.add("test1");
        instance.add("test2");
        assertEquals("test1", instance.get(0));
        assertEquals("test2", instance.get(1));
    }

    @Test
    public void testContains() {
        final List<String> instance = new WeakList<>();
        assertFalse(instance.contains("test1"));
        instance.add("test1");
        assertTrue(instance.contains("test1"));
    }

    /**
     * Test of toArray method, of class WeakList.
     */
    @Test
    public void testToArray0args() {
        final List<String> instance = new WeakList<>();
        assertEquals(0, instance.toArray().length);
        instance.add("test1");
        instance.add("test2");
        final Object[] result = instance.toArray();
        assertEquals(2, result.length);
        assertEquals("test1", result[0]);
        assertEquals("test2", result[1]);
    }

    /**
     * Test of toArray method, of class WeakList.
     */
    @Test
    public void testToArrayGenericType() {
        final List<String> instance = new WeakList<>();
        assertEquals(0, instance.toArray(new String[instance.size()]).length);
        instance.add("test1");
        instance.add("test2");
        final Object[] result = instance.toArray(new String[2]);
        assertEquals(2, result.length);
        assertEquals("test1", result[0]);
        assertEquals("test2", result[1]);
    }

    /**
     * Test of containsAll method, of class WeakList.
     */
    @Test
    public void testContainsAllAddAll() {
        final Collection<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        final List<String> instance = new WeakList<>();
        assertFalse(instance.containsAll(list));
        instance.addAll(list);
        assertTrue(instance.contains("test1"));
        assertTrue(instance.contains("test2"));
        assertTrue(instance.containsAll(list));
    }

    @Test
    public void testAddAllIntCollection() {
        final Collection<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        final List<String> instance = new WeakList<>();
        instance.add("test3");
        assertEquals("test3", instance.get(0));
        assertFalse(instance.containsAll(list));
        instance.addAll(0, list);
        assertEquals("test1", instance.get(0));
        assertEquals("test2", instance.get(1));
    }

    @Test
    public void testIndexOf() {
        final String one = "test1";
        final List<String> instance = new WeakList<>();
        instance.add(one);
        final String two = "test2";
        instance.add(two);
        assertEquals(2, instance.size());
        assertEquals(0, instance.indexOf(one));
        assertEquals(1, instance.indexOf(two));
    }

    @Test
    public void testLastIndexOf() {
        final String one = "test1";
        final List<String> instance = new WeakList<>();
        instance.add(one);
        final String two = "test2";
        instance.add(two);
        instance.add(one);
        assertEquals(3, instance.size());
        assertEquals(2, instance.lastIndexOf(one));
    }

    @Test
    public void testRemoveAll() {
        final Collection<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        final List<String> instance = new WeakList<>();
        instance.addAll(list);
        assertFalse(instance.isEmpty());
        instance.removeAll(list);
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testRetainAll() {
        final Collection<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        final List<String> instance = new WeakList<>();
        instance.addAll(list);
        instance.add("test3");
        instance.add("test4");
        assertFalse(list.size() == instance.size());
        instance.retainAll(list);
        assertTrue(list.size() == instance.size());
    }

    @Test
    public void testClear() {
        final Collection<String> list = new ArrayList<>();
        list.add("test1");
        list.add("test2");
        final List<String> instance = new WeakList<>();
        instance.addAll(list);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSet() {
        final List<String> instance = new WeakList<>();
        instance.add("test1");
        assertEquals("test1", instance.get(0));
        instance.set(0, "test2");
        assertEquals("test2", instance.get(0));
    }

    @Test
    public void testIterator() {
        final List<String> instance = new WeakList<>();
        Iterator<String> result = instance.iterator();
        assertFalse(result.hasNext());
        instance.add("test1");
        instance.add("test2");
        result = instance.iterator();
        assertEquals("test1", result.next());
        assertEquals("test2", result.next());
        assertFalse(result.hasNext());
    }

    @Test
    public void testListIterator0args() {
        final List<String> instance = new WeakList<>();
        Iterator<String> result = instance.listIterator();
        assertFalse(result.hasNext());
        instance.add("test1");
        instance.add("test2");
        result = instance.listIterator();
        assertEquals("test1", result.next());
        assertEquals("test2", result.next());
        assertFalse(result.hasNext());
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testListIteratorInt() {
        final List<String> instance = new WeakList<>();
        ListIterator<String> result = instance.listIterator(1);
        assertFalse(result.hasNext());
        instance.add("test1");
        instance.add("test2");
        instance.add("text3");
        result = instance.listIterator(1);
        assertEquals("test1", result.previous());
        assertEquals("test2", result.next());
        assertEquals("test3", result.next());
        assertFalse(result.hasNext());
    }

    @Test
    public void testSubList() {
        final List<String> instance = new WeakList<>();
        instance.add("test1");
        instance.add("test2");
        instance.add("test3");
        instance.add("test4");
        final List<String> result = instance.subList(1, 3);
        assertEquals(2, result.size());
        assertTrue(result.contains("test2"));
        assertTrue(result.contains("test3"));
    }

}
