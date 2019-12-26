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
package org.schorn.ella.node;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import org.schorn.ella.Provider;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveRef;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.Constraints;
import org.schorn.ella.node.ActiveNode.INode;
import org.schorn.ella.node.ActiveNode.Identity;
import org.schorn.ella.node.ActiveNode.Identity.IdentityType;
import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ObjectType.ObjectSchema;
import org.schorn.ella.node.ActiveNode.TypeAttribute;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueType.DataType;
import org.schorn.ella.node.ActiveNode.ValueType.DefaultValue;
import org.schorn.ella.node.ActiveNode.ValueType.FieldType;
import org.schorn.ella.node.ActiveNode.ValueType.PrimitiveType;
import org.schorn.ella.node.ActiveNode.ValueTypeMember;

/**
 * ActiveNode Provider Interface
 *
 * @author schorn
 *
 */
public interface NodeProvider extends Provider {

    /**
     * Implementation for this interface is retrieved
     * @return
     */
    static NodeProvider provider() {
        return Provider.Providers.NODE.getInstance(NodeProvider.class);
    }

    /**
     * Every instance of data has a unique integer activeId ActiveData ->
     * ArrayData, ObjectData, ValueData
     *
     * Upon creation of a data instance an activeId is issued and instance is
     * cataloged. Any instance can be directly retrieved through this method
     * regardless of its status or whether or not its in the Repo.
     *
     * @param activeId
     * @return
     */
    ActiveData getActiveData(Integer activeId);

    /**
     * Implements getActiveData(Integer activeId) in bulk.
     *
     * Conditions are optional.
     *
     *
     * @param activeIds
     * @param conditions
     * @return
     */
    @SuppressWarnings("unchecked")
    List<ActiveData> getActiveData(List<Integer> activeIds, Predicate<ActiveData>... conditions);

    /**
     * Converting a data point from one type to another.
     *
     *
     * String -> LocalDate String -> LocalTime String -> LocalDateTime String ->
     * BigDecimal String -> Long String -> Boolean String -> Enum BigDecimal ->
     * Long
     *
     *
     * @param classFrom (String.class)
     * @param classTo (LocalDate.class)
     * @param valueFrom "20180704"
     * @return valueTo 2018-07-04
     */
    <R> R typeConvert(Class<?> classFrom, Class<R> classTo, Object valueFrom) throws Exception;

    /**
     * addType - adds types to the library
     *
     * Included Base Types * ArrayType * ObjectType * ValueType * FieldType *
     * DataType * MemberType * ConstraintType
     */
    <T> T addType(AppContext context, Class<T> classOfT, String name, Object typeObj);

    /**
     * getType - retrieves types from the library
     *
     * Included Types * (see addType)
     * @return 
     */
    <T> T getType(AppContext context, Class<T> classOfT, String type_name);

    /**
     * getList - gets all registered types for a give base type (see addType)
     *
     * @param context
     * @param classOfT
     * @return
     */
    <T> List<T> getList(AppContext context, Class<T> classOfT);

    Identity createIdentity(AppContext context, IdentityType identityType, String userId) throws Exception;

    INode createINode(ActiveData activeData, Identity identity);

    boolean registerINode(INode inode);

    INode retrieveINode(UUID inodeUUID);

    /*
	 * Meta-Type Creation Methods 
     */
    //<T> Constraints.ConstraintType<T> createConstraintType(Class<T> classOfT, DataGroup dataGroup, String name);
    Constraints.ConstraintType<?> getConstraintType(String tag);

    DataType createDataType(AppContext context, String data_type, PrimitiveType<?> primitiveType, Constraints.ConstraintType<?>[] constraintTypes) throws Exception;

    FieldType createFieldType(AppContext context, String field_type, DataType dataType, Constraints constraints, Integer maxWidth) throws Exception;

    ValueType createValueType(AppContext context, String value_type, FieldType fieldType, EnumSet<ValueFlag> valueFlags) throws Exception;

    ValueType createDynamicValueType(AppContext context, String value_type, Object value) throws Exception;

    ObjectType createObjectType(AppContext context, String object_type, ObjectSchema objectMembers, List<TypeAttribute> attributes, List<ObjectType> baseTypes) throws Exception;

    ObjectType createTransientObjectType(AppContext context, String object_type, ObjectSchema objectMembers) throws Exception;

    ArrayType createArrayType(AppContext context, String array_type, ActiveType activeType, BondType bondType) throws Exception;

    ArrayType createTransientArrayType(AppContext context, String array_type, ActiveType activeType, BondType bondType) throws Exception;

    MemberDef createMemberDef(ActiveType activeType, BondType bondType, int index) throws Exception;

    Object setDefaultValue(ValueType valueType, Object value);

    Object setDefaultValue(ValueType valueType, DefaultValue defaultValue);

    Object getDefaultValue(ValueType valueType);

    /**
     * Return the ActiveRef for the given context.
     *
     * @param context
     * @return
     */
    ActiveRef getActiveRef(AppContext context);

    ArrayType getArrayType(AppContext context, String array_type);

    /**
     *
     * @param context
     * @param object_type
     * @return
     */
    ObjectType getObjectType(AppContext context, String object_type);

    ValueType getValueType(AppContext context, String value_type);

    ValueTypeMember createValueMemberType(ObjectType objectType, MemberDef memberDef) throws Exception;

    ValueTypeMember createValueMemberType(AppContext context, String valueTypeMemberStr) throws Exception;

}
