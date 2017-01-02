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

package com.dmdirc.util.validators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class OptionalEmailAddressValidatorTest {

    @Test
    public void testValidate_Null() throws Exception {
        assertFalse(new OptionalEmailAddressValidator().validate(null).isFailure());
    }

    @Test
    public void testValidate_Empty() throws Exception {
        assertFalse(new OptionalEmailAddressValidator().validate("").isFailure());
    }

    @Test
    public void testValidate_Valid() throws Exception {
        assertFalse(new OptionalEmailAddressValidator().validate("test@test.com").isFailure());
    }

    @Test
    public void testValidate_Invalid() throws Exception {
        assertTrue(new OptionalEmailAddressValidator().validate("email@example@example.com")
                .isFailure());
    }
}