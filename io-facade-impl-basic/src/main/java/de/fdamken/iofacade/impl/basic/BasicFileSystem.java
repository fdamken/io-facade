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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import de.fdamken.iofacade.AbstractFileSystem;
import de.fdamken.iofacade.Directory;
import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.config.Implementation;
import de.fdamken.iofacade.util.Assertion;

/**
 * Basic Java IO implementation of {@link FileSystem}.
 *
 */
@Implementation(id = "basic-java-io",
name = "Basic Java IO",
config = BasicFileSystemConfig.class)
public class BasicFileSystem extends AbstractFileSystem {
    /**
     * The configuration that was used to create this instance.
     *
     */
    private final BasicFileSystemConfig config;

    /**
     * Constructor of BasicFileSystem.
     *
     * @param config
     *            The configuration that was used to create this instance.
     */
    public BasicFileSystem(final BasicFileSystemConfig config) {
        this.config = config;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.FileSystem#getPath(java.lang.String)
     */
    @Override
    public Path getPath(final String path) throws IOException {
        return new BasicPath(this, Paths.get(this.config.getRoot(), this.splitPath(path)));
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.FileSystem#integrate(de.fdamken.iofacade.Directory,
     *      de.fdamken.iofacade.Path)
     */
    @Override
    public Path integrate(final Directory directory, final Path path) throws IOException {
        Assertion.acquire(directory).named("directory").notNull();
        Assertion.acquire(path).named("path").notNull();

        final BasicPath dir = this.asBasicPath(directory);
        final BasicPath file = this.asBasicPath(path);

        final java.nio.file.Path dirPath = dir.getPath();
        final java.nio.file.Path filePath = file.getPath();

        final java.nio.file.Path resultPath = Paths.get(dirPath.toString(), Objects.toString(filePath.getFileName()));

        return new BasicPath(this, resultPath);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.AbstractFileSystem#nativeCopy(de.fdamken.iofacade.Path,
     *      de.fdamken.iofacade.Path)
     */
    @Override
    protected void nativeCopy(final Path from, final Path to) throws IOException {
        Files.copy(this.asBasicPath(from).getPath(), this.asBasicPath(to).getPath());
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.AbstractFileSystem#nativeMove(de.fdamken.iofacade.Path,
     *      de.fdamken.iofacade.Path)
     */
    @Override
    protected void nativeMove(final Path from, final Path to) throws IOException {
        Files.move(this.asBasicPath(from).getPath(), this.asBasicPath(to).getPath());
    }

    /**
     * Checks whether the given {@link Path} is a {@link BasicPath} and converts
     * it, if it is.
     *
     * @param path
     *            The {@link Path} to convert.
     * @return The converted {@link BasicPath}.
     * @throws IllegalArgumentException
     *             If the given {@link Path} is not a {@link BasicPath}.
     */
    private BasicPath asBasicPath(final Path path) throws IllegalArgumentException {
        if (path instanceof BasicPath) {
            return (BasicPath) path;
        }
        throw new IllegalArgumentException("Path is no " + BasicPath.class.getCanonicalName() + "!");
    }

    /**
     *
     * @return {@link #config}.
     */
    public BasicFileSystemConfig getConfig() {
        return this.config;
    }
}
