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
package de.fdamken.iofacade.config.config.mapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.fdamken.iofacade.config.config.FileSystemConfig;
import de.fdamken.iofacade.config.config.PasswordType;
import de.fdamken.iofacade.config.config.exception.GenerationConfigurationException;
import de.fdamken.iofacade.config.config.exception.ParsingConfigurationException;
import de.fdamken.iofacade.config.config.mapper.ConfigMapping.Type;
import de.fdamken.iofacade.config.config.tree.ConfigTree;
import de.fdamken.iofacade.util.Assertion;

/**
 * A {@link ConfigMapper} is used to map {@link FileSystemConfig}s from/to
 * {@link ConfigTree}s. <br>
 * Later on, a {@link ConfigTree} can be written into a file.
 *
 * @param <T>
 *            The type of the configuration class that should be mapped.
 */
public class ConfigMapper<T> {
    /**
     * The configuration class to map configurations from/to.
     *
     */
    private final Class<T> configClass;

    /**
     * The configuration mappings.
     *
     */
    private final List<ConfigMapping> configMappings;

    /**
     * Constructor of ConfigMapper.
     *
     * @param configClass
     *            The configuration class to map configurations from/to.
     */
    public ConfigMapper(final Class<T> configClass) {
        Assertion.acquire(configClass).notNull().isInterface();

        this.configClass = configClass;
        this.configMappings = Collections.unmodifiableList(this.retrieveConfigMappings());
    }

    public T mapFromConfig(final ConfigTree config) throws ParsingConfigurationException {
        final Map<String, Object> dataMap = new HashMap<String, Object>();
        // TODO: Fill data map.
        for (final ConfigMapping configMapping : this.configMappings) {
            final String name = configMapping.getName();
            final ConfigTree dataNode = config.byName(name);

            final ConfigMapping.Type dataType = configMapping.getDataType();
            final Object defaultValue;
            if (dataType == ConfigMapping.Type.PASSWORD || dataType == ConfigMapping.Type.COMPLEX) {
                // It is not possible to parse a password nor a complex data
                // type.

                defaultValue = null;
            } else {
                defaultValue = configMapping.getDefaultValue();
            }

            // TODO: Implement data parsing.

            final Object data;
            if (dataNode != null) {
                switch (dataType) {
                    case PASSWORD:
                        data = new PasswordType() {
                            @Override
                            public String getEncrypted() {
                                // TODO Auto-generated method body.
                                return null;
                            }

                            @Override
                            public char[] getDecrypted() {
                                // TODO Auto-generated method body.
                                return null;
                            }
                        };
                        break;
                    case STRING:
                        break;
                    case INT:
                        break;
                    case DOUBLE:
                        break;
                    case STRING_ARRAY:
                        break;
                    case INT_ARRAY:
                        break;
                    case DOUBLE_ARRAY:
                        break;
                    case COMPLEX:
                        // TODO: Implement complex type parsing.
                        break;
                    default:
                        throw new ParsingConfigurationException("Unsupported data type " + dataType + "!");
                }
            } else if (defaultValue != null) {
                data = defaultValue;
            } else {
                data = null;
            }

            if (data == null) {
                if (!configMapping.isOptional()) {
                    throw new ParsingConfigurationException(name + " is required but no data is available!");
                }
            } else {
                dataMap.put(name, data);
            }
        }

        @SuppressWarnings("unchecked")
        final T result = (T) Proxy.newProxyInstance(this.configClass.getClassLoader(), new Class<?>[] { this.configClass },
                new ConfigInvocationHandler(dataMap));
        return result;
    }

    @SuppressWarnings("unchecked")
    public ConfigTree mapToConfig(final T config, final String configName) throws GenerationConfigurationException {
        final ConfigTree result = new ConfigTree(configName);
        for (final ConfigMapping configMapping : this.configMappings) {
            final Object rawData = configMapping.getValue(config);
            final String name = configMapping.getName();

            final Type dataType = configMapping.getDataType();
            if (dataType.isArray()) {
                final String singularName = configMapping.getSingularName();

                final List<String> data;
                switch (dataType) {
                    case STRING_ARRAY:
                        final String[] stringArray = (String[]) rawData;
                        data = Arrays.stream(stringArray).collect(Collectors.toList());
                        break;
                    case INT_ARRAY:
                        final int[] intArray = (int[]) rawData;
                        data = Arrays.stream(intArray).mapToObj(x -> String.valueOf(x)).collect(Collectors.toList());
                        break;
                    case DOUBLE_ARRAY:
                        final double[] doubleArray = (double[]) rawData;
                        data = Arrays.stream(doubleArray).mapToObj(x -> String.valueOf(x)).collect(Collectors.toList());
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported array type: " + dataType + "!");
                }

                final ConfigTree configData = new ConfigTree(name);
                data.forEach(str -> configData.addSubNode(new ConfigTree(singularName, str)));
                result.addSubNode(configData);
            } else if (dataType == ConfigMapping.Type.COMPLEX) {
                @SuppressWarnings("rawtypes")
                final ConfigMapper mapper = new ConfigMapper(configMapping.getComplexDataTypeClass());
                result.addSubNode(mapper.mapToConfig(rawData, name));
            } else {
                final String data = String.valueOf(rawData);
                result.addSubNode(new ConfigTree(name, data));
            }
        }
        return result;
    }

    /**
     * Retrieves the {@link ConfigMapping}s from the configuration class.
     *
     * <p>
     * <b> NOTE: Only methods that start with <code>"get"</code> will be mapped.
     * </b>
     * </p>
     *
     * @return The found mappings.
     */
    private List<ConfigMapping> retrieveConfigMappings() {
        return Arrays.stream(this.configClass.getMethods()).filter(method -> method.getName().startsWith("get"))
                .map(method -> ConfigMapping.createFromMethod(method)).collect(Collectors.toList());
    }

    /**
     *
     * @return {@link #configClass}.
     */
    public Class<T> getConfigClass() {
        return this.configClass;
    }

    private static class ConfigInvocationHandler implements InvocationHandler {
        private final Map<String, Object> data;

        public ConfigInvocationHandler(final Map<String, Object> data) {
            assert data != null : "Data must not be null!";

            this.data = data;
        }

        /**
         * {@inheritDoc}
         *
         * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
         *      java.lang.reflect.Method, java.lang.Object[])
         */
        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            // TODO Auto-generated method body.
            return null;
        }
    }
}
