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

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Reads a file in reverse.
 */
public class ReverseFileReader implements AutoCloseable {

    /** Path to the file we're reading. */
    private final Path file;
    /** File to manipulate. */
    private SeekableByteChannel byteChannel;
    /** Number of bytes to skip backwards at a time. */
    private byte seekLength = 50;

    /**
     * Create a new ReverseFileReader.
     *
     * @param file File to read
     *
     * @throws SecurityException If a security manager exists and its checkRead method denies
     * read access to the file.
     * @throws IOException If there is an error seeking to the end of the file.
     */
    public ReverseFileReader(final Path file) throws SecurityException, IOException {
        this.file = file;
        byteChannel = Files.newByteChannel(file, StandardOpenOption.READ);
        reset();
    }

    /**
     * Reset the file pointer to the end of the file.
     *
     * @throws IOException If there is an error seeking, or the file is closed.
     */
    public final void reset() throws IOException {
        if (!byteChannel.isOpen()) {
            throw new IOException("Channel has been closed.");
        }
        byteChannel = Files.newByteChannel(file, StandardOpenOption.READ);
        byteChannel.position(byteChannel.size());
    }

    /**
     * Get the current seekLength.
     *
     * @return current seekLength
     */
    public byte getSeekLength() {
        return seekLength;
    }

    /**
     * Set the seekLength.
     *
     * @param newValue New value for seekLength
     */
    public void setSeekLength(final byte newValue) {
        seekLength = newValue;
    }

    @Override
    public void close() throws IOException {
        if (byteChannel.isOpen()) {
            byteChannel.close();
        }
    }

    /**
     * Get the next full line.
     *
     * @return The next full line.
     * @throws IOException If an error reading or seeking occured, or if the fiel is closed.
     */
    public String getNextLine() throws IOException {
        if (!byteChannel.isOpen()) {
            throw new IOException("Channel has been closed.");
        }
        // Used to store result to output.
        final ArrayList<Byte> line = new ArrayList<>(seekLength);
        // Check current position, if 0 we are at the start of the file
        // and should throw an exception.
        final long startfp = byteChannel.position();
        if (startfp == 0) {
            throw new EOFException("Reached Start of file");
        }

        // Keep looping until we get a full line, or the end of the file
        boolean keepLooping = true;
        while (keepLooping) {
            // Get Current Position
            long fp = byteChannel.position();

            // Check how far to seek backwards (seekLength or to the start of the file)
            final int seekDistance;
            if (fp < seekLength) {
                // Seek to the start of the file;
                seekDistance = (int) fp;
                fp = 0;
            } else {
                // Seek to position current-seekLength
                seekDistance = seekLength;
                fp -= seekDistance;
            }
            // Seek!
            byteChannel.position(fp);

            final ByteBuffer bytes = ByteBuffer.allocate(seekDistance);
            // Read into the bytes array
            byteChannel.read(bytes);

            // And loop looking for data
            // This uses seekDistance so that only wanted data is checked.
            boolean gotNewLine = false;
            for (int i = seekDistance - 1; i >= 0; --i) {
                // Check for New line Character, or a non carriage-return char
                if (bytes.get(i) == '\n') {
                    // Seek to the location of this character and exit this loop.
                    byteChannel.position(fp + i);
                    gotNewLine = true;
                    break;
                } else if (bytes.get(i) != '\r') {
                    // Add to the result, the loop will continue going.
                    line.add(0, bytes.get(i));
                }
            }

            // We have now processed the data we read (Either added it all to the
            // buffer, or found a newline character.)

            if (fp == 0 && !gotNewLine) {
                // This part of the loop started from the start of the file, but didn't
                // find a new line anywhere. no more loops are possible, so Treat
                // this as "got new line"
                gotNewLine = true;
                byteChannel.position(0);
            }

            // Do we need to continue?
            if (gotNewLine) {
                // We have found a new line somewhere, thus we don't need
                // to read any more bytes, so exit the while loop!
                keepLooping = false;
            } else {
                // We have not found a new line anywhere,
                // Seek to the pre-read position, and repeat.
                byteChannel.position(fp);
            }

        }

        // Return the data obtained.
        final byte[] result = new byte[line.size()];
        for (int i = 0; i < line.size(); ++i) {
            result[i] = line.get(i);
        }
        return new String(result, Charset.forName("UTF-8"));
    }

    /**
     * Try and get x number of lines.
     * If the file is closed, an empty stack will be returned.
     *
     * @param numLines Number of lines to try and get.
     * @return The requested lines
     */
    public Stack<String> getLines(final int numLines) {
        final Stack<String> result = new Stack<>();
        for (int i = 0; i < numLines; ++i) {
            try {
                result.push(getNextLine());
            } catch (IOException e) {
                break;
            }
        }
        return result;
    }

    /**
     * Try and get x number of lines and return a \n delimited String.
     * If the file is closed, an empty string will be returned.
     *
     * @param numLines Number of lines to try and get.
     * @return The requested lines
     */
    public String getLinesAsString(final int numLines) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < numLines; ++i) {
            try {
                result.insert(0, '\n');
                result.insert(0, getNextLine());
            } catch (IOException e) {
                break;
            }
        }
        if (result.charAt(0) == '\n') {
            result.deleteCharAt(0);
        }
        return result.toString();
    }

}
