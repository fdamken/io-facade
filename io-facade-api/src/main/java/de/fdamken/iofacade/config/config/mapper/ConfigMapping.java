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
    private String name;
    /**
     * The singular name of this property. This is only used when this is an
     * array.
     *
     */
    private String singularName;
    /**
     * The default value, if any. Otherwise <code>null</code>.
     *
     */
    private String defaultValue;
    /**
     * Whether this property is optional.
     *
     */
    private boolean optional;
    /**
     * Whether this property is a password property.
     *
     */
    private boolean password;
    /**
     * The path to the secret key to decrypt the password. If {@link #password}
     * is <code>false</code>, this is <code>null</code>.
     *
     */
    private String secretKey;

    /**
     * The data type of this mapping.
     *
     */
    private ConfigMapping.Type dataType;
    /**
     * The class that represents the complex data type, if the data type above
     * is {@link ConfigMapping.Type#COMPLEX}.
     *
     */
    private Class<?> complexDataTypeClass;

    /**
     * Constructor of ConfigMapping.
     *
     * @param method
     *            The method to create this mapping from.
     */
    private ConfigMapping(final Method method) {
        assert method != null : "Method must not be null!";

        this.method = method;
        if (this.method.getParameterCount() > 0) {
            throw new IllegalArgumentException("A config mapping must not have any parameters!");
        }
    }

    /**
     * Parses the annotations of the given method and creates a new
     * configuration mapping.
     *
     * @param method
     *            The method to parse. Must not be null.
     * @param buildAll
     *            Whether to build all properties or not.
     * @return The newly created configuration mapping.
     */
    public static ConfigMapping createFromMethod(final Method method, final boolean buildAll) {
        Assertion.acquire(method).named("method").notNull();

        final ConfigMapping mapping = new ConfigMapping(method);
        if (buildAll) {
            mapping.buildName();
            mapping.buildSingularName();
            mapping.buildDefaultValue();
            mapping.buildOptional();
            mapping.buildPassword();
            mapping.buildDataType();
        }
        return mapping;
    }

    /**
     * Parses the annotations of the given method and creates a new
     * configuration mapping.
     *
     * <p>
     * This builds all properties.
     * </p>
     *
     * @param method
     *            The method to parse. Must not be null.
     * @return The newly created configuration mapping.
     */
    public static ConfigMapping createFromMethod(final Method method) {
        return ConfigMapping.createFromMethod(method, true);
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
     * Builds {@link #name}.
     *
     */
    public void buildName() {
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

        this.name = name;
    }

    /**
     * Builds {@link #singularName}.
     *
     */
    public void buildSingularName() {
        final String singularName;
        if (this.method.isAnnotationPresent(SingularName.class)) {
            singularName = this.method.getAnnotation(SingularName.class).value();
        } else {
            singularName = this.name.substring(0, this.name.length() - 1);
        }

        this.singularName = singularName;
    }

    /**
     * Builds {@link #defaultValue}.
     *
     */
    public void buildDefaultValue() {
        final String defaultValue;
        if (this.method.isAnnotationPresent(Default.class)) {
            defaultValue = this.method.getAnnotation(Default.class).value();
        } else {
            defaultValue = null;
        }

        this.defaultValue = defaultValue;
    }

    /**
     * Builds {@link #optional}.
     *
     */
    public void buildOptional() {
        final boolean optional = this.method.isAnnotationPresent(Optional.class);

        this.optional = optional;
    }

    /**
     * Builds {@link #secretKey} and {@link #password}.
     *
     */
    public void buildPassword() {
        final String secretKey;
        if (this.method.isAnnotationPresent(Password.class)) {
            secretKey = this.method.getAnnotation(Password.class).value();
        } else {
            secretKey = null;
        }

        this.secretKey = secretKey;
        this.password = secretKey != null;
    }

    /**
     * Builds {@link #dataType} and {@link #complexDataTypeClass}.
     *
     */
    public void buildDataType() {
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

        final Class<?> complexDataTypeClass;
        if (dataType == ConfigMapping.Type.COMPLEX) {
            complexDataTypeClass = this.method.getReturnType();
        } else {
            complexDataTypeClass = null;
        }

        this.dataType = dataType;
        this.complexDataTypeClass = complexDataTypeClass;
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
