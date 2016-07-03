/*
 * Copyright (c) 2006-2015 DMDirc Developers
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

package com.dmdirc.util.colours;

/**
 * Some util methods for dealing with colours.
 */
public final class ColourUtils {

    private ColourUtils() {
        //Do not instantiate.
    }

    /**
     * Retrieves the hex representation of the specified colour.
     *
     * @param colour The colour to be parsed
     *
     * @return A 6-digit hex string representing the colour
     */
    public static String getHex(final Colour colour) {
        final int r = colour.getRed();
        final int g = colour.getGreen();
        final int b = colour.getBlue();

        return toHex(r) + toHex(g) + toHex(b);
    }

    /**
     * Converts the specified integer (in the range 0-255) into a hex string.
     *
     * @param value The integer to convert
     *
     * @return A 2 char hex string representing the specified integer
     */
    private static String toHex(final int value) {
        final String hex = Integer.toHexString(value);
        return (hex.length() < 2 ? "0" : "") + hex;
    }
}
