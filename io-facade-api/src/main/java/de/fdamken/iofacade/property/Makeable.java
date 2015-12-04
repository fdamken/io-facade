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

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

/**
 * Marks a path as makeable (this path can be created).
 *
 */
public interface Makeable extends Existable {
    /**
     * Creates this path.
     *
     * <p>
     * It depends on the implementation whether a file, a directory, a link or
     * anything else is created.
     * </p>
     *
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileAlreadyExistsException
     *             If this path does already exist.
     */
    void create() throws IOException, FileAlreadyExistsException;

    /**
     * Creates this path.
     *
     * <p>
     * It depends on the implementation whether a file, a directory, a link or
     * anything else is created.
     * </p>
     *
     * @throws IOException
     *             If any I/O error occurs.
     */
    default void createIfNotExists() throws IOException {
        if (!this.exists()) {
            this.create();
        }
    }
}
