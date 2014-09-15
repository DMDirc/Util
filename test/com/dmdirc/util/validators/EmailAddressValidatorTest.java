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
package com.dmdirc.util.validators;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class EmailAddressValidatorTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"email@example.com", true},
            {"firstname.lastname@example.com", true},
            {"email@subdomain.example.com", true},
            {"firstname+lastname@example.com", true},
            {"1234567890@example.com", true},
            {"email@example-one.com", true},
            {"_______@example.com", true},
            {"email@example.name", true},
            {"email@example.museum", true},
            {"email@example.co.jp", true},
            {"firstname-lastname@example.com", true},
            {"plainaddress", false},
            {"#@%^%#$@#$@#.com", false},
            {"@example.com", false},
            {"Foo Bar <email@example.com>", false},
            {"email.example.com", false},
            {"email@example@example.com", false},
            {"?????@example.com", false},
            {"email@example.com (Foo Bar)", false},
            {"email@example", false},});
    }

    private final String input;

    private final boolean expected;

    public EmailAddressValidatorTest(final String input, final boolean expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test() {
        final Validator<String> instance = new EmailAddressValidator();
        assertEquals(expected, !instance.validate(input).isFailure());
    }
}
