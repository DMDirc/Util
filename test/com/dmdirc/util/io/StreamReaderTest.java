/*
 * Copyright (c) 2006-2014 DMDirc Developers
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

package com.dmdirc.util.io;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class StreamReaderTest {

    @Test
    public void testGetListDiscarding() throws Exception {
        final StreamReader streamReader = new StreamReader(getClass().getResource("test5.txt")
                .openStream());
        assertEquals(null, streamReader.getList());
    }

    @Test
    public void testGetListBuffer() throws Exception {
        final StringBuffer stringBuffer = new StringBuffer();
        final StreamReader streamReader = new StreamReader(getClass().getResource("test5.txt")
                .openStream(),
                stringBuffer);
        assertEquals(null, streamReader.getList());
        streamReader.run();
        assertEquals(null, streamReader.getList());
        assertEquals("Line 1\nLine 2\nLine 3", stringBuffer.toString());
    }

    @Test
    public void testGetListList() throws Exception {
        final List<String> list = new ArrayList<>();
        final StreamReader streamReader = new StreamReader(getClass().getResource("test5.txt")
                .openStream(), list);
        assertTrue(streamReader.getList().isEmpty());
        streamReader.run();
        assertEquals(4, list.size());
        assertEquals("", list.get(0));
        assertEquals("Line 1", list.get(1));
        assertEquals("Line 2", list.get(2));
        assertEquals("Line 3", list.get(3));
    }
}