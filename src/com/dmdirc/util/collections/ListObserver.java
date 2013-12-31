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

package com.dmdirc.util.collections;

/**
 * Interface for objects interested in being updated about changes to an
 * {@link ObservableList}.
 */
public interface ListObserver {

    /**
     * Called when one or more items is added to an observed list.
     *
     * @param source The source of the event
     * @param startIndex The index of the first item that was added
     * @param endIndex The index of the last item that was added
     */
    void onItemsAdded(Object source, int startIndex, int endIndex);

    /**
     * Called when one or more items is removed from an observed list.
     *
     * @param source The source of the event
     * @param startIndex The index of the first item that was removed
     * @param endIndex The index of the last item that was removed
     */
    void onItemsRemoved(Object source, int startIndex, int endIndex);

    /**
     * Called when one or more items is changed within an observed list.
     *
     * @param source The source of the event
     * @param startIndex The index of the first item that was changed
     * @param endIndex The index of the last item that was changed
     */
    void onItemsChanged(Object source, int startIndex, int endIndex);
}
