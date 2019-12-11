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
import java.util.Optional;

import org.schorn.ella.node.BondType;
import org.schorn.ella.node.NodeProvider;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;

/**
 * ValueType Extensions
 *
 * This are helper methods and can be implemented purely from the API (no inner
 * workings knowledge needed).
 *
 * @author schorn
 *
 */
public interface ValueTypeExt {

    default ObjectType keyOf() {
        if (this instanceof ValueType) {
            ValueType valueType = (ValueType) this;
            List<ObjectType> objectTypes = NodeProvider.provider().getList(valueType.context(), ObjectType.class);
            Optional<ObjectType> optObjectType = objectTypes.parallelStream()
                    .filter(ot -> ot.isKey(valueType))
                    .findFirst();
            if (optObjectType.isPresent()) {
                return optObjectType.get();
            }
        }
        return null;
    }

    /**
     * Gets the ArrayType for this ValueType. If one can not be found, then a
     * transient one is created and returned.
     *
     * @return
     * @throws Exception
     */
    default ArrayType arrayType() throws Exception {
        if (this instanceof ValueType) {
            ValueType valueType = (ValueType) this;
            ArrayType arrayType = ArrayType.get(valueType.context(), valueType.name());
            if (arrayType != null) {
                if (!arrayType.memberType().equals(valueType)) {
                    arrayType = null;
                }
            }
            if (arrayType == null) {
                arrayType = NodeProvider.provider().createTransientArrayType(valueType.context(), valueType.name(), valueType, BondType.OPTIONAL);
            }
            return arrayType;
        }
        return null;
    }

}
