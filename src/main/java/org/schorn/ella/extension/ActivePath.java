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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;

/**
 *
 * @author schorn
 *
 */
public interface ActivePath {

    default List<ValueData> getValues(String path) {
        if (this instanceof ActiveData) {
            return Support.getValues((ActiveData) this, path);
        }

        return new ArrayList<>();
    }

    static class Support {

        static boolean filterPath(ActiveData parent, ActiveData child, List<String> types, int level) {
            if (parent == null || level < 0) {
                return false;
            }
            if (parent.name().equals(types.get(level))) {
                if (level == 0) {
                    return true;
                }
                return filterPath(parent.getParent(), parent, types, level - 1);
            }
            if (parent.name().equals(child.name())) {
                return filterPath(parent.getParent(), parent, types, level);
            }
            return false;
        }

        static List<ValueData> getValues(ActiveData activeData, String path) {
            List<String> types = Arrays.asList(path.split("\\."));
            ValueType valueType = ValueType.get(activeData.context(), types.get(types.size() - 1));
            if (valueType == null) {
                return new ArrayList<>();
            }
            List<ValueData> nodes = activeData.find(valueType);
            List<ValueData> keepers = new ArrayList<>();
            for (ValueData node : nodes) {
                if (filterPath(node.getParent(), node, types, types.size() - 2)) {
                    keepers.add(node);
                }
            }
            return keepers;

        }

    }
}
