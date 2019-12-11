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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.NodeProvider;
import org.schorn.ella.node.DataGroup;
import org.schorn.ella.util.Functions;

/**
 * Value Type
 *
 *
 * @author schorn
 *
 */
public class ValueTypeImpl extends ActiveTypeImpl implements ValueType {

    static final Logger LGR = LoggerFactory.getLogger(ValueTypeImpl.class);

    private FieldType fieldType;

    protected ValueTypeImpl(AppContext context, String name, FieldType fieldType, Short activeIdx) {
        super(context, name, activeIdx);
        this.fieldType = fieldType;
    }

    /**
     *
     */
    @Override
    public ValueData create(Object value) {
        if (value == null) {
            try {
                return new ValueDataImpl(this, this.fieldType.dataType().primitiveType().nullInstance());
            } catch (Exception ex) {
                LGR.error(Functions.stackTraceToString(ex));
            }
        } else {
            Class<?> primitiveClass = this.fieldType.dataType().primitiveType().dataClass();
            if (this.fieldType.dataType().primitiveType().dataClass().isInstance(value)) {
                try {
                    if (this.fieldType.dataType().primitiveType().dataGroup().equals(DataGroup.TEXT)) {
                        Integer maxWidth = this.fieldType.maxWidth();
                        if (maxWidth != null) {
                            if (value instanceof String) {
                                String valueStr = (String) value;
                                if (valueStr.length() > maxWidth) {
                                    return new ValueDataImpl(this, this.fieldType.dataType().primitiveType().newInstance(valueStr.substring(0, maxWidth)));
                                }
                            }
                        }
                    }
                    return new ValueDataImpl(this, this.fieldType.dataType().primitiveType().newInstance(value));
                } catch (Exception ex) {
                    LGR.error(String.format("%s.create() - ValueType: %s -> %s",
                            ValueData.class.getSimpleName(),
                            this.name(),
                            ex.getMessage()));
                }
            } else {
                try {
                    Object convertedValue = NodeProvider.provider().typeConvert(value.getClass(), primitiveClass, value);
                    if (convertedValue == null) {
                        LGR.error(String.format("%s.create() - ValueType: %s -> unable to convert '%s' from %s to %s",
                                ValueData.class.getSimpleName(),
                                this.name(),
                                value.toString(),
                                value.getClass().getSimpleName(),
                                primitiveClass.getSimpleName()));
                        return new ValueDataImpl(this, this.fieldType.dataType().primitiveType().nullInstance());
                    }
                    return new ValueDataImpl(this, this.fieldType.dataType().primitiveType().newInstance(convertedValue));
                } catch (Exception ex) {
                    LGR.error(String.format("%s.create() - ValueType: %s -> %s",
                            ValueData.class.getSimpleName(),
                            this.name(),
                            ex.getMessage()));
                    try {
                        return new ValueDataImpl(this, this.fieldType.dataType().primitiveType().nullInstance());
                    } catch (Exception ex2) {
                        return null;
                    }
                }
            }
        }
        LGR.error(String.format("%s.create() - failed to create data instance of type '%s' with value '%s'.",
                this.getClass().getSimpleName(),
                this.toString(),
                value.toString()));
        return null;
    }

    /**
     * Attempts to create a 'blank' instance (instead of a null)
     *
     * @return
     * @throws Exception
     */
    public ValueData create() throws Exception {
        return new ValueDataImpl(this, this.fieldType.dataType().primitiveType().newInstance());
    }

    /*
	 * 
     */
    ValueData create0(Object value) throws Exception {
        if (value == null) {
            return new ValueDataImpl(this, this.fieldType.dataType().primitiveType().nullInstance());
        } else {
            return new ValueDataImpl(this, this.fieldType.dataType().primitiveType().newInstance(value));
        }
    }

    @Override
    public FieldType fieldType() {
        return this.fieldType;
    }

    @Override
    public int bytes() {
        return this.fieldType.dataType().primitiveType().bytes();
    }

}
