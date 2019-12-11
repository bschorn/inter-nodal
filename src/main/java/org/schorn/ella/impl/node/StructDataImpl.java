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
import java.util.List;
import java.util.stream.Collectors;

import org.schorn.ella.node.ActiveNode.StructData;

/**
 *
 * @author schorn
 *
 */
abstract class StructDataImpl extends ActiveDataImpl implements StructData {

    private final List<ActiveData> elements;

    StructDataImpl(ObjectType objectType, List<ActiveData> elements) {
        super(objectType);
        if (elements == null) {
            this.elements = new ArrayList<>(0);
        } else {
            this.elements = elements;
        }
        for (ActiveData activeData : elements) {
            if (activeData != null) {
                activeData.setParent(this);
            }
        }
    }

    StructDataImpl(ArrayType arrayType, List<ActiveData> elements) {
        super(arrayType);
        this.elements = elements;
    }

    final protected ActiveData get0(int index) {
        if (index >= 0 && index < this.elements.size()) {
            return this.elements.get(index);
        }
        return null;
    }

    /**
     * This returns a mutable list.
     *
     * @return
     */
    final protected List<ActiveData> nodes_mutable_all() {
        return this.elements;
    }

    /**
     * This returns an immutable list with nulls filtered out.
     *
     * @return
     */
    final protected List<ActiveData> nodes_immutable_non_null() {
        return this.elements.stream().filter(ad -> ad != null).collect(Collectors.toList());
    }

    final protected int size_nodes_mutable_all() {
        return this.elements.size();
    }

    final protected int size_nodes_immutable_non_null() {
        return this.nodes_immutable_non_null().size();
    }

}
