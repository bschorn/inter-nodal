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
package org.schorn.ella.impl.context;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import org.schorn.ella.context.ActiveContext.Meta;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveRef;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.Identity;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueType.FieldType;
import org.schorn.ella.node.NodeProvider;

/**
 *
 * @author schorn
 *
 */
class ContextMetaImpl implements Meta {

    private final AppContext context;
    private Identity[] identities = new Identity[0];
    private FieldType[] fieldTypes = new FieldType[0];
    private ValueType[] valueTypes = new ValueType[0];
    private ObjectType[] objectTypes = new ObjectType[0];
    private ArrayType[] arrayTypes = new ArrayType[0];

    ContextMetaImpl(AppContext context) {
        this.context = context;
    }

    @Override
    public Identity addIdentity(Identity identity) {
        Identity[] newIdentities = new Identity[this.identities.length + 1];
        System.arraycopy(this.identities, 0, newIdentities, 0, this.identities.length);
        newIdentities[this.identities.length] = identity;
        this.identities = newIdentities;
        return identity;
    }

    @Override
    public List<Identity> identities() {
        return Collections.unmodifiableList(Arrays.asList(this.identities));
    }

    @Override
    public List<ArrayType> arrayTypes() {
        return Collections.unmodifiableList(Arrays.asList(this.arrayTypes));
    }

    @Override
    public List<ObjectType> objectTypes() {
        return Collections.unmodifiableList(Arrays.asList(this.objectTypes));
    }

    @Override
    public List<ValueType> valueTypes() {
        return Collections.unmodifiableList(Arrays.asList(this.valueTypes));
    }

    @Override
    public List<FieldType> fieldTypes() {
        return Collections.unmodifiableList(Arrays.asList(this.fieldTypes));
    }

    @Override
    public FieldType getFieldType(short activeIdx) {
        return this.fieldTypes[activeIdx];
    }

    @Override
    public ValueType getValueType(short activeIdx) {
        return this.valueTypes[activeIdx];
    }

    @Override
    public ObjectType getObjectType(short activeIdx) {
        return this.objectTypes[activeIdx];
    }

    @Override
    public ArrayType getArrayType(short activeIdx) {
        return this.arrayTypes[activeIdx];
    }

    @Override
    public FieldType getFieldType(String name) {
        return (FieldType) this.getActiveType(Role.Field, name);
    }

    @Override
    public ValueType getValueType(String name) {
        return (ValueType) this.getActiveType(Role.Value, name);
    }

    @Override
    public ObjectType getObjectType(String name) {
        return (ObjectType) this.getActiveType(Role.Object, name);
    }

    @Override
    public ArrayType getArrayType(String name) {
        return (ArrayType) this.getActiveType(Role.Array, name);
    }

    private ActiveType getActiveType(Role role, String typeName) {
        short size = 0;
        switch (role) {
            case Value:
                size = (short) this.valueTypes.length;
                for (short i = 0; i < size; i += 1) {
                    if (this.valueTypes[i].name().equals(typeName)) {
                        return this.valueTypes[i];
                    }
                }
                break;
            case Object:
                size = (short) this.objectTypes.length;
                for (short i = 0; i < size; i += 1) {
                    if (this.objectTypes[i].name().equals(typeName)) {
                        return this.objectTypes[i];
                    }
                }
                break;
            case Array:
                size = (short) this.arrayTypes.length;
                for (short i = 0; i < size; i += 1) {
                    if (this.arrayTypes[i].name().equals(typeName)) {
                        return this.arrayTypes[i];
                    }
                }
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public ActiveType addType(ActiveType activeType) {
        synchronized (this) {
            switch (activeType.role()) {
                case Field: {
                    FieldType[] newFieldTypes = new FieldType[this.fieldTypes.length + 1];
                    System.arraycopy(this.fieldTypes, 0, newFieldTypes, 0, this.fieldTypes.length);
                    newFieldTypes[activeType.activeIdx()] = (FieldType) activeType;
                    this.fieldTypes = newFieldTypes;
                    break;
                }
                case Value: {
                    ValueType[] newValueTypes = new ValueType[this.valueTypes.length + 1];
                    System.arraycopy(this.valueTypes, 0, newValueTypes, 0, this.valueTypes.length);
                    newValueTypes[activeType.activeIdx()] = (ValueType) activeType;
                    this.valueTypes = newValueTypes;
                    break;
                }
                case Object: {
                    ObjectType[] newObjectTypes = new ObjectType[this.objectTypes.length + 1];
                    System.arraycopy(this.objectTypes, 0, newObjectTypes, 0, this.objectTypes.length);
                    newObjectTypes[activeType.activeIdx()] = (ObjectType) activeType;
                    this.objectTypes = newObjectTypes;
                    break;
                }
                case Array: {
                    ArrayType[] newArrayTypes = new ArrayType[this.arrayTypes.length + 1];
                    System.arraycopy(this.arrayTypes, 0, newArrayTypes, 0, this.arrayTypes.length);
                    newArrayTypes[activeType.activeIdx()] = (ArrayType) activeType;
                    this.arrayTypes = newArrayTypes;
                    break;
                }
                default:
                    break;
            }
        }
        return activeType;
    }

    @Override
    public ActiveRef getActiveRef() {
        return NodeProvider.provider().getActiveRef(this.context);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n", "Objects:\n", "");
        for (ObjectType objectType : this.objectTypes) {
            joiner.add(String.format("-> %s", objectType.name()));
        }
        return joiner.toString();
    }

}
