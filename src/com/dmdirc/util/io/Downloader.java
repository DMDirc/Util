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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Allows easy downloading of files from HTTP sites.
 */
public class Downloader {

    /**
     * Retrieves the specified page.
     *
     * @param url The URL to retrieve
     * @return A list of lines received from the server
     * @throws IOException If there's an I/O error while downloading
     */
    public List<String> getPage(final String url) throws IOException {

        return getPage(url, "");
    }

    /**
     * Retrieves the specified page, sending the specified post data.
     *
     * @param url The URL to retrieve
     * @param postData The raw POST data to send
     * @return A list of lines received from the server
     * @throws IOException If there's an I/O error while downloading
     */
    public List<String> getPage(final String url, final String postData)
            throws IOException {

        final List<String> res = new ArrayList<>();

        final URLConnection urlConn = getConnection(url, postData);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                urlConn.getInputStream()))) {
            String line;
            do {
                line = in.readLine();

                if (line != null) {
                    res.add(line);
                }
            } while (line != null);
        }

        return res;
    }

    /**
     * Retrieves the specified page, sending the specified post data.
     *
     * @param url The URL to retrieve
     * @param postData A map of post data that should be sent
     * @return A list of lines received from the server
     * @throws IOException If there's an I/O error while downloading
     */
    public List<String> getPage(final String url,
            final Map<String, String> postData) throws IOException {
        final StringBuilder data = new StringBuilder();

        for (Map.Entry<String, String> entry : postData.entrySet()) {
            data.append('&');
            data.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            data.append('=');
            data.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return getPage(url, data.length() == 0 ? "" : data.substring(1));
    }

    /**
     * Downloads the specified page to disk.
     *
     * @param url The URL to retrieve
     * @param file The file to save the page to
     * @throws IOException If there's an I/O error while downloading
     */
    public void downloadPage(final String url, final Path file)
            throws IOException {
        downloadPage(url, file, null);
    }

    /**
     * Downloads the specified page to disk.
     *
     * @param url The URL to retrieve
     * @param file The file to save the page to
     * @throws IOException If there's an I/O error while downloading
     */
    @Deprecated
    public void downloadPage(final String url, final String file)
            throws IOException {
        downloadPage(url, Paths.get(file), null);
    }

    /**
     * Downloads the specified page to disk.
     *
     * @param url The URL to retrieve
     * @param file The file to save the page to
     * @param listener The progress listener for this download
     * @throws IOException If there's an I/O error while downloading
     */
    @Deprecated
    public void downloadPage(final String url, final String file,
            final DownloadListener listener) throws IOException {
        downloadPage(url, Paths.get(file), listener);
    }

    /**
     * Downloads the specified page to disk.
     *
     * @param url The URL to retrieve
     * @param file The file to save the page to
     * @param listener The progress listener for this download
     * @throws IOException If there's an I/O error while downloading
     */
    public void downloadPage(final String url, final Path file,
            final DownloadListener listener) throws IOException {

        final URLConnection urlConn = getConnection(url, "");

        try (OutputStream output = Files.newOutputStream(file);
                InputStream input = urlConn.getInputStream()) {
            final int length = urlConn.getContentLength();

            if (listener != null) {
                listener.setIndeterminate(length == -1);
            }

            final byte[] buffer = new byte[512];
            int count;

            int current = 0;
            do {
                count = input.read(buffer);

                if (count > 0) {
                    current += count;
                    output.write(buffer, 0, count);

                    if (listener != null && length != -1) {
                        listener.downloadProgress(100 * (float) current
                                / length);
                    }
                }
            } while (count > 0);
        }
    }

    /**
     * Creates an URL connection for the specified URL and data.
     *
     * @param url The URL to connect to
     * @param postData The POST data to pass to the URL
     * @return An URLConnection for the specified URL/data
     * @throws IOException If an I/O exception occurs while connecting
     */
    private URLConnection getConnection(final String url,
            final String postData)
            throws IOException {
        final URLConnection urlConn = getURLConnection(url);

        urlConn.setUseCaches(false);
        urlConn.setDoInput(true);
        urlConn.setDoOutput(!postData.isEmpty());
        urlConn.setConnectTimeout(10000);

        if (!postData.isEmpty()) {
            urlConn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            try (DataOutputStream out = new DataOutputStream(urlConn.getOutputStream())) {
                out.writeBytes(postData);
                out.flush();
            }
        }

        return urlConn;
    }

    protected URLConnection getURLConnection(final String url) throws IOException {
        final URL myUrl = new URL(url);
        return myUrl.openConnection();
    }

}
