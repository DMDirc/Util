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

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Utility class to deal with files.
 */
public final class FileUtils {

    private FileUtils() {
        // Shouldn't be instansiated.
    }

    /**
     * Recursively copies the resources specified by the given URL to the destination folder.
     * Existing files will be replaced.
     *
     * @param source The resource to be copied (file or folder).
     * @param destination The destination folder to copy the resource to.
     * @throws IOException If the resource couldn't be copied.
     */
    public static void copyResources(final URL source, final Path destination) throws IOException {
        try {
            copyRecursively(Paths.get(source.toURI()), destination,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (URISyntaxException ex) {
            throw new IOException("Unable to get source URI", ex);
        }
    }

    /**
     * Recursively copies one path to another. Once complete, a deep copy of the source file or
     * folder will be present in the destination directory.
     *
     * @param source The path that should be copied.
     * @param destination The directory to place copied files in.
     * @param options Options to use when copying.
     * @throws IOException If any files couldn't be copied.
     */
    public static void copyRecursively(final Path source, final Path destination,
            final CopyOption... options) throws IOException {
        final Path destinationPath = destination.resolve(source.getFileName().toString());

        if (Files.isDirectory(source)) {
            Files.createDirectories(destinationPath);
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(source)) {
                for (Path path : directoryStream) {
                    copyRecursively(path, destinationPath, options);
                }
            }
        } else {
            Files.copy(source, destinationPath, options);
        }
    }

}
