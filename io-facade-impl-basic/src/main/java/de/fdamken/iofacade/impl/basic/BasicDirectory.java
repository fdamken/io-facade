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
import java.util.List;
import java.util.Objects;

import de.fdamken.iofacade.Directory;
import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.util.Assertion;
import de.fdamken.iofacade.util.DirectoryUtil;
import de.fdamken.iofacade.util.PathFilter;

/**
 * Basic Java IO implementation of {@link Directory}.
 *
 */
public class BasicDirectory extends BasicPath implements Directory {
    /**
     * Constructor of BasicDirectory.
     *
     * @param fileSystem
     *            The file system.
     * @param path
     *            The {@link java.nio.file.Path} to wrap.
     */
    public BasicDirectory(final FileSystem fileSystem, final java.nio.file.Path path) {
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
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Directory#listEntries(de.fdamken.iofacade.util.PathFilter)
     */
    @Override
    public List<Path> listEntries(final PathFilter filter) throws IOException, FileNotFoundException {
        final PathConvertingArrayList result = new PathConvertingArrayList(this.getFileSystem());
        Files.list(this.getPath()).filter(path -> Objects.equals(filter.apply(new BasicPath(this.getFileSystem(), path)), true))
                .forEach(result::addPath);
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Directory#listEntriesRecursive(de.fdamken.iofacade.util.PathFilter)
     */
    @Override
    public List<Path> listEntriesRecursive(final PathFilter filter) throws IOException, FileNotFoundException {
        return DirectoryUtil.listEntriesRecursive(this, filter);
    }
}
