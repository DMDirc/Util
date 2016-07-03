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

package com.dmdirc.util.validators;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ColourValidatorTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                        {"-1", false},
                        {"0", true},
                        {"1", true},
                        {"2", true},
                        {"2", true},
                        {"3", true},
                        {"4", true},
                        {"5", true},
                        {"6", true},
                        {"7", true},
                        {"8", true},
                        {"9", true},
                        {"10", true},
                        {"11", true},
                        {"12", true},
                        {"13", true},
                        {"14", true},
                        {"15", true},
                        {"16", false},
                        {"000000", true},
                        {"ffffff", true},
                        {"0000000", false},
                        {"gggggg", false},
                }
        );
    }

    private final String input;

    private final boolean expected;

    public ColourValidatorTest(final String input, final boolean expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test() {
        final Validator<String> instance = new ColourValidator();
        assertEquals(expected, !instance.validate(input).isFailure());
    }
}