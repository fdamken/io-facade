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

import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Collection;

import de.fdamken.iofacade.Directory;
import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.util.PathFilter;

/**
 * Basic Java IO implementation of {@link Directory}.
 *
 */
public class BasicDirectory extends BasicPath implements Directory {
    /**
     * Constructor of BasicDirectory.
     *
     * @param path
     *            The Java {@link java.nio.file.Path} to wrap.
     */
    public BasicDirectory(final java.nio.file.Path path) {
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
     * @see de.fdamken.iofacade.Directory#listEntries(de.fdamken.iofacade.util.PathFilter)
     */
    @Override
    public Collection<Path> listEntries(final PathFilter filter) throws IOException {
        // TODO Auto-generated method body.
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Directory#listEntries()
     */
    @Override
    public Collection<Path> listEntries() throws IOException {
        // TODO Auto-generated method body.
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Directory#listEntriesRecursive(java.io.FileFilter)
     */
    @Override
    public Collection<Path> listEntriesRecursive(final FileFilter filter) throws IOException {
        // TODO Auto-generated method body.
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Directory#listEntriesRecursive()
     */
    @Override
    public Collection<Path> listEntriesRecursive() throws IOException {
        // TODO Auto-generated method body.
        return null;
    }
}
