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
package org.schorn.ella.app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.MetaTypes;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueData;

/**
 *
 * This was stripped down and only exists because there is one piece that could
 * still be used.
 *
 * @author schorn
 *
 */
public interface AvailableActions {

    /**
     *
     * @param context
     * @return Integer - Id for Task
     */
    Integer registerAsTask(AppContext context);

    /**
     *
     *
     */
    public enum DefaultActions implements ActiveNode.ValueType.DefaultValue {
        AUTO_UUID,
        AUTO_TS,
        AUTO_VERSION,
        AUTO_KEY,;

        @Override
        public Object getValue(ObjectType objectType, ValueType valueType, List<ActiveData> otherMembers) {
            switch (this) {
                case AUTO_UUID:
                    return UUID.randomUUID();
                case AUTO_TS:
                    return LocalDateTime.now();
                case AUTO_VERSION:
                    for (ActiveData activeData : otherMembers) {
                        if (activeData != null && activeData.activeType().equals(MetaTypes.AutoTypes.over.valueType())) {
                            return activeData.activeValue();
                        }
                    }
                    return 0;
                case AUTO_KEY:
                    List<ValueData> keyValues = new ArrayList<>(objectType.valueTypeKeys().size());
                    for (ValueType activeType : objectType.valueTypeKeys()) {
                        for (ActiveData activeData : otherMembers) {
                            if (activeData != null) {
                                if (activeData.activeType().equals(activeType)) {
                                    keyValues.add((ValueData) activeData);
                                }
                            }
                        }
                    }
                    Integer key = ObjectData.KeyGenerator.get(objectType.context()).generateKey(objectType.context(), keyValues.toArray(new ValueData[0]));
                    return ObjectData.KeyGenerator.get(objectType.context()).keyAsString(key);
            }
            return null;
        }
    }
}
