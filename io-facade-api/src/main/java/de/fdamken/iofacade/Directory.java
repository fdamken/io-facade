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
package de.fdamken.iofacade;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import de.fdamken.iofacade.property.Makeable;
import de.fdamken.iofacade.util.PathFilter;

/**
 * Represents a directory within a file system.
 *
 */
public interface Directory extends Path, Makeable {
    /**
     * Lists all entries within this directory that are matching the given path
     * filter.
     *
     * @param filter
     *            The {@link PathFilter} that is used to filter the results.
     * @return All entries within this directory that are matching the given
     *         {@link PathFilter}.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If this path does not exist.
     */
    List<Path> listEntries(final PathFilter filter) throws IOException, FileNotFoundException;

    /**
     * Lists all entries within this directory.
     *
     * @return All entries within this directory.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If this path does not exist.
     */
    default List<Path> listEntries() throws IOException, FileNotFoundException {
        return this.listEntries(path -> true);
    }

    /**
     * Lists all entries within the file tree, starting from this directory that
     * are matching the given file filter.
     *
     * @param filter
     *            The {@link PathFilter} that is used to filter the results.
     * @return All entries within the file tree, starting from this directory,
     *         that are matching the given {@link PathFilter}.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If this path does not exist.
     */
    List<Path> listEntriesRecursive(final PathFilter filter) throws IOException, FileNotFoundException;

    /**
     * Lists all entries within the file tree, starting from this directory.
     *
     * @return All entries within the file tree, starting from this directory.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If this path does not exist.
     */
    default List<Path> listEntriesRecursive() throws IOException, FileNotFoundException {
        return this.listEntriesRecursive(path -> true);
    }
}
