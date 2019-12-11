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
package org.schorn.ella.impl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.model.ActiveModel;
import org.schorn.ella.model.ModelProvider;

/**
 *
 * @author bschorn
 */
public class ModelProviderImpl extends AbstractProvider implements ModelProvider {

    static private final Map<AppContext, Map<ActiveModel.ModelRole, List<ActiveModel.ModelType>>> TYPES = new HashMap<>();

    @Override
    public void init() throws Exception {
        this.mapInterfaceToImpl(ActiveModel.ModelConstraintType.class, ModelConstraintTypeImpl.class);
        this.mapInterfaceToImpl(ActiveModel.ModelDataType.class, ModelDataTypeImpl.class);
        this.mapInterfaceToImpl(ActiveModel.ModelFieldType.class, ModelFieldTypeImpl.class);
        this.mapInterfaceToImpl(ActiveModel.ModelValueType.class, ModelValueTypeImpl.class);
        this.mapInterfaceToImpl(ActiveModel.ModelObjectType.class, ModelObjectTypeImpl.class);
        this.mapInterfaceToImpl(ActiveModel.ModelArrayType.class, ModelArrayTypeImpl.class);
    }

    @Override
    public void registerContext(AppContext context) throws Exception {

    }

    @Override
    public void addMember(ActiveModel.ModelObjectType objectType, ActiveModel.ModelMember member) {

    }

    private <T> List<T> getTypes(Class<T> classOfT, AppContext context, ActiveModel.ModelRole role) {
        Map<ActiveModel.ModelRole, List<ActiveModel.ModelType>> contextMap = TYPES.get(context);
        if (contextMap != null) {
            List<ActiveModel.ModelType> list = contextMap.get(role);
            if (list != null) {
                return list.stream()
                        .map(m -> classOfT.cast(m))
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void addType(ActiveModel.ModelType type) {
        AppContext context = AppContext.Model;
        Map<ActiveModel.ModelRole, List<ActiveModel.ModelType>> contextMap = TYPES.get(context);
        if (contextMap == null) {
            contextMap = new HashMap<>();
            TYPES.put(context, contextMap);
        }
        List<ActiveModel.ModelType> list = contextMap.get(type.modelRole());
        if (list == null) {
            list = new ArrayList<>();
            contextMap.put(type.modelRole(), list);
        }
        list.add(type);
    }

    @Override
    public List<ActiveModel.ModelDataType> dataTypes(AppContext Model) {
        return this.getTypes(ActiveModel.ModelDataType.class, Model, ActiveModel.ModelRole.DataType);
    }

    @Override
    public List<ActiveModel.ModelFieldType> fieldTypes(AppContext Model) {
        return this.getTypes(ActiveModel.ModelFieldType.class, Model, ActiveModel.ModelRole.FieldType);
    }

    @Override
    public List<ActiveModel.ModelValueType> valueTypes(AppContext Model) {
        return this.getTypes(ActiveModel.ModelValueType.class, Model, ActiveModel.ModelRole.ValueType);
    }

    @Override
    public List<ActiveModel.ModelObjectType> objectTypes(AppContext Model) {
        return this.getTypes(ActiveModel.ModelObjectType.class, Model, ActiveModel.ModelRole.ObjectType);
    }

    @Override
    public List<ActiveModel.ModelArrayType> arrayTypes(AppContext Model) {
        return this.getTypes(ActiveModel.ModelArrayType.class, Model, ActiveModel.ModelRole.ArrayType);
    }

}
