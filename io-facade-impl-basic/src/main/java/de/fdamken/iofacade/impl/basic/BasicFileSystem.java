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
import java.nio.file.Paths;

import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.Path;

/**
 * Basic Java IO implementation of {@link FileSystem}.
 *
 */
public class BasicFileSystem implements FileSystem {
    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.FileSystem#getPath(java.lang.String)
     */
    @Override
    public Path getPath(final String path) throws IOException {
        return new BasicPath(Paths.get(path));
    }
}
