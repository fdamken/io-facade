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
import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.base.AbstractFile;

/**
 * Basic Java IO implementation of {@link File}.
 *
 */
public class BasicFile extends AbstractFile<BasicPath> {
    /**
     * Constructor of BasicFile.
     *
     * @param path
     *            The base path.
     */
    public BasicFile(final BasicPath path) {
        super(path.getFileSystem(), path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.File#openInputStream()
     */
    @Override
    public InputStream openInputStream() throws IOException, FileNotFoundException {
        return Files.newInputStream(this.getBase().getPath());
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.File#openOutputStream()
     */
    @Override
    public OutputStream openOutputStream() throws IOException, FileNotFoundException {
        return Files.newOutputStream(this.getBase().getPath());
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.property.Makeable#create()
     */
    @Override
    public void create() throws IOException, FileAlreadyExistsException {
        Files.createFile(this.getBase().getPath());
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.base.AbstractPath#nativeCopy(de.fdamken.iofacade.Path)
     */
    @Override
    protected void nativeCopy(final Path destination) throws IOException, FileNotFoundException, FileAlreadyExistsException {
        this.getBase().copy(destination);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.base.AbstractPath#nativeMove(de.fdamken.iofacade.Path)
     */
    @Override
    protected void nativeMove(final Path destination) throws IOException, FileNotFoundException, FileAlreadyExistsException {
        this.getBase().move(destination);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.base.AbstractFile#internalCopy(de.fdamken.iofacade.Path)
     */
    @Override
    protected void internalCopy(final Path destination) throws IOException, FileNotFoundException, FileAlreadyExistsException {
        super.internalCopy(destination);
    }
}
