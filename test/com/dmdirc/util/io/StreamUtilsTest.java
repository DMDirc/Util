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

package com.dmdirc.util.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@SuppressWarnings("resource")
@RunWith(MockitoJUnitRunner.class)
public class StreamUtilsTest {

    @Rule public ExpectedException thrown = ExpectedException.none();
    @Mock private Closeable closeable;

    @Test
    public void testCloseWithNullStream() {
        // Shouldn't throw an exception
        StreamUtils.close(null);
    }

    @Test
    public void testReadStream() throws Exception {
        final InputStream inputStream = getClass().getResource("test5.txt").openStream();
        StreamUtils.readStream(inputStream);
        inputStream.close();
        thrown.expect(IOException.class);
        thrown.expectMessage("Stream closed");
        final int result = inputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testCloseWithIoException() throws IOException {
        doThrow(new IOException("Oops")).when(closeable).close();
        StreamUtils.close(closeable);
        verify(closeable).close();
    }

    @Test
    public void testCloseNormally() throws IOException {
        StreamUtils.close(closeable);
        verify(closeable).close();
    }

}