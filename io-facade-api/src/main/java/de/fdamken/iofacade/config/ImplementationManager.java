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

import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.util.Assertion;

/**
 * The implementation manager is used to handle all available implementations on
 * the classpath that are tagged with the annotation {@link Implementation}.
 *
 * <p>
 * It also handles the instantiation of any {@link FileSystem} class, which is
 * the main entry point for any implementation.
 * </p>
 *
 */
public class ImplementationManager {
    /**
     * The one and only instance of this class.
     *
     */
    private static final ImplementationManager INSTANCE = new ImplementationManager();

    /**
     * Constructor of ImplementationManager.
     *
     */
    private ImplementationManager() {
        // Nothing to do.
    }

    /**
     * Retrieves the {@link Implementation} annotation from the given class. If
     * the annotation is not present, an annotation is thrown.
     *
     * @param fileSystemClass
     *            The {@link FileSystem} class to retrieve the annotation from.
     * @return The {@link Implementation} annotation object.
     */
    public Implementation retrieveImplementation(final Class<? extends FileSystem> fileSystemClass) {
        Assertion.acquire(fileSystemClass).notNull().annotationPresent(Implementation.class);

        return fileSystemClass.getAnnotation(Implementation.class);
    }

    /**
     *
     * @return {@link #INSTANCE}.
     */
    public static ImplementationManager getInstance() {
        return ImplementationManager.INSTANCE;
    }
}
