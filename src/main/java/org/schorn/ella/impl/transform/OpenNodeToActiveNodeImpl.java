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
package org.schorn.ella.impl.transform;

import java.util.HashMap;
import java.util.Map;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.node.OpenNode.OpenArray;
import org.schorn.ella.node.OpenNode.OpenObject;
import org.schorn.ella.node.OpenNode.OpenValue;
import org.schorn.ella.transform.ActiveTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OpenNode --> StructData
 *
 *
 * @author schorn
 *
 */
public class OpenNodeToActiveNodeImpl extends TransformExceptionImpl implements ActiveTransform.OpenNodeToActiveNode {

    static final Logger LGR = LoggerFactory.getLogger(OpenNodeToActiveNodeImpl.class);

    private final AppContext context;
    private final boolean useDynamicTypes;
    private final Map<Thread, Map<Class<? extends ActiveType>, Map<String, ActiveType>>> dynamicTypes;

    public OpenNodeToActiveNodeImpl(AppContext context) {
        this.context = context;
        boolean useDynamicTypes0;
        try {
            useDynamicTypes0 = this.isAutoDynamicTypes();
        } catch (Exception ex) {
            useDynamicTypes0 = false;
        }
        this.useDynamicTypes = useDynamicTypes0;
        if (this.useDynamicTypes) {
            this.dynamicTypes = new HashMap<>();
            LGR.info("{}.ctor() - ActiveContext: {} will automatically create types (dynamic) for undeclared types (meta-data).",
                    this.getClass().getSimpleName(), context.name());
        } else {
            this.dynamicTypes = null;
        }
    }

    @Override
    public String toString() {
        return String.format("%s", this.getClass().getSimpleName(), this.sourceFormat().toString(),
                this.targetFormat().toString());
    }

    private void init() {
        this.clearException();

        if (this.useDynamicTypes) {
            Map<Class<? extends ActiveType>, Map<String, ActiveType>> roleMap = this.dynamicTypes.get(Thread.currentThread());
            if (roleMap == null) {
                roleMap = new HashMap<>();
                this.dynamicTypes.put(Thread.currentThread(), roleMap);
            }
            roleMap.put(ValueType.class, new HashMap<>());
            roleMap.put(ObjectType.class, new HashMap<>());
            roleMap.put(ArrayType.class, new HashMap<>());
        }

    }

    /**
     * Apply
     * @param openNode
     * @return
     */
    @Override
    public StructData apply(OpenNode openNode) {
        try {
            this.init();
            if (openNode.role().equals(Role.Object)) {
                ObjectData odata = createObject((OpenObject) openNode);
                return odata;
            } else if (openNode.role().equals(Role.Array)) {
                ArrayData adata = createArray((OpenArray) openNode);
                return adata;
            }
        } catch (Exception ex) {
            this.setException(ex);
        }
        return null;
    }

    /**
     *
     * @param classForT
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    <T extends ActiveType> T getType(Class<T> classForT, String name) {
        Map<Class<? extends ActiveType>, Map<String, ActiveType>> roleMap = this.dynamicTypes.get(Thread.currentThread());
        Map<String, ActiveType> typeMap = roleMap.get(classForT);
        if (typeMap != null) {
            return (T) typeMap.get(name);
        }
        return null;
    }

    /**
     *
     * @param classForT
     * @param name
     * @return
     */
    <T extends ActiveType> void reuseType(Class<T> classForT, String name, ActiveType activeType) {
        Map<Class<? extends ActiveType>, Map<String, ActiveType>> roleMap = this.dynamicTypes.get(Thread.currentThread());
        Map<String, ActiveType> typeMap = roleMap.get(classForT);
        if (typeMap == null) {
            typeMap = new HashMap<>();
            roleMap.put(classForT, typeMap);
        }
        typeMap.put(name, activeType);
    }

    ValueData createValue(OpenValue openValue) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return this.useDynamicTypes ? createValue0(openValue) : createValue1(openValue);
        } finally {
            getTransformance().add(Role.Value, Long.valueOf((int) (System.currentTimeMillis() - start)));
        }
    }

    ObjectData createObject(OpenObject openObject) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return this.useDynamicTypes ? createObject0(openObject) : createObject1(openObject);
        } finally {
            getTransformance().add(Role.Object, Long.valueOf((int) (System.currentTimeMillis() - start)));
        }
    }

    ArrayData createArray(OpenArray openArray) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return this.useDynamicTypes ? createArray0(openArray) : createArray1(openArray);
        } finally {
            getTransformance().add(Role.Array, Long.valueOf((int) (System.currentTimeMillis() - start)));
        }
    }

    /*
	 * OpenNode.OpenValue -> ValueData
     */
    private ValueData createValue0(OpenValue openValue) throws Exception {
        String name = openValue.name();
        Object value = openValue.value();
        ValueType vtype = ValueType.get(this.context, name);
        if (vtype == null) {
            vtype = ValueType.dynamic(this.context, name, value);
            if (vtype == null) {
                throw new Exception(String.format("ONO.createVData() - There is no %s named '%s' defined.",
                        ValueType.class.getSimpleName(), name));
            }
        }
        if (value instanceof String && ((String) value).isEmpty()) {
            return vtype.create();
        } else {
            return vtype.create(value);
        }
    }

    /*
	 * OpenNode.OpenValue -> ValueData
	 * 
	 * No dynamic types created.
     */
    private ValueData createValue1(OpenValue openValue) throws Exception {
        String name = openValue.name();
        Object value = openValue.value();
        ValueType vtype = ValueType.get(this.context, name);
        if (vtype == null) {
            return null;
        }
        if (value instanceof String && ((String) value).isEmpty()) {
            return vtype.create();
        } else {
            return vtype.create(value);
        }
    }

    /*
	 * OpenNode.OpenObject -> OData
     */
    private ObjectData createObject0(OpenObject openObject) throws Exception {
        String name = openObject.name();
        ObjectType otype = ObjectType.get(this.context, name);
        if (otype == null) {
            otype = this.getType(ObjectType.class, name);
        }
        if (otype == null) {
            otype = ObjectType.dynamic(this.context, name);
            this.reuseType(ObjectType.class, name, otype);
        }
        ObjectData.Builder builder = otype.builder();
        for (OpenNode openNode : openObject.nodes()) {
            switch (openNode.role()) {
                case Value:
                    builder.add(createValue((OpenValue) openNode));
                    break;
                case Object:
                    builder.add(createObject((OpenObject) openNode));
                    break;
                case Array:
                    ArrayData arrayData = createArray((OpenArray) openNode);
                    builder.add(arrayData);
                    break;
                default:
                    break;
            }
        }
        ObjectData objectData = builder.build();
        if (objectData == null) {
            throw new Exception("ObjectData.Builder() failed on: " + openObject.toString());
        }
        return objectData;
    }

    /*
	 * OpenNode.OpenObject -> OData
	 * 
	 * No dynamic types created.
     */
    private ObjectData createObject1(OpenObject openObject) throws Exception {
        String name = openObject.name();
        ObjectType otype = ObjectType.get(this.context, name);
        if (otype == null) {
            return null;
        }
        ObjectData.Builder builder = otype.builder();
        for (OpenNode openNode : openObject.nodes()) {
            switch (openNode.role()) {
                case Value:
                    builder.add(createValue((OpenValue) openNode));
                    break;
                case Object:
                    builder.add(createObject((OpenObject) openNode));
                    break;
                case Array:
                    ArrayData arrayData = createArray((OpenArray) openNode);
                    builder.add(arrayData);
                    break;
                default:
                    break;
            }
        }
        ObjectData objectData = builder.build();
        if (objectData == null) {
            throw new Exception("ObjectData.Builder() failed on: " + openObject.toString());
        }
        return objectData;
    }

    /*
	 * OpenNode.OpenArray -> AData
	 * 
	 * Special handling of ArrayType dynamic creation
     */
    private ArrayData createArray0(OpenArray openArray) throws Exception {
        String name = openArray.name();
        ArrayType atype = ArrayType.get(this.context, name);
        if (atype == null) {
            atype = this.getType(ArrayType.class, name);
        }
        if (atype == null) {
            atype = ArrayType.dynamic(this.context, name);
            this.reuseType(ArrayType.class, name, atype);
        }
        if (atype == null) {
            throw new Exception(String.format("ONO.createAData() - There is no %s named '%s' defined.",
                    ArrayType.class.getSimpleName(), name));
        }
        ArrayData adata = atype.create();
        for (OpenNode openNode : openArray.nodes()) {
            switch (openNode.role()) {
                case Value:
                    adata.add(createValue((OpenValue) openNode));
                    break;
                case Object:
                    adata.add(createObject((OpenObject) openNode));
                    break;
                case Array:
                    adata.add(createArray((OpenArray) openNode));
                    break;
                default:
                    break;
            }
        }
        return adata;
    }

    /*
	 * OpenNode.OpenArray -> AData
	 * 
	 * No dynamic types created.
     */
    private ArrayData createArray1(OpenArray openArray) throws Exception {
        String name = openArray.name();
        ArrayType atype = ArrayType.get(this.context, name);
        if (atype == null) {
            return null;
        }
        ArrayData adata = atype.create();
        for (OpenNode openNode : openArray.nodes()) {
            switch (openNode.role()) {
                case Value:
                    ValueData valueData = createValue((OpenValue) openNode);
                    if (valueData != null) {
                        adata.add(valueData);
                    }
                    break;
                case Object:
                    ObjectData objectData = createObject((OpenObject) openNode);
                    if (objectData != null) {
                        adata.add(objectData);
                    }
                    break;
                case Array:
                    ArrayData arrayData = createArray((OpenArray) openNode);
                    if (arrayData != null) {
                        adata.add(arrayData);
                    }
                    break;
                default:
                    break;
            }
        }
        return adata;
    }
}
