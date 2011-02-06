/*
 * Copyright (c) 2006-2011 Chris Smith, Shane Mc Cormack, Gregory Holmes
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

import org.junit.Test;
import static org.junit.Assert.*;

public class DateUtilsTest {

    @Test
    public void testFormatDurationSeconds() {
        assertEquals("1 second", DateUtils.formatDuration(1));
        assertEquals("2 seconds", DateUtils.formatDuration(2));
    }

    @Test
    public void testFormatDurationMinutes() {
        assertEquals("1 minute", DateUtils.formatDuration(60));
        assertEquals("1 minute, 1 second", DateUtils.formatDuration(61));
        assertEquals("1 minute, 2 seconds", DateUtils.formatDuration(62));
        assertEquals("2 minutes, 2 seconds", DateUtils.formatDuration(122));
    }

    @Test
    public void testFormatDurationHours() {
        assertEquals("1 hour", DateUtils.formatDuration(3600));
        assertEquals("1 hour, 1 second", DateUtils.formatDuration(3601));
        assertEquals("2 hours, 1 minute, 5 seconds", DateUtils.formatDuration(7265));
    }

    @Test
    public void testFormatDurationDays() {
        assertEquals("1 day", DateUtils.formatDuration(86400));
        assertEquals("1 day, 10 minutes, 1 second", DateUtils.formatDuration(87001));
    }

    @Test
    public void testFormatNoSeconds() {
        assertEquals("0 seconds", DateUtils.formatDuration(0));
        assertEquals("0 seconds", DateUtils.formatDuration(-100));
    }

}
