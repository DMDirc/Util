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

package com.dmdirc.util.text;

import java.util.Objects;

/**
* Describes a single link found within some text.
*/
public class Link {

    private final int start;
    private final int end;
    private final String content;

    public Link(final int start, final int end, final String content) {
        this.start = start;
        this.end = end;
        this.content = content;
    }

    /**
     * Gets the character offset that the link was found at.
     *
     * @return The position the link was found at
     */
    public int getStart() {
        return start;
    }

    /**
     * Gets the character offset of the end of the link.
     *
     * @return The position immediately after the last character of the link.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Gets the content of the link.
     *
     * @return The link's content
     */
    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof Link) {
            final Link link = (Link) o;
            return end == link.getEnd()
                    && start == link.getStart()
                    && Objects.equals(content, link.getContent());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, content);
    }

    @Override
    public String toString() {
        return "Link{start=" + start + ", end=" + end + ", content='" + content + "'}";
    }
}
