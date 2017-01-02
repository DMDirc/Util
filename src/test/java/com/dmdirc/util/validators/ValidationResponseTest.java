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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ValidationResponseTest {

    @Test
    public void testIsFailure_Reason() throws Exception {
        assertTrue(new ValidationResponse("Reason").isFailure());
    }

    @Test
    public void testIsFailure_EmptyReason() throws Exception {
        assertTrue(new ValidationResponse("").isFailure());
    }

    @Test
    public void testIsFailure_NoArgs() throws Exception {
        assertFalse(new ValidationResponse().isFailure());
    }

    @Test
    public void testIsFailure_Null() throws Exception {
        assertFalse(new ValidationResponse(null).isFailure());
    }

    @Test
    public void testGetFailureReason_Null() throws Exception {
        assertEquals(null, new ValidationResponse().getFailureReason());
    }

    @Test
    public void testGetFailureReason_Empty() throws Exception {
        assertEquals("", new ValidationResponse("").getFailureReason());
    }

    @Test
    public void testGetFailureReason_Args() throws Exception {
        assertEquals("RAR", new ValidationResponse("RAR").getFailureReason());
    }
}