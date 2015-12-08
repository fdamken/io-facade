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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import de.fdamken.iofacade.config.config.PasswordType;
import de.fdamken.iofacade.config.config.annotation.Default;
import de.fdamken.iofacade.config.config.annotation.Named;
import de.fdamken.iofacade.config.config.annotation.Optional;
import de.fdamken.iofacade.config.config.annotation.Password;
import de.fdamken.iofacade.config.config.annotation.SingularName;
import de.fdamken.iofacade.config.config.exception.GenerationConfigurationException;
import de.fdamken.iofacade.util.Assertion;

/**
 * A configuration mapping is the mapping between methods and configuration
 * properties. With this class it is possible to easily parse the method
 * annotations and the method name into a valid configuration mapping.
 *
 */
public class ConfigMapping {
    /**
     * The method this configuration mapping was extracted from.
     *
     */
    private final Method method;

    /**
     * The name of this property.
     *
     */
    private final String name;
    /**
     * The singular name of this property. This is only used when this is an
     * array.
     *
     */
    private final String singularName;
    /**
     * The default value, if any. Otherwise <code>null</code>.
     *
     */
    private final String defaultValue;
    /**
     * Whether this property is optional.
     *
     */
    private final boolean optional;
    /**
     * Whether this property is a password property.
     *
     */
    private final boolean password;
    /**
     * The path to the secret key to decrypt the password. If {@link #password}
     * is <code>false</code>, this is <code>null</code>.
     *
     */
    private final String secretKey;

    /**
     * The data type of this mapping.
     *
     */
    private final ConfigMapping.Type dataType;
    /**
     * The class that represents the complex data type, if the data type above
     * is {@link ConfigMapping.Type#COMPLEX}.
     *
     */
    private final Class<?> complexDataTypeClass;

    /**
     * Constructor of ConfigMapping.
     *
     * @param method
     *            The method to create this mapping from.
     */
    private ConfigMapping(final Method method) {
        assert method != null : "Method must not be null!";

        this.method = method;
        this.name = this.extractName();
        this.singularName = this.extractSingularName();
        this.defaultValue = this.extractDefaultValue();
        this.optional = this.extractIsOptional();
        this.secretKey = this.extractSecretKey();
        this.password = this.secretKey != null;
        this.dataType = this.extractDataType();
        this.complexDataTypeClass = this.dataType == ConfigMapping.Type.COMPLEX ? this.extractComplexDataType() : null;
    }

    /**
     * Parses the annotations of the given method and creates a new
     * configuration mapping.
     *
     * @param method
     *            The method to parse. Must not be null.
     * @return The newly created configuration mapping.
     */
    public static ConfigMapping createFromMethod(final Method method) {
        Assertion.acquire(method).named("method").notNull();

        return new ConfigMapping(method);
    }

    /**
     * Retrieves the value from the given object for this configuration mapping.
     *
     * @param obj
     *            The object to retrieve the value from. Must be an instance of
     *            the class that was used whilst creating this configuration
     *            mapping.
     * @return The object, if any.
     * @throws GenerationConfigurationException
     *             If an error occurs whilst accessing the methods.
     */
    public Object getValue(final Object obj) throws GenerationConfigurationException {
        Assertion.acquire(obj).named("obj").notNull().isInstanceOf(this.method.getDeclaringClass());
        try {
            final Object data = this.method.invoke(obj);
            return this.dataType == ConfigMapping.Type.PASSWORD ? ((PasswordType) data).getEncrypted() : data;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException cause) {
            throw new GenerationConfigurationException("An error occurred whilst retrieving data from an object mapping!", cause);
        }
    }

    /**
     * Extracts the property name from the method.
     *
     * @return The extracted name.
     */
    private String extractName() {
        final String name;
        if (this.method.isAnnotationPresent(Named.class)) {
            name = this.method.getAnnotation(Named.class).value();
        } else {
            String rawPropertyName = this.method.getName();
            rawPropertyName = rawPropertyName.replaceAll("([A-Z])", "-$1");
            rawPropertyName = rawPropertyName.replace('_', '-');
            rawPropertyName = rawPropertyName.replaceAll("[^a-zA-Z]", "");
            rawPropertyName = rawPropertyName.replaceFirst("^get-?", "");
            name = rawPropertyName;
        }
        return name;
    }

    /**
     * Extracts the singular name from the method.
     *
     * @return The singular name.
     */
    private String extractSingularName() {
        final String name;
        if (this.method.isAnnotationPresent(SingularName.class)) {
            name = this.method.getAnnotation(SingularName.class).value();
        } else {
            name = this.name.substring(0, this.name.length() - 1);
        }
        return name;
    }

    /**
     * Extracts the default value from the method.
     *
     * @return The extracted default value, if any. Otherwise <code>null</code>.
     */
    private String extractDefaultValue() {
        final String defaultValue;
        if (this.method.isAnnotationPresent(Default.class)) {
            defaultValue = this.method.getAnnotation(Default.class).value();
        } else {
            defaultValue = null;
        }
        return defaultValue;
    }

    /**
     * Extracts whether the property is optional or not from the method.
     *
     * @return Whether the property is optional or not.
     */
    private boolean extractIsOptional() {
        return this.method.isAnnotationPresent(Optional.class);
    }

    /**
     * Extracts the private key from the method if the annotation
     * {@link Password} is present.
     *
     * @return The extracted private key, if any. Otherwise <code>null</code>.
     */
    private String extractSecretKey() {
        final String privateKey;
        if (this.method.isAnnotationPresent(Password.class)) {
            privateKey = this.method.getAnnotation(Password.class).value();
        } else {
            privateKey = null;
        }
        return privateKey;
    }

    /**
     * Extracts the data type from the method.
     *
     * @return The extracted data type.
     */
    private ConfigMapping.Type extractDataType() {
        final ConfigMapping.Type dataType;
        final Class<?> returnType = this.method.getReturnType();
        if (returnType == null) {
            throw new IllegalArgumentException("A property cannot be void!");
        } else if (returnType == PasswordType.class) {
            dataType = ConfigMapping.Type.PASSWORD;
        } else if (returnType == String.class) {
            dataType = ConfigMapping.Type.STRING;
        } else if (returnType == int.class) {
            dataType = ConfigMapping.Type.INT;
        } else if (returnType == double.class) {
            dataType = ConfigMapping.Type.DOUBLE;
        } else if (returnType == String[].class) {
            dataType = ConfigMapping.Type.STRING_ARRAY;
        } else if (returnType == int[].class) {
            dataType = ConfigMapping.Type.INT_ARRAY;
        } else if (returnType == double[].class) {
            dataType = ConfigMapping.Type.DOUBLE_ARRAY;
        } else if (returnType.isInterface()) {
            dataType = ConfigMapping.Type.COMPLEX;
        } else {
            throw new IllegalArgumentException("The return type " + returnType.getCanonicalName() + " is not supported!");
        }
        return dataType;
    }

    /**
     * Extracts the complex data type from the method.
     *
     * @return The extracted complex data type.
     */
    private Class<?> extractComplexDataType() {
        return this.method.getReturnType();
    }

    /**
     *
     * @return {@link #name}.
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return {@link #singularName}.
     */
    public String getSingularName() {
        return this.singularName;
    }

    /**
     * Parses the default value and returns it. If the data type of this mapping
     * is {@link ConfigMapping.Type#PASSWORD} or
     * {@link ConfigMapping.Type#COMPLEX}, this results in an
     * {@link UnsupportedOperationException}.
     *
     * @return The default value of this mapping.
     */
    public Object getDefaultValue() {
        return this.defaultValue == null ? null : this.dataType.parse(this.defaultValue);
    }

    /**
     *
     * @return {@link #optional}.
     */
    public boolean isOptional() {
        return this.optional;
    }

    /**
     *
     * @return {@link #password}.
     */
    public boolean isPassword() {
        return this.password;
    }

    /**
     *
     * @return {@link #secretKey}.
     */
    public String getSecretKey() {
        return this.secretKey;
    }

    /**
     *
     * @return {@link #dataType}.
     */
    public ConfigMapping.Type getDataType() {
        return this.dataType;
    }

    /**
     *
     * @return {@link #complexDataTypeClass}.
     */
    public Class<?> getComplexDataTypeClass() {
        return this.complexDataTypeClass;
    }

    /**
     * The data type of the configuration mapping.
     *
     */
    public static enum Type {
        /**
         * A password.
         *
         */
        PASSWORD(false) {
            /**
             * {@inheritDoc}
             *
             * @see de.fdamken.iofacade.config.config.mapper.ConfigMapping.Type#parse(java.lang.String)
             */
            @Override
            public Object parse(final String str) {
                throw new UnsupportedOperationException("Passwords are not parsable from a string!");
            }
        },
        /**
         * A simple string.
         *
         */
        STRING(false) {
            /**
             * {@inheritDoc}
             *
             * @see de.fdamken.iofacade.config.config.mapper.ConfigMapping.Type#parse(java.lang.String)
             */
            @Override
            public Object parse(final String str) {
                return str;
            }
        },
        /**
         * A simple integer.
         *
         */
        INT(false) {
            /**
             * {@inheritDoc}
             *
             * @see de.fdamken.iofacade.config.config.mapper.ConfigMapping.Type#parse(java.lang.String)
             */
            @Override
            public Object parse(final String str) {
                return Integer.valueOf(str);
            }
        },
        /**
         * A simple floating point number.
         *
         */
        DOUBLE(false) {
            /**
             * {@inheritDoc}
             *
             * @see de.fdamken.iofacade.config.config.mapper.ConfigMapping.Type#parse(java.lang.String)
             */
            @Override
            public Object parse(final String str) {
                return Double.valueOf(str);
            }
        },
        /**
         * An array of strings.
         *
         */
        STRING_ARRAY(true) {
            /**
             * {@inheritDoc}
             *
             * @see de.fdamken.iofacade.config.config.mapper.ConfigMapping.Type#parse(java.lang.String)
             */
            @Override
            public Object parse(final String str) {
                final String[] split = str.split(", *");
                return split;
            }
        },
        /**
         * An array of integers.
         *
         */
        INT_ARRAY(true) {
            /**
             * {@inheritDoc}
             *
             * @see de.fdamken.iofacade.config.config.mapper.ConfigMapping.Type#parse(java.lang.String)
             */
            @Override
            public Object parse(final String str) {
                final String[] split = str.split(", *");
                return Arrays.stream(split).mapToInt(s -> Integer.parseInt(s)).toArray();
            }
        },
        /**
         * An array of floating point numbers.
         *
         */
        DOUBLE_ARRAY(true) {
            /**
             * {@inheritDoc}
             *
             * @see de.fdamken.iofacade.config.config.mapper.ConfigMapping.Type#parse(java.lang.String)
             */
            @Override
            public Object parse(final String str) {
                final String[] split = str.split(", *");
                return Arrays.stream(split).mapToDouble(s -> Double.parseDouble(s)).toArray();
            }
        },
        /**
         * A complex data type, represented by another configuration mapping
         * class.
         *
         */
        COMPLEX(false) {
            /**
             * {@inheritDoc}
             *
             * @see de.fdamken.iofacade.config.config.mapper.ConfigMapping.Type#parse(java.lang.String)
             */
            @Override
            public Object parse(final String str) {
                throw new UnsupportedOperationException("A complex type cannot be parsed from a string!");
            }
        };

        /**
         * Whether this is an array type.
         *
         */
        private final boolean array;

        /**
         * Constructor of Type.
         *
         * @param array
         *            Whether this is an array type.
         */
        private Type(final boolean array) {
            this.array = array;
        }

        /**
         * Parses the given string into an object that represents this data
         * type.
         *
         * <p>
         * <b> NOTE: A passwords nor a complex type can be parsed from a string!
         * </b>
         * </p>
         *
         * @param str
         *            The string to parse.
         * @return The parsed string.
         * @throws UnsupportedOperationException
         *             If this operation is not supported.
         */
        public abstract Object parse(final String str) throws UnsupportedOperationException;

        /**
         *
         * @return {@link #array}.
         */
        public boolean isArray() {
            return this.array;
        }
    }
}
