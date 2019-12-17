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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.format.SupportString;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.DataGroup;

/**
 *
 * @author schorn
 *
 */
class ObjectTypeImpl extends ActiveTypeImpl implements ObjectType {

    private final ObjectSchema schema;
    private final List<ObjectType> baseTypes;
    private final List<TypeAttribute> attributeList;
    private final Map<Class<? extends TypeAttribute>, TypeAttribute> attributeMap;

    protected ObjectTypeImpl(AppContext context, String name, ObjectSchema schema,
            Short activeIdx, List<TypeAttribute> attributes, List<ObjectType> baseTypes) {
        super(context, name, activeIdx);
        this.schema = schema;
        this.baseTypes = baseTypes;
        this.attributeList = attributes;
        this.attributeMap = new HashMap<>();
        for (TypeAttribute typeAttribute : attributes) {
            this.attributeMap.put(typeAttribute.getClass(), typeAttribute);
        }
        for (ObjectType baseType : baseTypes) {
            for (TypeAttribute typeAttribute : baseType.attributes()) {
                if (!this.attributeMap.containsKey(typeAttribute.getClass())) {
                    this.attributeList.add(typeAttribute);
                    this.attributeMap.put(typeAttribute.getClass(), typeAttribute);
                }
            }
        }
    }

    @Override
    public boolean isDynamic() {
        return false;
    }

    @Override
    public List<ObjectType> baseTypes() {
        return this.baseTypes;
    }

    @Override
    public <T extends TypeAttribute> T getTypeAttribute(Class<T> classForT) {
        return (T) this.attributeMap.get(classForT);
    }

    @Override
    public boolean hasTypeAttribute(TypeAttribute typeAttribute) {
        TypeAttribute typeAttribute2 = this.attributeMap.get(typeAttribute.getClass());
        return (typeAttribute2 != null && typeAttribute2 == typeAttribute);
    }

    @Override
    public List<TypeAttribute> attributes() {
        return this.attributeList;
    }

    @Override
    public ObjectSchema schema() {
        return this.schema;
    }

    @Override
    public int bytes() {
        return this.schema.bytes();
    }

    @Override
    public String toString() {
        return SupportString.format(this);
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

    /**
     * Raw Values
     */
    @Override
    public ObjectData create(Object... values) throws Exception {
        ObjectData.Builder builder = new ObjectDataBuilderImpl(this);
        int idx = 0;
        for (MemberDef memberDef : this.schema.memberDefs()) {
            if (memberDef.activeType().role().equals(Role.Value)) {
                ValueType memberVType = (ValueType) memberDef.activeType();
                DataGroup bestGroup = DataGroup.bestFit(values[idx]);
                DataGroup memberGroup = memberVType.fieldType().dataType().primitiveType().dataGroup();
                if ((bestGroup.isNumeric() == memberGroup.isNumeric())
                        || (bestGroup.isTemporal() == memberGroup.isTemporal())
                        || (bestGroup.isQuoted() == memberGroup.isQuoted())) {
                    builder.add(memberVType.create(values[idx++]));
                }
            }
        }
        return builder.build();
    }

}
