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
package de.fdamken.iofacade.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.fdamken.iofacade.Path;

/**
 * Utility class for handling directories.
 *
 */
public class DirectoryUtil {
    /**
     * Constructor of DirectoryUtil.
     *
     */
    private DirectoryUtil() {
        // Nothing to do.
    }

    /**
     * Lists all entries in the given directory recursively (it reads the whole
     * file tree starting from the given directory). The given
     * {@link PathFilter} is used to filter the result of this method. Please
     * lookup the {@link PathFilter} for further documentation about the
     * meanings of the result of the {@link PathFilter#apply(Path)} method.
     *
     * @param rootDir
     *            The root directory to start listing from. This is included in
     *            the result.
     * @param filter
     *            The {@link PathFilter} to apply.
     * @return All files in the given directory and its sub-directories,
     *         matching the {@link PathFilter}.
     * @throws FileNotFoundException
     *             If the given directory was not found.
     * @throws IOException
     *             If any I/O error occurs.
     */
    public static List<Path> listEntriesRecursive(final Path rootDir, final PathFilter filter) throws FileNotFoundException,
            IOException {
        final List<Path> result = new ArrayList<Path>();
        if (rootDir.isDirectory()) {
            for (final Path path : rootDir.asDirectory().listEntries()) {
                final Boolean fileFilterResult = filter.apply(path);
                if (fileFilterResult == null || fileFilterResult) {
                    result.addAll(DirectoryUtil.listEntriesRecursive(path, filter));
                }
                if (fileFilterResult != null && fileFilterResult) {
                    result.add(path);
                }
            }
        }
        return result;
    }
}
