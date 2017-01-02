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

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TextFileTest {

    @Mock private Path ro;
    @Mock private FileSystem mockedFileSystem;
    @Mock private FileSystemProvider fileSystemProvider;
    private Path test1;
    private Path temp;
    private FileSystem fileSystem;

    @Before
    public void setup() throws IOException {
        fileSystem = Jimfs.newFileSystem(Configuration.unix());
        Files.copy(getClass().getResourceAsStream("test1.txt"), fileSystem.getPath("/test1.txt"));
        test1 = fileSystem.getPath("/test1.txt");
        temp = fileSystem.getPath("/temp.txt");
        when(mockedFileSystem.provider()).thenReturn(fileSystemProvider);
        when(ro.getFileSystem()).thenReturn(mockedFileSystem);
        doThrow(new AccessDeniedException("Nup.")).when(fileSystemProvider).checkAccess(ro, AccessMode.WRITE);
    }

    @After
    public void tearDown() throws IOException {
        fileSystem.close();
    }

    @Test
    public void testGetLines() throws IOException, URISyntaxException {
        final TextFile file = new TextFile(test1, Charset.forName("UTF-8"));
        final List<String> lines = file.getLines();

        assertEquals(7, lines.size());
        assertEquals("Line 1", lines.get(0));
    }

    @Test
    public void testGetLines2() throws IOException, URISyntaxException {
        final TextFile file = new TextFile(test1, Charset.forName("UTF-8"));
        final List<String> lines = file.getLines();

        assertEquals(7, lines.size());
        assertEquals("Line 1", lines.get(0));
    }

    @Test
    public void testWrite() throws IOException {
        final TextFile file = new TextFile(temp, Charset.forName("UTF-8"));
        final List<String> lines = Arrays.asList("hello", "this is a test", "meep");
        file.writeLines(lines);
        final List<String> newLines = file.getLines();
        assertEquals(lines, newLines);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIllegalWrite() throws IOException, URISyntaxException {
        final TextFile file = new TextFile(ro, Charset.forName("UTF-8"));
        file.writeLines(Arrays.asList("hello", "this is a test", "meep"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIllegalDelete() throws IOException, URISyntaxException {
        final TextFile file = new TextFile(ro, Charset.forName("UTF-8"));
        file.delete();
    }

    @Test
    public void testDelete() throws IOException {
        final TextFile file = new TextFile(temp, Charset.forName("UTF-8"));
        final List<String> lines = Arrays.asList("hello", "this is a test", "meep");
        file.writeLines(lines);
        assertTrue(Files.exists(temp));
        file.delete();
        assertFalse(Files.exists(temp));
    }

    @Test
    public void testIsWriteable_Stream() throws IOException {
        final TextFile file = new TextFile(Files.newInputStream(test1), Charset.forName("UTF-8"));
        assertFalse(file.isWritable());
    }

    @Test
    public void testReadLine_Stream() throws IOException {
        final TextFile file = new TextFile(Files.newInputStream(test1), Charset.forName("UTF-8"));
        final List<String> lines = file.getLines();
        assertEquals(7, lines.size());
        assertEquals("Line 1", lines.get(0));
    }

}
