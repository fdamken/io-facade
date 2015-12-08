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
package de.fdamken.iofacade.config.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.fdamken.iofacade.config.config.FileSystemConfig;
import de.fdamken.iofacade.config.config.PasswordType;

/**
 * This annotation is used to define properties as password properties within a
 * {@link FileSystemConfig}.
 *
 * <p>
 * Passwords are stored encrypted within the configuration file and it is
 * required to set the private key in order to use them! Without a secret key,
 * the usage is not possible.
 * </p>
 * <p>
 * <b> NOTE: If you use this annotation, the return type must be
 * {@link PasswordType} in order to provide correct encryption/decryption. </b>
 * </p>
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    /**
     *
     * @return The location of the secret key. This may be a reference to
     *         another property. References are identified by using a hash sign
     *         (i.e. #another-property).
     */
    String value();
}
