/* This file is part of IO Facade.
 *
 * Copyright (C) 2015  Fabian Damken
 *
 * IO Facade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * IO Facade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with IO Facade.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fdamken.iofacade.util;

import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.nio.file.FileAlreadyExistsException;
import java.util.function.Function;

import de.fdamken.iofacade.property.Existable;

/**
 * An assertion is used to provide simple parameter-checking.
 *
 * <p>
 * For example, if you want to check that <code>obj</code> is not
 * <code>null</code>: <code>
 * <pre>
 * public void foo(Object obj) {
 *     {@link Assertion}.{@link Assertion#acquire(Object) acquire(obj)}.{@link Assertion#named(String) named("obj")}.{@link Assertion#notNull() notNull()};
 * }
 * </pre>
 * </code> If <code>obj</code> is <code>null</code>, in
 * {@link IllegalArgumentException} with the message
 * <code>"obj shall not be null!"</code> is thrown.
 * </p>
 *
 */
public final class Assertion {
    /**
     * The object to check.
     *
     */
    private Object obj;
    /**
     * The name that is used within exception messages to identify the object
     * that is being checked (i.e. a parameter name).
     *
     */
    private String name;

    /**
     * Constructor of Assertion.
     *
     * @param obj
     *            The object that is being checked.
     */
    private Assertion(final Object obj) {
        this.obj = obj;
    }

    /**
     * Constructs a new {@link Assertion} object.
     *
     * @param obj
     *            The object that is being checked.
     * @return The created {@link Assertion} object.
     */
    public static Assertion acquire(final Object obj) {
        return new Assertion(obj).named(null);
    }

    /**
     * Names this assertion in order to use the name within exception messages.
     *
     * <p>
     * If no name is set, <code>"obj"</code> will be used.
     * </p>
     *
     * @param name
     *            The name to set. If <code>null</code>, <code>"obj"</code> is
     *            being used instead.
     * @return <code>this</code>
     */
    public Assertion named(final String name) {
        this.name = name == null ? "obj" : name;
        return this;
    }

    /**
     * Asserts that the object is not <code>null</code>.
     *
     * @return <code>this</code>
     * @throws IllegalArgumentException
     *             If the object is <code>null</code>.
     */
    public Assertion notNull() throws IllegalArgumentException {
        if (this.obj == null) {
            throw new IllegalArgumentException(this.getName() + " shall not be null!");
        }
        return this;
    }

    /**
     * Asserts that the object exist (file operation).
     *
     * <p>
     * <b> NOTE: This does only work if the object is an instance of
     * {@link Existable}. </b>
     * </p>
     *
     * @return <code>this</code>
     * @throws FileNotFoundException
     *             If the object does not exist.
     */
    public Assertion exists() throws FileNotFoundException {
        if (!(this.obj instanceof Existable)) {
            throw new IllegalStateException("Obj cannot exist ( no instance of " + Existable.class.getCanonicalName() + ")!");
        }
        if (!((Existable) this.obj).exists()) {
            throw new FileNotFoundException(this.getName() + ": " + this.obj.toString());
        }
        return this;
    }

    /**
     * Asserts that the object does not exist (file operation).
     *
     * <p>
     * <b> NOTE: This does only work if the object is an instance of
     * {@link Existable}. </b>
     * </p>
     *
     * @return <code>this</code>
     * @throws FileAlreadyExistsException
     *             If the object exists.
     */
    public Assertion notExists() throws FileAlreadyExistsException {
        if (!(this.obj instanceof Existable)) {
            throw new IllegalStateException("Obj cannot exist (no instance of " + Existable.class.getCanonicalName() + ")!");
        }
        if (((Existable) this.obj).exists()) {
            throw new FileAlreadyExistsException(this.getName() + ": " + this.obj.toString());
        }
        return this;
    }

    /**
     * Tests whether the given annotation is present on the current object.
     *
     * @param annotationClass
     *            The annotation to test for.
     * @return <code>this</code>
     * @throws IllegalArgumentException
     *             If the annotation is not present.
     */
    public Assertion annotationPresent(final Class<? extends Annotation> annotationClass) throws IllegalArgumentException {
        if (this.obj instanceof Class) {
            if (!((Class<?>) this.obj).isAnnotationPresent(annotationClass)) {
                throw new IllegalArgumentException(this.getName() + " does not have the annotation "
                        + annotationClass.getCanonicalName() + "!");
            }
        } else {
            if (!this.obj.getClass().isAnnotationPresent(annotationClass)) {
                throw new IllegalArgumentException(this.getName() + " does not have the annotation "
                        + annotationClass.getCanonicalName() + "!");
            }
        }
        return this;
    }

    /**
     * Tests whether the toString() method obj the object returns a string that
     * starts with the given string.
     *
     * @param str
     *            The string that the toString()-string should start with.
     * @return <code>this</code>
     * @throws IllegalArgumentException
     *             If the toString()-string does not start with the given
     *             string.
     */
    public Assertion startsWith(final String str) throws IllegalArgumentException {
        if (!this.obj.toString().startsWith(str)) {
            throw new IllegalArgumentException(this.getName() + " does not start with " + str + "!");
        }
        return this;
    }

    /**
     * Tests whether the object is an interface.
     *
     * <p>
     * <b> NOTE: This does only work if the object is an instance of
     * {@link Class}. </b>
     * </p>
     *
     * @return <code>this</code>
     * @throws IllegalArgumentException
     *             If the object is not an interface.
     */
    public Assertion isInterface() throws IllegalArgumentException {
        if (this.obj instanceof Class) {
            if (!((Class<?>) this.obj).isInterface()) {
                throw new IllegalArgumentException(this.getName() + " is not an interface!");
            }
        } else {
            throw new IllegalStateException("Obj cannot be an interface (no instance of " + Class.class.getCanonicalName() + ")!");
        }
        return this;
    }

    /**
     * Tests whether the object is an instance of the given class.
     *
     * @param clazz
     *            The class to check against.
     * @return <code>this</code>
     * @throws IllegalArgumentException
     *             If the object is not an instance of the given class.
     */
    public Assertion isInstanceOf(final Class<?> clazz) throws IllegalArgumentException {
        if (!clazz.isInstance(this.obj)) {
            throw new IllegalArgumentException(this.getName() + " is not an instance of " + clazz.getCanonicalName() + "!");
        }
        return this;
    }

    /**
     * Converts the stored value into a string.
     *
     * @param <T>
     *            The type of the stored data. This must be consistent with the
     *            type of the stored object!
     * @param func
     *            The function to apply to convert the stored object into a
     *            string.
     * @return <code>this</code>
     */
    @SuppressWarnings("unchecked")
    public <T> Assertion asString(final Function<T, String> func) {
        this.obj = func.apply((T) this.obj);
        return this;
    }

    /**
     *
     * @return The name doe use in exceptions. Normally {@link #obj}. If
     *         {@link #obj} is <code>null</code>, <code>"obj"</code> is
     *         returned.
     */
    private String getName() {
        return this.name == null ? "obj" : this.name;
    }
}
