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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class IntegerPortValidatorTest {

    @Test
    public void testValidate_TooLow() throws Exception {
        assertTrue(new IntegerPortValidator().validate(0).isFailure());
    }

    @Test
    public void testValidate_Min() throws Exception {
        assertFalse(new IntegerPortValidator().validate(1).isFailure());
    }

    @Test
    public void testValidate_Normal() throws Exception {
        assertFalse(new IntegerPortValidator().validate(5).isFailure());
    }

    @Test
    public void testValidate_Max() throws Exception {
        assertFalse(new IntegerPortValidator().validate(65535).isFailure());
    }

    @Test
    public void testValidate_TooHigh() throws Exception {
        assertTrue(new IntegerPortValidator().validate(65536).isFailure());
    }

}