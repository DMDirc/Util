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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Simple stream reader to read a stream and save/discard the data.
 */
public class StreamReader extends Thread {

    /** This is the Input Stream we are reading. */
    private final InputStream stream;
    /** List to store output in. */
    private List<String> list;
    /** StringBuffer to store output in. */
    private StringBuffer buffer; // NOPMD

    /**
     * Create a new Stream Reader that discards output.
     *
     * @param stream The stream to read
     */
    public StreamReader(final InputStream stream) {
        super("StreamReader");

        this.stream = stream;
    }

    /**
     * Create a new Stream Reader that outputs to a List.
     *
     * @param stream The stream to read
     * @param list The list to store the output from the stream in
     */
    public StreamReader(final InputStream stream, final List<String> list) {
        super("StreamReader");

        this.stream = stream;
        this.list = list;
    }

    /**
     * Create a new Stream Reader that outputs to a StringBuffer.
     *
     * @param stream The stream to read
     * @param buffer The StringBuffer to store the output from the stream in
     */
    public StreamReader(final InputStream stream, final StringBuffer buffer) {
        super("StreamReader");

        this.stream = stream;
        this.buffer = buffer;
    }

    /**
     * Get the list that the output is being stored in.
     *
     * @return The output list
     */
    public List<String> getList() {
        return list;
    }

    /**
     * Wait for input on stream, and output/throw away/save to list.
     */
    @Override
    public void run() {
        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (list != null) {
                    list.add(line);
                } else if (buffer != null) {
                    if (buffer.length() > 0) { buffer.append('\n'); }
                    buffer.append(line);
                }
            }
        } catch (IOException ex) {
            // OH WELL
        } finally {
            StreamUtils.close(stream);
        }
    }
}
