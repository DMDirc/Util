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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dmdirc.util;

import java.io.Serializable;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleInjectorTest {

    @Test
    public void testNoParams() {
        final SimpleInjector instance = new SimpleInjector();
        assertTrue(instance.getParameters().isEmpty());
    }

    @Test
    public void testAddParams() {
        final Object object = new Object();
        final SimpleInjector instance = new SimpleInjector();
        instance.addParameter(Object.class, object);
        assertEquals(1, instance.getParameters().size());
    }

    @Test
    public void testAddObjectsParams() {
        final TestObject object = new TestObject("");
        final SimpleInjector instance = new SimpleInjector();
        instance.addParameter(object);
        assertEquals(3, instance.getParameters().size());
    }

    @Test
    public void testParentsParams() {
        final Object object = new Object();
        final SimpleInjector parent = new SimpleInjector();
        parent.addParameter(Object.class, object);
        final SimpleInjector child = new SimpleInjector(parent);
        assertEquals(1, child.getParameters().size());
    }

    @Test
    public void testCreateInstanceNoParams() {
        final SimpleInjector instance = new SimpleInjector();
        final Object result = instance.createInstance(Object.class);
        assertNotNull(result);
    }

    @Test
    public void testCreateInstanceParams() {
        final SimpleInjector instance = new SimpleInjector();
        instance.addParameter(String.class, "test");
        instance.addParameter(SimpleInjectorTest.class, this);
        final TestObject result = instance.createInstance(TestObject.class);
        assertNotNull(result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateUnsatisfiedConstructor() {
        final SimpleInjector instance = new SimpleInjector();
        final Object result = instance.createInstance(TestObject.class);
        assertNull(result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreatePrivateConstructor() {
        final SimpleInjector instance = new SimpleInjector();
        instance.addParameter(String.class, "");
        final Object result = instance.createInstance(PrivateObject.class);
        assertNull(result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateExceptionConstructor() {
        final SimpleInjector instance = new SimpleInjector();
        instance.addParameter(String.class, "");
        instance.addParameter(SimpleInjectorTest.class, this);
        final Object result = instance.createInstance(ExceptionObject.class);
        assertNull(result);
    }

    static class TestObject implements Serializable {

        public TestObject(final String test) { // NOPMD
        }
    }

    static class PrivateObject implements Serializable {

        private PrivateObject() {
        }
    }

    static class ExceptionObject implements Serializable {

        public ExceptionObject() {
            throw new IndexOutOfBoundsException();
        }
    }
}
