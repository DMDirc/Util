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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class ColourTest {

    @Test
    public void testGetters() {
        final Colour colour = new Colour(12, 0, 255);
        assertEquals(12, colour.getRed());
        assertEquals(0, colour.getGreen());
        assertEquals(255, colour.getBlue());
    }

    @Test
    public void testEquals() {
        assertTrue(new Colour(1, 2, 3).equals(new Colour(1, 2, 3)));
        assertTrue(Colour.RED.equals(new Colour(255, 0, 0)));
        assertFalse(new Colour(3, 2, 1).equals(new Colour(1, 2, 3)));
        assertFalse(Colour.RED.equals(new Colour(255, 1, 0)));
    }

    @Test
    public void testHashcodeSameForEqualColours() {
        assumeTrue(new Colour(1, 2, 3).equals(new Colour(1, 2, 3)));
        assertEquals(new Colour(1, 2, 3).hashCode(), new Colour(1, 2, 3).hashCode());

        assumeTrue(Colour.RED.equals(new Colour(255, 0, 0)));
        assertEquals(Colour.RED.hashCode(), new Colour(255, 0, 0).hashCode());
    }

}