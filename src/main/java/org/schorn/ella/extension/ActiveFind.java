/* 
 * The MIT License
 *
 * Copyright 2019 Bryan Schorn.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.schorn.ella.extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;

/**
 * Extension Interface - Find
 *
 * Find provides selection by one or more ActiveTypes (within the same Role)
 *
 * 1) find - finds all 2) findFirst - finds the first of any params (not first
 * of all)
 *
 * @author schorn
 *
 */
public interface ActiveFind {

    default List<ValueData> findAndFilter(ValueType valueType, String filterPath) {
        if (valueType != null && this instanceof ActiveData) {
            ActiveData activeData = (ActiveData) this;
            List<ValueData> nodes = activeData.find(valueType);
            List<ValueData> keepers = new ArrayList<>();
            if (filterPath != null) {
                List<String> types = Arrays.asList(filterPath.split("\\."));
                for (ValueData node : nodes) {
                    if (SupportFind.filterPath(node.getParent(), node, types, types.size() - 2)) {
                        keepers.add(node);
                    }
                }
                return keepers;
            } else {
                return nodes;
            }
        }
        return new ArrayList<>(0);
    }

    /**
     * Find All ValueData matching [parm1...] ValueType(s)
     *
     *
     * @param valueTypes
     * @return
     */
    default List<ValueData> find(ValueType... valueTypes) {
        if (this instanceof ActiveData) {
            List<ValueData> results = new ArrayList<>();
            SupportFind.find((ActiveData) this, results, -1, valueTypes);
            return results;
        }
        return new ArrayList<>(0);
    }

    /**
     * Find First ValueData matching [parm1...] ValueTypes
     *
     * @param valueTypes
     * @return
     */
    default ValueData findFirst(ValueType... valueTypes) {
        if (this instanceof ActiveData) {
            List<ValueData> results = new ArrayList<>();
            SupportFind.find((ActiveData) this, results, 1, valueTypes);
            if (!results.isEmpty()) {
                return results.get(0);
            }
        }
        return null;
    }

    /**
     * Find All ArrayData matching [parm1...] ArrayTypes
     *
     *
     * @param ArrayType
     * @return
     */
    default List<ArrayData> find(ArrayType... arrayTypes) {
        if (this instanceof ActiveData) {
            List<ArrayData> results = new ArrayList<>();
            SupportFind.find((ActiveData) this, results, -1, arrayTypes);
            return results;
        }
        return new ArrayList<>(0);
    }

    /**
     * Find First ArrayData matching [parm1...] ArrayTypes
     *
     * @param arrayTypes
     * @param ArrayType
     * @return
     */
    default ArrayData findFirst(ArrayType... arrayTypes) {
        if (this instanceof ActiveData) {
            List<ArrayData> results = new ArrayList<>();
            SupportFind.find((ActiveData) this, results, 1, arrayTypes);
            if (!results.isEmpty()) {
                return results.get(0);
            }
        }
        return null;
    }

    /**
     * Find All ObjectData matching [parm1...] ObjectTypes
     *
     * @param objectTypes
     * @return
     */
    default List<ObjectData> find(ObjectType... objectTypes) {
        if (this instanceof ActiveData) {
            List<ObjectData> results = new ArrayList<>();
            SupportFind.find((ActiveData) this, results, -1, objectTypes);
            return results;
        }
        return new ArrayList<>(0);
    }

    /**
     * Find First ObjectData matching [parm1...] ObjectTypes
     *
     * @param oTypes
     * @return
     */
    default ObjectData findFirst(ObjectType... objectTypes) {
        if (this instanceof ActiveData) {
            List<ObjectData> results = new ArrayList<>();
            SupportFind.find((ActiveData) this, results, 1, objectTypes);
            if (!results.isEmpty()) {
                return results.get(0);
            }
        }
        return null;
    }

    /**
     * Find ValueData matching ValueType [parm1] name
     *
     * @param name
     * @return
     */
    default ValueData findValueData(String value_type) {
        if (this instanceof ActiveData) {
            ActiveData activeData = (ActiveData) this;
            ValueType valueType = ValueType.get(activeData.context(), value_type);
            if (valueType != null) {
                List<ValueData> results = new ArrayList<>();
                SupportFind.find((ActiveData) this, results, 1, valueType);
                if (!results.isEmpty()) {
                    return results.get(0);
                }
            }
        }
        return null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *								IMPLEMENTATION
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    static class SupportFind {

        /*
    	 * 
         */
        static void find(ActiveData activeData, List<ValueData> results, int limit, ValueType... valueTypes) {
            /*
	        if (Arrays.asList(valueTypes).stream()
	        	.filter(ot -> ot.equals(activeData.activeType()))
	        	.findAny().isPresent()) {
	        	results.add((ValueData) activeData); 
	        }
             */
            if (limit > 0 && results.size() >= limit) {
                return;
            }
            switch (activeData.role()) {
                case Value:
                    for (ValueType valueType : valueTypes) {
                        if (activeData.activeType().equals(valueType)) {
                            results.add((ValueData) activeData);
                        }
                    }
                    break;
                case Array:
                case Object:
                    StructData structData = (StructData) activeData;
                    for (ActiveData activeData0 : structData.nodes()) {
                        if (activeData0 == null) {
                            continue;
                        }
                        find(activeData0, results, limit, valueTypes);
                    }
                    break;
                default:
                    break;
            }
        }


        /*
    	 * 
         */
        static void find(ActiveData activeData, List<ArrayData> results, int limit, ArrayType... arrayTypes) {
            if (Arrays.asList(arrayTypes).stream()
                    .filter(ot -> ot.equals(activeData.activeType()))
                    .findAny().isPresent()) {
                results.add((ArrayData) activeData);
            }
            if (limit > 0 && results.size() >= limit) {
                return;
            }
            switch (activeData.role()) {
                case Array:
                case Object:
                    StructData structData = (StructData) activeData;
                    for (ActiveData activeData0 : structData.nodes()) {
                        if (activeData0 == null) {
                            continue;
                        }
                        find(activeData0, results, limit, arrayTypes);
                    }
                    break;
                default:
                    break;
            }
        }

        /*
	     * 
         */
        static void find(ActiveData activeData, List<ObjectData> results, int limit, ObjectType... objectTypes) {
            if (Arrays.asList(objectTypes).stream()
                    .filter(ot -> ot.equals(activeData.activeType()))
                    .findAny().isPresent()) {
                results.add((ObjectData) activeData);
            }
            if (limit > 0 && results.size() >= limit) {
                return;
            }
            switch (activeData.role()) {
                case Array:
                case Object:
                    StructData structData = (StructData) activeData;
                    for (ActiveData activeData0 : structData.nodes()) {
                        if (activeData0 == null) {
                            continue;
                        }
                        find(activeData0, results, limit, objectTypes);
                    }
                    break;
                default:
                    break;
            }
        }

        /*
	     * 
         */
        static boolean filterPath(ActiveData parent, ActiveData child, List<String> types, int level) {
            if (parent == null || level < 0) {
                return false;
            }
            if (parent.name().equals(types.get(level))) {
                if (level == 0) {
                    return true;
                }
                return filterPath(parent.getParent(), parent, types, level - 1);
            }
            if (parent.name().equals(child.name())) {
                return filterPath(parent.getParent(), parent, types, level);
            }
            return false;
        }
    }
}
