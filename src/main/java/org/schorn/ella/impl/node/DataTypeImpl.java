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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.format.SupportString;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType;
import org.schorn.ella.node.ActiveNode.ValueType.DataType;
import org.schorn.ella.node.ActiveNode.ValueType.PrimitiveType;

/**
 *
 * @author schorn
 *
 */
class DataTypeImpl implements DataType {

    private final AppContext context;
    private final String name;
    private final PrimitiveType<?> primitiveType;
    private final List<ConstraintType<?>> constraintTypes;

    DataTypeImpl(AppContext context, String name, PrimitiveType<?> primitiveType, ConstraintType<?>[] constraintTypes) {
        this.context = context;
        this.name = name;
        this.primitiveType = primitiveType;
        this.constraintTypes = Collections.unmodifiableList(Arrays.asList(constraintTypes));
    }

    @Override
    public AppContext context() {
        return this.context;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public PrimitiveType<?> primitiveType() {
        return this.primitiveType;
    }

    @Override
    public List<ConstraintType<?>> constraintTypes() {
        return this.constraintTypes;
    }

    @Override
    public String toString() {
        return SupportString.format(this);
    }

}
