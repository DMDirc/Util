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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

/**
 * Implements a list of weak references. The weak references (and subsequent
 * garbage collection) are handled transparently.
 *
 * @param <T> The type of object that this list will contain.
 */
public class WeakList<T> implements List<T> {

    /** The items in this list. */
    private final List<WeakReference<T>> list = new ArrayList<>();

    /**
     * Removes any entries from the list that have been GC'd.
     */
    private void cleanUp() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get() == null) {
                list.remove(i--);
            }
        }
    }

    /**
     * Dereferences the specified list of WeakReferences to get a plain List.
     *
     * @param list The list to be dereferenced
     * @return A list containing the items referenced by the specified list
     */
    private List<T> dereferenceList(final List<WeakReference<T>> list) {
        return list.stream().filter(item -> item.get() != null)
                .map(WeakReference::get)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new collection of weak references to elements in the specified
     * collection.
     *
     * @param c The collection whose elements should be referenced
     * @return A copy of the specified collection, with each item wrapped in
     * a weak reference.
     */
    @SuppressWarnings("unchecked")
    private Collection<WeakReference<T>> referenceCollection(
            final Collection<?> c) {
        final Collection<WeakReference<T>> res = new ArrayList<>();

        for (Object item : c) {
            res.add(new EquatableWeakReference(item));
        }

        return res;
    }

    @Override
    public int size() {
        cleanUp();

        return list.size();
    }

    @Override
    public boolean isEmpty() {
        cleanUp();

        return list.isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(final Object o) {
        return list.contains(new EquatableWeakReference(o));
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return dereferenceList(list).iterator();
    }

    @Nonnull
    @Override
    public Object[] toArray() {
        return dereferenceList(list).toArray();
    }

    @Nonnull
    @Override
    public <S> S[] toArray(@Nonnull final S[] a) {
        return dereferenceList(list).toArray(a);
    }

    @Override
    public boolean add(final T e) {
        return list.add(new EquatableWeakReference<>(e));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(final Object o) {
        return list.remove(new EquatableWeakReference(o));
    }

    @Override
    public boolean containsAll(@Nonnull final Collection<?> c) {
        return dereferenceList(list).containsAll(c);
    }

    @Override
    public boolean addAll(@Nonnull final Collection<? extends T> c) {
        return list.addAll(referenceCollection(c));
    }

    @Override
    public boolean addAll(final int index, @Nonnull final Collection<? extends T> c) {
        return list.addAll(index, referenceCollection(c));
    }

    @Override
    public boolean removeAll(@Nonnull final Collection<?> c) {
        return list.removeAll(referenceCollection(c));
    }

    @Override
    public boolean retainAll(@Nonnull final Collection<?> c) {
        return list.retainAll(referenceCollection(c));
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public T get(final int index) {
        cleanUp();

        return list.get(index).get();
    }

    @Override
    public T set(final int index, final T element) {
        list.set(index, new EquatableWeakReference<>(element));

        return element;
    }

    @Override
    public void add(final int index, final T element) {
        list.add(index, new EquatableWeakReference<>(element));
    }

    @Override
    public T remove(final int index) {
        return list.remove(index).get();
    }

    @Override
    public int indexOf(final Object o) {
        cleanUp();

        return dereferenceList(list).indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        cleanUp();

        return dereferenceList(list).lastIndexOf(o);
    }

    @Nonnull
    @Override
    public ListIterator<T> listIterator() {
        cleanUp();

        return dereferenceList(list).listIterator();
    }

    @Nonnull
    @Override
    public ListIterator<T> listIterator(final int index) {
        cleanUp();

        return dereferenceList(list).listIterator(index);
    }

    @Nonnull
    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return dereferenceList(list.subList(fromIndex, toIndex));
    }
}
