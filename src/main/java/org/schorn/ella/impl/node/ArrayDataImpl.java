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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Array Data Implementation
 *
 *
 * @author schorn
 *
 */
public class ArrayDataImpl extends StructDataImpl implements ArrayData {

    static final Logger LGR = LoggerFactory.getLogger(ArrayDataImpl.class);

    //private final List<ActiveData> rows;
    private final Set<Integer> ids;

    public ArrayDataImpl(ArrayType arrayType) {
        super(arrayType, new ArrayList<ActiveData>(100));
        //this.rows = new ArrayList<>(100);
        this.ids = new HashSet<>(100);
    }

    @Override
    public ActiveType memberType() {
        return this.arrayType().memberType();
    }

    @Override
    public boolean add(ActiveData activeData) {
        if (activeData == null) {
            return false;
        }
        if (this.arrayType().isDynamic() && this.arrayType().memberDef() == null) {
            ArrayTypeImpl impl = (ArrayTypeImpl) this.arrayType();
            impl.setMemberType(activeData.activeType());
        }
        if (activeData.activeType().equals(this.memberType())) {
            if (this.ids.contains(activeData.activeId())) {
                LGR.error("{}.add() ignoring duplicate: {}",
                        this.getClass().getSimpleName(),
                        activeData.activeOut(OutFormat.CSV));
                return true;
            } else {
                try {
                    this.nodes_mutable_all().add(activeData);
                    activeData.setParent(this);
                    this.link(activeData);
                } catch (Exception ex) {
                    LGR.error("{}.add() error adding entry into array: {}",
                            this.getClass().getSimpleName(),
                            activeData.activeOut(OutFormat.CSV));
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ActiveData get(int index) {
        return super.get0(index);
    }

    @Override
    public List<ActiveData> nodes() {
        return this.nodes_mutable_all();
    }

    @Override
    public int size() {
        return this.size_nodes_mutable_all();
    }

    @Override
    public Object activeValue() {
        return null;
    }

    @Override
    public List<Object> activeValues() {
        return this.nodes().stream()
                .map(ad -> Object.class.cast(ad))
                .collect(Collectors.toList());
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT) {
        return this.nodes().stream()
                .filter(ad -> classForT.isInstance(ad))
                .map(ad -> classForT.cast(ad))
                .collect(Collectors.toList());
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, Predicate<T> filter) {
        return this.nodes().stream()
                .filter(ad -> classForT.isInstance(ad))
                .map(ad -> classForT.cast(ad))
                .filter(filter)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> activeValues(int startIdx, int length) {
        if (startIdx >= this.nodes().size()
                || startIdx < 0
                || length <= 0) {
            return new ArrayList<>(0);
        }
        return this.nodes().stream()
                .skip(startIdx)
                .limit(length)
                .map(ad -> Object.class.cast(ad))
                .collect(Collectors.toList());
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, int startIdx, int length) {
        return this.activeValues(classForT).stream()
                .skip(startIdx)
                .limit(length)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isBlank() {
        return this.nodes().isEmpty();
    }

}
