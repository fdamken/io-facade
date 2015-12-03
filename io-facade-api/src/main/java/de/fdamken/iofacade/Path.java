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

import de.fdamken.iofacade.exception.NoDirectoryIOFacadeException;
import de.fdamken.iofacade.exception.NoFileIOFacadeException;
import de.fdamken.iofacade.exception.NoSymbolicLinkIOFacadeException;
import de.fdamken.iofacade.property.Copyable;
import de.fdamken.iofacade.property.Deletable;
import de.fdamken.iofacade.property.Moveable;

/**
 * A path represents a basic instance on a file system. It can be a file, for
 * example.
 *
 */
public interface Path extends Copyable, Deletable, Moveable {
    /**
     *
     * @return Whether this file exists.
     */
    boolean exists();

    /**
     *
     * @return Whether this path is a file.
     */
    boolean isFile();

    /**
     *
     * @return Whether this path is a directory.
     */
    boolean isDirectory();

    /**
     *
     * @return Whether this path is a symbolic link.
     */
    boolean isSymbolicLink();

    /**
     * Converts this path into a {@link File}.
     *
     * @return The created {@link File}.
     * @throws NoFileIOFacadeException
     *             If this path is not a file.
     */
    File asFile() throws NoFileIOFacadeException;

    /**
     * Converts this path into a {@link Directory}.
     *
     * @return The created {@link Directory}.
     * @throws NoDirectoryIOFacadeException
     *             If this path is not a directory.
     */
    Directory asDirectory() throws NoDirectoryIOFacadeException;

    /**
     * Converts this path into a {@link SymbolicLink}.
     *
     * @return The created {@link SymbolicLink}.
     * @throws NoSymbolicLinkIOFacadeException
     *             If this path is not a symbolic link.
     */
    SymbolicLink asSymbolicLink() throws NoSymbolicLinkIOFacadeException;
}
