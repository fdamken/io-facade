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
package de.fdamken.iofacade.impl.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;

import de.fdamken.iofacade.File;

/**
 * Basic Java IO implementation of {@link File}.
 *
 */
public class BasicFile extends BasicPath implements File {
    /**
     * Constructor of BasicFile.
     *
     * @param path
     *            The Java {@link java.nio.file.Path} to wrap.
     */
    public BasicFile(final java.nio.file.Path path) {
        super(path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Makeable#create()
     */
    @Override
    public void create() throws IOException, FileAlreadyExistsException {
        // TODO Auto-generated method body.

    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.File#openInputStream()
     */
    @Override
    public InputStream openInputStream() throws IOException {
        // TODO Auto-generated method body.
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.File#openOutputStream()
     */
    @Override
    public OutputStream openOutputStream() throws IOException {
        // TODO Auto-generated method body.
        return null;
    }
}
