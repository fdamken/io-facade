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

/**
 * Marks a path as deletable.
 *
 */
public interface Deletable {
    /**
     * Deletes this path.
     *
     * <p>
     * <b> NOTE: Deleting is recursive. </b>
     * </p>
     *
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If this path does not exist.
     */
    void delete() throws IOException, FileNotFoundException;

    /**
     * Deletes this path, if it exist. Otherwise, this does nothing.
     *
     * <p>
     * <b> NOTE: Deleting is recursive. </b>
     * </p>
     *
     * @throws IOException
     *             If any I/O error occurs.
     */
    void deleteIfExists() throws IOException;
}
