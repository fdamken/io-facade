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
package de.fdamken.iofacade.impl.basic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.base.AbstractPath;
import de.fdamken.iofacade.exception.NoDirectoryIOFacadeRuntimeException;
import de.fdamken.iofacade.exception.NoFileIOFacadeRuntimeException;
import de.fdamken.iofacade.exception.NoSymbolicLinkIOFacadeRuntimeException;

/**
 * Basic Java IO implementation of {@link Path}.
 *
 */
public class BasicPath extends AbstractPath<Path> {
    /**
     * The wrapped {@link java.nio.file.Path}.
     *
     */
    private final java.nio.file.Path path;

    /**
     * Constructor of BasicPath.
     *
     * @param fileSystem
     *            The file system that is used to create instances of a path.
     * @param path
     *            The {@link java.nio.file.Path} to wrap.
     */
    public BasicPath(final FileSystem fileSystem, final java.nio.file.Path path) {
        super(fileSystem, null);

        this.path = path;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Deletable#delete()
     */
    @Override
    public void delete() throws IOException, FileNotFoundException {
        if (!this.exists()) {
            throw new FileNotFoundException(this.toString());
        }

        Files.delete(this.path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Existable#exists()
     */
    @Override
    public boolean exists() {
        return Files.exists(this.path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#isFile()
     */
    @Override
    public boolean isFile() {
        return !this.exists() || Files.isRegularFile(this.path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#isDirectory()
     */
    @Override
    public boolean isDirectory() {
        return !this.exists() || Files.isDirectory(this.path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#isSymbolicLink()
     */
    @Override
    public boolean isSymbolicLink() {
        return !this.exists() || Files.isSymbolicLink(this.path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#asFile()
     */
    @Override
    public BasicFile asFile() throws NoFileIOFacadeRuntimeException {
        if (!this.isFile()) {
            throw new NoFileIOFacadeRuntimeException(this);
        }
        return new BasicFile(this);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#asDirectory()
     */
    @Override
    public BasicDirectory asDirectory() throws NoDirectoryIOFacadeRuntimeException {
        if (!this.isDirectory()) {
            throw new NoDirectoryIOFacadeRuntimeException(this);
        }
        return new BasicDirectory(this);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#asSymbolicLink()
     */
    @Override
    public BasicSymbolicLink asSymbolicLink() throws NoSymbolicLinkIOFacadeRuntimeException {
        if (!this.isSymbolicLink()) {
            throw new NoSymbolicLinkIOFacadeRuntimeException(this);
        }
        return new BasicSymbolicLink(this);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.base.AbstractPath#nativeCopy(de.fdamken.iofacade.Path)
     */
    @Override
    protected void nativeCopy(final Path destination) throws IOException, FileNotFoundException, FileAlreadyExistsException {
        Files.copy(this.path, this.asBasicPath(destination).getPath());
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.base.AbstractPath#nativeMove(de.fdamken.iofacade.Path)
     */
    @Override
    protected void nativeMove(final Path destination) throws IOException, FileNotFoundException, FileAlreadyExistsException {
        Files.move(this.path, this.asBasicPath(destination).getPath());
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.base.AbstractPath#internalCopy(de.fdamken.iofacade.Path)
     */
    @Override
    protected void internalCopy(final Path destination) throws IOException {
        if (this.isFile()) {
            this.asFile().internalCopy(destination);
        } else if (this.isDirectory()) {
            this.asDirectory().internalCopy(destination);
        } else if (this.isSymbolicLink()) {
            this.asSymbolicLink().internalCopy(destination);
        } else {
            throw new IllegalArgumentException("Destination must be either a file, a directory or a symbolic link!");
        }
    }

    /**
     *
     * @return {@link #path}.
     */
    public java.nio.file.Path getPath() {
        return this.path;
    }

    /**
     * Converts the given path to a {@link BasicPath}.
     *
     * @param path
     *            The path to convert.
     * @return The converted {@link BasicPath}.
     * @throws IllegalArgumentException
     *             If the given path is not an instance of {@link BasicPath}.
     */
    protected BasicPath asBasicPath(final Path path) throws IllegalArgumentException {
        if (path instanceof BasicPath) {
            return (BasicPath) path;
        }
        throw new IllegalArgumentException("Path is not an instance of " + BasicPath.class.getSimpleName() + "!");
    }
}
