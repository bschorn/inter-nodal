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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveRef;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.ValueType;

/**
 *
 * @author schorn
 *
 */
public class ActiveRefImpl extends AbstractContextual implements ActiveRef {

    static final Logger LGR = LoggerFactory.getLogger(ActiveRefImpl.class);

    /**
     *
     *
     */
    static class ReferencesImpl implements ActiveRef.References {

        private final ObjectType objectType;
        private final Map<ReferenceType, List<ObjectType>> references;
        private final Map<ObjectType, List<ReferenceType>> reverse;

        ReferencesImpl(ObjectType objectType) {
            this.objectType = objectType;
            this.references = new HashMap<>();
            this.reverse = new HashMap<>();
        }

        @Override
        public String toString() {
            StringJoiner joiner1 = new StringJoiner("; ", this.objectType.name() + ": ", "");
            for (ReferenceType rt : ReferenceType.values()) {
                List<ObjectType> list = this.references.get(rt);
                if (list != null) {
                    StringJoiner joiner2 = new StringJoiner(", ", rt.name() + "->[ ", " ]");
                    list.stream().forEach(ot -> joiner2.add(ot.name()));
                    joiner1.add(joiner2.toString());
                }
            }
            return joiner1.toString();
        }

        @Override
        public ObjectType objectType() {
            return this.objectType;
        }

        @Override
        public boolean add(ReferenceType refType, ObjectType objectType) {
            List<ObjectType> list = this.references.get(refType);
            if (list == null) {
                list = new ArrayList<>();
                this.references.put(refType, list);
            }
            boolean retval = false;
            if (!list.stream().filter(ot -> ot.equals(objectType)).findAny().isPresent()) {
                list.add(objectType);
                retval = true;
            }
            List<ReferenceType> rlist = this.reverse.get(objectType);
            if (rlist == null) {
                rlist = new ArrayList<>();
                this.reverse.put(objectType, rlist);
            }
            if (!rlist.stream().filter(rt -> rt.equals(refType)).findAny().isPresent()) {
                rlist.add(refType);
            }

            return retval;
        }

        @Override
        public boolean del(ReferenceType refType, ObjectType objectType) {
            List<ObjectType> list = this.references.get(refType);
            if (list == null) {
                return false;
            }
            boolean retval = false;
            if (!list.stream().filter(ot -> ot.equals(objectType)).findAny().isPresent()) {
                list.remove(objectType);
                retval = true;
            }
            List<ReferenceType> rlist = this.reverse.get(refType);
            if (!rlist.stream().filter(ot -> ot.equals(refType)).findAny().isPresent()) {
                rlist.remove(refType);
            }
            return retval;
        }

        @Override
        public List<ObjectType> get(ReferenceType relationshipType) {
            List<ObjectType> objectTypes = this.references.get(relationshipType);
            if (objectTypes != null) {
                return Collections.unmodifiableList(objectTypes);
            } else {
                return new ArrayList<>(0);
            }
        }

        @Override
        public List<ReferenceType> get(ObjectType objectType) {
            List<ReferenceType> referenceTypes = this.reverse.get(objectType);
            if (referenceTypes != null) {
                return Collections.unmodifiableList(referenceTypes);
            } else {
                return new ArrayList<>(0);
            }
        }

        @Override
        public ObjectType getParentType() {
            List<ObjectType> parentTypes = this.get(ReferenceType.PARENT);
            if (parentTypes != null && parentTypes.size() > 0) {
                return parentTypes.get(0);
            }
            parentTypes = this.get(ReferenceType.PARENT_AS_ATTRIBUTE);
            if (parentTypes != null && parentTypes.size() > 0) {
                return parentTypes.get(0);
            }
            return this.objectType;
        }

        @Override
        public boolean isParentAsAttribute() {
            List<ObjectType> parentTypes = this.get(ReferenceType.PARENT_AS_ATTRIBUTE);
            if (parentTypes != null && parentTypes.size() > 0) {
                return true;
            }
            return false;
        }

        @Override
        public ObjectType getAttributeOf() {
            List<ObjectType> attributeOfTypes = this.get(ReferenceType.ATTRIBUTE);
            if (attributeOfTypes != null && attributeOfTypes.size() > 0) {
                return attributeOfTypes.get(0);
            }
            return null;
        }
    }
    /*
	 * "object_types": [
	 * 	{
	 * 		"name": "A",
	 * 		"domain_type": "entity",
	 * 		"member_types": [
	 * 			{ "value_type": "vtfield1", "bond_type": "surrogate_key" },
	 *			{ "value_type": "vtfield2", "bond_type": "optional" },
	 * 			{ "object_type": "otfield", "bond_type": "optional" },    <<<------- sibling
	 * 			{ "array_type": "atfield", "bond_type": "optional" }      <<<------- descendant
	 * 		]
	 * }
	 * 
	 * 
	 * Parent ObjectType -> Child ObjectType (member_type)
	 * 
	 * -> an attribute_object (or reversed hiearchy facility->deal) has a bond_type of 'immutable'
	 * 		and the parent has the key fields of the child as attributes
	 * -> a sister_object (or extension object) if the bond_type is 'optional'
	 * 		and the parent/child share their unique key (so they have the same key fields)
	 * 
	 * Parent ObjectType -> Child ArrayType (member_type)
	 * 
	 * -> a descendant_object (or one-to-many) has a bond_type of 'optional'
	 * 		and the child typically has the parent's key fields as 'immutable'
	 * 		or 'foreign_key'
     */

    private final Map<ObjectType, References> map;

    ActiveRefImpl(AppContext context) {
        super(context);
        this.map = new HashMap<>();
    }

    @Override
    public void autoCreateReferences() {
        this.autoCreateReferencesOneToOne();
        this.autoCreateReferencesOneToMany();
    }

    @Override
    public References get(ObjectType objectType) {
        References references = this.map.get(objectType);
        if (references == null) {
            synchronized (ActiveRef.class) {
                references = this.map.get(objectType);
                if (references == null) {
                    references = new ReferencesImpl(objectType);
                    this.map.put(objectType, references);
                }
            }
        }
        return references;
    }

    @Override
    public void createReference(ObjectType objectType, ReferenceType relationshipType, ObjectType memberType) {
        References references = this.get(objectType);
        references.add(relationshipType, memberType);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n", "", "");
        for (References references : this.map.values()) {
            joiner.add(references.toString());
        }
        return joiner.toString();
    }

    /*
	 * Search for Descendant Relationships in MetaData
     */
    private void autoCreateReferencesOneToMany() {
        for (ObjectType objectType : this.context().objectTypes()) {
            for (ArrayType memberType : objectType.arrayTypes()) {
                if (memberType.memberType().role().equals(Role.Value)) {
                    continue;
                }
                ObjectType arraysObjectType = (ObjectType) memberType.memberType();
                switch (objectType.getBondType(memberType)) {
                    case IMMUTABLE:
                    case OPTIONAL: {
                        int count = 0;
                        for (ValueType keyType : objectType.valueTypeKeys()) {
                            if (arraysObjectType.valueTypes().stream()
                                    .filter(vt -> vt.equals(keyType))
                                    .findAny()
                                    .isPresent()) {
                                count += 1;
                            }
                        }
                        if (count == objectType.valueTypeKeys().size()) {
                            createReference(objectType, ReferenceType.DESCENDANT, arraysObjectType);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

    /*
	 * Search for Sibling and Attribute Relationships in MetaData
     */
    private void autoCreateReferencesOneToOne() {
        for (ObjectType objectType : this.context().objectTypes()) {
            for (ObjectType memberType : objectType.objectTypes()) {

                int count = 0;
                for (ValueType keyType : memberType.valueTypeKeys()) {
                    if (objectType.valueTypeKeys().stream()
                            .filter(vt -> vt.equals(keyType))
                            .findAny()
                            .isPresent()) {
                        count += 1;
                    }
                }
                if (count == memberType.valueTypeKeys().size()) {
                    createReference(objectType, ReferenceType.SIBLING, memberType);
                }
                count = 0;
                for (ValueType keyType : memberType.valueTypeKeys()) {
                    if (objectType.valueTypes().stream()
                            .filter(vt -> vt.equals(keyType))
                            .findAny()
                            .isPresent()) {
                        count += 1;
                    }
                }
                if (count == memberType.valueTypeKeys().size()) {
                    createReference(objectType, ReferenceType.ATTRIBUTE, memberType);
                }
            }
        }
    }

}
