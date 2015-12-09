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
 * A file system configuration is used to describe file systems and configure
 * them properly for later usage.
 *
 * <p>
 * Any field that is contained by a file system configuration is mapped to the
 * configuration file.
 * </p>
 * <p>
 * You may use any annotations contained by the
 * {@link de.fdamken.iofacade.config.config.annotation} package in order to
 * configure the mappings.
 * </p>
 * <p>
 * The names of the properties are generated from the method names. <br>
 * The are generated like this:
 * <table border="1px solid black">
 * <tr>
 * <td>method name</td>
 * <td>property name</td>
 * </tr>
 * <tr>
 * <td>getName</td>
 * <td>name</td>
 * </tr>
 * <tr>
 * <td>getDisplayName</td>
 * <td>display-name</td>
 * </tr>
 * <tr>
 * <td>getReal_Name</td>
 * <td>real-name</td>
 * </tr>
 * </table>
 * Every method that does not start with <code>get</code> is completely ignored!
 * </p>
 * <p>
 * There are various property types that are supported:
 * <ul>
 * <li>String</li>
 * <li>int</li>
 * <li>double</li>
 * <li>String[] <b>or</b> List&lt;String&gt;</li>
 * </ul>
 * To load encrypted passwords, you shall use the {@link Password} annotation.
 * </p>
 * <p>
 * It is also possible to load more complex (self-defined) types. These complex
 * types must be interfaces in the same style as a basic
 * {@link FileSystemConfig} (tagged with annotations). Then, it is possible to
 * map these to the configuration file. The properties that are contained by the
 * sub-type are mapped under the root type (i.e. the root property is
 * <code>foo</code> and the sub-type contains a property named <code>bar</code>,
 * then <code>bar</code> is available as <code>foo.bar</code> in the root type
 * (like java method chaining)).
 * </p>
 * <p>
 * <b> NOTE: Method that are not starting with <code>get*</code> are completely
 * ignored within the mapping process. And they thrown an
 * {@link UnsupportedOperationException} when invoked. </b>
 * </p>
 *
 */
public interface FileSystemConfig {
    // Nothing to do.
}
