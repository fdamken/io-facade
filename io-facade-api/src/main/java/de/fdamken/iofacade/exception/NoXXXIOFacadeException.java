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

/**
 * This is a base exception.
 *
 * <p>
 * <b> NOTE: You shall not create instances of this class directly! Rather use
 * any subclass. </b>
 * </p>
 *
 */
public abstract class NoXXXIOFacadeException extends IOFacadeException {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = 7804562800747882186L;

    /**
     * Constructor of NoXXXIOFacadeException.
     *
     * @param path
     *            The path that was failed to convert.
     * @param what
     *            The name of the path subclass the given path was failed to
     *            convert to.
     */
    public NoXXXIOFacadeException(final Path path, final String what) {
        super(path + " cannot be made to a " + what + " as it is no " + what + "!");
    }
}
