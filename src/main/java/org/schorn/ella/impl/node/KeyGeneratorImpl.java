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

import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectData.KeyGenerator;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.util.StringCached;

/**
 *
 * @author schorn
 *
 */
public class KeyGeneratorImpl extends AbstractContextual implements KeyGenerator {

    private static final Logger LGR = LoggerFactory.getLogger(KeyGeneratorImpl.class);

    protected KeyGeneratorImpl(AppContext context) {
        super(context);
    }

    @Override
    public Integer generateKey(AppContext context, ValueData[] values) {
        try {
            Integer key = createKey(context, values);
            if (key == null) {
                StringJoiner joiner = new StringJoiner(", ", "[", "]");
                for (ValueData valueData : values) {
                    if (valueData == null) {
                        joiner.add("null");
                    } else if (valueData.isNull()) {
                        joiner.add("undefined");
                    } else if (valueData.isBlank()) {
                        joiner.add("blank");
                    }
                }
                throw new Exception(String.format("%s.generateKey() - Context: %s ValueData: %s",
                        this.getClass().getSimpleName(), context.name(), joiner.toString()));
            }
            return key;
        } catch (Exception ex) {
            this.setException(ex);
        }
        return null;
    }

    @Override
    public Integer generateKey(ObjectType targetType, ObjectData sourceData, ValueData[] values) throws Exception {
        Integer key = createKey(targetType.context(), values);
        if (key == null) {
            StringJoiner joiner = new StringJoiner(", ", "[", "]");
            for (ValueData valueData : values) {
                if (valueData == null) {
                    joiner.add("null");
                } else if (valueData.isNull()) {
                    joiner.add("undefined");
                } else if (valueData.isBlank()) {
                    joiner.add("blank");
                }
            }
            throw new Exception(String.format("%s.generateKey() - Context: '%s' TargetType: '%s' ValueData: %s SourceData: '%s'",
                    this.getClass().getSimpleName(),
                    targetType.context().name(),
                    targetType.name(),
                    joiner.toString(),
                    sourceData.toString()
            )
            );
        }
        return key;
    }

    @Override
    public Integer generateKey(ObjectData objectData) {
        try {
            ValueData[] values = objectData.getKeyValues().toArray(new ValueData[0]);
            Integer key = createKey(objectData.context(), values);
            if (key == null) {
                StringJoiner joiner = new StringJoiner(", ", "[", "]");
                for (ValueData valueData : values) {
                    if (valueData == null) {
                        joiner.add("null");
                    } else if (valueData.isNull()) {
                        joiner.add("undefined");
                    } else if (valueData.isBlank()) {
                        joiner.add("blank");
                    }
                }
                throw new Exception(String.format("%s.generateKey() - Context: '%s' TargetType: '%s' ValueData: %s ObjectData: '%s'",
                        this.getClass().getSimpleName(),
                        objectData.context().name(),
                        objectData.name(),
                        joiner.toString(),
                        objectData.toString()
                )
                );
            }
            return key;
        } catch (Exception ex) {
            this.setException(ex);
        }
        return null;
    }

    private Integer createKey(AppContext context, ValueData[] values) throws Exception {
        if (context != null && values != null && values.length > 0) {
            StringJoiner joiner = new StringJoiner(";", String.format("%s;", context.name()), "");
            for (ValueData valueData : values) {
                if (valueData == null || valueData.isNull() || valueData.isBlank()) {
                    joiner.add("null");
                } else {
                    joiner.add(valueData.activeValue().toString());
                }
            }
            StringCached strKey = new StringCached(joiner.toString());
            return strKey.toInteger();
        }
        return null;
    }

    @Override
    public String keyAsString(Integer key) {
        return StringCached.fromId(key);
    }

}
