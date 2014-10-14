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

import javax.annotation.Nonnull;

/**
 * Utilities for dealing with strings.
 */
public final class StringUtils {

    private StringUtils() {
        // Shouldn't be instansiated.
    }

    /**
     * Returns the indexes for the word surrounding the index in the specified string.
     *
     * @param text  Text to get word from
     * @param index Index to get surrounding word
     *
     * @return An array containing two elements: the index of the first character of the word, and
     * the index of the first character beyond the end of the word. If the specified index is not
     * contained within a word (i.e., is whitespace) then 0,0 is returned.
     */
    public static int[] indiciesOfWord(@Nonnull final CharSequence text, final int index) {
        final int start = indexOfStartOfWord(text, index);
        final int end = indexOfEndOfWord(text, index);

        if (start > end) {
            return new int[]{0, 0};
        }

        return new int[]{start, end};

    }

    /**
     * Returns the start index for the word surrounding the index in the specified string.
     *
     * @param text  Text to get word from
     * @param index Index to get surrounding word
     *
     * @return Start index of the word surrounding the index
     */
    public static int indexOfStartOfWord(@Nonnull final CharSequence text, final int index) {
        int start = index;

        // Traverse backwards
        while (start > 0 && start < text.length() && text.charAt(start) != ' ') {
            start--;
        }
        if (start + 1 < text.length() && text.charAt(start) == ' ') {
            start++;
        }

        return start;
    }

    /**
     * Returns the end index for the word surrounding the index in the specified string.
     *
     * @param text  Text to get word from
     * @param index Index to get surrounding word
     *
     * @return End index of the word surrounding the index
     */
    public static int indexOfEndOfWord(@Nonnull final CharSequence text, final int index) {
        int end = index;

        // And forwards
        while (end < text.length() && end >= 0 && text.charAt(end) != ' ') {
            end++;
        }

        return end;
    }

}
