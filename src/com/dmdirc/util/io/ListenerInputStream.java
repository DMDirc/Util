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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

/**
 * Wrapped {@link InputStream} that provides updates to a {@link DownloadListener} as it reads a
 * stream.
 */
public class ListenerInputStream extends FilterInputStream {

    private final DownloadListener listener;
    private final int length;
    private int count;
    private int mark;

    /**
     * Creates a new stream.
     *
     * @param in        Stream to wrap
     * @param listener  Listener to gives up dates to
     * @param length    Length of the stream, if -1 the listener will be indeterminate
     */
    public ListenerInputStream(@Nonnull final InputStream in, final DownloadListener listener,
            final int length) {
        super(in);
        this.listener = listener;
        this.length = length;
        count = 0;
        mark = count;
        if (listener != null) {
            listener.setIndeterminate(length == -1);
        }
    }

    @Override
    public int read() throws IOException {
        final int read = super.read();
        return update(read);
    }

    @Override
    public int read(@Nonnull final byte[] b) throws IOException {
        final int read = super.read(b);
        return update(read);
    }

    @Override
    public int read(@Nonnull final byte[] b, final int off, final int len) throws IOException {
        final int read = super.read(b, off, len);
        return update(read);
    }

    @Override
    public long skip(final long n) throws IOException {
        final long read = super.skip(n);
        return update((int) read);
    }

    @Override
    public int available() throws IOException {
        return super.available();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    @Override
    public synchronized void mark(final int readlimit) {
        mark = count;
        super.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        update(mark - count);
        super.reset();
    }

    @Override
    public boolean markSupported() {
        return super.markSupported();
    }

    /**
     * Updates the listener and total read count.
     *
     * @param read Number of bytes further into stream.
     *
     * @return Returns the number of bytes read.
     */
    private int update(final int read) {
        if (read > 0) {
            count += read;
            if (listener != null && length != -1) {
                listener.downloadProgress(100 * (float) count / length);
            }
        }
        return read;
    }
}
