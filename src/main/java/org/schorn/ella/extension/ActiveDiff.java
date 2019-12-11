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
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ValueData;

/**
 *
 * @author schorn
 *
 */
public interface ActiveDiff {

    public enum DiffType {
        IDENTICAL, // exact same objects (same ref)
        EQUALITY, // values equal
        DIFFERENT, // values have differences
        ILLOGICAL, // not a logical comparison
        NULLREF,;
    }

    default DiffType diff(ActiveData node) {
        if (this instanceof ActiveData) {
            //return Impl.diff((ActiveData) this, node);
            ActiveData ad1 = (ActiveData) this;
            ActiveData ad2 = node;
            {
                if (ad2 == null) {
                    return DiffType.NULLREF;
                }
                if (!ad1.activeType().equals(ad2.activeType())) {
                    return DiffType.ILLOGICAL;
                }
                switch (ad1.role()) {
                    case Value:
                        if (((ValueData) ad1).compareTo((ValueData) ad2) == 0) {
                            return DiffType.EQUALITY;
                        } else {
                            return DiffType.DIFFERENT;
                        }
                    case Object:
                        if (((ObjectData) ad1).compareTo((ObjectData) ad2) == 0) {
                            return DiffType.EQUALITY;
                        } else {
                            return DiffType.DIFFERENT;
                        }
                    case Array:
                        if (((ArrayData) ad1).compareTo((ArrayData) ad2) == 0) {
                            return DiffType.EQUALITY;
                        } else {
                            return DiffType.DIFFERENT;
                        }
                    default:
                        return DiffType.ILLOGICAL;
                }
            }
        }
        return DiffType.ILLOGICAL;
    }

}
