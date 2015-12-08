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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;

import de.fdamken.iofacade.util.Assertion;

/**
 * An abstract implementation of {@link FileSystem} that shall be extended by
 * the implementation aware {@link FileSystem}s in order to provide
 * cross-platform file access.
 *
 */
public abstract class AbstractFileSystem implements FileSystem {
    /**
     * Copies the path <code>from</code> the the path <code>to</code>. This uses
     * the native copy operations of the concrete implementation.
     *
     * @param from
     *            The path to copy. Must exist.
     * @param to
     *            The destination. Must not exist.
     * @throws IOException
     *             If any I/O error occurs.
     */
    protected abstract void nativeCopy(final Path from, final Path to) throws IOException;

    /**
     * Moves the path <code>from</code> the the path <code>to</code>. This uses
     * the native move operations of the concrete implementation.
     *
     * @param from
     *            The path to move. Must exist.
     * @param to
     *            The destination. Must not exist.
     * @throws IOException
     *             If any I/O error occurs.
     */
    protected abstract void nativeMove(final Path from, final Path to) throws IOException;

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.FileSystem#copy(de.fdamken.iofacade.Path,
     *      de.fdamken.iofacade.Path, boolean)
     */
    @Override
    public void copy(final Path from, final Path to, final boolean overwrite) throws IOException, FileNotFoundException,
            FileAlreadyExistsException {
        Assertion.acquire(from).named("from").notNull().exists();
        Assertion.acquire(to).named("to").notNull();

        final Path dest;
        if (overwrite) {
            dest = to;
        } else {
            if (to.exists() && to.isDirectory()) {
                dest = this.integrate(to.asDirectory(), from);
            } else {
                dest = to;
            }
            Assertion.acquire(dest).named("dest").notExists();
        }
        dest.deleteIfExists();
        if (this.isSameImplementation(from, dest)) {
            this.nativeCopy(from, dest);
        } else {
            this.internalCopy(from, dest);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.FileSystem#move(de.fdamken.iofacade.Path,
     *      de.fdamken.iofacade.Path, boolean)
     */
    @Override
    public void move(final Path from, final Path to, final boolean overwrite) throws IOException, FileNotFoundException,
            FileAlreadyExistsException {
        Assertion.acquire(from).named("from").notNull().exists();
        Assertion.acquire(to).named("to").notNull();

        final Path dest;
        if (overwrite) {
            dest = to;
        } else {
            if (to.exists() && to.isDirectory()) {
                dest = this.integrate(to.asDirectory(), from);
            } else {
                dest = to;
            }
            Assertion.acquire(dest).named("dest").notExists();
        }
        dest.deleteIfExists();
        if (this.isSameImplementation(from, dest)) {
            this.nativeMove(from, dest);
        } else {
            this.internalCopy(from, dest);
            from.delete();
        }
    }

    /**
     * An internal method to copy files.
     *
     * @param from
     *            The path to copy from. Shall exist.
     * @param to
     *            The path to copy to. Shall not exist.
     * @throws IOException
     *             If any I/O error occurs.
     */
    protected void internalCopy(final Path from, final Path to) throws IOException {
        Assertion.acquire(from).named("from").notNull().exists();
        Assertion.acquire(to).named("to").notNull().notExists();

        if (from.isDirectory()) {
            to.asDirectory().create();
            for (final Path path : from.asDirectory().listEntries()) {
                path.copy(to);
            }
        } else if (from.isFile()) {
            final File file = to.asFile();
            file.create();
            final InputStream in = new BufferedInputStream(from.asFile().openInputStream());
            final OutputStream out = new BufferedOutputStream(file.openOutputStream());
            int length = 0;
            final byte[] buffer = new byte[1024];
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        } else {
            throw new IllegalArgumentException("From must be either a directory or a file!");
        }
    }

    /**
     * Checks whether the given {@link Path}s are in the same implementation.
     *
     * @param path0
     *            The first path.
     * @param path1
     *            The second path.
     * @return Whether the given to paths are in the same implementation.
     */
    private boolean isSameImplementation(final Path path0, final Path path1) {
        assert path0 != null : "path0 must not be null!";
        assert path1 != null : "path1 must not be null!";

        final String path0ImplId = path0.getImplementation().getId();
        final String path1ImplId = path1.getImplementation().getId();

        return path0ImplId.equals(path1ImplId);
    }
}
