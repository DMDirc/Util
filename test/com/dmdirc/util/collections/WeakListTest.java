/*
 * Copyright (c) 2006-2012 DMDirc Developers
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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

import static org.junit.Assert.*;

public class WeakListTest {

    public void testIsEmpty() {
        final WeakList<String> instance = new WeakList<String>();
        assertTrue(instance.isEmpty());
        instance.add("test1");
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testAdd() {
        final WeakList<String> instance = new WeakList<String>();
        assertTrue(instance.add("test1"));
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testAdd_Int() {
        final WeakList<String> instance = new WeakList<String>();
        assertTrue(instance.add("test1"));
        instance.add(0, "test2");
        assertEquals("test2", instance.get(0));
    }

    @Test
    public void testRemove() {
        final WeakList<String> instance = new WeakList<String>();
        instance.add("test1");
        assertFalse(instance.isEmpty());
        assertTrue(instance.remove("test1"));
        assertFalse(instance.remove("test1"));
    }

    @Test
    public void testRemove_Int() {
        final WeakList<String> instance = new WeakList<String>();
        assertTrue(instance.isEmpty());
        instance.add("test1");
        instance.add("test2");
        assertEquals(2, instance.size());
        instance.remove(1);
        assertTrue(instance.contains(("test1")));
        assertFalse(instance.contains(("test2")));
    }

    @Test
    public void testGet() {
        final WeakList<String> instance = new WeakList<String>();
        instance.add("test1");
        instance.add("test2");
        assertEquals("test1", instance.get(0));
        assertEquals("test2", instance.get(1));
    }

    public void testContains() {
        final WeakList<String> instance = new WeakList<String>();
        assertFalse(instance.contains("test1"));
        instance.add("test1");
        assertTrue(instance.contains("test1"));
    }

    /**
     * Test of toArray method, of class WeakList.
     */
    @Test
    public void testToArray_0args() {
        final WeakList<String> instance = new WeakList<String>();
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
    public void testToArray_GenericType() {
        final WeakList<String> instance = new WeakList<String>();
        assertEquals(0, instance.toArray(new String[0]).length);
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
        final List<String> list = new ArrayList<String>();
        list.add("test1");
        list.add("test2");
        final WeakList<String> instance = new WeakList<String>();
        assertFalse(instance.containsAll(list));
        instance.addAll(list);
        assertTrue(instance.contains("test1"));
        assertTrue(instance.contains("test2"));
        assertTrue(instance.containsAll(list));
    }

    /**
     * Test of addAll method, of class WeakList.
     */
    @Test
    public void testAddAll_int_Collection() {
        final List<String> list = new ArrayList<String>();
        list.add("test1");
        list.add("test2");
        final WeakList<String> instance = new WeakList<String>();
        instance.add("test3");
        System.out.println(instance);
        assertEquals("test3", instance.get(0));
        assertFalse(instance.containsAll(list));
        instance.addAll(0, list);
        assertEquals("test1", instance.get(0));
        assertEquals("test2", instance.get(1));
    }

    /**
     * Test of indexOf method, of class WeakList.
     */
    @Test
    public void testIndexOf() {
        final String one = "test1";
        final String two = "test2";
        final WeakList<String> instance = new WeakList<String>();
        instance.add(one);
        instance.add(two);
        assertEquals(2, instance.size());
        assertEquals(0, instance.indexOf(one));
        assertEquals(1, instance.indexOf(two));
    }

    /**
     * Test of lastIndexOf method, of class WeakList.
     */
    @Test
    public void testLastIndexOf() {
        final String one = "test1";
        final String two = "test2";
        final WeakList<String> instance = new WeakList<String>();
        instance.add(one);
        instance.add(two);
        instance.add(one);
        assertEquals(3, instance.size());
        assertEquals(2, instance.lastIndexOf(one));
    }

    /**
     * Test of removeAll method, of class WeakList.
     */
    @Test
    public void testRemoveAll() {
        final List<String> list = new ArrayList<String>();
        list.add("test1");
        list.add("test2");
        final WeakList<String> instance = new WeakList<String>();
        instance.addAll(list);
        assertFalse(instance.isEmpty());
        instance.removeAll(list);
        assertTrue(instance.isEmpty());
    }

    /**
     * Test of retainAll method, of class WeakList.
     */
    @Test
    public void testRetainAll() {
        final List<String> list = new ArrayList<String>();
        list.add("test1");
        list.add("test2");
        final WeakList<String> instance = new WeakList<String>();
        instance.addAll(list);
        instance.add("test3");
        instance.add("test4");
        assertFalse(list.size() == instance.size());
        instance.retainAll(list);
        assertTrue(list.size() == instance.size());
    }

    /**
     * Test of clear method, of class WeakList.
     */
    @Test
    public void testClear() {
        final List<String> list = new ArrayList<String>();
        list.add("test1");
        list.add("test2");
        final WeakList<String> instance = new WeakList<String>();
        instance.addAll(list);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    /**
     * Test of set method, of class WeakList.
     */
    @Test
    public void testSet() {
        final WeakList<String> instance = new WeakList<String>();
        instance.add("test1");
        assertEquals("test1", instance.get(0));
        instance.set(0, "test2");
        assertEquals("test2", instance.get(0));
    }

    /**
     * Test of iterator method, of class WeakList.
     */
    @Test
    public void testIterator() {
        final WeakList<String> instance = new WeakList<String>();
        Iterator result = instance.iterator();
        assertFalse(result.hasNext());
        instance.add("test1");
        instance.add("test2");
        result = instance.iterator();
        int i = 0;
        while (result.hasNext()) {
            result.next();
            i++;
        }
        assertEquals(2, i);
    }

    /**
     * Test of listIterator method, of class WeakList.
     */
    @Test
    public void testListIterator_0args() {
        final WeakList<String> instance = new WeakList<String>();
        ListIterator result = instance.listIterator();
        assertFalse(result.hasNext());
        instance.add("test1");
        instance.add("test2");
        result = instance.listIterator();
        assertTrue(result.hasNext());
        result.next();
        assertTrue(result.hasNext());
        result.next();
        assertFalse(result.hasNext());
    }

    /**
     * Test of listIterator method, of class WeakList.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void testListIterator_int() {
        final WeakList<String> instance = new WeakList<String>();
        ListIterator result = instance.listIterator(1);
        assertFalse(result.hasNext());
        instance.add("test1");
        instance.add("test2");
        instance.add("text3");
        result = instance.listIterator(1);
        assertTrue(result.hasPrevious());
        result.previous();
        assertTrue(result.hasNext());
        result.next();
        assertTrue(result.hasNext());
        result.next();
        assertFalse(result.hasNext());
        result.next();
        assertFalse(result.hasNext());
    }

    /**
     * Test of subList method, of class WeakList.
     */
    @Test
    public void testSubList() {
        final WeakList<String> instance = new WeakList<String>();
        instance.add("test1");
        instance.add("test2");
        instance.add("test3");
        instance.add("test4");
        List result = instance.subList(1, 3);
        assertEquals(2, result.size());
        assertFalse(result.contains("test1"));
        assertTrue(result.contains("test2"));
        assertTrue(result.contains("test3"));
    }

}
