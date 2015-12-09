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

import de.fdamken.iofacade.util.Assertion;

/**
 * An implementation of {@link PasswordType}.
 *
 */
public class PasswordTypeImpl implements PasswordType {
    /**
     * The encrypted password.
     *
     */
    private final String encryptedPassword;
    /**
     * The path to the secret key to decrypt the encrypted password.
     *
     */
    // TODO: Use this value for decryption.
    @SuppressWarnings("unused")
    private final String secretKey;

    /**
     * Constructor of PasswordTypeImpl.
     *
     * @param encryptedPassword
     *            The encrypted password.
     * @param secretKey
     *            The path to the secret key to decrypt the encrypted password.
     */
    public PasswordTypeImpl(final String encryptedPassword, final String secretKey) {
        Assertion.acquire(encryptedPassword).named("encryptedPassword").notNull();
        Assertion.acquire(secretKey).named("secretKey").notNull();

        this.encryptedPassword = encryptedPassword;
        this.secretKey = secretKey;
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.config.config.PasswordType#getDecrypted()
     */
    @Override
    public char[] getDecrypted() {
        // TODO: Implement decryption.
        return this.encryptedPassword.toCharArray();
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.config.config.PasswordType#getEncrypted()
     */
    @Override
    public String getEncrypted() {
        return this.encryptedPassword;
    }
}
