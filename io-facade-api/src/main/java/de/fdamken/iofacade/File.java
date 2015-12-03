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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.fdamken.iofacade.property.Makeable;

/**
 * Represents a file within a file system.
 *
 */
public interface File extends Path, Makeable {
    /**
     * Opens a stream in order to read from this file.
     *
     * @return The opened stream.
     * @throws IOException
     *             If any I/O error occurs.
     */
    InputStream openInputStream() throws IOException;

    /**
     * Opens a stream in order to write to this file.
     *
     * @return The opened stream.
     * @throws IOException
     *             If any I/O error occurs.
     */
    OutputStream openOutputStream() throws IOException;
}
