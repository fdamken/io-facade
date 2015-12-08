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

import de.fdamken.iofacade.exception.NoDirectoryIOFacadeRuntimeException;
import de.fdamken.iofacade.exception.NoFileIOFacadeRuntimeException;
import de.fdamken.iofacade.property.Copyable;
import de.fdamken.iofacade.property.Deletable;
import de.fdamken.iofacade.property.Existable;
import de.fdamken.iofacade.property.FileSystemAware;
import de.fdamken.iofacade.property.Moveable;

/**
 * A path represents a basic instance on a file system. It can be a file, for
 * example.
 *
 */
public interface Path extends Copyable, Deletable, Existable, Moveable, FileSystemAware {
    /**
     *
     * @return Whether this path is a file. If this path does not exists, this
     *         returns <code>true</code>.
     */
    boolean isFile();

    /**
     *
     * @return Whether this path is a directory. If this path does not exists,
     *         this returns <code>true</code>.
     */
    boolean isDirectory();

    /**
     * Converts this path into a {@link File}.
     *
     * <p>
     * If this path does not exist, this returns a file object that points to a
     * non existing file.
     * </p>
     *
     * @return The created {@link File}.
     * @throws NoFileIOFacadeRuntimeException
     *             If this path is not a file.
     */
    File asFile() throws NoFileIOFacadeRuntimeException;

    /**
     * Converts this path into a {@link Directory}.
     *
     * <p>
     * If this path does not exist, this returns a directory object that points
     * to a non existing directory.
     * </p>
     *
     * @return The created {@link Directory}.
     * @throws NoDirectoryIOFacadeRuntimeException
     *             If this path is not a directory.
     */
    Directory asDirectory() throws NoDirectoryIOFacadeRuntimeException;
}
