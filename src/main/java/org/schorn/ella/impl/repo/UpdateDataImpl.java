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

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.MetaTypes;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.node.OpenNode.OpenArray;
import org.schorn.ella.node.OpenNode.OpenObject;
import org.schorn.ella.node.OpenNode.OpenValue;
import org.schorn.ella.repo.RepoData;
import org.schorn.ella.repo.RepoSupport.UpdateData;
import org.schorn.ella.util.StringCached;

/**
 *
 *
 * @author schorn
 *
 */
public class UpdateDataImpl extends AbstractContextual implements UpdateData {

    private ConcurrentHashMap<Integer, Thread> OKEY_LOCK = new ConcurrentHashMap<>();

    private final ObjectType targetType;
    private final OpenNode openNode;
    private String statusDescription = null;
    private UpdateData.Status status = UpdateData.Status.Inprocess;
    private boolean aggregateLevelTransactions = true;
    private OpenObject updateObj = null;
    private StringCached updateKey = null;
    private Integer updateVersion = null;
    private ObjectData updatedData = null;

    public UpdateDataImpl(AppContext context) {
        super(context);
        this.targetType = null;
        this.openNode = null;
    }

    public UpdateDataImpl(AppContext context, ObjectType targetType, OpenNode openNode) {
        super(context);
        this.targetType = targetType;
        this.openNode = openNode;
        this.init();
    }

    /**
     *
     */
    @Override
    public UpdateData renew(Object... params) {
        OpenNode openNode = this.openNode;
        ObjectType targetType = this.targetType;
        for (Object param : params) {
            if (param instanceof OpenNode.OpenObject) {
                openNode = (OpenNode) param;
            } else if (param instanceof ObjectType) {
                targetType = (ObjectType) param;
            }
        }
        return new UpdateDataImpl(this.context(), targetType, openNode);
    }

    /*
	 * Initialize
     */
    protected void init() {
        if (this.openNode.role() == Role.Object) {
            OpenNode.OpenObject openObject = (OpenNode.OpenObject) this.openNode;
            if (openObject.name().equals(this.targetType.name())) {
                this.updateObj = openObject;
                OpenValue ukeyValue = this.updateObj.getValue(MetaTypes.AutoTypes.okey.valueType().name());
                if (ukeyValue != null) {
                    this.updateKey = new StringCached(ukeyValue.value().toString());
                    OpenValue versionValue = this.updateObj.getValue(MetaTypes.AutoTypes.over.valueType().name());
                    if (versionValue != null) {
                        this.updateVersion = Integer.valueOf(versionValue.value().toString());
                        return;
                    }
                }
            }
        }
        this.status = UpdateData.Status.Rejected;
        this.statusDescription = String.format("%s.updateObj() - failed to interpret node as update for type '%s':\n%s",
                this.getClass().getSimpleName(),
                this.targetType.name(),
                this.openNode.toString());
    }

    /**
     *
     */
    @Override
    public ObjectType targetType() {
        return this.targetType;
    }

    /**
     *
     */
    @Override
    public boolean isAggregateLevel() {
        return this.aggregateLevelTransactions;
    }

    /**
     *
     */
    @Override
    public OpenObject updateObj() {
        return this.updateObj;
    }

    /**
     *
     */
    @Override
    public String statusDescription() {
        return this.statusDescription;
    }

    /**
     *
     */
    @Override
    public Status status() {
        return this.status;
    }

    /**
     *
     */
    @Override
    public String keyStr() {
        return this.updateKey.toString();
    }

    /**
     *
     */
    @Override
    public Integer keyInt() {
        return this.updateKey.toInteger();
    }

    /**
     *
     */
    @Override
    public Integer version() {
        return this.updateVersion;
    }

    /**
     *
     */
    @Override
    public ObjectData updateData() {
        return this.updatedData;
    }

    /**
     *
     */
    @Override
    public ObjectData currentData() {
        return RepoData.CurrentState.get(this.context()).get(this.targetType, this.updateKey.toInteger());
    }

    /**
     *
     */
    @Override
    public Integer currentVersion() {
        ObjectData currentData = this.currentData();
        if (currentData != null) {
            return currentData.getVersion();
        }
        return null;
    }

    /**
     *
     */
    @Override
    public UpdateData call() throws Exception {
        try {
            if (OKEY_LOCK.containsKey(this.keyInt())) {
                throw new Exception(String.format("%s.apply() - okey: %s is currently being updated. Please refresh and retry"));
            } else {
                OKEY_LOCK.put(this.keyInt(), Thread.currentThread());
            }

            switch (this.status()) {
                case Updated:
                case Rejected:
                    return this;
                default:
                    break;
            }

            switch (this.scenario()) {
                case Error:
                    break;
                case Insert:
                    // TODO
                    break;
                case None:
                    break;
                case Reject:
                    break;
                case Update:
                    if (this.isAggregateLevel()) {
                        this.updatedData = replicate(this.currentData(), this);
                    } else {
                        // TODO
                    }
                    break;
                default:
                    break;
            }
            if (this.updatedData != null) {
                this.context().repo().submit(this.updatedData);
                this.status = Status.Updated;
                this.statusDescription = "Updated";
            }
        } catch (Exception ex) {
            this.setException(ex);
        } finally {
            OKEY_LOCK.remove(this.keyInt());
        }
        return this;
    }

    /**
     *
     * @param currentData
     * @param builder
     * @param updateData
     * @throws Exception
     */
    private ObjectData replicate(ObjectData currentData, UpdateData updateData) throws Exception {
        ObjectData.Builder builder = currentData.objectType().builder();
        boolean modified = false;
        for (ActiveData activeData : currentData.nodes()) {
            switch (activeData.role()) {
                case Value:
                    ValueData valueData = updateData.getValueData(currentData, (ValueData) activeData);
                    builder.add(valueData);
                    if (activeData != valueData) {
                        modified = true;
                    }
                    break;
                case Object:
                    ObjectData childObject = replicate((ObjectData) activeData, updateData);
                    if (childObject != null) {
                        builder.add(childObject);
                    }
                    break;
                case Array:
                    ArrayData childArray = (ArrayData) activeData;
                    ArrayData arrayData = null;
                    switch (childArray.memberType().role()) {
                        case Object:
                            arrayData = childArray.arrayType().create();
                            for (ActiveData childData : childArray.nodes()) {
                                ObjectData childDataObject = replicate((ObjectData) childData, updateData);
                                if (childDataObject != null) {
                                    arrayData.add(childDataObject);
                                }
                            }
                            break;
                        case Value:
                            arrayData = updateData.getArrayData(currentData, arrayData);
                            break;
                        default:
                            break;
                    }
                    if (arrayData != null) {
                        builder.add(arrayData);
                    }
                default:
                    break;
            }
        }
        if (modified) {
            builder.add(MetaTypes.AutoTypes.over.valueType().create(currentData.getVersion() + 1));
            builder.add(MetaTypes.AutoTypes.octs.valueType().create(LocalDateTime.now()));
        }
        return builder.build();
    }

    /**
     * Taking the current version's value as input and determining whether the
     * UpdateData has new values that need to be updated.
     *
     */
    @Override
    public ValueData getValueData(ObjectData currentObjectData, ValueData currentValueData) {
        if (this.updateObj.name().equals(currentObjectData.name())) {
            OpenValue openValue = this.updateObj.getValue(currentValueData.name());
            if (openValue != null && !currentValueData.activeValue().toString().equals(openValue.value().toString())) {
                return currentValueData.valueType().create(openValue.value());
            }
        } else {
            List<OpenNode> openNodes = this.updateObj.find(currentObjectData.name());
            if (openNodes != null) {
                for (OpenNode openNode : openNodes) {
                    switch (openNode.role()) {
                        case Object:
                            OpenObject openObject = (OpenObject) openNode;
                            OpenValue keyValue = openObject.getValue("okey");
                            if (keyValue != null && keyValue.value().toString().equals(currentObjectData.getKey())) {
                                OpenValue openValue = openObject.getValue(currentValueData.name());
                                if (openValue != null && !currentValueData.activeValue().toString().equals(openValue.value().toString())) {
                                    return currentValueData.valueType().create(openValue.value());
                                } else {
                                    return currentValueData;
                                }
                            }
                        default:
                            break;
                    }
                }
            }
        }
        return currentValueData;
    }

    /**
     * Taking the current version's value as input and determining whether the
     * UpdateData has new values that need to be updated.
     *
     */
    @Override
    public ArrayData getArrayData(ObjectData currentObjectData, ArrayData currentArrayData) {
        ValueType valueType = (ValueType) currentArrayData.memberType();
        if (this.updateObj.name().equals(currentObjectData.name())) {
            OpenArray openArray = this.updateObj.getArray(currentArrayData.name());
            if (openArray != null && !currentArrayData.activeValue().toString().equals(openArray.value().toString())) {
                ArrayData newArray = currentArrayData.arrayType().create();
                for (OpenNode openNode : openArray.nodes()) {
                    newArray.add(valueType.create(openNode.value()));
                }
                return newArray;
            }
        } else {
            List<OpenNode> openNodes = this.updateObj.find(currentObjectData.name());
            if (openNodes != null) {
                for (OpenNode openNode : openNodes) {
                    switch (openNode.role()) {
                        case Object:
                            OpenObject openObject = (OpenObject) openNode;
                            OpenValue keyValue = openObject.getValue("okey");
                            if (keyValue != null && keyValue.value().toString().equals(currentObjectData.getKey())) {
                                OpenArray openArray = this.updateObj.getArray(currentArrayData.name());
                                if (openArray != null && !currentArrayData.activeValue().toString().equals(openArray.value().toString())) {
                                    ArrayData newArray = currentArrayData.arrayType().create();
                                    for (OpenNode valNode : openArray.nodes()) {
                                        newArray.add(valueType.create(valNode.value()));
                                    }
                                    return newArray;
                                }
                            }
                        default:
                            break;
                    }
                }
            }
        }
        return currentArrayData;
    }
}
