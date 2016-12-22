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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DownloaderTest {

    @Mock private URLConnection mockedConnection;
    @Mock private DownloadListener listener;
    private ByteArrayOutputStream os;
    private FileSystem fakeFS;

    @Before
    public void setup() throws IOException {
        fakeFS = Jimfs.newFileSystem(Configuration.unix());
        os = new ByteArrayOutputStream();

        final ByteArrayInputStream is = new ByteArrayInputStream(
                "OMG IM A FAKE DOWNLOAD".getBytes("UTF-8"));
        when(mockedConnection.getInputStream()).thenReturn(is);
        when(mockedConnection.getOutputStream()).thenReturn(os);
    }

    @Test
    public void testGetPage() throws IOException {
        new TestableDownloader().getPage("rar");
        verify(mockedConnection, never()).setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        assertEquals(0, os.size());
    }

    @Test
    public void testGetPageString() throws IOException {
        final String postData = "sdfgsdfgsdfg";
        new TestableDownloader().getPage("rar", postData);
        verify(mockedConnection).setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        assertEquals(postData, os.toString());
    }

    @Test
    public void testGetPageMap() throws IOException {
        final Map<String, String> postData = Maps.newHashMap();
        postData.put("key1", "value1");
        postData.put("key2", "value2");
        new TestableDownloader().getPage("rar", postData);
        verify(mockedConnection).setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        final String postDataString1 = "key1=value1&key2=value2";
        final String postDataString2 = "key2=value2&key1=value1";
        assertTrue(postDataString1.equals(os.toString()) || postDataString2.equals(os.toString()));
    }

    @Test
    public void testGetPageMapEncoding() throws IOException {
        final Map<String, String> postData = Maps.newHashMap();
        postData.put("ke&y1", "value1");
        postData.put("key2", "val&ue2");
        new TestableDownloader().getPage("rar", postData);
        verify(mockedConnection).setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        final String postDataString1 = "ke%26y1=value1&key2=val%26ue2";
        final String postDataString2 = "key2=val%26ue2&ke%26y1=value1";
        assertTrue(postDataString1.equals(os.toString()) || postDataString2.equals(os.toString()));
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
        verify(listener, atLeastOnce()).downloadProgress(anyFloat());
    }

    private class TestableDownloader extends Downloader {
        @Override
        protected URLConnection getURLConnection(final String url) throws IOException {
            return mockedConnection;
        }
    }

}