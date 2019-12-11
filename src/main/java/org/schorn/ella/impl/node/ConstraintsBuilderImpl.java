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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.schorn.ella.node.DataGroup;
import org.schorn.ella.node.ActiveNode.Constraints;
import org.schorn.ella.node.ActiveNode.Constraints.Builder;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintData;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType;

/**
 *
 * @author schorn
 *
 */
public class ConstraintsBuilderImpl implements Builder {

    private final DataGroup dataGroup;
    private final List<ConstraintType<?>> constraintTypes = new ArrayList<>();
    private final Map<ConstraintType<?>, ConstraintData> constraintMap = new HashMap<>();

    public ConstraintsBuilderImpl(DataGroup dataGroup) {
        this.dataGroup = dataGroup;
    }

    @Override
    public List<ConstraintType<?>> constraintTypes() {
        return this.constraintTypes;
    }

    @Override
    public Builder add(ConstraintType<?> constraintType, List<Object> values) {
        List<Object> constraintValues = new ArrayList<>();
        ConstraintData constraintData = this.constraintMap.get(constraintType);
        if (constraintData != null) {
            constraintValues.addAll(constraintData.constraintValues());
        }
        values.forEach(v -> constraintValues.add(v));
        constraintData = new ConstraintsImpl.ConstraintDataImpl(constraintType, constraintValues);
        this.constraintMap.put(constraintType, constraintData);
        this.constraintTypes.add(constraintType);
        return this;
    }

    @Override
    public Constraints build() {
        return new ConstraintsImpl(this.dataGroup, this.constraintTypes, this.constraintMap);
    }

}
