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

/**
 * Utility methods associated with dates.
 */
public final class DateUtils {

    /** Private contructor to stop instantiation of class. */
    private DateUtils() {
        //Shouldn't be used
    }

    /**
     * Tests for and adds one component of the duration format.
     *
     * @param builder The string builder to append text to
     * @param current The number of seconds in the duration
     * @param duration The number of seconds in this component
     * @param name The name of this component
     * @return The number of seconds used by this component
     */
    private static int doDuration(final StringBuilder builder, final int current,
            final int duration, final String name) {
        int res = 0;

        if (current >= duration) {
            final int units = current / duration;
            res = units * duration;

            if (builder.length() > 0) {
                builder.append(", ");
            }

            builder.append(units);
            builder.append(' ');
            builder.append(name);
            builder.append(units == 1 ? "" : 's');
        }

        return res;
    }

    /**
     * Formats the specified number of seconds as a string containing the
     * number of days, hours, minutes and seconds.
     *
     * @param duration The duration in seconds to be formatted
     * @return A textual version of the duration in words (e.g. '3 days, 1 minute, 4 seconds').
     */
    public static String formatDuration(final int duration) {
        final StringBuilder buff = new StringBuilder();

        int seconds = duration;

        seconds -= doDuration(buff, seconds, 60 * 60 * 24, "day");
        seconds -= doDuration(buff, seconds, 60 * 60, "hour");
        seconds -= doDuration(buff, seconds, 60, "minute");
        doDuration(buff, seconds, 1, "second");

        return buff.length() == 0 ? "0 seconds" : buff.toString();
    }

    /**
     * Appends the specified number as a 0-padded 2 digit time.
     *
     * @param builder The builder to append the number to.
     * @param number The number to be appended.
     * @return The given builder, as a convenience.
     */
    private static StringBuilder appendTime(final StringBuilder builder, final int number) {
        if (number < 10) {
            builder.append('0');
        }
        builder.append(number);
        return builder;
    }

    /**
     * Formats the specified number of seconds as a string containing the
     * number of hours, minutes and seconds.
     *
     * @param duration The duration in seconds to be formatted
     * @return A textual version of the duration as a time (e.g. '03:02:12').
     */
    public static String formatDurationAsTime(final int duration) {
        final StringBuilder result = new StringBuilder();
        final int hours = duration / 3600;
        final int minutes = duration / 60 % 60;
        final int seconds = duration % 60;

        if (hours > 0) {
            appendTime(result, hours).append(':');
        }

        appendTime(result, minutes).append(':');
        appendTime(result, seconds);
        return result.toString();
    }
}
