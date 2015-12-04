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
package de.fdamken.iofacade.property;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import de.fdamken.iofacade.Path;

/**
 * Marks a path as moveable.
 *
 */
public interface Moveable {
    /**
     * Moves this path to the given path.
     *
     * @param destination
     *            The path to move this path to.
     * @param overwrite
     *            Whether to overwrite already existing files or not.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If this path does not exist.
     * @throws FileAlreadyExistsException
     *             If <code>overwrite</code> is <code>false</code> and the
     *             destination or any file within the destination is about to be
     *             overwritten.
     */
    void move(final Path destination, final boolean overwrite) throws IOException, FileNotFoundException,
            FileAlreadyExistsException;

    /**
     * Moves this path to the given path.
     *
     * @param destination
     *            The path to move this path to.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If this path does not exist.
     * @throws FileAlreadyExistsException
     *             If the destination or any file within the destination is
     *             about to be overwritten.
     */
    default void move(final Path destination) throws IOException, FileNotFoundException, FileAlreadyExistsException {
        this.move(destination, false);
    }
}
