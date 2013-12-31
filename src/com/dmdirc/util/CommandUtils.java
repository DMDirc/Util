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

package com.dmdirc.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities methods associated with commands.
 */
public final class CommandUtils {

    /** Private contructor to stop instantiation of class. */
    private CommandUtils() {
        //Shouldn't be used.
    }

    /**
     * Parses the specified command into an array of arguments. Arguments are
     * separated by spaces. Multi-word arguments may be specified by starting
     * the argument with a quote (") and finishing it with a quote (").
     *
     * @param command The command to parse
     * @return An array of arguments corresponding to the command
     */
    public static String[] parseArguments(final String command) {
        final List<String> args = new ArrayList<>();
        final StringBuilder builder = new StringBuilder();
        boolean inquote = false;

        for (String word : command.split(" ")) {
            if (word.endsWith("\"") && inquote) {
                args.add(builder.toString() + ' ' + word.substring(0, word.length() - 1));
                builder.delete(0, builder.length());
                inquote = false;
            } else if (inquote) {
                builder.append(' ');
                builder.append(word);
            } else if (word.startsWith("\"") && !word.endsWith("\"")) {
                inquote = true;
                builder.append(word.substring(1));
            } else if (word.startsWith("\"") && word.endsWith("\"")) {
                if (word.length() == 1) {
                    inquote = true;
                } else {
                    args.add(word.substring(1, word.length() - 1));
                }
            } else {
                args.add(word);
            }
        }

        return args.toArray(new String[args.size()]);
    }
}
