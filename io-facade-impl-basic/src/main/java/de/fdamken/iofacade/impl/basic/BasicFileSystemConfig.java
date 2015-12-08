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

import de.fdamken.iofacade.config.config.FileSystemConfig;
import de.fdamken.iofacade.config.config.annotation.Default;
import de.fdamken.iofacade.config.config.annotation.Optional;

/**
 * Basic Java IO {@link FileSystemConfig}.
 *
 */
public interface BasicFileSystemConfig extends FileSystemConfig {
    /**
     *
     * @return The root of the file system that should be used. Paths that are
     *         starting with a <code>/</code> are starting from this place
     *         (chroot).
     */
    @Default("/")
    @Optional
    String getRoot();
}
