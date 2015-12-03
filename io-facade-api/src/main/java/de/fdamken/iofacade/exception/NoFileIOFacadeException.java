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
package de.fdamken.iofacade.exception;

import de.fdamken.iofacade.File;
import de.fdamken.iofacade.Path;

/**
 * This exception is thrown if a {@link Path} cannot be converted into a
 * {@link File} (because it is no file).
 *
 */
public class NoFileIOFacadeException extends NoXXXIOFacadeException {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = 126372141867354758L;

    /**
     * Constructor of NoDirectoryIOFacadeException.
     *
     * @param path
     *            The {@link Path} that was failed to convert into a file.
     */
    public NoFileIOFacadeException(final Path path) {
        super(path, "file");
    }
}
