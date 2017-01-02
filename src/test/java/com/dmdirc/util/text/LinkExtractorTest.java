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

package com.dmdirc.util.text;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinkExtractorTest {

    private LinkExtractor extractor;

    @Before
    public void setup() {
        extractor = new LinkExtractor();
    }

    private void testLinks(final CharSequence input, final Link ... expected) {
        final List<Link> result = extractor.findLinks(input);
        assertEquals(Arrays.asList(expected), result);
    }

    @Test
    public void testEmptyString() {
        testLinks("");
    }

    @Test
    public void testNoLinks() {
        testLinks("no links here!");
    }

    @Test
    public void testPlainWwwLink() {
        testLinks("www.google.com", new Link(0, 14, "www.google.com"));
    }

    @Test
    @Ignore("Not implemented yet")
    public void testPlainWwwLinkInSentence() {
        testLinks("word www.google.com word", new Link(5, 19, "www.google.com"));
        testLinks("word www.google.com, word", new Link(5, 19, "www.google.com"));
    }

    @Test
    @Ignore("Not implemented yet")
    public void testPlainWwwLinkWithPunctuation() {
        testLinks("www.google.com.", new Link(0, 14, "www.google.com"));
        testLinks("www.google.com!", new Link(0, 14, "www.google.com"));
        testLinks("www.google.com?", new Link(0, 14, "www.google.com"));
    }

    @Test
    @Ignore("Not implemented yet")
    public void testPlainWwwLinkWithBrackets() {
        testLinks("(www.google.com)", new Link(1, 15, "www.google.com"));
        testLinks("[www.google.com]", new Link(1, 15, "www.google.com"));
        testLinks("<www.google.com>", new Link(1, 15, "www.google.com"));
    }

    @Test
    @Ignore("Not implemented yet")
    public void testPlainWwwLinkWithComplicatedBrackets() {
        testLinks("(foo: www.google.com)", new Link(6, 21, "www.google.com"));
        testLinks("[foo: www.google.com]", new Link(6, 21, "www.google.com"));
        testLinks("<foo: www.google.com>", new Link(6, 21, "www.google.com"));

        testLinks("(foo: 'www.google.com')", new Link(7, 22, "www.google.com"));
        testLinks("('www.google.com')", new Link(2, 17, "www.google.com"));
        testLinks("[\"www.google.com\"]", new Link(2, 17, "www.google.com"));

        testLinks("www.example.com/blah(foobar)", new Link(0, 28, "www.example.com/blah(foobar)"));
    }

    @Test
    @Ignore("Not implemented yet")
    public void testPlainWwwLinkWithQuotes() {
        testLinks("\"www.google.com\"", new Link(1, 15, "www.google.com"));
        testLinks("'www.google.com'", new Link(1, 15, "www.google.com"));
    }

    @Test
    public void testPrefixedLinks() {
        testLinks("http://www.google.com", new Link(0, 21, "http://www.google.com"));
        testLinks("http://www.google.com:80/test#flub",
                new Link(0, 34, "http://www.google.com:80/test#flub"));

        testLinks("svn+ssh://foo:bar", new Link(0, 17, "svn+ssh://foo:bar"));
    }

    @Test
    @Ignore("Not implemented yet")
    public void testMultipleLinks() {
        testLinks("www.foo.com www.bar.com",
                new Link(0, 11, "www.foo.com"),
                new Link(12, 23, "www.bar.com"));

        testLinks("(www.foo.com www.bar.com)",
                new Link(1, 12, "www.foo.com"),
                new Link(13, 24, "www.bar.com"));

        testLinks("('www.foo.com')->x(\"www.bar.com\")",
                new Link(2, 13, "www.foo.com"),
                new Link(20, 31, "www.bar.com"));

        testLinks("\"foo\" www.foo.com \"www.bar.com\"",
                new Link(6, 17, "www.foo.com"),
                new Link(19, 30, "www.bar.com"));
    }

    @Test
    public void testIncompleteLinks() {
        testLinks("www...");
        testLinks("http://...");
        testLinks("/var/web/www.google.com/blah");
    }

}