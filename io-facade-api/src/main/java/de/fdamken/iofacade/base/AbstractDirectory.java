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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fdamken.iofacade.Directory;
import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.util.PathFilter;

/**
 * Abstract implementation of {@link Directory}. This is used for inter-platform
 * file moving and copying.
 *
 * @param <B>
 *            The type of the wrapper base path.
 */
public abstract class AbstractDirectory<B extends Path> extends AbstractPath<B> implements Directory {
    /**
     * The logger.
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDirectory.class);

    /**
     * Constructor of AbstractDirectory.
     *
     * @param fileSystem
     *            The file system that is used to create instances of a path.
     * @param base
     *            The base path. A lots of methods will delegate to it.
     */
    public AbstractDirectory(final FileSystem fileSystem, final B base) {
        super(fileSystem, base);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Directory#listEntriesRecursive(de.fdamken.iofacade.util.PathFilter)
     */
    @Override
    public List<Path> listEntriesRecursive(final PathFilter filter) throws IOException, FileNotFoundException {
        if (!this.exists()) {
            throw new FileNotFoundException(this.toString());
        }

        return this.internalListEntriesRecursive(this.getBase(), filter);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.base.AbstractPath#internalCopy(de.fdamken.iofacade.Path)
     */
    @Override
    protected void internalCopy(final Path destination) throws IOException, FileNotFoundException, FileAlreadyExistsException {
        destination.asDirectory().create();
        for (final Path path : this.listEntries()) {
            path.copy(destination);
        }
    }

    /**
     * Internal method for recursive entry listing.
     *
     * @param rootPath
     *            The root path from which to search the file tree.
     * @param filter
     *            The {@link PathFilter} to apply.
     * @return Every entry that matches the given {@link PathFilter} in the file
     *         tree.
     */
    private List<Path> internalListEntriesRecursive(final Path rootPath, final PathFilter filter) {
        final List<Path> result = new ArrayList<Path>();

        final Boolean filterResult = filter.apply(rootPath);

        if (rootPath.isDirectory() && (filterResult == null || filterResult)) {
            try {
                rootPath.asDirectory().listEntries().forEach(result::add);
            } catch (final IOException ex) {
                AbstractDirectory.LOGGER.error("Failed to retrieve entries of directory " + rootPath + "!", ex);
            }
            result.forEach(path -> result.addAll(this.internalListEntriesRecursive(path, filter)));
        }

        if (filterResult != null && filterResult) {
            result.add(rootPath);
        }

        return result;
    }
}
