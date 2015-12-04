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
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.SymbolicLink;
import de.fdamken.iofacade.base.AbstractSymbolicLink;

/**
 * Basic Java IO implementation of {@link SymbolicLink}.
 *
 */
public class BasicSymbolicLink extends AbstractSymbolicLink<BasicPath> {
    /**
     * Constructor of BasicSymbolicLink.
     *
     * @param base
     *            The base path.
     */
    public BasicSymbolicLink(final BasicPath base) {
        super(base.getFileSystem(), base);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.SymbolicLink#readLink()
     */
    @Override
    public Path readLink() throws IOException {
        return new BasicPath(this.getFileSystem(), Files.readSymbolicLink(this.getBase().getPath()));
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.base.AbstractPath#nativeCopy(de.fdamken.iofacade.Path)
     */
    @Override
    protected void nativeCopy(final Path destination) throws IOException, FileNotFoundException, FileAlreadyExistsException {
        this.getBase().nativeCopy(destination);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.base.AbstractPath#nativeMove(de.fdamken.iofacade.Path)
     */
    @Override
    protected void nativeMove(final Path destination) throws IOException, FileNotFoundException, FileAlreadyExistsException {
        this.getBase().nativeMove(destination);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.base.AbstractSymbolicLink#internalCopy(de.fdamken.iofacade.Path)
     */
    @Override
    protected void internalCopy(final Path destination) throws IOException {
        super.internalCopy(destination);
    }
}
