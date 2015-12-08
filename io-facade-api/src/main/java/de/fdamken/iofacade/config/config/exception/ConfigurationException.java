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
package de.fdamken.iofacade.config.config.exception;

import de.fdamken.iofacade.exception.IOFacadeException;

/**
 * This is the base exception for all exceptions relating to configuration
 * problems.
 *
 */
public class ConfigurationException extends IOFacadeException {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = 6037628766363894942L;

    /**
     * Constructor of ConfigurationException.
     *
     * @param message
     *            A detailed error message.
     * @param cause
     *            The error causing this exception.
     */
    public ConfigurationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor of ConfigurationException.
     *
     * @param message
     *            A detailed error message.
     */
    public ConfigurationException(final String message) {
        super(message);
    }
}
