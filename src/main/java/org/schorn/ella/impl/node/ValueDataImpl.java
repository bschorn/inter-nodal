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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.schorn.ella.node.NodeProvider;
import org.schorn.ella.node.ActiveNode.ValueData;

/**
 *
 * @author schorn
 *
 */
public class ValueDataImpl extends ActiveDataImpl implements ValueData {

    PrimitiveData<?> data;

    protected ValueDataImpl(ValueType valueType, PrimitiveData<?> data) {
        super(valueType);
        this.data = data;
    }

    @Override
    public int size() {
        return 1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> classOfT) throws Exception {
        if (this.data.dataClass().equals(classOfT)) {
            return (T) this.data.asLocal();
        }
        return (T) NodeProvider.provider().typeConvert(this.data.dataClass(), classOfT, this.data.asLocal());
    }

    @Override
    public Object activeValue() {
        return this.data.asLocal();
    }

    @Override
    public List<Object> activeValues() {
        return Arrays.asList(new Object[]{this.activeValue()});
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT) {
        if (classForT.isInstance(this)) {
            return Arrays.asList(new ValueData[]{this}).stream()
                    .map(vd -> classForT.cast(vd))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, Predicate<T> filter) {
        if (classForT.isInstance(this)) {
            return Arrays.asList(new ValueData[]{this}).stream()
                    .map(vd -> classForT.cast(vd))
                    .filter(filter)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<Object> activeValues(int startIdx, int length) {
        if (startIdx != 0 || length <= 0) {
            return new ArrayList<>(0);
        }
        return Arrays.asList(new Object[]{this.activeValue()});
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, int startIdx, int length) {
        return this.activeValues(classForT).stream()
                .skip(startIdx)
                .limit(length)
                .collect(Collectors.toList());
    }

    @Override
    public void write(ObjectOutput out) throws IOException {
        this.data.writeExternal(out);
    }

    @Override
    public void read(ObjectInput in) throws IOException, ClassNotFoundException {
        this.data.readExternal(in);
    }

    @Override
    public boolean isNull() {
        return this.data.isNull();
    }

    @Override
    public boolean isBlank() {
        return this.data.isBlank();
    }

}
