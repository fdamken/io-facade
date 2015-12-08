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

import de.fdamken.iofacade.Directory;
import de.fdamken.iofacade.File;
import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.exception.NoDirectoryIOFacadeRuntimeException;
import de.fdamken.iofacade.exception.NoFileIOFacadeRuntimeException;
import de.fdamken.iofacade.util.Assertion;

/**
 * Basic Java IO implementation of {@link Path}.
 *
 */
public class BasicPath implements Path {
    /**
     * The file system.
     *
     */
    private final FileSystem fileSystem;
    /**
     * The wrapped {@link java.nio.file.Path}.
     *
     */
    private final java.nio.file.Path path;

    /**
     * Constructor of BasicPath.
     *
     * @param fileSystem
     *            The file system.
     * @param path
     *            The {@link java.nio.file.Path} to wrap.
     */
    public BasicPath(final FileSystem fileSystem, final java.nio.file.Path path) {
        Assertion.acquire(fileSystem).named("fileSystem").notNull();
        Assertion.acquire(path).named("path").notNull();

        this.fileSystem = fileSystem;
        this.path = path;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Copyable#copy(de.fdamken.iofacade.Path,
     *      boolean)
     */
    @Override
    public void copy(final Path destination, final boolean overwrite) throws IOException, FileAlreadyExistsException {
        this.fileSystem.copy(this, destination, overwrite);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Deletable#delete()
     */
    @Override
    public void delete() throws IOException, FileNotFoundException {
        Assertion.acquire(this).exists();

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
     * @see de.fdamken.iofacade.property.Moveable#move(de.fdamken.iofacade.Path,
     *      boolean)
     */
    @Override
    public void move(final Path destination, final boolean overwrite) throws IOException, FileNotFoundException,
    FileAlreadyExistsException {
        this.fileSystem.move(this, destination, overwrite);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.FileSystemAware#getFileSystemClass()
     */
    @Override
    public Class<? extends FileSystem> getFileSystemClass() {
        return BasicFileSystem.class;
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
     * @see de.fdamken.iofacade.Path#asFile()
     */
    @Override
    public File asFile() throws NoFileIOFacadeRuntimeException {
        return new BasicFile(this.fileSystem, this.path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#asDirectory()
     */
    @Override
    public Directory asDirectory() throws NoDirectoryIOFacadeRuntimeException {
        return new BasicDirectory(this.fileSystem, this.path);
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
     * @return {@link #path}.
     */
    public java.nio.file.Path getPath() {
        return this.path;
    }
}
