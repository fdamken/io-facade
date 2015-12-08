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

import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.Implementation;

/**
 * Basic Java IO implementation of {@link Implementation}.
 *
 */
public class BasicImplementation implements Implementation {
    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Implementation#getId()
     */
    @Override
    public String getId() {
        return "basic-java-io";
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Implementation#getName()
     */
    @Override
    public String getName() {
        return "Basic Java IO";
    }

    /**
     * {@inheritDoc}
     *
     * @see de.fdamken.iofacade.Implementation#getFileSystem()
     */
    @Override
    public Class<? extends FileSystem> getFileSystem() {
        return BasicFileSystem.class;
    }
}
