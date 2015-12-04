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
package de.fdamken.iofacade.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import de.fdamken.iofacade.Directory;
import de.fdamken.iofacade.File;
import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.SymbolicLink;
import de.fdamken.iofacade.exception.NoDirectoryIOFacadeRuntimeException;
import de.fdamken.iofacade.exception.NoFileIOFacadeRuntimeException;
import de.fdamken.iofacade.exception.NoSymbolicLinkIOFacadeRuntimeException;

/**
 * Abstract implementation of {@link Path}. This is used for inter-platform file
 * moving and copying.
 *
 * @param <B>
 *            The type of the wrapper base path.
 */
public abstract class AbstractPath<B extends Path> implements Path {
    /**
     * The file system that is used to create instances of a path.
     *
     */
    private final FileSystem fileSystem;

    /**
     * The base path. A lots of methods will delegate to it.
     *
     */
    private final B base;

    /**
     * Constructor of AbstractPath.
     *
     * @param fileSystem
     *            The file system that is used to create instances of a path.
     * @param base
     *            The base path. A lots of methods will delegate to it.
     */
    public AbstractPath(final FileSystem fileSystem, final B base) {
        this.fileSystem = fileSystem;
        this.base = base;
    }

    /**
     * Invokes a call to the native copy functionality of the file system. It is
     * used when no inter-platform I/O is done.
     *
     * <p>
     * Directories are copied recursively!
     * </p>
     * <p>
     * The given file must <b>not</b> exist!
     * </p>
     *
     * @param destination
     *            The destination to copy this path to.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If this path does not exist.
     */
    protected abstract void nativeCopy(final Path destination) throws IOException, FileNotFoundException;

    /**
     * Invokes a call to the native move functionality of the file system. It is
     * used when no inter-platform I/O is done.
     *
     * <p>
     * Directories are copied recursively!
     * </p>
     * <p>
     * The given file must <b>not</b> exist!
     * </p>
     *
     * @param destination
     *            The destination to move this path to.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If this path does not exist.
     */
    protected abstract void nativeMove(final Path destination) throws IOException, FileNotFoundException;

    /**
     * Copies this path to the given path.
     *
     * <p>
     * Directories are copied recursively!
     * </p>
     * <p>
     * The given file must <b>not</b> exist!
     * </p>
     *
     * @param destination
     *            The path to copy this path to. Must <b>not</b> exist.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If this file does not exist.
     */
    protected abstract void internalCopy(final Path destination) throws IOException;

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Copyable#copy(de.fdamken.iofacade.Path,
     *      boolean)
     */
    @Override
    public void copy(final Path destination, final boolean overwrite) throws IOException, FileNotFoundException,
    FileAlreadyExistsException {
        if (!this.exists()) {
            throw new FileNotFoundException(this.toString());
        }

        final Path dest;
        if (overwrite) {
            dest = destination;
        } else {
            if (destination.exists() && destination.isDirectory()) {
                dest = this.fileSystem.integrate(destination.asDirectory(), this);
            } else {
                dest = destination;
            }
            if (dest.exists()) {
                throw new FileAlreadyExistsException(dest.toString());
            }
        }
        dest.deleteIfExists();
        if (this.isSameImplementation(dest)) {
            this.nativeCopy(dest);
        } else {
            this.internalCopy(dest);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Moveable#move(de.fdamken.iofacade.Path,
     *      boolean)
     */
    @Override
    public void move(final Path destination, final boolean overwrite) throws IOException, FileNotFoundException,
            FileAlreadyExistsException {
        if (!this.exists()) {
            throw new FileNotFoundException(this.toString());
        }

        final Path dest;
        if (overwrite) {
            dest = destination;
        } else {
            if (destination.exists() && destination.isDirectory()) {
                dest = this.fileSystem.integrate(destination.asDirectory(), this);
            } else {
                dest = destination;
            }
            if (dest.exists()) {
                throw new FileAlreadyExistsException(dest.toString());
            }
        }
        dest.deleteIfExists();
        if (this.isSameImplementation(dest)) {
            this.nativeMove(dest);
        } else {
            this.internalCopy(dest);
            this.delete();
        }
    }

    /**
     * Tests whether the given path is the same implementation as this path.
     *
     * @param path
     *            The path to test.
     * @return Whether this path is contained by the same implementation as the
     *         given path.
     */
    private boolean isSameImplementation(final Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null!");
        }

        return this.getClass().isInstance(path);
    }

    /**
     *
     * @return {@link #fileSystem}.
     */
    public FileSystem getFileSystem() {
        return this.fileSystem;
    }

    /**
     *
     * @return {@link #base}.
     */
    public B getBase() {
        return this.base;
    }

    // Delegate methods.
    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Deletable#delete()
     */
    @Override
    public void delete() throws IOException, FileNotFoundException {
        this.base.delete();
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Existable#exists()
     */
    @Override
    public boolean exists() {
        return this.base.exists();
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#isFile()
     */
    @Override
    public boolean isFile() {
        return this.base.isFile();
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#isDirectory()
     */
    @Override
    public boolean isDirectory() {
        return this.base.isDirectory();
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#isSymbolicLink()
     */
    @Override
    public boolean isSymbolicLink() {
        return this.base.isSymbolicLink();
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#asFile()
     */
    @Override
    public File asFile() throws NoFileIOFacadeRuntimeException {
        return this.base.asFile();
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#asDirectory()
     */
    @Override
    public Directory asDirectory() throws NoDirectoryIOFacadeRuntimeException {
        return this.base.asDirectory();
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#asSymbolicLink()
     */
    @Override
    public SymbolicLink asSymbolicLink() throws NoSymbolicLinkIOFacadeRuntimeException {
        return this.base.asSymbolicLink();
    }
}
