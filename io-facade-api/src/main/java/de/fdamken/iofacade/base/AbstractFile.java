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
package de.fdamken.iofacade.base;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;

import de.fdamken.iofacade.File;
import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.Path;

/**
 * Abstract implementation of {@link File}. This is used for inter-platform file
 * moving and copying.
 *
 * @param <B>
 *            The type of the wrapper base path.
 */
public abstract class AbstractFile<B extends Path> extends AbstractPath<B> implements File {
    /**
     * Constructor of AbstractFile.
     *
     * @param fileSystem
     *            The file system that is used to create instances of a path.
     * @param base
     *            The base path. A lots of methods will delegate to it.
     */
    public AbstractFile(final FileSystem fileSystem, final B base) {
        super(fileSystem, base);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Copyable#copy(de.fdamken.iofacade.Path)
     */
    @Override
    protected void internalCopy(final Path destination) throws IOException, FileNotFoundException, FileAlreadyExistsException {
        final File file = destination.asFile();
        file.create();
        final InputStream in = new BufferedInputStream(this.openInputStream());
        final OutputStream out = new BufferedOutputStream(file.openOutputStream());
        int length = 0;
        final byte[] buffer = new byte[1024];
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.close();
    }
}
