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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import de.fdamken.iofacade.config.config.FileSystemConfig;
import de.fdamken.iofacade.config.config.PasswordType;
import de.fdamken.iofacade.config.config.PasswordTypeImpl;
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
     * A map where instances of {@link ConfigMapper}s are cached.
     *
     */
    private static final Map<Class<?>, ConfigMapper<?>> MAPPER_CACHE = new WeakHashMap<Class<?>, ConfigMapper<?>>();

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
    private ConfigMapper(final Class<T> configClass) {
        Assertion.acquire(configClass).notNull().isInterface();

        this.configClass = configClass;
        this.configMappings = Collections.unmodifiableList(this.retrieveConfigMappings());
    }

    /**
     * Constructs a new {@link ConfigMapper} with the given class.
     *
     * @param configClass
     *            The configuration class to map configurations from/to.
     * @return The created {@link ConfigMapper}.
     */
    @SuppressWarnings("unchecked")
    public static <T> ConfigMapper<T> create(final Class<T> configClass) {
        ConfigMapper<T> result = (ConfigMapper<T>) ConfigMapper.MAPPER_CACHE.get(configClass);
        if (result == null) {
            synchronized (ConfigMapper.MAPPER_CACHE) {
                result = (ConfigMapper<T>) ConfigMapper.MAPPER_CACHE.get(configClass);
                if (result == null) {
                    result = new ConfigMapper<T>(configClass);
                    ConfigMapper.MAPPER_CACHE.put(configClass, result);
                }
            }
        }
        return result;
    }

    /**
     * Maps the given configuration tree to the mapped configuration class.
     *
     * @param config
     *            The {@link ConfigTree} to map.
     * @return The mapped configuration.
     * @throws ParsingConfigurationException
     *             If any parsing error occurs.
     */
    public T mapFromConfig(final ConfigTree config) throws ParsingConfigurationException {
        final Map<String, Object> dataMap = new HashMap<String, Object>();
        final List<ConfigMapping> passwordMappings = new ArrayList<ConfigMapping>();

        for (final ConfigMapping configMapping : this.configMappings) {
            final String name = configMapping.getName();
            final ConfigTree dataNode = config.byName(name);
            final ConfigMapping.Type dataType = configMapping.getDataType();

            if (dataType == ConfigMapping.Type.PASSWORD) {
                passwordMappings.add(configMapping);
                continue;
            }

            final Object defaultValue = this.getDefaultValue(configMapping);
            final Object data;

            if (dataNode != null) {
                data = this.getValueOf(configMapping, dataNode);
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

        this.mapPasswords(config, dataMap, passwordMappings);

        @SuppressWarnings("unchecked")
        final T result = (T) Proxy.newProxyInstance(this.configClass.getClassLoader(), new Class<?>[] { this.configClass },
                new ConfigInvocationHandler(dataMap));
        return result;
    }

    /**
     * Maps the given configuration to a configuration tree.
     *
     * @param config
     *            the configuration to map.
     * @param configName
     *            The name of the root node of the mapped configuration tree.
     * @return The mapped {@link ConfigTree}.
     * @throws GenerationConfigurationException
     *             If an generation error occurs.
     */
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
                final ConfigMapper mapper = ConfigMapper.create(configMapping.getComplexDataTypeClass());
                result.addSubNode(mapper.mapToConfig(rawData, name));
            } else {
                final String data = String.valueOf(rawData);
                result.addSubNode(new ConfigTree(name, data));
            }
        }
        return result;
    }

    /**
     * Maps the given passwords to the data from the given configuration.
     * Passwords must be mapped at last as they may have references to other
     * properties.
     *
     * @param config
     *            The configuration tree to map the data from.
     * @param dataMap
     *            The data map to put the mapped passwords into and to
     *            dereference references to.
     * @param passwordMappings
     *            The passwords to map. This list must only contain mappings
     *            with the data type {@link ConfigMapping.Type#PASSWORD}!
     * @throws ParsingConfigurationException
     *             If any parsing error occurs.
     */
    private void mapPasswords(final ConfigTree config, final Map<String, Object> dataMap,
            final List<ConfigMapping> passwordMappings) throws ParsingConfigurationException {
        assert config != null : "Config must not be null!";
        assert dataMap != null : "DataMap must not be null!";
        assert passwordMappings != null : "PasswordMappings must not be null!";
        assert passwordMappings.stream().allMatch(mapping -> mapping.getDataType() == ConfigMapping.Type.PASSWORD) : "PasswordMappings must only contain mappings with the data type PASSWORD!";

        for (final ConfigMapping passwordMapping : passwordMappings) {
            final String name = passwordMapping.getName();
            final ConfigTree dataNode = config.byName(name);

            if (dataNode == null) {
                if (!passwordMapping.isOptional()) {
                    throw new ParsingConfigurationException(name + " is required but no data is available!");
                }
            } else {
                final String encryptedPassword = dataNode.asString();
                final String secretKey = passwordMapping.getSecretKey();
                final PasswordType password = new PasswordTypeImpl(encryptedPassword, this.resolvePropertyReference(secretKey,
                        dataMap));

                dataMap.put(name, password);
            }
        }
    }

    /**
     * Resolves the given property reference, if it is one. Property references
     * are starting with a hash sign (<code>#</code>).
     *
     * <p>
     * If the given reference does not start with a hash sign, it is returned as
     * is.
     * </p>
     *
     * @param referenceName
     *            The name of the reference.
     * @param dataMap
     *            The data map to fetch the data from.
     * @return The dereferenced reference value.
     * @throws ParsingConfigurationException
     *             If the reference name cannot be found or the referenced
     *             property is not a string.
     */
    private String resolvePropertyReference(final String referenceName, final Map<String, Object> dataMap)
            throws ParsingConfigurationException {
        final String result;
        if (referenceName.startsWith("#")) {
            final Object referenceValue = dataMap.get(referenceName.substring(1));
            if (referenceValue == null) {
                throw new ParsingConfigurationException("Unable to dereference a reference to " + referenceName
                        + ", because no data is available!");
            }
            if (!(referenceValue instanceof String)) {
                throw new ParsingConfigurationException("Unable to dereference a reference to " + referenceName
                        + ", because the property is no string!");
            }
            result = (String) referenceValue;
        } else {
            result = referenceName;
        }
        return result;
    }

    /**
     * Fetches the default value of the given configuration mapping.
     *
     * <p>
     * If the given mapping has the data type {@link ConfigMapping.Type#COMPLEX}, <code>null</code> is returned as complex types cannot be mapped from a
     * string.
     * </p>
     *
     * @param configMapping
     *            The configuration mapping to fetch the default value from.
     * @return The default value for the given configuration mapping, if any.
     *         Otherwise <code>null</code>.
     */
    private Object getDefaultValue(final ConfigMapping configMapping) {
        return configMapping.getDataType() == ConfigMapping.Type.COMPLEX ? null : configMapping.getDefaultValue();
    }

    /**
     * Fetches the value of the given configuration mapping from the given data
     * node.
     *
     * @param configMapping
     *            The configuration mapping to fetch the data for. Must have the
     *            same name as the tree node.
     * @param dataNode
     *            The tree node to fetch the data from. Must have the same name
     *            as the configuration mapping.
     * @return The fetched data, if any. Otherwise <code>null</code>.
     * @throws ParsingConfigurationException
     *             If any parsing error occurs.
     */
    private Object getValueOf(final ConfigMapping configMapping, final ConfigTree dataNode) throws ParsingConfigurationException {
        assert configMapping != null : "ConfigMapping must not be null!";
        assert dataNode != null : "DataNode must not be null!";
        assert dataNode.getName().equals(configMapping.getName()) : "The names of configMapping and dataNode must match!";

        final ConfigMapping.Type dataType = configMapping.getDataType();
        final Object result;
        switch (dataType) {
            case STRING:
                result = dataNode.asString();
                break;
            case INT:
                result = dataNode.asInt();
                break;
            case DOUBLE:
                result = dataNode.asDouble();
                break;
            case STRING_ARRAY:
                result = dataNode.asStringArray();
                break;
            case INT_ARRAY:
                result = dataNode.asIntArray();
                break;
            case DOUBLE_ARRAY:
                result = dataNode.asDoubleArray();
                break;
            case COMPLEX:
                final ConfigMapper<?> mapper = ConfigMapper.create(configMapping.getComplexDataTypeClass());
                result = mapper.mapFromConfig(dataNode);
                break;
            default:
                throw new ParsingConfigurationException("Unsupported data type " + dataType + "!");
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

    /**
     * This {@link InvocationHandler} is used to handle invocations on
     * configuration interfaces. It maps any method call to a map of data that
     * are containing the result.
     *
     */
    private static class ConfigInvocationHandler implements InvocationHandler {
        /**
         * The results for any method invocations. The keys of the map are the
         * names of the properties that will be mapped.
         *
         */
        private final Map<String, Object> data;

        /**
         * Constructor of ConfigInvocationHandler.
         *
         * @param data
         *            The results for any method invocations. The keys of the
         *            map must be the names of the properties that will be
         *            mapped (what is returned by
         *            {@link ConfigMapping#getName()}.
         */
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
            if (!method.getName().startsWith("get")) {
                throw new UnsupportedOperationException("Only methods starting with get* are supported!");
            }

            final ConfigMapping mapping = ConfigMapping.createFromMethod(method, false);
            mapping.buildName();
            final String name = mapping.getName();

            final Object result = this.data.get(name);
            if (result == null) {
                throw new UnsupportedOperationException("Unsupported mapping name: " + name);
            }
            return result;
        }
    }
}
