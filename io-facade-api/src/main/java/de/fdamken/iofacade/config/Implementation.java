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
package de.fdamken.iofacade.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.config.config.FileSystemConfig;

/**
 * A {@link FileSystem} that is tagged with this annotation is detected by the
 * {@link ImplementationManager} as a valid implementation of the IO Facade.
 *
 * <p>
 * This annotation contains required information like an implementation ID.
 * </p>
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Implementation {
    /**
     *
     * @return The ID of this implementation. Must be unique.
     */
    String id();

    /**
     *
     * @return The name of this implementation.
     */
    String name();

    /**
     *
     * @return The class that contains the mappings for any configuration.
     */
    Class<? extends FileSystemConfig> config();
}
