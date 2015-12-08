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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import de.fdamken.iofacade.File;
import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.util.Assertion;

/**
 * Basic Java IO implementation of {@link File}.
 *
 */
public class BasicFile extends BasicPath implements File {
    /**
     * Constructor of BasicFile.
     *
     * @param fileSystem
     *            The file system.
     * @param path
     *            The {@link java.nio.file.Path} to wrap.
     */
    public BasicFile(final FileSystem fileSystem, final java.nio.file.Path path) {
        super(fileSystem, path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Makeable#create()
     */
    @Override
    public void create() throws IOException, FileAlreadyExistsException {
        Assertion.acquire(this).notExists();

        Files.createFile(this.getPath());
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.File#openInputStream()
     */
    @Override
    public InputStream openInputStream() throws IOException, FileNotFoundException {
        Assertion.acquire(this).exists();

        return Files.newInputStream(this.getPath());
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.File#openOutputStream()
     */
    @Override
    public OutputStream openOutputStream() throws IOException, FileNotFoundException {
        Assertion.acquire(this).exists();

        return Files.newOutputStream(this.getPath());
    }
}
