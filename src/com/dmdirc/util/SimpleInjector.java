/*
 * Copyright (c) 2006-2011 DMDirc Developers
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

package com.dmdirc.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Facilitates simple injection of parameters into a constructor.
 */
public class SimpleInjector {

    /** The parent injector, if any. */
    private final SimpleInjector parent;

    /** A mapping of known classes to the objects that should be injected. */
    private final Map<Class<?>, Object> parameters
            = new HashMap<Class<?>, Object>();

    /**
     * Creates a new injector with no parent.
     */
    public SimpleInjector() {
        this(null);
    }

    /**
     * Creates a new injector which will inherit injection parameters from
     * the specified parent. Parent graphs must be acyclic.
     *
     * @param parent The injector to inherit parameters from.
     */
    public SimpleInjector(final SimpleInjector parent) {
        this.parent = parent;
    }

    /**
     * Adds a new injectable object to this injector. If a constructor requires
     * an instance of the specified class, it will be given the specified
     * object. If an association has already been made for the specified class,
     * it will be replaced with the new object.
     *
     * @param clazz The type of parameter which should be injected
     * @param object The value to inject for the parameter
     */
    public void addParameter(final Class<?> clazz, final Object object) {
        parameters.put(clazz, object);
    }

    /**
     * Adds the specified object as an injectable parameter for all of its
     * known classes and interface. This is equivalent to calling
     * {@link #addParameter(java.lang.Class, java.lang.Object)} for the object's
     * class, all superclasses, and all interfaces.
     *
     * @param object The object to be injected
     */
    public void addParameter(final Object object) {
        // Iterate the object hierarchy up
        Class<?> target = object.getClass();
        do {
            addParameter(target, object);
            target = target.getSuperclass();
        } while (target != null);

        // Add all interfaces
        for (Class<?> iface : object.getClass().getInterfaces()) {
            addParameter(iface, object);
        }
    }

    /**
     * Retrieves a mapping of known injectable types to their corresponding
     * values. If this injector was created with a parent, this mapping will
     * also include mappings defined in the parent.
     *
     * @return A map of injectable parameters
     */
    public Map<Class<?>, Object> getParameters() {
        final Map<Class<?>, Object> localParams
                = new HashMap<Class<?>, Object>(parameters.size());

        if (parent != null) {
            localParams.putAll(parent.getParameters());
        }

        // Note: insert local parameters after so they override parent params.
        localParams.putAll(parameters);

        return localParams;
    }

    /**
     * Attempts to create a new instance of the specified class by injecting
     * parameters into the constructor(s). If no constructors are found which
     * can be satisfied by the known parameters, or if all satisfiable
     * constructors throw exceptions, an IllegalArgumentException will be
     * thrown.
     * <p>
     * If multiple constructors are available that are satisfiable using the
     * known arguments, no guarantee is given as to which will be used.
     *
     * @param <T> The type of object being instantiated
     * @param clazz The class to create an instance of
     * @return An instance of the specified class
     * @throws IllegalArgumentException If the class could not be instantiated
     */
    @SuppressWarnings("unchecked")
    public <T> T createInstance(final Class<T> clazz) {
        final Map<Class<?>, Object> params = getParameters();
        Throwable throwable = null;

        for (Constructor<?> rawCtor : clazz.getConstructors()) {
            final Constructor<T> ctor = (Constructor<T>) rawCtor;
            final Object[] args = new Object[ctor.getParameterTypes().length];

            int i = 0;
            for (Class<?> paramType : ctor.getParameterTypes()) {
                if (params.containsKey(paramType)) {
                    args[i++] = params.get(paramType);
                } else {
                    break;
                }
            }

            if (i == args.length) {
                try {
                    return ctor.newInstance(args);
                } catch (IllegalAccessException ex) {
                    throwable = ex;
                } catch (IllegalArgumentException ex) {
                    throwable = ex;
                } catch (InstantiationException ex) {
                    throwable = ex;
                } catch (InvocationTargetException ex) {
                    throwable = ex;
                } catch (LinkageError err) {
                    throwable = err;
                }
            }
        }

        if (throwable == null) {
            throw new IllegalArgumentException("No declared constructors "
                    + "could be satisfied with known parameters");
        }

        throw new IllegalArgumentException("A satisfiable constructor was "
                + "found but threw an exception", throwable);
    }

}
