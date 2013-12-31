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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A LinkedHashSet with a Queue implementation added, also supports readding
 * unique items to the head of the queue.
 *
 * @param <E> the type of elements held in this collection
 */
public class QueuedLinkedHashSet<E> extends LinkedHashSet<E>
        implements Queue<E> {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 1;

    /** {@inheritDoc} */
    @Override
    public boolean offer(final E e) {
        return add(e);
    }

    /** {@inheritDoc} */
    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        return poll();
    }

    /** {@inheritDoc} */
    @Override
    public E poll() {
        final E object = peek();
        if (object != null) {
            remove(object);
        }
        return object;
    }

    /** {@inheritDoc} */
    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }
        return peek();
    }

    /** {@inheritDoc} */
    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        final Iterator<E> iterator = iterator();
        E object = null;
        while (iterator.hasNext()) {
            object = iterator.next();
        }
        return object;
    }

    /**
     * Offers an item to this queue, if the item exists in the queue it removes
     * it and re-adds it to the queue.
     *
     * @param e Object to add
     *
     * @return true iif the item was added
     */
    public boolean offerAndMove(final E e) {
        if (contains(e)) {
            remove(e);
        }
        return offer(e);
    }
}
