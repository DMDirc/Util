/*
 * Copyright (c) 2006-2012 DMDirc Developers
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
    public void testFormatDurationSecond() {
        assertEquals("1 second", DateUtils.formatDuration(1));
    }

    @Test
    public void testFormatDurationSeconds() {
        assertEquals("2 seconds", DateUtils.formatDuration(2));
    }

    @Test
    public void testFormatDurationMinute() {
        assertEquals("1 minute", DateUtils.formatDuration(60));
        assertEquals("1 minute, 1 second", DateUtils.formatDuration(61));
    }

    @Test
    public void testFormatDurationMinutes() {
        assertEquals("2 minutes", DateUtils.formatDuration(120));
        assertEquals("2 minutes, 1 second", DateUtils.formatDuration(121));
    }

    @Test
    public void testFormatDurationHour() {
        assertEquals("1 hour", DateUtils.formatDuration(3600));
        assertEquals("1 hour, 1 second", DateUtils.formatDuration(3601));
    }

    @Test
    public void testFormatDurationHours() {
        assertEquals("2 hours", DateUtils.formatDuration(7200));
        assertEquals("2 hours, 1 minute, 1 second", DateUtils.formatDuration(7261));
    }

    @Test
    public void testFormatDurationDay() {
        assertEquals("1 day", DateUtils.formatDuration(86400));
        assertEquals("1 day, 1 hour, 1 minute, 1 second", DateUtils.formatDuration(90061));
    }

    @Test
    public void testFormatDurationDays() {
        assertEquals("2 days", DateUtils.formatDuration(172800));
        assertEquals("2 days, 1 hour, 1 minute, 1 second", DateUtils.formatDuration(176461));
    }

    @Test
    public void testFormatNoSeconds() {
        assertEquals("0 seconds", DateUtils.formatDuration(0));
        assertEquals("0 seconds", DateUtils.formatDuration(-100));
    }

}
