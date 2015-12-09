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
package de.fdamken.iofacade.config.config;

import de.fdamken.iofacade.config.config.annotation.Password;

/**
 * The password type is used to for password fields within configuration
 * mappings. It <b>must</b> be used as the return type if {@link Password} is
 * used.
 *
 */
public interface PasswordType {
    /**
     * Lookup <a href=
     * "http://www.codingeek.com/java/strings/why-to-use-char-array-instead-of-string-for-storing-password-in-java-security/"
     * >Why to use Char Array instead of String for storing password in Java -
     * Security</a> to learn why a <code>char[]</code> is returned instead of a
     * {@link String}.
     *
     * @return The decrypted password.
     */
    char[] getDecrypted();

    /**
     *
     * @return The encrypted password.
     */
    String getEncrypted();
}
