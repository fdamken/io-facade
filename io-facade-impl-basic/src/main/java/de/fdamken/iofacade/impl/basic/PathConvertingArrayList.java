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

import java.util.ArrayList;

import de.fdamken.iofacade.FileSystem;
import de.fdamken.iofacade.Path;

/**
 * Implementation of {@link ArrayList} that can convert
 * {@link java.nio.file.Path}s to {@link Path}s on-the-fly.
 *
 */
public class PathConvertingArrayList extends ArrayList<Path> {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = -1156314242702996853L;

    /**
     * The file system to create new {@link Path}s.
     *
     */
    private final FileSystem fileSystem;

    /**
     * Constructor of PathConvertingArrayList.
     *
     * @param fileSystem
     *            The file system to create new {@link Path}s.
     */
    public PathConvertingArrayList(final FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    /**
     * Adds the given path to this list.
     *
     * @param path
     *            The path to add.
     */
    public void addPath(final java.nio.file.Path path) {
        this.add(new BasicPath(this.fileSystem, path));
    }
}
