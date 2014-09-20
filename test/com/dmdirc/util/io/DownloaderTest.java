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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyByte;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DownloaderTest {

    @Mock private URLConnection mockedConnection;
    @Mock private OutputStream os;
    @Mock private DownloadListener listener;
    private FileSystem fakeFS;

    @Before
    public void setup() throws IOException {
        fakeFS = Jimfs.newFileSystem(Configuration.unix());
        ByteArrayInputStream is = new ByteArrayInputStream(
                "OMG IM A FAKE DOWNLOAD".getBytes("UTF-8"));
        when(mockedConnection.getInputStream()).thenReturn(is);
        when(mockedConnection.getOutputStream()).thenReturn(os);
        when(mockedConnection.getLastModified()).thenReturn(10L, (Long)11L);
    }

    @Test
    public void testGetPage() throws IOException {
        new TestableDownloader().getPage("rar");
        verify(mockedConnection, never()).setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        verify(os, never()).write(anyByte());
    }

    @Test
    public void testGetPageString() throws IOException {
        final String postData = "sdfgsdfgsdfg";
        new TestableDownloader().getPage("rar", postData);
        verify(mockedConnection).setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        final InOrder order = inOrder(os);
        for (int i=0; i < postData.length(); i++) {
            order.verify(os).write((byte) postData.charAt(i));
        }
    }

    @Test
    public void testGetPageMap() throws IOException {
        final Map<String, String> postData = Maps.newHashMap();
        postData.put("key1", "value1");
        postData.put("key2", "value2");
        final String postDataString = "key1=value1&key2=value2";
        new TestableDownloader().getPage("rar", postData);
        verify(mockedConnection).setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        final InOrder order = inOrder(os);
        for (int i=0; i < postDataString.length(); i++) {
            order.verify(os).write(postDataString.charAt(i));
        }
    }

    @Test
    public void testDownloadPage() throws IOException {
        final Path file = fakeFS.getPath("test.txt");
        assertFalse(Files.exists(file));
        new TestableDownloader().downloadPage("rar", file);
        assertTrue(Files.exists(file));
        assertEquals(Lists.newArrayList("OMG IM A FAKE DOWNLOAD"), Files.readAllLines(file,
                Charset.forName("UTF-8")));
    }

    @Test
    public void testListener() throws IOException {
        final Path file = fakeFS.getPath("test.txt");
        assertFalse(Files.exists(file));
        new TestableDownloader().downloadPage("rar", file, listener);
        assertTrue(Files.exists(file));
        assertEquals(Lists.newArrayList("OMG IM A FAKE DOWNLOAD"), Files.readAllLines(file,
                Charset.forName("UTF-8")));
        verify(listener).setIndeterminate(anyBoolean());
        verify(listener, atLeastOnce()).downloadProgress(anyInt());
    }

    private class TestableDownloader extends Downloader {
        protected URLConnection getURLConnection(final String url) throws IOException {
            return mockedConnection;
        }
    }

}