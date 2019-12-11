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
package org.schorn.ella.extension;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.DataGroup;
import org.schorn.ella.node.BondType;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.Constraints;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintData;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType;
import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.util.Functions;

/**
 *
 * @author schorn
 *
 */
public interface ActiveMeta {

    /**
     *
     * @return
     */
    default ObjectData getMetaObject() {
        if (this instanceof ActiveType) {
            try {
                ActiveType activeType = (ActiveType) this;
                switch (activeType.role()) {
                    case Value:
                        return Support.getMetaObject((ValueType) activeType).build();
                    case Object:
                        if (activeType instanceof ObjectType) {
                            return Support.getMetaObject((ObjectType) activeType).build();
                        }
                        break;
                    case Array:
                        if (activeType instanceof ArrayType) {
                            return Support.getMetaObject((ArrayType) activeType).build();
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception ex) {

            }
        }
        return null;
    }

    default ObjectData getContainerTypes() {
        if (this instanceof ActiveType) {
            return Support.getContainerTypes((ActiveType) this);
        }
        return null;
    }

    default ObjectType getForeignKeyOwnerType() {
        if (this instanceof ActiveType) {
            return Support.getForeignKeyOwnerType((ActiveType) this);
        }
        return null;
    }

    /**
     *
     * Static Implementations
     *
     */
    static class Support {

        private static final Logger LGR = LoggerFactory.getLogger(Support.class);

        /**
         *
         * @param valueType
         * @return
         */
        static ObjectData.Builder getMetaObject(ValueType valueType) {
            try {
                ObjectData.Builder metaObj = ObjectType.dynamic(valueType.context(), valueType.name()).builder();
                //metaObj.add(ValueType.dynamic(valueType.context(), "active_id", Integer.valueOf(0)).create(valueType.activeId()));
                metaObj.add(ValueType.dynamic(valueType.context(), "primitive_type", new String("")).create(valueType.fieldType().dataType().primitiveType().name()));
                metaObj.add(ValueType.dynamic(valueType.context(), "data_group", new String("")).create(valueType.fieldType().dataType().primitiveType().dataGroup().name()));
                Constraints constraints = valueType.fieldType().constraints();
                for (ConstraintType<?> constraintType : constraints.constraintTypes()) {
                    ConstraintData constraintData = constraints.constraint(constraintType);
                    List<Object> constraintValues = constraintData.constraintValues();
                    if (constraintType.dataGroup().equals(DataGroup.ENUM) || constraintValues.size() > 1) {
                        StringJoiner joiner = new StringJoiner(",", "", "");
                        constraintValues.forEach(obj -> joiner.add(obj.toString()));
                        metaObj.add(ValueType.dynamic(valueType.context(), constraintType.name(), new String("")).create(joiner.toString()));
                    } else if (constraintValues.size() == 1) {
                        metaObj.add(ValueType.dynamic(valueType.context(), constraintType.name(), new String("")).create(constraintValues.get(0)));
                    }
                }
                return metaObj;
            } catch (Exception ex) {
                LGR.error(Functions.getStackTraceAsString(ex));
                return null;
            }
        }

        /**
         *
         * @param objectType
         * @return
         */
        static ObjectData.Builder getMetaObject(ObjectType objectType) {
            try {
                ObjectData.Builder metaObj = ObjectType.dynamic(objectType.context(), objectType.name()).builder();
                //metaObj.add(ValueType.dynamic(objectType.context(), "active_id", Integer.valueOf(0)).create(objectType.activeId()));
                for (MemberDef memberDef : objectType.schema().memberDefs()) {
                    ActiveType activeType = memberDef.activeType();
                    BondType bondType = memberDef.bondType();
                    ObjectData.Builder memberObj;
                    switch (activeType.role()) {
                        case Object:
                            memberObj = getMetaObject((ObjectType) activeType);
                            break;
                        case Array:
                            memberObj = getMetaObject((ArrayType) activeType);
                            break;
                        default:
                            memberObj = getMetaObject((ValueType) activeType);
                            break;
                    }
                    memberObj.add(ValueType.dynamic(activeType.context(), "bond_type", new String("")).create(bondType.name().toLowerCase()));
                    metaObj.add(memberObj.build());
                }
                return metaObj;
            } catch (Exception ex) {
                LGR.error(Functions.getStackTraceAsString(ex));
                return null;
            }
        }

        /**
         *
         * @param arrayType
         * @return
         */
        static ObjectData.Builder getMetaObject(ArrayType arrayType) {
            try {
                ObjectData.Builder metaObj = ObjectType.dynamic(arrayType.context(), arrayType.name()).builder();
                //metaObj.add(ValueType.dynamic(arrayType.context(), "active_id", Integer.valueOf(0)).create(arrayType.activeId()));
                MemberDef memberDef = arrayType.memberDef();
                ActiveType activeType = memberDef.activeType();
                BondType bondType = memberDef.bondType();
                ObjectData.Builder memberObj;
                switch (activeType.role()) {
                    case Object:
                        memberObj = getMetaObject((ObjectType) activeType);
                        break;
                    case Array:
                        memberObj = getMetaObject((ArrayType) activeType);
                        break;
                    default:
                        memberObj = getMetaObject((ValueType) activeType);
                }
                memberObj.add(ValueType.dynamic(activeType.context(), "bond_type", new String("")).create(bondType.name().toLowerCase()));
                metaObj.add(memberObj.build());
                return metaObj;
            } catch (Exception ex) {
                LGR.error(Functions.getStackTraceAsString(ex));
                return null;
            }
        }

        /**
         *
         * @param activeType
         * @return
         */
        static ObjectData getContainerTypes(ActiveType activeType) {
            try {
                AppContext context = activeType.context();
                List<ObjectType> allObjectTypes = context.objectTypes();
                List<ObjectType> containingObjectTypes = allObjectTypes.stream()
                        .filter(ot -> ot.schema().getIndex(activeType) != null && ot.schema().getIndex(activeType) != -1)
                        .collect(Collectors.toList());

                containingObjectTypes.sort((a, b) -> a.name().compareToIgnoreCase(b.name()));

                ObjectData.Builder metaObj = ObjectType.dynamic(context, activeType.name()).builder();

                for (ObjectType objectType : containingObjectTypes) {
                    ObjectData.Builder ownerObj = ObjectType.dynamic(objectType.context(), objectType.name()).builder();
                    MemberDef memberDef = objectType.schema().get(activeType);
                    ValueData bondValue = ValueType.dynamic(activeType.context(), "bond_type",
                            memberDef.bondType().name().toLowerCase()).create(memberDef.bondType().name().toLowerCase());
                    ownerObj.add(bondValue);
                    metaObj.add(ownerObj.build());
                }
                return metaObj.build();
            } catch (Exception ex) {
                LGR.error(Functions.getStackTraceAsString(ex));
            }
            return null;

        }

        /**
         *
         * @param activeType
         * @return
         */
        static ObjectType getForeignKeyOwnerType(ActiveType activeType) {
            /*	    	
	    	try {
		    	NodeContext nodeContext = activeType.context();
		    	List<ObjectType> allObjectTypes = nodeContext.Library().getTypes(ObjectType.class);
		    	List<ObjectType> containingObjectTypes = allObjectTypes.stream().filter(ct -> !ct.isHidden()).filter(t -> t.isMember(activeType)).collect(Collectors.toList());
		    	
				for (ObjectType compositeType : containingObjectTypes) {
					if (compositeType.isUniqueKey(activeType)) {
						return compositeType;
					}
				}
	    	} catch (Exception ex) {
	    		LGR.error(Functions.getStackTraceAsString(ex));
	    	}
             */
            return null;
        }

    }
}
