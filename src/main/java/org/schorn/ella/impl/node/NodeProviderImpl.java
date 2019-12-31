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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.schorn.ella.Mingleton;
import org.schorn.ella.Renewable;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.convert.TypeConversionImpl;
import org.schorn.ella.convert.TypeConverter;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveRef;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.Constraints;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType;
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
import org.schorn.ella.node.BondType;
import org.schorn.ella.node.NodeProvider;
import org.schorn.ella.node.ValueFlag;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author schorn
 *
 */
public class NodeProviderImpl extends AbstractProvider implements NodeProvider {

    private static final Logger LGR = LoggerFactory.getLogger(NodeProviderImpl.class);

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                                
	 *                                MEMBERS
	 *                                
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private List<Class<? extends Mingleton>> mingletons = new ArrayList<>();
    private List<Class<? extends Renewable<?>>> renewables = new ArrayList<>();
    private Map<AppContext, TypeLibrary> library = new HashMap<>();
    private TypeConverter converter = new TypeConverter();
    private Map<UUID, INode> inodes = new TreeMap<>();
    private Map<ValueType, Object> defaultValues = new HashMap<>();
    private Map<String, Class<? extends ConstraintType<?>>> constraintTypes = new HashMap<>();

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                                
	 *                                METHODS
	 *                                
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void init() {
        this.mapInterfaceToImpl(ActiveNode.Config.class, NodeConfigImpl.class);
        this.mapInterfaceToImpl(ActiveNode.ObjectType.Builder.class, ObjectTypeBuilderImpl.class);
        this.mapInterfaceToImpl(ActiveNode.Constraints.Builder.class, ConstraintsBuilderImpl.class);
        this.mapInterfaceToImpl(ActiveNode.ValueType.DataType.class, DataTypeImpl.class);
        this.mapInterfaceToImpl(ActiveNode.Constraints.ConstraintType.class, ConstraintsImpl.ConstraintTypeImpl.class);
        this.mapInterfaceToImpl(ActiveNode.ValueType.FieldType.class, FieldTypeImpl.class);
        this.mapInterfaceToImpl(ActiveNode.ValueType.class, ValueTypeImpl.class);
        this.mapInterfaceToImpl(ActiveNode.ObjectType.class, ObjectTypeImpl.class);
        this.mapInterfaceToImpl(ActiveNode.MemberDef.class, MemberDefImpl.class);
        this.mapInterfaceToImpl(ActiveNode.ArrayType.class, ArrayTypeImpl.class);
        this.mapInterfaceToImpl(ActiveNode.Identity.class, IdentityImpl.class);
        this.mapInterfaceToImpl(ActiveNode.INode.class, INodeImpl.class);
        this.mapInterfaceToImpl(ActiveNode.ValueData.class, ValueDataImpl.class);
        this.mapInterfaceToImpl(ActiveNode.ObjectData.class, ObjectDataImpl.class);
        this.mapInterfaceToImpl(ActiveNode.ArrayData.class, ArrayDataImpl.class);
        this.mapInterfaceToImpl(ActiveNode.ActiveRef.class, ActiveRefImpl.class);
        this.mapInterfaceToImpl(ActiveNode.TypeConversion.class, TypeConversionImpl.class);
        this.mapInterfaceToImpl(ActiveNode.ObjectData.KeyGenerator.class, KeyGeneratorImpl.class);
        this.mapInterfaceToImpl(ActiveNode.ValueTypeMember.class, ValueTypeMemberImpl.class);

        this.mingletons.add(ActiveNode.ActiveRef.class);
        this.mingletons.add(ActiveNode.ObjectData.KeyGenerator.class);

        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.holidays.name(), StandardConstraintType.Holidays.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.day_of_week.name(), StandardConstraintType.DayOfWeek.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.max_date.name(), StandardConstraintType.MaxDate.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.max_datetime.name(), StandardConstraintType.MaxDateTime.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.max_decimal.name(), StandardConstraintType.MaxDecimal.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.max_integer.name(), StandardConstraintType.MaxInteger.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.max_time.name(), StandardConstraintType.MaxTime.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.min_date.name(), StandardConstraintType.MinDate.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.min_datetime.name(), StandardConstraintType.MinDateTime.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.min_decimal.name(), StandardConstraintType.MinDecimal.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.min_integer.name(), StandardConstraintType.MinInteger.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.min_time.name(), StandardConstraintType.MinTime.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.pattern.name(), StandardConstraintType.StringPattern.class);
        this.constraintTypes.put(ActiveNode.Constraints.ConstraintType.StandardTypes.list.name(), StandardConstraintType.EnumeratedList.class);

    }

    /**
     * Register Context
     *
     * Give us a change to create the context specific objects.
     *
     * @throws Exception
     */
    @Override
    public void registerContext(AppContext context) throws Exception {
        for (Class<?> classFor : this.mingletons) {
            this.createReusable(classFor, context);
            LGR.info(String.format("%s.registerContext('%s') - create Mingleton: %s",
                    this.getClass().getSimpleName(),
                    context.name(),
                    classFor.getSimpleName()
            ));
        }
        for (Class<?> classFor : this.renewables) {
            this.createReusable(classFor, context);
            LGR.info(String.format("%s.registerContext('%s') - create Renewable: %s",
                    this.getClass().getSimpleName(),
                    context.name(),
                    classFor.getSimpleName()
            ));
        }

    }

    @Override
    public <R> R typeConvert(Class<?> classFrom, Class<R> classTo, Object valueFrom) throws Exception {
        return this.converter.convert(classFrom, classTo, valueFrom);
    }

    /*
	 * Identity
     */
    @Override
    public Identity createIdentity(AppContext context, IdentityType identityType, String userId) throws Exception {
        Identity identity = IdentityImpl.create(context, identityType, userId);
        context.addIdentity(identity);
        return identity;
    }

    /*
	 * INode
     */
    @Override
    public INode createINode(ActiveData activeData, Identity identity) {
        INode inode = INodeImpl.create(activeData, identity);
        registerINode(inode);
        return inode;
    }

    @Override
    public boolean registerINode(INode inode) {
        INode oldINode = this.inodes.put(inode.uuid(), inode);
        return oldINode == null;
    }

    @Override
    public INode retrieveINode(UUID inodeUUID) {
        return this.inodes.get(inodeUUID);
    }

    /*
	 * Create and add to Library
	 * 
     */
    @Override
    public DataType createDataType(AppContext context, String data_type, PrimitiveType<?> primitiveType,
            ConstraintType<?>[] constraintTypes) throws Exception {
        return this.addType(context, DataType.class, data_type,
                createInstance(DataType.class, context, data_type, primitiveType, constraintTypes));
    }

    /*
	@SuppressWarnings("unchecked")
	@Override
	public <T> ConstraintType<T> createConstraintType(Class<T> classOfT, DataGroup dataGroup, String name) {
		return (ConstraintType<T>) ConstraintsImpl.createConstraintType(classOfT, dataGroup, name);
	}
     */
    @Override
    public ConstraintType<?> getConstraintType(String tag) {
        try {
            Class<? extends ConstraintType<?>> classOf = this.constraintTypes.get(tag);
            if (classOf != null) {
                //return classOf.getConstructor().newInstance();
                return classOf.newInstance();
            }
        } catch (Exception ex) {
            LGR.error(Functions.stackTraceToString(ex));
        }
        LGR.error(String.format("%s.getConstraintType() - '%s' was not found.", this.getClass().getSimpleName(), tag));
        return null;
    }

    @Override
    public FieldType createFieldType(AppContext context, String field_type, DataType dataType, Constraints constraints, Integer maxWidth) throws Exception {
        //return this.addType(context, FieldType.class, field_type,
        //      createInstance(FieldType.class, context, field_type, dataType, constraints, maxWidth));
        return (FieldType) context.addType(
                createInstance(
                        FieldType.class,
                        context,
                        field_type,
                        dataType,
                        constraints,
                        maxWidth,
                        Integer.valueOf(context.fieldTypes().size()).shortValue()));
    }

    @Override
    public ValueType createValueType(AppContext context, String value_type, FieldType fieldType, EnumSet<ValueFlag> valueFlags) throws Exception {
        return (ValueType) context.addType(createInstance(ValueType.class, context, value_type, fieldType,
                Integer.valueOf(context.valueTypes().size()).shortValue(), ValueFlag.getLongFromSet(valueFlags)));
    }

    @Override
    public ValueType createDynamicValueType(AppContext context, String value_type, Object value) throws Exception {
        FieldType fieldType = FieldType.forClass(value.getClass());
        return createInstance(ValueType.class, context, value_type, fieldType, Short.valueOf((short) -1));
    }

    /**
     *
     * @param context
     * @param object_type
     * @param objectMembers
     * @return
     * @throws Exception
     */
    @Override
    public ObjectType createObjectType(AppContext context, String object_type,
            ObjectSchema objectMembers, List<TypeAttribute> attributes,
            List<ObjectType> parentTypes) throws Exception {
        return (ObjectType) context.addType(createInstance(ObjectType.class, context, object_type, objectMembers,
                Integer.valueOf(context.objectTypes().size()).shortValue(), attributes, parentTypes));
    }

    @Override
    public ObjectType createTransientObjectType(AppContext context,
            String object_type, ObjectSchema objectMembers) throws Exception {
        return createInstance(ObjectType.class, context, object_type, objectMembers, Short.valueOf((short) -1));
    }

    @Override
    public ArrayType createArrayType(AppContext context, String array_type,
            ActiveType activeType, BondType bondType) throws Exception {
        MemberDef memberDef = null;
        if (activeType != null) {
            if (bondType == null) {
                bondType = BondType.OPTIONAL;
            }
            memberDef = MemberDef.create(activeType, bondType, 0);
        }
        return (ArrayType) context.addType(createInstance(ArrayType.class, context, array_type, memberDef,
                Integer.valueOf(context.arrayTypes().size()).shortValue()));
    }

    @Override
    public ArrayType createTransientArrayType(AppContext context, String array_type, ActiveType activeType, BondType bondType) throws Exception {
        MemberDef memberDef = null;
        if (activeType != null) {
            if (bondType == null) {
                bondType = BondType.OPTIONAL;
            }
            memberDef = MemberDef.create(activeType, bondType, 0);
        }
        return (ArrayType) createInstance(ArrayType.class, context, array_type, memberDef, Short.valueOf((short) -1));
    }

    @Override
    public MemberDef createMemberDef(ActiveType activeType, BondType bondType, int index) throws Exception {
        return createInstance(MemberDef.class, activeType, bondType, index);
    }

    @Override
    public ActiveRef getActiveRef(AppContext context) {
        return this.getMingleton(ActiveRef.class, context);
    }

    /*
     * Add Type
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T addType(AppContext context, Class<T> classOfT, String name, Object typeObj) {
        TypeLibrary typeLibrary = this.library.get(context);
        if (typeLibrary == null) {
            typeLibrary = new TypeLibrary(context);
            this.library.put(context, typeLibrary);
        }
        typeLibrary.set(classOfT, name, typeObj);
        return (T) typeObj;
    }

    /*
     * Get Type
     */
    @Override
    public <T> T getType(AppContext context, Class<T> classOfT, String type_name) {
        TypeLibrary typeLibrary = this.library.get(context);
        if (typeLibrary == null) {
            return null;
        }
        return typeLibrary.get(classOfT, type_name);
    }

    /*
     * Get List
     */
    @Override
    public <T> List<T> getList(AppContext context, Class<T> classOfT) {
        TypeLibrary typeLibrary = this.library.get(context);
        if (typeLibrary == null) {
            return null;
        }
        return typeLibrary.get(classOfT);
    }

    /**
     * Get a data instance by id
     */
    @Override
    public ActiveData getActiveData(Integer activeId) {
        return ActiveDataImpl.forActiveId(activeId);
    }

    /**
     * Get a list of data instances by id (conditions are optional)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ActiveData> getActiveData(List<Integer> activeIds, Predicate<ActiveData>... predicates) {
        Predicate<ActiveData> filters = ad -> false;
        for (Predicate<ActiveData> predicate : predicates) {
            filters = filters.or(predicate);
        }
        return activeIds.stream()
                .map(activeId -> ActiveDataImpl.forActiveId(activeId))
                .filter(activeData -> activeData != null)
                .filter(filters)
                .collect(Collectors.toList());
    }

    @Override
    public Object setDefaultValue(ValueType valueType, Object value) {
        return this.defaultValues.put(valueType, value);
    }

    @Override
    public Object setDefaultValue(ValueType valueType, DefaultValue value) {
        return this.defaultValues.put(valueType, value);
    }

    @Override
    public Object getDefaultValue(ValueType valueType) {
        return this.defaultValues.get(valueType);
    }

    @Override
    public ArrayType getArrayType(AppContext context, String array_type) {
        return context.getArrayType(array_type);
    }

    @Override
    public ObjectType getObjectType(AppContext context, String object_type) {
        return context.getObjectType(object_type);
    }

    @Override
    public ValueType getValueType(AppContext context, String value_type) {
        return context.getValueType(value_type);
    }

    @Override
    public ValueTypeMember createValueMemberType(ObjectType objectType, MemberDef memberDef) throws Exception {
        return createInstance(ValueTypeMember.class, objectType, memberDef);
    }

    @Override
    public ValueTypeMember createValueMemberType(AppContext context, String valueTypeMemberStr) throws Exception {
        return createInstance(ValueTypeMember.class, context, valueTypeMemberStr);
    }

}
