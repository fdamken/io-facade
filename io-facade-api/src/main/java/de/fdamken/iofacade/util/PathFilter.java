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

import java.util.function.Function;

import de.fdamken.iofacade.Path;

/**
 * A path filter is used to filter {@link Path}s.
 *
 */
@FunctionalInterface
public interface PathFilter extends Function<Path, Boolean> {
    /**
     * {@inheritDoc}
     *
     * <p>
     * This decides whether the given path should be processed or not. Depending
     * on the type of the given path, the resulting values may have a different
     * meaning:
     * <table border="1px solid black">
     * <tr>
     * <td style="width: 150px;">&nbsp;</td>
     * <th style="width: 150px;"><code>true</code></th>
     * <th style="width: 150px;"><code>false</code></th>
     * <th style="width: 150px;"><code>null</code></th>
     * </tr>
     * <tr>
     * <th>File</th>
     * <td>include</td>
     * <td>exclude</td>
     * <td>exclude</td>
     * </tr>
     * <tr>
     * <th>Directory</th>
     * <td>include</td>
     * <td>exclude</td>
     * <td>exclude &amp; step in</td>
     * </tr>
     * <tr>
     * <th>Symbolic Link</th>
     * <td>include</td>
     * <td>exclude</td>
     * <td>exclude &amp; follow</td>
     * </tr>
     * </table>
     * <ul>
     * <li><code>include</code> means that the invoking method should include
     * the path in its result</li>
     * <li><code>exclude</code> means that the invoking method should exclude
     * the path in its result</li>
     * <li><code>step in</code> means that the invoking method should step into
     * that path (i.e. in a recursive context)</li>
     * <li><code>follow</code> means that a link should be followed to its
     * destination</li>
     * </ul>
     * </p>
     *
     * @see java.util.function.Function#apply(java.lang.Object)
     */
    @Override
    public Boolean apply(Path path);
}
