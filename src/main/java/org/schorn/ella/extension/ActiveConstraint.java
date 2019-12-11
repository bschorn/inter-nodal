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

import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.Constraints;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintData;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType;

/**
 *
 * @author schorn
 *
 */
public interface ActiveConstraint {

    default boolean validate() {
        if (this instanceof ActiveData) {
            return SupportConstraint.validate((ActiveData) this);
        }
        return true;
    }

    /**
     *
     *
     *
     */
    static class SupportConstraint {

        static boolean validate(ActiveData activeData) {
            switch (activeData.role()) {
                case Value:
                    return SupportConstraint.validate((ValueData) activeData);
                case Object:
                    return SupportConstraint.validate((ObjectData) activeData);
                case Array:
                    return SupportConstraint.validate((ArrayData) activeData);
                default:
                    return true;
            }

        }

        static boolean validate(ValueData valueData) {
            ValueType valueType = valueData.valueType();
            Constraints constraints = valueType.fieldType().constraints();
            for (ConstraintType<?> constraintType : constraints.constraintTypes()) {
                ConstraintData constraintData = constraints.constraint(constraintType);
                if (!constraintData.test(valueData.activeValue())) {
                    return false;
                }
            }
            return true;
        }

        static boolean validate(ObjectData objectData) {
            return objectData.nodes().stream().filter(activeData -> SupportConstraint.validate(activeData) == false).findAny().isPresent();
        }

        static boolean validate(ArrayData arrayData) {
            return arrayData.nodes().stream().filter(activeData -> SupportConstraint.validate(activeData) == false).findAny().isPresent();
        }
    }
}
