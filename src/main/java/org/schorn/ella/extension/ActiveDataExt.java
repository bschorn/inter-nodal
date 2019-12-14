/*
 * The MIT License
 *
 * Copyright 2019 bschorn.
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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.ActiveNode.ValueData;

/**
 *
 * @author bschorn
 */
public interface ActiveDataExt extends Externalizable {

    @Override
    default void writeExternal(ObjectOutput out) throws IOException {
        if (this instanceof ValueData) {
            ValueData vdata = (ValueData) this;
            vdata.write(out);
        } else if (this instanceof StructData) {
            StructData sdata = (StructData) this;
            for (ActiveData adata : sdata.nodes()) {
                adata.writeExternal(out);
            }
        }
    }

    @Override
    default void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        if (this instanceof ValueData) {
        } else if (this instanceof StructData) {
        }
    }
}
