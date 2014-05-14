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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows reading and writing to a plain text file via a list of lines.
 */
public class TextFile {

    /**
     * The file we're dealing with.
     */
    private Path path;
    /**
     * The input stream we're dealing with.
     */
    private InputStream is;
    /**
     * The lines we've read from the file.
     */
    private List<String> lines;
    /**
     * The charset to use to read the file.
     */
    private final Charset charset;

    /**
     * Creates a new instance of TextFile for the specified file, and uses the
     * default charset.
     *
     * @param filename The file to be read/written
     */
    public TextFile(final String filename) {
        this(new File(filename));
    }

    /**
     * Creates a new instance of TextFile for the specified File, and uses the
     * default charset.
     *
     * @param file The file to read
     */
    public TextFile(final File file) {
        this(file, Charset.defaultCharset());
    }

    /**
     * Creates a new instance of TextFile for the specified Path, and uses the
     * default charset.
     *
     * @param path The path to read
     */
    public TextFile(final Path path) {
        this(path, Charset.defaultCharset());
    }

    /**
     * Creates a new instance of TextFile for an input stream, and uses the
     * default charset.
     *
     * @param is The input stream to read from
     */
    public TextFile(final InputStream is) {
        this(is, Charset.defaultCharset());
    }

    /**
     * Creates a new instance of TextFile for the specified File, which is to be
     * read using the specified charset.
     *
     * @param file The file to read
     * @param charset The charset to read the file in
     * @since 0.6.3m1
     */
    public TextFile(final File file, final Charset charset) {
        this(file.toPath(), charset);
    }

    /**
     * Creates a new instance of TextFile for the specified Path, which is to be
     * read using the specified charset.
     *
     * @param path The path to read
     * @param charset The charset to read the file in
     */
    public TextFile(final Path path, final Charset charset) {
        this.path = path;
        this.charset = charset;
    }

    /**
     * Creates a new instance of TextFile for an input stream, which is to be
     * read using the specified charset.
     *
     * @param is The input stream to read from
     * @param charset The charset to read the file in
     * @since 0.6.3m1
     */
    public TextFile(final InputStream is, final Charset charset) {
        this.is = is;
        this.charset = charset;
    }

    /**
     * Retrieves the contents of the file as a list of lines. If getLines() or
     * readLines() has previously been called, a cached version is returned.
     *
     * @return A list of lines in the file
     * @throws IOException if an I/O exception occurs
     */
    public List<String> getLines() throws IOException {
        if (lines == null) {
            readLines();
        }

        return lines;
    }

    /**
     * Reads the contents of the file into this TextFile's line cache.
     *
     * @throws IOException If an I/O exception occurs
     */
    public void readLines() throws IOException {
        try (BufferedReader reader = path == null
                ? new BufferedReader(new InputStreamReader(is, charset))
                : new BufferedReader(Files.newBufferedReader(path, charset))) {
            lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
    }

    /**
     * Determines if this file is writable or not.
     *
     * @return True if the file is writable, false otherwise
     */
    public boolean isWritable() {
        System.out.println("PATH: " + path);
        System.out.println("Exists: " + Files.exists(path));
        System.out.println("WRITEABLE: " + Files.isWritable(path));
        if (Files.exists(path)) {
            return path != null && Files.isWritable(path);
        } else {
            return path != null && Files.isWritable(path.getParent());
        }
    }

    /**
     * Writes the specified list of lines to the file.
     *
     * @param lines The lines to be written
     * @throws IOException if an I/O exception occurs
     */
    public void writeLines(final List<String> lines) throws IOException {
        if (path == null) {
            throw new UnsupportedOperationException("Cannot write to TextFile "
                    + "opened with an InputStream");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Retrieves the File for this TextFile, if there is one.
     *
     * @return This TextFile's file, or null
     */
    public File getFile() {
        if (path == null) {
            return null;
        }
        return path.toFile();
    }

    /**
     * Retrieves the Path for this TextFile, if there is one.
     *
     * @return This TextFile's path, or null
     */
    public Path getPath() {
        if (path == null) {
            return null;
        }
        return path;
    }

    /**
     * Deletes the file associated with this textfile, if there is one.
     *
     * @throws IOException if the file is unable to be deleted
     */
    public void delete() throws IOException {
        if (path == null) {
            throw new UnsupportedOperationException("Cannot delete TextFile "
                    + "opened with an InputStream");
        }

        Files.delete(path);
    }

}
