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

import de.fdamken.iofacade.Path;
import de.fdamken.iofacade.SymbolicLink;

/**
 * This exception is thrown if a {@link Path} cannot be converted into a
 * {@link SymbolicLink} (because it is no symbolic link).
 *
 */
public class NoSymbolicLinkIOFacadeException extends NoXXXIOFacadeException {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = 1609499727023368508L;

    /**
     * Constructor of NoSymbolicLinkIOFacadeException.
     *
     * @param path
     *            The {@link Path} that was failed to convert into a symbolic
     *            link.
     */
    public NoSymbolicLinkIOFacadeException(final Path path) {
        super(path, "symbolic link");
    }
}
