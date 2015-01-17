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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class IntegerValidatorTest {

    private IntegerValidator instance;

    @Before
    public void setUp() throws Exception {
        instance = new IntegerValidator(5, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstruction() throws Exception {
        instance = new IntegerValidator(10, 5);
    }

    @Test
    public void testPlaceholderMinimum() throws Exception {
        instance = new IntegerValidator(-1, 10);
        assertEquals(Integer.MIN_VALUE, instance.getMin());
    }

    @Test
    public void testPlaceholderMaximum() throws Exception {
        instance = new IntegerValidator(5, -1);
        assertEquals(Integer.MAX_VALUE, instance.getMax());
    }

    @Test
    public void testGetMax() throws Exception {
        assertEquals(5, instance.getMin());
    }

    @Test
    public void testGetMin() throws Exception {
        assertEquals(10, instance.getMax());

    }

    @Test
    public void testValidate_Less_Reason() throws Exception {
        assertEquals("Must be at least 5", instance.validate(4).getFailureReason());
    }

    @Test
    public void testValidate_LessThanMin() throws Exception {
        assertTrue(instance.validate(4).isFailure());
    }

    @Test
    public void testValidate_Min() throws Exception {
        assertFalse(instance.validate(5).isFailure());
    }

    @Test
    public void testValidate_Max() throws Exception {
        assertFalse(instance.validate(10).isFailure());
    }

    @Test
    public void testValidate_MoreThanMax() throws Exception {
        assertTrue(instance.validate(11).isFailure());
    }

    @Test
    public void testValidate_Max_Reason() throws Exception {
        assertEquals("Must be at most 10", instance.validate(11).getFailureReason());
    }

    @Test
    public void testValidate_Normal() throws Exception {
        assertFalse(instance.validate(6).isFailure());
    }
}