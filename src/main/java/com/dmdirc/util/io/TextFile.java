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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Allows reading and writing to a plain text file via a list of lines.
 */
public class TextFile {

    /**
     * The file we're dealing with.
     */
    private final Path path;
    /**
     * The input stream we're dealing with.
     */
    private final InputStream is;
    /**
     * The charset to use to read the file.
     */
    private final Charset charset;
    /**
     * The lines we've read from the file.
     */
    private List<String> lines;

    /**
     * Creates a new instance of TextFile for the specified Path, which is to be
     * read using the specified charset.
     *
     * @param path The path to read
     * @param charset The charset to read the file in
     */
    public TextFile(final Path path, final Charset charset) {
        this.path = path;
        this.is = null;
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
        this.path = null;
        this.is = is;
        this.charset = charset;
    }

    /**
     * Retrieves the contents of the file as a list of lines. If getLines() or
     * readLines() has previously been called, a cached version is returned.
     *
     * @return An unmodifiable list of lines in the file
     * @throws IOException if an I/O exception occurs
     */
    public List<String> getLines() throws IOException {
        if (lines == null) {
            readLines();
        }

        return Collections.unmodifiableList(lines);
    }

    /**
     * Reads the contents of the file into this TextFile's line cache.
     *
     * @throws IOException If an I/O exception occurs
     */
    public void readLines() throws IOException {
        if (path == null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset))) {
                lines = reader.lines().collect(Collectors.toList());
            }
        } else {
            lines = Files.readAllLines(path, charset);
        }
    }

    /**
     * Determines if this file is writable or not.
     *
     * @return True if the file is writable, false otherwise
     */
    public boolean isWritable() {
        if (path == null) {
            return false;
        }
        if (Files.exists(path)) {
            return Files.isWritable(path);
        } else {
            return Files.isWritable(path.getParent());
        }
    }

    /**
     * Writes the specified list of lines to the file.
     *
     * @param lines The lines to be written
     * @throws IOException if an I/O exception occurs
     */
    public void writeLines(final Iterable<String> lines) throws IOException {
        if (!isWritable()) {
            throw new UnsupportedOperationException("Cannot write to TextFile "
                    + "opened with an InputStream");
        }

        Files.write(path, lines, charset);
    }

    /**
     * Deletes the file associated with this textfile, if there is one.
     *
     * @throws IOException if the file is unable to be deleted
     */
    public void delete() throws IOException {
        if (!isWritable()) {
            throw new UnsupportedOperationException("Cannot delete TextFile "
                    + "opened with an InputStream");
        }

        Files.delete(path);
    }

}
