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
package org.schorn.ella.impl.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.schorn.ella.format.SupportString;
import org.schorn.ella.node.DataGroup;
import org.schorn.ella.node.ActiveNode.Constraints;

/**
 *
 * @author schorn
 *
 */
public class ConstraintsImpl implements Constraints {

    /*
	 * 
     */
    static private Map<DataGroup, List<ConstraintType<?>>> TYPE_MAP = new HashMap<>();

    /*
	 * 
     */
    static public <T> ConstraintType<?> createConstraintType(Class<T> classOfT, DataGroup dataGroup, String name) {
        ConstraintType<T> constraintType = new ConstraintTypeImpl<T>(classOfT, name, dataGroup);
        List<ConstraintType<?>> list = TYPE_MAP.get(dataGroup);
        if (list == null) {
            list = new ArrayList<>();
            TYPE_MAP.put(dataGroup, list);
        }
        list.add(constraintType);
        return constraintType;
    }

    /*
	 * 
     */
    private final DataGroup dataGroup;
    private final List<ConstraintType<?>> constraintTypes;
    private final Map<ConstraintType<?>, ConstraintData> constraintMap;

    @Override
    public String toString() {
        return SupportString.format(this);
    }

    /*
	 * 
     */
    ConstraintsImpl(DataGroup dataGroup, List<ConstraintType<?>> constraintTypes, Map<ConstraintType<?>, ConstraintData> constraintMap) {
        this.dataGroup = dataGroup;
        this.constraintTypes = Collections.unmodifiableList(constraintTypes);
        this.constraintMap = constraintMap;
    }

    /**
     *
     */
    @Override
    public DataGroup dataGroup() {
        return this.dataGroup;
    }

    /**
     *
     */
    @Override
    public List<ConstraintType<?>> constraintTypes() {
        return this.constraintTypes;
    }

    /**
     *
     */
    @Override
    public ConstraintData constraint(ConstraintType<?> constraintType) {
        return this.constraintMap.get(constraintType);
    }

    /**
     *
     */
    static class ConstraintDataImpl implements ConstraintData {

        private final ConstraintType<?> constraintType;
        private final List<Object> constraintValues;

        public ConstraintDataImpl(ConstraintType<?> constraintType, List<Object> constraintValues) {
            this.constraintType = constraintType;
            List<Object> tValues = new ArrayList<>(constraintValues.size());
            for (Object value : constraintValues) {
                tValues.add(value);
            }
            this.constraintValues = Collections.unmodifiableList(tValues);
        }

        /**
         *
         */
        @Override
        public ConstraintType<?> constraintType() {
            return this.constraintType;
        }

        /**
         *
         */
        @Override
        public List<Object> constraintValues() {
            return this.constraintValues;
        }

        /*
		 * this could have been done better
		 * 
         */
        @Override
        public boolean test(Object value) {
            return this.constraintType.test(this.constraintValues, value);
        }

        @Override
        public String toString() {
            StringJoiner joiner = new StringJoiner(",", "[", "]");
            this.constraintValues.stream().forEach(v -> joiner.add(v.toString()));
            return String.format("%s: %s",
                    this.constraintType.toString(),
                    joiner.toString()
            );
        }
    }

    /*
	 * 
     */
    static class ConstraintTypeImpl<T> implements ConstraintType<T> {

        private final String name;
        private final DataGroup dataGroup;
        private final Class<T> dataClass;

        ConstraintTypeImpl(Class<T> classOfT, String name, DataGroup dataGroup) {
            this.name = name;
            this.dataGroup = dataGroup;
            this.dataClass = classOfT;
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public DataGroup dataGroup() {
            return this.dataGroup;
        }

        @Override
        public Class<T> dataClass() {
            return this.dataClass;
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return false;
        }

        @Override
        public String toString() {
            return String.format("%s {%s - %s}",
                    this.name == null ? "name?" : this.name,
                    this.dataGroup == null ? "dataGroup?" : this.dataGroup.name(),
                    this.dataClass == null ? "dataClass?" : this.dataClass.getSimpleName()
            );
        }

    }
}
