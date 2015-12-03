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

import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.SymbolicLink;

/**
 * Basic Java IO implementation of {@link SymbolicLink}.
 *
 */
public class BasicSymbolicLink extends BasicPath implements SymbolicLink {
    /**
     * Constructor of BasicSymbolicLink.
     *
     * @param path
     *            The Java {@link java.nio.file.Path} to wrap.
     */
    public BasicSymbolicLink(final java.nio.file.Path path) {
        super(path);
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.SymbolicLink#readLink()
     */
    @Override
    public Path readLink() throws IOException {
        // TODO Auto-generated method body.
        return null;
    }
}
