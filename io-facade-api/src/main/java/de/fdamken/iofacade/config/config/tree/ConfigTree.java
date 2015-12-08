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
package de.fdamken.iofacade.config.config.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@link ConfigTree} is a simple implementation of a tree structure where
 * every node has a name and every node may have sub-nodes or a values, but not
 * both. <br>
 * This can be used to represent a simple XML structure without any properties.
 *
 */
public class ConfigTree {
    /**
     * The sub tree nodes.
     *
     * <p>
     * This is tree node is a value node, or this tree node does not have any
     * sub nodes, this list is empty.
     * </p>
     *
     */
    private final List<ConfigTree> subNodes = new ArrayList<ConfigTree>();
    /**
     * The value of this tree node.
     *
     * <p>
     * <b> NOTE: This may be <code>null</code> if this tree node contains other
     * tree nodes. </b>
     * </p>
     *
     */
    private final String value;

    /**
     * The name of this node.
     *
     */
    private final String name;

    /**
     * Constructor of ConfigTree.
     *
     * @param name
     *            The name of this node.
     * @param value
     *            The value of this tree node. If no value is available (i.e.
     *            this tree node contains other nodes), simply set this value to
     *            <code>null</code>.
     */
    public ConfigTree(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Constructor of ConfigTree.
     *
     * @param name
     *            The name of this node.
     */
    public ConfigTree(final String name) {
        this(name, null);
    }

    /**
     * Searches for a direct sub-node of this element by the given name.
     *
     * @param name
     *            The name to search for.
     * @return The sub-node, if any. Otherwise <code>null</code>.
     */
    public ConfigTree byName(final String name) {
        return this.subNodes.stream().filter(subNode -> subNode.getName().equalsIgnoreCase(name)).findAny().get();
    }

    /**
     * Adds the given sub-node to this tree.
     *
     * @param subNode
     *            The sub-node to add.
     */
    public void addSubNode(final ConfigTree subNode) {
        this.subNodes.add(subNode);
    }

    /**
     *
     * @return The stored value as a {@link String}.
     * @throws IllegalStateException
     *             If no value is present.
     */
    public String asString() throws IllegalStateException {
        if (!this.isValuePresent()) {
            throw new IllegalStateException("No such value!");
        }

        return this.value;
    }

    /**
     *
     * @return The stored value as an <code>int</code>.
     */
    public int asInt() {
        return Integer.parseInt(this.asString());
    }

    /**
     *
     * @return The stored value as a <code>double</code>.
     */
    public double asDouble() {
        return Double.parseDouble(this.asString());
    }

    /**
     *
     * @return The sub-nodes as a {@link String}[].
     */
    public String[] asStringArray() {
        return this.subNodes.stream().map(subNode -> subNode.asString()).toArray(String[]::new);
    }

    /**
     *
     * @return The sub-nodes as a <code>int</code>[].
     */
    public int[] asIntArray() {
        return this.subNodes.stream().mapToInt(subNode -> subNode.asInt()).toArray();
    }

    /**
     *
     * @return The sub-nodes as a <code>double</code>[].
     */
    public double[] asDoubleArray() {
        return this.subNodes.stream().mapToDouble(subNode -> subNode.asDouble()).toArray();
    }

    /**
     *
     * @return Whether a value is present.
     */
    public boolean isValuePresent() {
        return this.value != null;
    }

    /**
     *
     * @return Whether a value is present and it is a string.
     */
    public boolean isStringValue() {
        return this.isValuePresent();
    }

    /**
     *
     * @return Whether a value is present and it can be parsed into an
     *         <code>int</code>.
     */
    public boolean isIntValue() {
        if (!this.isValuePresent()) {
            return false;
        }
        try {
            Integer.parseInt(this.asString());
        } catch (final NumberFormatException dummy) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return Whether a value is present and it can be parsed into an
     *         <code>double</code>.
     */
    public boolean isDoubleValue() {
        if (!this.isValuePresent()) {
            return false;
        }
        try {
            Double.parseDouble(this.asString());
        } catch (final NumberFormatException dummy) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return Whether the sub-nodes can be parsed into a {@link String}[].
     */
    public boolean isStringArray() {
        return !this.isValuePresent() && this.subNodes.stream().allMatch(subNode -> subNode.isStringValue());
    }

    /**
     *
     * @return Whether the sub-nodes can be parsed into a <code>int</code>[].
     */
    public boolean isIntArray() {
        return !this.isValuePresent() && this.subNodes.stream().allMatch(subNode -> subNode.isIntValue());
    }

    /**
     *
     * @return Whether the sub-nodes can be parsed into a <code>int</code>[].
     */
    public boolean isDoubleArray() {
        return !this.isValuePresent() && this.subNodes.stream().allMatch(subNode -> subNode.isDoubleValue());
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
     * @return {@link #subNodes}.
     */
    public List<ConfigTree> getSubNodes() {
        return Collections.unmodifiableList(this.subNodes);
    }
}
