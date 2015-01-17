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

package com.dmdirc.util.text;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(MockitoJUnitRunner.class)
public class LinkTest {

    private Link instance;

    @Before
    public void setUp() throws Exception {
        instance = new Link(5, 10, "rarrarrarrar");
    }

    @Test
    public void testGetStart() throws Exception {
        assertEquals(5, instance.getStart());
    }

    @Test
    public void testGetEnd() throws Exception {
        assertEquals(10, instance.getEnd());
    }

    @Test
    public void testGetContent() throws Exception {
        assertEquals("rarrarrarrar", instance.getContent());
    }

    @Test
    public void testEquals_Equals() throws Exception {
        assertEquals(new Link(5, 10, "rarrarrarrar"), instance);
    }

    @Test
    public void testEquals_NotEquals() throws Exception {
        assertNotEquals(new Link(1, 5, "rarrarrarrar"), instance);
    }

    @Test
    public void testEquals_DifferentType() throws Exception {
        assertNotEquals("RAR", instance);
    }
}