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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Decorates a {@link List} to add observable functionality.
 *
 * @param <T> The type of object the list contains
 */
public class ObservableListDecorator<T> implements ObservableList<T> {

    /** The list being decorated. */
    private final List<T> list;

    /** The listeners for this list. */
    private final ListenerList listeners = new ListenerList();

    /**
     * Creates a new {@link ObservableListDecorator} which will decorate the
     * given list.
     *
     * @param list The list to be decorated
     */
    public ObservableListDecorator(final List<T> list) {
        this.list = list;
    }

    /** {@inheritDoc} */
    @Override
    public void addListListener(final ListObserver listener) {
        listeners.add(ListObserver.class, listener);
    }

    /** {@inheritDoc} */
    @Override
    public void removeListListener(final ListObserver listener) {
        listeners.remove(ListObserver.class, listener);
    }

    /** {@inheritDoc} */
    @Override
    public int size() {
        return list.size();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Object o) {
        return list.contains(o);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    /** {@inheritDoc} */
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    /** {@inheritDoc} */
    @Override
    public <T> T[] toArray(final T[] a) {
        return list.toArray(a);
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(final T e) {
        list.add(e);

        listeners.getCallable(ListObserver.class).onItemsAdded(this,
                list.size() - 1, list.size() - 1);

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(final Object o) {
        final int index = list.indexOf(o);

        if (list.remove(o)) {
            listeners.getCallable(ListObserver.class).onItemsRemoved(this,
                    index, index);

            return true;
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsAll(final Collection<?> c) {
        return list.containsAll(c);
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final Collection<? extends T> c) {
        if (list.addAll(c)) {
            listeners.getCallable(ListObserver.class).onItemsAdded(this,
                    list.size() - c.size(), list.size() - 1);
            return true;
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        if (list.addAll(index, c)) {
            listeners.getCallable(ListObserver.class).onItemsAdded(this,
                    index, index + c.size());
            return true;
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeAll(final Collection<?> c) {
        final int length = list.size();

        if (list.removeAll(c)) {
            listeners.getCallable(ListObserver.class).onItemsChanged(this, 0,
                    length - 1);

            return true;
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean retainAll(final Collection<?> c) {
        final int length = list.size();

        if (list.retainAll(c)) {
            listeners.getCallable(ListObserver.class).onItemsChanged(this, 0,
                    length - 1);

            return true;
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void clear() {
        final int length = list.size();

        list.clear();

        if (length > 0) {
            listeners.getCallable(ListObserver.class).onItemsRemoved(this, 0,
                    length - 1);
        }
    }

    /** {@inheritDoc} */
    @Override
    public T get(final int index) {
        return list.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public T set(final int index, final T element) {
        final T res = list.set(index, element);

        listeners.getCallable(ListObserver.class).onItemsChanged(this, index, index);

        return res;
    }

    /** {@inheritDoc} */
    @Override
    public void add(final int index, final T element) {
        list.add(index, element);

        listeners.getCallable(ListObserver.class).onItemsAdded(this, index, index);
    }

    /** {@inheritDoc} */
    @Override
    public T remove(final int index) {
        final T res = list.remove(index);

        listeners.getCallable(ListObserver.class).onItemsRemoved(this, index, index);

        return res;
    }

    /** {@inheritDoc} */
    @Override
    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    /** {@inheritDoc} */
    @Override
    public int lastIndexOf(final Object o) {
        return list.lastIndexOf(o);
    }

    /** {@inheritDoc} */
    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    /** {@inheritDoc} */
    @Override
    public ListIterator<T> listIterator(final int index) {
        return list.listIterator(index);
    }

    /** {@inheritDoc} */
    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

}
