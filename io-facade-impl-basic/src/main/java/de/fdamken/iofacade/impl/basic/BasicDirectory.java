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
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import de.fdamken.iofacade.Directory;
import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.util.PathFilter;

/**
 * Basic Java IO implementation of {@link Directory}.
 *
 */
public class BasicDirectory extends BasicPath implements Directory {
    /**
     * Constructor of BasicDirectory.
     *
     * @param path
     *            The Java {@link java.nio.file.Path} to wrap.
     */
    public BasicDirectory(final java.nio.file.Path path) {
        super(path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Makeable#create()
     */
    @Override
    public void create() throws IOException, FileAlreadyExistsException {
        if (this.exists()) {
            throw new FileAlreadyExistsException("The path " + this.getPath() + " does already exist!");
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Directory#listEntries(de.fdamken.iofacade.util.PathFilter)
     */
    @Override
    public List<Path> listEntries(final PathFilter filter) throws IOException {
        final ConvertingPathList result = new ConvertingPathList();
        Files.list(this.getPath()).filter(path -> Objects.equals(filter.apply(new BasicFile(path)), true))
        .forEach(result::addPath);
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Directory#listEntries()
     */
    @Override
    public List<Path> listEntries() throws IOException {
        return this.listEntries(path -> true);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Directory#listEntriesRecursive(de.fdamken.iofacade.util.PathFilter)
     */
    @Override
    public List<Path> listEntriesRecursive(final PathFilter filter) throws IOException {
        final ConvertingPathList result = new ConvertingPathList();
        this.internalListEntriesRecursive(this.getPath(), filter).stream().forEach(result::addPath);
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Directory#listEntriesRecursive()
     */
    @Override
    public List<Path> listEntriesRecursive() throws IOException {
        return this.listEntriesRecursive(path -> true);
    }

    /**
     * Implementation of {@link #listEntriesRecursive(PathFilter)}.
     *
     * @param dir
     *            The directory to scan.
     * @param filter
     *            The {@link PathFilter} to apply.
     * @return The recursive entries.
     * @throws IOException
     *             If any I/O error occurs.
     */
    private Collection<java.nio.file.Path> internalListEntriesRecursive(final java.nio.file.Path dir, final PathFilter filter)
            throws IOException {
        final List<java.nio.file.Path> result = new CopyOnWriteArrayList<java.nio.file.Path>();

        if (Files.isDirectory(dir)) {
            Files.list(dir).forEach(result::add);
            for (final java.nio.file.Path path : result) {
                final Boolean pathFilterResult = filter.apply(new BasicPath(path));
                if (pathFilterResult == null || pathFilterResult) {
                    result.addAll(this.internalListEntriesRecursive(path, filter));
                }
            }
        }

        final Boolean dirFilterResult = filter.apply(new BasicPath(dir));
        if (dirFilterResult != null && dirFilterResult) {
            result.add(dir);
        }

        return result;
    }

    /**
     * This list is used to convert {@link java.nio.file.Path} objects to
     * {@link Path} objects on demand.
     *
     */
    private static class ConvertingPathList extends ArrayList<Path> {
        /**
         * The serial version UID.
         *
         */
        private static final long serialVersionUID = -3385351299995522885L;

        /**
         * Adds the given {@link java.nio.file.Path}.
         *
         * @param path
         *            The {@link java.nio.file.Path} to add.
         */
        public void addPath(final java.nio.file.Path path) {
            this.add(new BasicPath(path));
        }
    }
}
