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
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;

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
public class ConfigFileTest {

    @Mock private Path ro;
    @Mock private FileSystem mockedFileSystem;
    @Mock private FileSystemProvider fileSystemProvider;
    private ConfigFile cf;
    private Path temp;
    private FileSystem fileSystem;

    @Before
    public void setUp() throws Exception {
        fileSystem = Jimfs.newFileSystem(Configuration.unix());
        Files.copy(getClass().getResourceAsStream("test2.txt"), fileSystem.getPath("/test2.txt"));
        cf = new ConfigFile(fileSystem.getPath("/test2.txt"));
        temp = fileSystem.getPath("/temp.txt");
        when(mockedFileSystem.provider()).thenReturn(fileSystemProvider);
        when(ro.getFileSystem()).thenReturn(mockedFileSystem);
        doThrow(new AccessDeniedException("Nup.")).when(fileSystemProvider).checkAccess(ro, AccessMode.WRITE);
    }

    @After
    public void tearDown() throws Exception {
        fileSystem.close();
    }

    @Test
    public void testRead() throws IOException, InvalidConfigFileException {
        cf.read();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWrite() throws IOException {
        new ConfigFile(ro).write();
    }

    @Test
    public void testDomains() throws IOException, InvalidConfigFileException {
        cf.read();
        assertTrue(cf.hasDomain("keysections"));
        assertTrue(cf.hasDomain("section alpha"));
        assertTrue(cf.hasDomain("section one point one"));
        assertTrue(cf.hasDomain("section one"));
        assertFalse(cf.hasDomain("random domain"));
    }

    @Test
    public void testKeyDomains() throws IOException, InvalidConfigFileException {
        cf.read();
        assertTrue(cf.isKeyDomain("section one"));
        assertFalse(cf.isKeyDomain("section one point one"));
        assertFalse(cf.isKeyDomain("section two"));
    }

    @Test
    public void testFlatDomains() throws IOException, InvalidConfigFileException {
        cf.read();
        assertTrue(cf.isFlatDomain("keysections"));
        assertTrue(cf.isFlatDomain("section alpha"));
        assertTrue(cf.isFlatDomain("section one point one"));
        assertFalse(cf.isFlatDomain("section one"));
        assertFalse(cf.hasDomain("random domain"));
    }

    @Test
    public void testFlatDomainContents() throws IOException, InvalidConfigFileException {
        cf.read();
        assertEquals(2, cf.getFlatDomain("section alpha").size());
        assertEquals("line 1", cf.getFlatDomain("section alpha").get(0));
        assertEquals("line 2", cf.getFlatDomain("section alpha").get(1));
    }

    @Test
    public void testKeyDomainContents() throws IOException, InvalidConfigFileException {
        cf.read();
        assertEquals(3, cf.getKeyDomain("section one").size());
        assertEquals("one", cf.getKeyDomain("section one").get("1"));
        assertEquals("two", cf.getKeyDomain("section one").get("2"));
        assertEquals("three", cf.getKeyDomain("section one").get("3"));
    }

    @Test
    public void testColons() throws IOException, InvalidConfigFileException {
        final ConfigFile config = new ConfigFile(temp);
        final Map<String, String> data = new HashMap<>();
        data.put("test1", "hello");
        data.put("test:2", "hello");
        data.put("test3", "hello:");
        config.addDomain("test", data);
        config.write();

        final ConfigFile config2 = new ConfigFile(temp);
        config2.read();

        assertTrue(config2.isKeyDomain("test"));
        final Map<String, String> test = config2.getKeyDomain("test");
        assertEquals("hello", test.get("test1"));
        assertEquals("hello", test.get("test:2"));
        assertEquals("hello:", test.get("test3"));
    }

    @Test
    public void testEquals() throws IOException, InvalidConfigFileException {
        final ConfigFile config = new ConfigFile(temp);
        final Map<String, String> data = new HashMap<>();
        data.put("test1", "hello");
        data.put("test=2", "hello");
        data.put("test3", "hello=");
        config.addDomain("test", data);
        config.write();

        final ConfigFile config2 = new ConfigFile(temp);
        config2.read();

        assertTrue(config2.isKeyDomain("test"));
        final Map<String, String> test = config2.getKeyDomain("test");
        assertEquals("hello", test.get("test1"));
        assertEquals("hello", test.get("test=2"));
        assertEquals("hello=", test.get("test3"));
    }

    @Test
    public void testNewlines() throws IOException, InvalidConfigFileException {
        final ConfigFile config = new ConfigFile(temp);
        final Map<String, String> data = new HashMap<>();
        data.put("test1", "hello");
        data.put("test2", "hello\ngoodbye");
        data.put("test3", "hello\n");
        data.put("test4", "hello\r\ngoodbye");
        config.addDomain("test", data);
        config.write();

        final ConfigFile config2 = new ConfigFile(temp);
        config2.read();

        assertTrue(config2.isKeyDomain("test"));
        final Map<String, String> test = config2.getKeyDomain("test");
        assertEquals("hello", test.get("test1"));
        assertEquals("hello\ngoodbye", test.get("test2"));
        assertEquals("hello\n", test.get("test3"));
        assertEquals("hello\r\ngoodbye", test.get("test4"));
    }

    @Test
    public void testBackslash() throws IOException, InvalidConfigFileException {
        final ConfigFile config = new ConfigFile(temp);
        final Map<String, String> data = new HashMap<>();
        data.put("test1", "hello\\");
        data.put("test2", "\\nhello");
        data.put("test3\\", "hello");
        config.addDomain("test", data);
        config.write();

        final ConfigFile config2 = new ConfigFile(temp);
        config2.read();

        assertTrue(config2.isKeyDomain("test"));
        final Map<String, String> test = config2.getKeyDomain("test");
        assertEquals("hello\\", test.get("test1"));
        assertEquals("\\nhello", test.get("test2"));
        assertEquals("hello", test.get("test3\\"));
    }

    @Test
    public void testHash() throws IOException, InvalidConfigFileException {
        final ConfigFile config = new ConfigFile(temp);
        final Map<String, String> data = new HashMap<>();
        data.put("test1#", "hello");
        data.put("#test2", "hello");
        data.put("test3", "#hello");
        config.addDomain("test", data);
        config.write();

        final ConfigFile config2 = new ConfigFile(temp);
        config2.read();

        assertTrue(config2.isKeyDomain("test"));
        final Map<String, String> test = config2.getKeyDomain("test");
        assertEquals("hello", test.get("test1#"));
        assertEquals("hello", test.get("#test2"));
        assertEquals("#hello", test.get("test3"));
    }

    @Test
    public void testEscape() {
        final String input = "blah blah\\foo\r\nbar=:";
        final String output = "blah blah\\\\foo\\r\\nbar\\=\\:";
        assertEquals(output, ConfigFile.escape(input));
    }

    @Test
    public void testUnescape() {
        final String input = "blah blah\\foo\r\nbar=:";
        assertEquals(input, ConfigFile.unescape(ConfigFile.escape(input)));
    }

    @Test
    public void testDelete() throws IOException {
        final ConfigFile config = new ConfigFile(temp);
        config.write();
        assertTrue(Files.exists(temp));
        config.delete();
        assertFalse(Files.exists(temp));
    }

    @Test
    public void testDuplicateKeys() throws IOException, InvalidConfigFileException {
        final ConfigFile file = new ConfigFile(getClass().getResourceAsStream("test2.txt"));
        file.read();

        assertTrue(file.isKeyDomain("section one"));
        assertEquals(3, file.getKeyDomain("section one").size());
        assertTrue(file.isFlatDomain("section one point one"));
    }

    @Test(expected = InvalidConfigFileException.class)
    public void testInvalidLine() throws IOException, InvalidConfigFileException {
        final ConfigFile file = new ConfigFile(getClass().getResourceAsStream("test1.txt"));
        file.read();
    }

}
