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
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.BondType;
import org.schorn.ella.node.MetaTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Object Data Implementation
 *
 * Immutable Data Object
 *
 *
 * @author schorn
 *
 */
public class ObjectDataImpl extends StructDataImpl implements ObjectData {

    static final Logger LGR = LoggerFactory.getLogger(ObjectDataImpl.class);

    private static List<ActiveType> ZERO = new ArrayList<>(0);
    //private final SeriesKey keyData;
    private final boolean isNull;
    private final boolean isBlank;
    private final boolean isIncomplete;
    boolean persisted = false;
    private List<ActiveType> missingTypes = ZERO;

    /**
     *
     * @param objectType
     * @param members
     * @param keyData
     */
    protected ObjectDataImpl(ObjectType objectType, List<ActiveData> members) {
        super(objectType, members);
        boolean nonBlank = false;
        boolean nonNull = false;
        for (ActiveData ad : members) {
            if (ad != null && !ad.isNull()) {
                nonNull = true;
                if (!ad.isBlank()) {
                    nonBlank = true;
                    break;
                }
            }
        }
        this.isBlank = nonBlank == false;
        this.isNull = nonNull == false;
        boolean isIncomplete = false;
        for (MemberDef memberDef : this.objectType().schema().memberDefs()) {
            if (memberDef.index() >= this.nodes().size()) {
                isIncomplete = true;
            } else {
                ActiveData activeData = this.nodes().get(memberDef.index());
                if (activeData == null) {
                    isIncomplete = true;
                }
            }
        }
        this.isIncomplete = isIncomplete;
    }

    protected ObjectDataImpl(ObjectType objectType, List<ActiveData> members, List<ActiveType> missingTypes) {
        this(objectType, members);
        this.missingTypes = missingTypes;
    }

    @Override
    public boolean isIncomplete() {
        return this.isIncomplete;
    }

    @Override
    public List<ActiveType> missingTypes() {
        return this.missingTypes;
    }

    @Override
    public List<ActiveData> nodes() {
        return this.nodes_immutable_non_null();
    }

    @Override
    public int size() {
        return this.size_nodes_immutable_non_null();
    }

    @Override
    public Object activeValue() {
        StringJoiner joiner = new StringJoiner("\t", "", "");
        this.nodes().stream()
                .forEach(n -> joiner.add(n.activeValue().toString()));
        return joiner.toString();
    }

    @Override
    public List<Object> activeValues() {
        return this.nodes().stream()
                .map(ad -> ad.activeValue())
                .collect(Collectors.toList());
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT) {
        return this.nodes().stream()
                .filter(ad -> classForT.isInstance(ad))
                .map(ad -> classForT.cast(ad))
                .collect(Collectors.toList());
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, Predicate<T> filter) {
        return this.nodes_mutable_all().stream()
                .filter(ad -> classForT.isInstance(ad))
                .map(ad -> classForT.cast(ad))
                .filter(filter)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> activeValues(int startIdx, int length) {
        if (startIdx >= this.nodes().size()
                || startIdx < 0
                || length <= 0) {
            return new ArrayList<>(0);
        }
        return this.nodes().stream()
                .skip(startIdx)
                .limit(length)
                .map(ad -> Object.class.cast(ad))
                .collect(Collectors.toList());
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, int startIdx, int length) {
        return this.activeValues(classForT).stream()
                .skip(startIdx)
                .limit(length)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isNull() {
        return this.isNull;
    }

    @Override
    public boolean isBlank() {
        return this.isBlank;
    }

    @Override
    public Builder replicate() throws Exception {
        ObjectData.Builder builder = this.objectType().builder();
        replicate0(this, builder);
        if (this.objectType().isMember(MetaTypes.AutoTypes.over.valueType())) {
            builder.add(MetaTypes.AutoTypes.over.valueType().create(this.getVersion() + 1));
        }
        return builder;
    }

    /*
	 * 
	 * 
     */
    private void replicate0(ObjectData parentData, ObjectData.Builder parentBuilder) throws Exception {
        for (ValueData valueData : parentData.activeValues(ValueData.class, ActiveNode.SKIP_AUTO_TYPES)) {
            //parentBuilder.add(valueData.valueType().create(valueData.activeValue()));
            // to save space we will reuse the data object
            parentBuilder.add(valueData);
        }
        /*
		for (ObjectData objectData : parentData.activeValues(ObjectData.class)) {
			ObjectData.Builder childBuilder = objectData.replicant();
			replicate(objectData, childBuilder);
			parentBuilder.add(childBuilder.build());
		}
		for (ArrayData arrayData : parentData.activeValues(ArrayData.class)) {
			ArrayData arrayDitto = arrayData.arrayType().create();
			switch (arrayData.arrayType().memberType().role()) {
			case Object:
				for (ObjectData objectRow : arrayData.activeValues(ObjectData.class)) {
					ObjectData.Builder rowBuilder = objectRow.replicant();
					
				}
				break;
			case Value:
				break;
			default:
				break;
			}
		}
         */
    }

    @Override
    public boolean appendChild(ObjectData childData) {
        MemberDef memberDef = this.objectType().schema().get(childData.objectType());
        if (memberDef != null && memberDef.activeType().role().equals(Role.Object)) {
            return addSibling(childData);
        }
        ArrayType childArrayType = ArrayType.get(childData.context(), childData.name());
        if (childArrayType != null) {
            return addChild(childData);
        }
        return false;
    }

    @Override
    public boolean addSibling(ObjectData siblingData) {
        MemberDef memberDef = this.objectType().schema().get(siblingData.objectType());
        if (memberDef != null && memberDef.bondType().equals(BondType.OPTIONAL)) {
            this.nodes_mutable_all().set(memberDef.index(), siblingData);
            siblingData.setParent(this);
            return true;
        }
        LGR.error("{}.addSibling() - can not add {} to {}",
                this.getClass().getSimpleName(),
                siblingData.name(),
                this.name());
        return false;
    }

    @Override
    public boolean addChild(ObjectData childData) {
        ArrayType arrayType = ArrayType.get(this.context(), childData.name());
        if (arrayType == null) {
            return false;
        }
        MemberDef memberDef = this.objectType().schema().get(arrayType);
        if (memberDef != null && memberDef.bondType().equals(BondType.OPTIONAL)) {
            ArrayData arrayData = (ArrayData) this.get(memberDef.activeType());
            if (arrayData == null) {
                arrayData = arrayType.create();
                this.nodes_mutable_all().set(memberDef.index(), arrayData);
            }
            arrayData.add(childData);
            return true;
        }
        LGR.error("{}.addChild() - can not add {} to {}",
                this.getClass().getSimpleName(),
                childData.name(),
                this.name());
        return false;
    }

    @Override
    public ActiveData get(int index) {
        return super.get0(index);
    }

}
