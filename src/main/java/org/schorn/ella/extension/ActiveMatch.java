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

import org.schorn.ella.node.DataGroup;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ValueData;

/**
 * This extension is to facilitate using ActiveData instances as inputs to a
 * query. The ActiveData instances created to find another will not always pass
 * the ActiveData equality test (ActiveData.equals()) this provides a more
 * lenient.
 *
 *
 *
 * @author schorn
 *
 */
public interface ActiveMatch {

    /**
     * This will verify the ActiveDatas have the same names.
     *
     * ActiveData containers will have to override and supply an implementation.
     *
     * @param node
     * @return
     */
    default int compareName(ActiveData node) throws Exception {
        throw new Exception("Unsupported");
    }

    /**
     * This will verify the ActiveDatas have the same values.
     *
     * ActiveData containers will have to override and supply an implementation.
     *
     * @param node
     * @return
     */
    default int compareValue(ActiveData other) {
        if (this instanceof ValueData && other instanceof ValueData) {
            ValueData vd1 = (ValueData) this;
            ValueData vd2 = (ValueData) other;
            return vd1.compareTo(vd2);
        } else if (this instanceof ObjectData && other instanceof ObjectData) {
            ObjectData od1 = (ObjectData) this;
            ObjectData od2 = (ObjectData) other;
            return od1.compareTo(od2);
        } else if (this instanceof ArrayData && other instanceof ArrayData) {
            ArrayData ad1 = (ArrayData) this;
            ArrayData ad2 = (ArrayData) other;
            return ad1.compareTo(ad2);
        }
        return this.toString().compareTo(other.toString());
    }

    default int compareValue(Object value) {
        if (this instanceof ValueData) {
            ValueData valueData = (ValueData) this;
            Object thisValue = valueData.activeValue();
            DataGroup thisDataGroup = valueData.valueType().fieldType().dataType().primitiveType().dataGroup();
            DataGroup thatDataGroup = DataGroup.quickFit(value);
            switch (thatDataGroup) {
                case DECIMAL: {
                    switch (thisDataGroup) {
                        case DECIMAL:
                        case NUMBER:
                            Number n1 = (Number) thisValue;
                            Number n2 = (Number) value;
                            return n1.doubleValue() > n2.doubleValue() ? 1 : n1.doubleValue() < n2.doubleValue() ? -1 : 0;
                        default:
                            return thisValue.toString().compareTo(value.toString());
                    }
                }
                case TIMESTAMP: {
                    switch (thisDataGroup) {
                        case TIMESTAMP:
                        case DATE:
                            Number n1 = (Number) thisValue;
                            Number n2 = (Number) value;
                            return n1.doubleValue() > n2.doubleValue() ? 1 : n1.doubleValue() < n2.doubleValue() ? -1 : 0;
                        default:
                            return thisValue.toString().compareTo(value.toString());
                    }
                }
                default: {
                    return thisValue.toString().compareTo(value.toString());
                }
            }
        }
        return this.toString().compareTo(value.toString());
    }

    /**
     * Match has a lower threshold than equals() It only requires the
     * nodeRole(), nodeName() and activeValue() to equal. The ActiveDataContext
     * and ActiveDataType can be different
     *
     * @param node
     * @return
     */
    default boolean match(ActiveData node) {
        if (this instanceof ActiveData) {
            return Impl.match((ActiveData) this, node);
        } else {
            return false;
        }
    }

    default boolean match(Object value) {
        if (this instanceof ActiveData) {
            return Impl.match((ActiveData) this, value);
        } else {
            return false;
        }
    }

    /**
     * Implementation of ActiveDataMatch functionality
     *
     */
    static class Impl {

        static boolean match(ActiveData ad1, ActiveData ad2) {
            if (ad1.role().equals(ad2.role())) {
                if (ad1.name().compareTo(ad2.name()) == 0) {
                    switch (ad1.role()) {
                        case Value:
                            return ((ValueData) ad1).compareTo((ValueData) ad1) == 0;
                        case Object:
                            return ((ObjectData) ad1).compareTo((ObjectData) ad1) == 0;
                        case Array:
                            return ((ArrayData) ad1).compareTo((ArrayData) ad1) == 0;
                        default:
                            break;
                    }
                }
            }
            return false;
        }

        static boolean match(ActiveData node0, Object value) {
            if (node0 instanceof ActiveData) {
                if (node0 instanceof ValueData) {
                    return ((ActiveData) node0).activeValue().toString().compareTo(value.toString()) == 0;
                }
            }
            return false;
        }
    }
}
