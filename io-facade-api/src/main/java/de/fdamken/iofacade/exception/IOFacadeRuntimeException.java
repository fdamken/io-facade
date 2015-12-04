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

/**
 * The base exception for the whole IO Facade project. Every runtime exception
 * in this project should expend this runtime exception.
 *
 * <p>
 * <b> NOTE: You shall not create an instance of this class directly! Rather use
 * any subclass. </b>
 * </p>
 *
 */
public abstract class IOFacadeRuntimeException extends RuntimeException {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = 1413505595163336797L;

    /**
     * Constructor of IOFacadeException.
     *
     * @param message
     *            A detailed error message.
     * @param cause
     *            The error causing this exception.
     */
    public IOFacadeRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor of IOFacadeException.
     *
     * @param message
     *            A detailed error message.
     */
    public IOFacadeRuntimeException(final String message) {
        super(message);
    }
}
