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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Finds links within a body of text.
 */
public class LinkExtractor {

    /** Defines all characters treated as trailing punctuation that are illegal in URLs. */
    private static final String URL_PUNCT_ILLEGAL = "\"";
    /** Defines all characters treated as trailing punctuation that're legal in URLs. */
    private static final String URL_PUNCT_LEGAL = "';:!,\\.\\?";
    /** Defines all trailing punctuation. */
    private static final String URL_PUNCT = URL_PUNCT_ILLEGAL + URL_PUNCT_LEGAL;
    /** Defines all characters allowed in URLs that aren't treated as trailing punct. */
    private static final String URL_NOPUNCT = "a-z0-9$\\-_@&\\+\\*\\(\\)=/#%~\\|";
    /** Defines all characters allowed in URLs per W3C specs. */
    private static final String URL_CHARS = '[' + URL_PUNCT_LEGAL + URL_NOPUNCT
            + "]*[" + URL_NOPUNCT + "]+[" + URL_PUNCT_LEGAL + URL_NOPUNCT + "]*";
    /** The regular expression to use for marking up URLs. */
    private static final String URL_REGEXP = "(?i)((?>(?<!" + "[a-f0-9]{5})[a-f]|[g-z+])+://" +
            URL_CHARS + "|(?<![a-z0-9:/])www\\." + URL_CHARS + ')';
    /** The pattern to use to match URLs. */
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEXP);

    /**
     * Finds all available links within the given text.
     *
     * @param text The text to search for links.
     * @return A list of found links, ordered according to their position in the text.
     */
    public List<Link> findLinks(final CharSequence text) {
        final List<Link> res = new ArrayList<>();
        final Matcher matcher = URL_PATTERN.matcher(text);

        while (matcher.find()) {
            res.add(new Link(matcher.start(), matcher.end(), matcher.group()));
        }

        return res;
    }

}
