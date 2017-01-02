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

package com.dmdirc.util.collections;

import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class CollectionFunctionsTest {

    @Test
    public void testflattenAndEncodeKeyPair() throws Exception {
        final Map<String, String> map = ImmutableMap.<String, String>builder()
                .put("key", "value")
                .put("key&", "value&")
                .build();
        final List<String> result = map.entrySet().stream()
                .flatMap(CollectionFunctions::flattenAndEncodeKeyPair)
                .collect(Collectors.toList());
        assertEquals(2, result.size());
        assertEquals("key=value", result.get(0));
        assertEquals("key%26=value%26", result.get(1));
    }
}