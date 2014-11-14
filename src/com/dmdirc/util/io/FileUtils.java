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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class to deal with files.
 */
public final class FileUtils {

    private static final Optional<FileSystem> JAR_FILE_SYSTEM = getJarFileSystem();

    private FileUtils() {
        // Shouldn't be instansiated.
    }

    private static Optional<FileSystem> getJarFileSystem() {
        if (isRunningFromJar(FileUtils.class)) {
            try {
                final FileSystem fs = FileSystems.newFileSystem(
                        getApplicationPath(FileUtils.class), null);
                return Optional.of(fs);
            } catch (IOException ex) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Checks whether this application has been run from a jar or not.
     *
     * @param clazz Class used to locate the application
     *
     * @return true if the application was launched from a jar, false otherwise
     *
     * @throws IllegalStateException If the application path cannot be determined for any reason
     */
    public static boolean isRunningFromJar(final Class<?> clazz) throws IllegalStateException {
        return getApplicationPath(clazz).getFileName().toString().endsWith(".jar");
    }

    /**
     * Returns the base location for the specified class, this is either the a jar or a folder
     * depending on how the application was launched.
     *
     * This can fail for a number of reasons, it is not wholly reliable.
     *
     * @param clazz Class used to locate the application
     *
     * @return A {@link Path} to application location
     *
     * @throws IllegalStateException If the application path cannot be determined for any reason
     */
    public static Path getApplicationPath(final Class<?> clazz) throws IllegalStateException {
        final ProtectionDomain pd;
        try {
            pd = clazz.getProtectionDomain();
        } catch (SecurityException ex) {
            throw new IllegalStateException("Unable to get protection domain", ex);
        }
        final CodeSource cs = pd.getCodeSource();
        if (cs == null) {
            throw new IllegalStateException("Unable to get the code source");
        }
        final URI uri;
        try {
            uri = cs.getLocation().toURI();
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Unable to convert location to URI", ex);
        }
        return Paths.get(uri);
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
            final String path = source.toURI().toString();
            final int index = path.indexOf("!/");
            if (index > -1) {
                final String file = path.substring(0, index);
                final Map<String, String> env = new HashMap<>();
                try (final FileSystem fs = FileSystems.newFileSystem(URI.create(file), env)) {
                    copyRecursively(fs.getPath(path.substring(index + 2)), destination,
                            StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                copyRecursively(Paths.get(source.toURI()), destination,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (URISyntaxException ex) {
            throw new IOException("Unable to get source URI", ex);
        }
    }

    /**
     * Returns a {@link Path} for the specified bundled resource. If the app is running from a jar,
     * the resource will be backed by a Zip file system.
     *
     * @param resource The resource to get a path for
     * @return The path for the specified resource
     * @throws URISyntaxException If the resource could not be mapped to a path
     */
    public static Path getPathForResource(final URL resource) throws URISyntaxException {
        if (JAR_FILE_SYSTEM.isPresent()) {
            final String path = resource.toURI().toString();
            final int index = path.indexOf("!/");
            if (index > -1) {
                return JAR_FILE_SYSTEM.get().getPath(path.substring(index + 1));
            }
        }

        return Paths.get(resource.toURI());
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
