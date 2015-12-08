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
import java.nio.file.FileAlreadyExistsException;

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

    /**
     * Integrates the given path into the given directory. This creates a new
     * path that <i>may</i> exist.
     *
     * <p>
     * For example: <br>
     * If <code>directory</code> is <code>/tmp/example/dir</code> and
     * <code>path</code> is <code>/etc/some/directory</code>, a new path
     * <code>/tmp/example/dir/directory</code> is created.
     * </p>
     * <p>
     * Another example: <br>
     * If <code>directory</code> is <code>/usr/share</code> and
     * <code>path</code> is <code>/usr/bin/xrandr</code>, a new path
     * <code>/usr/share/xrandr</code> is created.
     * </p>
     *
     * <p>
     * <b> NOTE: It is not checked whether the resulting path exists or not.
     * </b>
     * </p>
     *
     * @param directory
     *            The directory to integrate the path in.
     * @param path
     *            The path to integrate.
     * @return The integrated path.
     * @throws IOException
     *             If any I/O error occurs.
     */
    Path integrate(final Directory directory, final Path path) throws IOException;

    /**
     * Copies the given path to the given destination.
     *
     * @param from
     *            The path to copy to path <code>to</code>.
     * @param to
     *            The path to copy the path <code>from</code> to.
     * @param overwrite
     *            Whether to overwrite already existing files or not.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If <code>from</code> does not exist.
     * @throws FileAlreadyExistsException
     *             If <code>overwrite</code> is <code>false</code> and the
     *             destination or any file within the destination is about to be
     *             overwritten.
     */
    void copy(final Path from, final Path to, final boolean overwrite) throws IOException, FileNotFoundException,
    FileAlreadyExistsException;

    /**
     * Copies the given path to the given destination. Does not overwrite.
     *
     * @param from
     *            The path to copy.
     * @param to
     *            The path to copy the path <code>from</code> to.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If <code>from</code> does not exist.
     * @throws FileAlreadyExistsException
     *             If <code>overwrite</code> is <code>false</code> and the
     *             destination or any file within the destination is about to be
     *             overwritten.
     */
    default void copy(final Path from, final Path to) throws IOException, FileNotFoundException, FileAlreadyExistsException {
        this.copy(from, to, false);
    }

    /**
     * Moves the given path to the given path.
     *
     * @param from
     *            The path to move.
     * @param to
     *            The path to move the path <code>from</code> to.
     * @param overwrite
     *            Whether to overwrite already existing files or not.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If <code>from</code> does not exist.
     * @throws FileAlreadyExistsException
     *             If <code>overwrite</code> is <code>false</code> and the
     *             destination or any file within the destination is about to be
     *             overwritten.
     */
    void move(final Path from, final Path to, final boolean overwrite) throws IOException, FileNotFoundException,
    FileAlreadyExistsException;

    /**
     * Moves the given path to the given path. Does not overwrite.
     *
     * @param from
     *            The path to move.
     * @param to
     *            The path to move the path <code>from</code> to.
     * @throws IOException
     *             If any I/O error occurs.
     * @throws FileNotFoundException
     *             If <code>from</code> does not exist.
     * @throws FileAlreadyExistsException
     *             If <code>overwrite</code> is <code>false</code> and the
     *             destination or any file within the destination is about to be
     *             overwritten.
     */
    default void move(final Path from, final Path to) throws IOException, FileNotFoundException, FileAlreadyExistsException {
        this.move(from, to, false);
    }
}
