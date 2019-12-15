/*
 * The MIT License
 *
 * Copyright 2019 bschorn.
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
package org.schorn.ella.node;

import org.schorn.ella.node.ActiveNode.TypeAttribute;

/**
 *
 * @author bschorn
 */
public class TypeAttributes {

    static TypeAttribute valueFromTag(ActiveNode.ValueType attributeType, String attributeValue) {
        if (attributeType.equals(DomainType.valueType())) {
            return DomainType.valueFromTag(attributeValue);
        } else if (attributeType.equals(ObjectCategory.valueType())) {
            return ObjectCategory.valueFromTag(attributeValue);
        } else if (attributeType.equals(ObjectPurpose.valueType())) {
            return ObjectPurpose.valueFromTag(attributeValue);
        } else if (attributeType.equals(ObjectLevel.valueType())) {
            return ObjectLevel.valueFromTag(attributeValue);
        }
        return null;
    }

    /**
     *
     *
     *
     */
    public enum DomainType implements ActiveNode.TypeAttribute {
        Attribute("attribute"),
        ValueObject("value_object"),
        Entity("entity"),
        Aggregate("aggregate"),
        Unknown("unknown"),
        Dynamic("dynamic"),
        Meta("meta"),
        Mapping("mapping");

        String tagName;

        DomainType(String tagName) {
            this.tagName = tagName;
        }

        /**
         *
         * @return
         */
        public String tagName() {
            return this.tagName;
        }

        @Override
        public String attributeName() {
            return DomainType.valueType().name();
        }

        @Override
        public String attributeValue() {
            return this.name();
        }

        static public DomainType valueFromTag(String tagName) {
            for (DomainType domainType : DomainType.values()) {
                if (domainType.tagName().equals(tagName)) {
                    return domainType;
                }
            }
            return DomainType.Unknown;
        }

        static public ActiveNode.ValueType valueType() {
            return MetaTypes.ValueTypes.domain_type.valueType();
        }
    }

    /**
     *
     *
     *
     */
    public enum ObjectCategory implements ActiveNode.TypeAttribute {
        FACT,
        ACT,
        REACT,
        META,
        UNK;

        /**
         *
         * @return
         */
        public String tagName() {
            return this.name();
        }

        @Override
        public String attributeName() {
            return ObjectCategory.valueType().name();
        }

        @Override
        public String attributeValue() {
            return this.name();
        }

        static public ObjectCategory valueFromTag(String tagName) {
            if (tagName == null) {
                return ObjectCategory.UNK;
            }
            for (ObjectCategory category : ObjectCategory.values()) {
                if (category.name().equalsIgnoreCase(tagName)) {
                    return category;
                }
            }
            return ObjectCategory.UNK;
        }
        static public ActiveNode.ValueType valueType() {
            return MetaTypes.ValueTypes.object_category.valueType();
        }
    }

    public enum ObjectPurpose implements ActiveNode.TypeAttribute {
        ENTITY(ObjectCategory.FACT),
        ASSOCIATION(ObjectCategory.FACT),
        REFERENCE(ObjectCategory.FACT),
        REQUEST(ObjectCategory.ACT),
        EXECUTION(ObjectCategory.ACT),
        REALTIME(ObjectCategory.REACT),
        SNAPSHOT(ObjectCategory.REACT),
        META(ObjectCategory.META),
        UNK(ObjectCategory.UNK);

        ObjectCategory category;

        ObjectPurpose(ObjectCategory category) {
            this.category = category;
        }

        public ObjectCategory category() {
            return this.category;
        }

        @Override
        public String attributeName() {
            return ObjectPurpose.valueType().name();
        }

        @Override
        public String attributeValue() {
            return this.name();
        }

        static public ObjectPurpose valueFromTag(String tagName) {
            if (tagName == null) {
                return ObjectPurpose.UNK;
            }
            for (ObjectPurpose purpose : ObjectPurpose.values()) {
                if (purpose.name().equalsIgnoreCase(tagName)) {
                    return purpose;
                }
            }
            return ObjectPurpose.UNK;
        }
        static public ActiveNode.ValueType valueType() {
            return MetaTypes.ValueTypes.object_purpose.valueType();
        }
    }

    public enum ObjectLevel implements ActiveNode.TypeAttribute {
        UNIVERSAL,
        INDUSTRY,
        ENTERPRISE,
        DIVISION,
        DEPARTMENT,
        LOB,
        APPLICATION,
        META,
        UNK;

        public String tagName() {
            return this.name();
        }

        @Override
        public String attributeName() {
            return ObjectLevel.valueType().name();
        }

        @Override
        public String attributeValue() {
            return this.name();
        }

        static public ObjectLevel valueFromTag(String tagName) {
            if (tagName == null) {
                return ObjectLevel.UNK;
            }
            for (ObjectLevel level : ObjectLevel.values()) {
                if (level.name().equalsIgnoreCase(tagName)) {
                    return level;
                }
            }
            return ObjectLevel.UNK;

        }
        static public ActiveNode.ValueType valueType() {
            return MetaTypes.ValueTypes.object_level.valueType();
        }
    }
}
