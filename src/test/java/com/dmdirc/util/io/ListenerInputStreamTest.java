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

package com.dmdirc.util.io;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ListenerInputStreamTest {

    @Mock private DownloadListener listener;

    private ListenerInputStream instance;

    @Before
    public void setUp() throws Exception {
        instance = new ListenerInputStream(new MockedInputStream(), listener, 100);
    }

    @Test
    public void testInit_Indeterminate() throws Exception {
        instance = new ListenerInputStream(new MockedInputStream(), listener, -1);
        assertEquals(2, instance.read());
        verify(listener, never()).downloadProgress(2);
        verify(listener).setIndeterminate(true);
    }

    @Test
    public void testInit_Determinate() throws Exception {
        assertEquals(2, instance.read());
        verify(listener).downloadProgress(2);
        verify(listener).setIndeterminate(false);
    }

    @Test
    public void testRead() throws Exception {
        assertEquals(2, instance.read());
        verify(listener).downloadProgress(2);
    }

    @Test
    public void testRead1() throws Exception {
        assertEquals(4, instance.read(new byte[6]));
        verify(listener).downloadProgress(4);
    }

    @Test
    public void testRead2() throws Exception {
        assertEquals(6, instance.read(new byte[8], 0, 8));
        verify(listener).downloadProgress(6);
    }

    @Test
    public void testSkip() throws Exception {
        assertEquals(8, instance.skip(4));
        verify(listener).downloadProgress(8);
    }

    @Test
    public void testAvailable() throws Exception {
        assertEquals(10, instance.available());
    }

    @Test(expected = IOException.class)
    public void testClose() throws Exception {
        instance.close();
    }

    @Test(expected = IOException.class)
    public void testMarkAndReset_Unsupported() throws Exception {
        instance.mark(0);
        instance.reset();
    }

    @Test
    public void testMarkSupported() throws Exception {
        assertTrue(instance.markSupported());
    }

    private static class MockedInputStream extends InputStream {

        public MockedInputStream() {
        }

        @Override
        public int read() throws IOException {
            return 2;
        }

        @Override
        public int read(@Nonnull final byte[] b) throws IOException {
            return 4;
        }

        @Override
        public int read(@Nonnull final byte[] b, final int off, final int len) throws IOException {
            if (off == 0 && len == 6) {
                return 4;
            } else {
                return 6;
            }
        }

        @Override
        public long skip(final long n) throws IOException {
            return 8;
        }

        @Override
        public int available() throws IOException {
            return 10;
        }

        @Override
        public void close() throws IOException {
            throw new IOException("Test");
        }

        @Override
        public synchronized void mark(final int readlimit) {
        }

        @Override
        public synchronized void reset() throws IOException {
            throw new IOException("Test");
        }

        @Override
        public boolean markSupported() {
            return true;
        }
    }
}