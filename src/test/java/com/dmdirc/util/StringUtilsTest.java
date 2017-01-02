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

package com.dmdirc.util;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class StringUtilsTest {

    @Test
    public void testIndexOfStartOfWord() {
        final String input = "abc 123 def";
        assertEquals(0, StringUtils.indexOfStartOfWord(input, 0));
        assertEquals(0, StringUtils.indexOfStartOfWord(input, 1));
        assertEquals(0, StringUtils.indexOfStartOfWord(input, 2));

        assertEquals(4, StringUtils.indexOfStartOfWord(input, 3));
        assertEquals(4, StringUtils.indexOfStartOfWord(input, 4));
        assertEquals(4, StringUtils.indexOfStartOfWord(input, 5));
        assertEquals(4, StringUtils.indexOfStartOfWord(input, 6));

        assertEquals(8, StringUtils.indexOfStartOfWord(input, 7));
        assertEquals(8, StringUtils.indexOfStartOfWord(input, 8));
        assertEquals(8, StringUtils.indexOfStartOfWord(input, 9));
        assertEquals(8, StringUtils.indexOfStartOfWord(input, 10));
    }

    @Test
    public void testIndexOfEndOfWord() {
        final String input = "abc 123 def";
        assertEquals(3, StringUtils.indexOfEndOfWord(input, 0));
        assertEquals(3, StringUtils.indexOfEndOfWord(input, 1));
        assertEquals(3, StringUtils.indexOfEndOfWord(input, 2));
        assertEquals(3, StringUtils.indexOfEndOfWord(input, 3));

        assertEquals(7, StringUtils.indexOfEndOfWord(input, 4));
        assertEquals(7, StringUtils.indexOfEndOfWord(input, 5));
        assertEquals(7, StringUtils.indexOfEndOfWord(input, 6));
        assertEquals(7, StringUtils.indexOfEndOfWord(input, 7));

        assertEquals(11, StringUtils.indexOfEndOfWord(input, 8));
        assertEquals(11, StringUtils.indexOfEndOfWord(input, 9));
        assertEquals(11, StringUtils.indexOfEndOfWord(input, 10));
    }

    @Test
    public void testIndicesOfWord() {
        final String input = "abc 123 def";
        assertArrayEquals(new int[]{0, 3}, StringUtils.indiciesOfWord(input, 0));
        assertArrayEquals(new int[]{0, 3}, StringUtils.indiciesOfWord(input, 1));
        assertArrayEquals(new int[]{0, 3}, StringUtils.indiciesOfWord(input, 2));

        assertArrayEquals(new int[]{0, 0}, StringUtils.indiciesOfWord(input, 3));

        assertArrayEquals(new int[]{4, 7}, StringUtils.indiciesOfWord(input, 4));
        assertArrayEquals(new int[]{4, 7}, StringUtils.indiciesOfWord(input, 5));
        assertArrayEquals(new int[]{4, 7}, StringUtils.indiciesOfWord(input, 6));

        assertArrayEquals(new int[]{0, 0}, StringUtils.indiciesOfWord(input, 7));

        assertArrayEquals(new int[]{8, 11}, StringUtils.indiciesOfWord(input, 8));
        assertArrayEquals(new int[]{8, 11}, StringUtils.indiciesOfWord(input, 9));
        assertArrayEquals(new int[]{8, 11}, StringUtils.indiciesOfWord(input, 10));
    }

}