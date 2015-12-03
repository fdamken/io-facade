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

/**
 * This class is the basic access point for any I/O types (like basic Java IO).
 * It is used to acquire paths.
 *
 */
public interface FileSystem {
    /**
     * Creates a path for the given string representation.
     *
     * <p>
     * NOTE: Paths are always chrooted to any directory, by default the regular
     * file system root (or a FTP root). <br>
     * It is possible to chroot a file system via any configuration file. If it
     * is, every path is starting from that chrooted root.
     * </p>
     *
     * @param path
     *            The string representation of the path to acquire.
     * @return The {@link Path} object representing the given string.
     * @throws IOException
     *             If any I/O error occurs.
     */
    Path getPath(final String path) throws IOException;
}
