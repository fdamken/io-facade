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
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import de.fdamken.iofacade.Directory;
import de.fdamken.iofacade.File;
import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.SymbolicLink;
import de.fdamken.iofacade.exception.NoDirectoryIOFacadeException;
import de.fdamken.iofacade.exception.NoFileIOFacadeException;
import de.fdamken.iofacade.exception.NoSymbolicLinkIOFacadeException;

/**
 * Basic Java IO implementation of {@link Path}.
 *
 */
public class BasicPath implements Path {
    /**
     * The wrapped Java {@link java.nio.file.Path}.
     *
     */
    private final java.nio.file.Path path;

    /**
     * Constructor of BasicPath.
     *
     * @param path
     *            The java {@link java.nio.file.Path} to wrap.
     */
    public BasicPath(final java.nio.file.Path path) {
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
        final CopyOption[] copyOptions = new CopyOption[overwrite ? 1 : 0];
        if (overwrite) {
            copyOptions[0] = StandardCopyOption.REPLACE_EXISTING;
        }
        Files.copy(this.path, this.asBasicPath(destination).getPath(), copyOptions);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Copyable#copy(de.fdamken.iofacade.Path)
     */
    @Override
    public void copy(final Path destination) throws IOException, FileAlreadyExistsException {
        this.copy(destination, false);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Deletable#delete()
     */
    @Override
    public void delete() throws IOException, FileNotFoundException {
        if (!this.exists()) {
            throw new FileNotFoundException("The file " + this.path + " does not exist!");
        }

        Files.walkFileTree(this.path, new FileVisitor<java.nio.file.Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final java.nio.file.Path dir, final BasicFileAttributes attrs)
                    throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final java.nio.file.Path file, final BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(final java.nio.file.Path file, final IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(final java.nio.file.Path dir, final IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Deletable#deleteIfExists()
     */
    @Override
    public void deleteIfExists() throws IOException {
        if (this.exists()) {
            this.delete();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Moveable#move(de.fdamken.iofacade.Path,
     *      boolean)
     */
    @Override
    public void move(final Path destination, final boolean overwrite) throws IOException, FileAlreadyExistsException {
        final CopyOption[] copyOptions = new CopyOption[overwrite ? 1 : 0];
        if (overwrite) {
            copyOptions[0] = StandardCopyOption.REPLACE_EXISTING;
        }
        Files.move(this.path, this.asBasicPath(destination).getPath(), copyOptions);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Moveable#move(de.fdamken.iofacade.Path)
     */
    @Override
    public void move(final Path destination) throws IOException, FileAlreadyExistsException {
        this.move(destination, false);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#exists()
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
        return Files.isRegularFile(this.path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#isDirectory()
     */
    @Override
    public boolean isDirectory() {
        return Files.isDirectory(this.path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#isSymbolicLink()
     */
    @Override
    public boolean isSymbolicLink() {
        return Files.isSymbolicLink(this.path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#asFile()
     */
    @Override
    public File asFile() throws NoFileIOFacadeException {
        if (!this.isFile()) {
            throw new NoFileIOFacadeException(this);
        }
        return new BasicFile(this.path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#asDirectory()
     */
    @Override
    public Directory asDirectory() throws NoDirectoryIOFacadeException {
        if (!this.isDirectory()) {
            throw new NoDirectoryIOFacadeException(this);
        }
        return new BasicDirectory(this.path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Path#asSymbolicLink()
     */
    @Override
    public SymbolicLink asSymbolicLink() throws NoSymbolicLinkIOFacadeException {
        if (!this.isSymbolicLink()) {
            throw new NoSymbolicLinkIOFacadeException(this);
        }
        return new BasicSymbolicLink(this.path);
    }

    /**
     * Converts (casts) the given {@link Path} to a {@link BasicPath}.
     *
     * @param path
     *            The {@link Path} to convert.
     * @return The converted path.
     * @throws IllegalArgumentException
     *             If The path is not a {@link BasicPath}.
     */
    protected BasicPath asBasicPath(final Path path) throws IllegalArgumentException {
        if (path instanceof BasicPath) {
            return (BasicPath) path;
        }
        throw new IllegalArgumentException("Path " + path + " is not an instance of " + BasicPath.class.getName() + "!");
    }

    /**
     *
     * @return {@link #path}.
     */
    protected java.nio.file.Path getPath() {
        return this.path;
    }
}
