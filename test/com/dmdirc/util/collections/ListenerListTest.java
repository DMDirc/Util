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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dmdirc.util.collections;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ListenerListTest {

    @Test
    public void testGenericAddListener() {
        final Object listener = new Object();
        final Object listener2 = new Object();
        ListenerList instance = new ListenerList();
        instance.add(Object.class, listener);
        instance.add(Object.class, listener2);
        assertTrue(instance.get(Object.class).contains(listener));
        assertTrue(instance.get(Object.class).contains(listener));
    }


    @Test(expected=IllegalArgumentException.class)
    public void testGenericAddNull() {
        ListenerList instance = new ListenerList();
        instance.add(Object.class, null);
        assertFalse(instance.get(Object.class).contains(null));
    }

    @Test
    public void testStringAddListener() {
        final Object listener = new Object();
        final Object listener2 = new Object();
        ListenerList instance = new ListenerList();
        instance.add("Object", listener);
        instance.add("Object", listener2);
        assertTrue(instance.get("Object").contains(listener));
        assertTrue(instance.get("Object").contains(listener2));
    }
    @Test(expected=IllegalArgumentException.class)
    public void testStringAddNull() {
        ListenerList instance = new ListenerList();
        instance.add("Object", null);
        assertFalse(instance.get(Object.class).contains(null));
    }

    @Test
    public void testGenericRemoveListener() {
        final Object listener = new Object();
        ListenerList instance = new ListenerList();
        instance.add(Object.class, listener);
        assertTrue(instance.get(Object.class).contains(listener));
        instance.remove(Object.class, listener);
        assertFalse(instance.get(Object.class).contains(listener));
    }

    @Test
    public void testStringRemoveListener() {
        final Object listener = new Object();
        ListenerList instance = new ListenerList();
        instance.add("Object", listener);
        assertTrue(instance.get("Object").contains(listener));
        instance.remove("Object", listener);
        assertFalse(instance.get("Object").contains(listener));
    }

    @Test
    public void testStringRemoveUnknownListener() {
        final Object listener = new Object();
        ListenerList instance = new ListenerList();
        instance.add("Object", listener);
        assertTrue(instance.get("Object").contains(listener));
        instance.remove("String", listener);
        assertTrue(instance.get("Object").contains(listener));
    }

    @Test
    public void testGenericGetListeners() {
        final Object listener = new Object();
        final String listener2 = "";
        ListenerList instance = new ListenerList();
        assertTrue(instance.get(Object.class).isEmpty());
        instance.add(Object.class, listener);
        assertTrue(instance.get(Object.class).contains(listener));
        assertTrue(instance.get(String.class).isEmpty());
        instance.add(String.class, listener2);
        assertTrue(instance.get(String.class).contains(listener2));
    }

    @Test
    public void testStringGetListeners() {
        final Object listener = new Object();
        final String listener2 = "";
        ListenerList instance = new ListenerList();
        assertTrue(instance.get("Object").isEmpty());
        instance.add("Object", listener);
        assertTrue(instance.get("Object").contains(listener));
        assertTrue(instance.get("String").isEmpty());
        instance.add("String", listener2);
        assertTrue(instance.get("String").contains(listener2));
    }

    @Test
    public void testGetCallableNoArgs() {
        final TestCallable one = mock(TestCallable.class);
        final TestCallable two = mock(TestCallable.class);
        ListenerList instance = new ListenerList();
        instance.add(TestCallable.class, one);
        instance.add(TestCallable.class, two);
        final TestCallable test = instance.getCallable(TestCallable.class);
        test.testMethod();
        verify(one).testMethod();
        verify(two).testMethod();
    }

    @Test
    public void testGetCallableArg() {
        final TestCallable one = mock(TestCallable.class);
        ListenerList instance = new ListenerList();
        instance.add(TestCallable.class, one);
        final TestCallable test = instance.getCallable(TestCallable.class);
        test.testMethod("test");
        verify(one).testMethod("test");
    }

    @Test
    public void testGetCallableArgs() {
        final TestCallable one = mock(TestCallable.class);
        ListenerList instance = new ListenerList();
        instance.add(TestCallable.class, one);
        final TestCallable test = instance.getCallable(TestCallable.class);
        test.testMethod("test", "test1");
        verify(one).testMethod("test", "test1");
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetCallableThrowsException() {
        final TestCallable one = mock(TestCallable.class);
        when(one.testMethod()).thenThrow(new IndexOutOfBoundsException());
        ListenerList instance = new ListenerList();
        instance.add(TestCallable.class, one);
        final TestCallable test = instance.getCallable(TestCallable.class);
        test.testMethod();
        verify(one).testMethod();
    }

    private interface TestCallable {
        String testMethod();
        String testMethod(final String test);
        String testMethod(final String test, final String test2);
    }
}
