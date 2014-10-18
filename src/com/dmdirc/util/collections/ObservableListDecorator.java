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

import javax.annotation.Nonnull;

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
    @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
    public ObservableListDecorator(final List<T> list) {
        this.list = list;
    }

    @Override
    public void addListListener(final ListObserver listener) {
        listeners.add(ListObserver.class, listener);
    }

    @Override
    public void removeListListener(final ListObserver listener) {
        listeners.remove(ListObserver.class, listener);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return list.contains(o);
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Nonnull
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Nonnull
    @Override
    public <S> S[] toArray(@Nonnull final S[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(final T e) {
        list.add(e);

        listeners.getCallable(ListObserver.class).onItemsAdded(this,
                list.size() - 1, list.size() - 1);

        return true;
    }

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

    @Override
    public boolean containsAll(@Nonnull final Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(@Nonnull final Collection<? extends T> c) {
        if (list.addAll(c)) {
            listeners.getCallable(ListObserver.class).onItemsAdded(this,
                    list.size() - c.size(), list.size() - 1);
            return true;
        }

        return false;
    }

    @Override
    public boolean addAll(final int index, @Nonnull final Collection<? extends T> c) {
        if (list.addAll(index, c)) {
            listeners.getCallable(ListObserver.class).onItemsAdded(this,
                    index, index + c.size());
            return true;
        }

        return false;
    }

    @Override
    public boolean removeAll(@Nonnull final Collection<?> c) {
        final int length = list.size();

        if (list.removeAll(c)) {
            listeners.getCallable(ListObserver.class).onItemsChanged(this, 0,
                    length - 1);

            return true;
        }

        return false;
    }

    @Override
    public boolean retainAll(@Nonnull final Collection<?> c) {
        final int length = list.size();

        if (list.retainAll(c)) {
            listeners.getCallable(ListObserver.class).onItemsChanged(this, 0,
                    length - 1);

            return true;
        }

        return false;
    }

    @Override
    public void clear() {
        final int length = list.size();

        list.clear();

        if (length > 0) {
            listeners.getCallable(ListObserver.class).onItemsRemoved(this, 0,
                    length - 1);
        }
    }

    @Override
    public T get(final int index) {
        return list.get(index);
    }

    @Override
    public T set(final int index, final T element) {
        final T res = list.set(index, element);

        listeners.getCallable(ListObserver.class).onItemsChanged(this, index, index);

        return res;
    }

    @Override
    public void add(final int index, final T element) {
        list.add(index, element);

        listeners.getCallable(ListObserver.class).onItemsAdded(this, index, index);
    }

    @Override
    public T remove(final int index) {
        final T res = list.remove(index);

        listeners.getCallable(ListObserver.class).onItemsRemoved(this, index, index);

        return res;
    }

    @Override
    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return list.lastIndexOf(o);
    }

    @Nonnull
    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Nonnull
    @Override
    public ListIterator<T> listIterator(final int index) {
        return list.listIterator(index);
    }

    @Nonnull
    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

}
