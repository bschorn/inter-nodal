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
import java.util.List;
import java.util.Optional;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ObjectType.Builder;
import org.schorn.ella.node.ActiveNode.ObjectType.ObjectSchema;
import org.schorn.ella.node.ActiveNode.TypeAttribute;
import org.schorn.ella.node.BondType;
import org.schorn.ella.node.NodeProvider;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
class ObjectTypeBuilderImpl implements Builder {

    private static final Logger LGR = LoggerFactory.getLogger(ObjectTypeBuilderImpl.class);

    private final AppContext context;
    private final String name;
    private final List<MemberDef> members;
    private final List<TypeAttribute> attributes;
    private final List<ObjectType> baseTypes;
    private ObjectType dynamicType = null;

    ObjectTypeBuilderImpl(AppContext context, String name, List<TypeAttribute> attributes, List<ObjectType> baseTypes) {
        this.context = context;
        this.name = name;
        this.members = new ArrayList<>(10);
        this.attributes = attributes;
        this.baseTypes = baseTypes;
    }

    ObjectTypeBuilderImpl(AppContext context, String name, List<TypeAttribute> attributes) {
        this.context = context;
        this.name = name;
        this.members = new ArrayList<>(10);
        this.attributes = attributes;
        this.baseTypes = new ArrayList<>();
    }
    ObjectTypeBuilderImpl(AppContext context, String name) {
        this.context = context;
        this.name = name;
        this.members = new ArrayList<>(10);
        this.attributes = new ArrayList<>();
        this.baseTypes = new ArrayList<>();
    }

    /**
     *
     */
    @Override
    public Builder add(ActiveType activeType) {
        return this.add(activeType, BondType.OPTIONAL);
    }

    /**
     * @throws Exception
     *
     */
    @Override
    public Builder add(ActiveType activeType, BondType bondType) {
        Optional<MemberDef> optMemberType = this.members.stream()
                .filter(mti -> mti.activeType().equals(activeType))
                .findAny();

        if (optMemberType.isPresent()) {
            LGR.error("{}.add('{}.{}.{}','{}') - was NOT added because there is already a member with this name!",
                    this.getClass().getSimpleName(),
                    activeType.context().name(),
                    activeType.role().name(),
                    activeType.name(),
                    bondType.name());
            return this;
        }

        MemberDef memberType;
        try {
            memberType = MemberDef.create(activeType, bondType, this.members.size());
        } catch (Exception ex) {
            LGR.error(Functions.getStackTraceAsString(ex));
            return null;
        }
        if (memberType == null) {
            LGR.error("{}.add() - failed to create MemberType for {} with bond type {}",
                    this.getClass().getSimpleName(), activeType.name(), bondType.name());
        } else {
            this.members.add(memberType);
        }
        return this;
    }

    @Override
    public Builder addBaseType(ObjectType parentType) {
        this.baseTypes.add(parentType);
        return this;
    }

    /**
     *
     */
    @Override
    public ObjectType build() {
        ObjectSchema schema = new ObjectSchemaImpl(this.members);
        try {
            return NodeProvider.provider().createObjectType(context, name, schema, this.attributes, this.baseTypes);
        } catch (Exception ex) {
            LGR.error(Functions.getStackTraceAsString(ex));
            return null;
        }
    }

    @Override
    public ObjectType lease() {
        ObjectSchema schema = new ObjectSchemaImpl(this.members);
        try {
            return NodeProvider.provider().createTransientObjectType(context, name, schema);
        } catch (Exception ex) {
            LGR.error(Functions.getStackTraceAsString(ex));
            return null;
        }
    }

    /**
     *
     */
    @Override
    public ObjectType dynamic() {
        this.dynamicType = new Dynamic(this);
        return this.dynamicType;
    }

    /**
     *
     */
    static class Dynamic extends ActiveTypeImpl implements ObjectType, Builder {

        ObjectTypeBuilderImpl builder;

        Dynamic(ObjectTypeBuilderImpl builder) {
            super(builder.context, builder.name, (short) -1);
            this.builder = builder;
        }

        @Override
        public int bytes() {
            return this.schema().bytes();
        }

        @Override
        public ObjectSchema schema() {
            return new ObjectSchemaImpl(this.builder.members);
        }

        @Override
        public Builder add(ActiveType activeType) {
            return this.builder.add(activeType);
        }

        @Override
        public Builder add(ActiveType activeType, BondType bondType) {
            return this.builder.add(activeType, bondType);
        }

        @Override
        public ObjectType build() {
            return this;
        }

        @Override
        public ObjectType lease() {
            return this;
        }

        @Override
        public ObjectType dynamic() {
            return this;
        }

        @Override
        public boolean isDynamic() {
            return true;
        }

        @Override
        public ObjectData.Builder builder() {
            return new ObjectDataBuilderImpl(this);
        }

        @Override
        public ObjectData create(ActiveData... entries) throws Exception {
            ObjectData.Builder builder = new ObjectDataBuilderImpl(this);
            for (ActiveData entry : entries) {
                builder.add(entry);
            }
            return builder.build();
        }

        @Override
        public ObjectData create(Object... values) throws Exception {
            ObjectData.Builder builder = new ObjectDataBuilderImpl(this);
            int fieldNum = 0;
            for (Object value : values) {
                ValueType valueType = ValueType.dynamic(this.context(), String.format("field_%d", ++fieldNum), value);
                ValueData valueData = valueType.create(value);
                builder.add(valueData);
            }
            return builder.build();
        }

        @Override
        public List<ObjectType> baseTypes() {
            return new ArrayList<>();
        }

        @Override
        public Builder addBaseType(ObjectType parentType) {
            return this;
        }

    }

}
