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

import java.io.IOException;

import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.SymbolicLink;

/**
 * Abstract implementation of {@link SymbolicLink}. This is used for
 * inter-platform file moving and copying.
 *
 * @param <B>
 *            The type of the wrapper base path.
 */
public abstract class AbstractSymbolicLink<B extends Path> extends AbstractPath<B> implements SymbolicLink {
    /**
     * Constructor of AbstractSymbolicLink.
     *
     * @param fileSystem
     *            The file system that is used to create instances of a path.
     * @param base
     *            The base path. A lots of methods will delegate to it.
     */
    public AbstractSymbolicLink(final FileSystem fileSystem, final B base) {
        super(fileSystem, base);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.base.AbstractPath#internalCopy(de.fdamken.iofacade.Path)
     */
    @Override
    protected void internalCopy(final Path destination) throws IOException {
        this.nativeCopy(destination);
    }
}
