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
package org.schorn.ella.impl.repo;

import java.util.function.Function;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType.ObjectSchema;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;

/**
 *
 * @author schorn
 *
 */
public class CopyReduce extends AbstractContextual implements Function<StructData, StructData> {

    private final ActiveQuery activeQuery;

    public CopyReduce(ActiveQuery activeQuery) {
        super(activeQuery.context());
        this.activeQuery = activeQuery;
    }

    /**
     *
     * @param structData
     */
    @Override
    public StructData apply(StructData structData) {
        try {
            switch (structData.role()) {
                case Object:
                    return createObject((ObjectData) structData);
                case Array:
                    return createArray((ArrayData) structData);
                default:
                    break;
            }
        } catch (Exception ex) {
            this.setException(ex);
        }
        return null;
    }

    /**
     *
     * @param arrayData
     * @return
     * @throws java.lang.Exception
     */
    public ArrayData createArray(ArrayData arrayData) throws Exception {
        ArrayData newData = arrayData.arrayType().create();
        for (ActiveData activeData : arrayData.nodes()) {
            switch (activeData.role()) {
                case Value:
                    ValueData vdata = (ValueData) activeData;
                    if (this.activeQuery.isSelected(arrayData.arrayType(), vdata.valueType())) {
                        newData.add(vdata);
                    }
                    break;
                case Object:
                    ObjectData oData = createObject((ObjectData) activeData);
                    if (oData != null && !oData.isNull()) {
                        newData.add(oData);
                    }
                    break;
                case Array:
                    ArrayData aData = createArray((ArrayData) activeData);
                    if (aData.size() > 0) {
                        newData.add(aData);
                    }
                    break;
                default:
                    break;
            }
        }
        return newData;
    }

    /**
     *
     * @param objectData
     * @return
     * @throws java.lang.Exception
     */
    public ObjectData createObject(ObjectData objectData) throws Exception {
        ObjectData.Builder builder = objectData.objectType().builder();
        ObjectSchema schema = objectData.objectType().schema();
        for (MemberDef memberDef : schema.memberDefs()) {
            ActiveData activeData = objectData.get(memberDef.activeType());
            if (activeData == null) {
                if (memberDef.activeType().role().equals(Role.Value)) {
                    if (this.activeQuery.isSelected(objectData.objectType(), (ValueType) memberDef.activeType())) {
                        builder.add(memberDef.activeType(), null);
                    }
                }
            } else {
                switch (activeData.role()) {
                    case Value:
                        ValueData vdata = (ValueData) activeData;
                        if (this.activeQuery.isSelected(objectData.objectType(), vdata.valueType())) {
                            builder.add(vdata);
                        }
                        break;
                    case Object:
                        ObjectData oData = createObject((ObjectData) activeData);
                        if (oData != null && !oData.isNull()) {
                            builder.add(oData);
                        }
                        break;
                    case Array:
                        ArrayData aData = createArray((ArrayData) activeData);
                        if (aData.size() > 0) {
                            builder.add(aData);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        if (builder.isEmpty()) {
            return null;
        }
        return builder.build();
    }
}
