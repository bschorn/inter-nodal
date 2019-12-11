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
package org.schorn.ella.format;

import java.util.ArrayList;
import java.util.List;

import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.repo.RepoSupport.FilteredObjectData;

/**
 *
 * @author schorn
 *
 */
abstract class ActiveDataToString<T> implements CustomToString<T> {

    abstract public String format(T data);

    protected String formatValue(ValueData vdata, int position, int generation) {
        return String.format("%s %d %s = %s", indent(generation), position, vdata.name(),
                vdata.activeValue() != null ? vdata.activeValue().toString() : "null");
    }

    protected List<String> formatObject(FilteredObjectData objectData, int position, int generation) {
        String header = String.format("%s %d %s {}", indent(generation), position, objectData.name());
        List<String> tokens = new ArrayList<>(20);
        tokens.add(header);
        for (ActiveData activeData : objectData.nodes()) {
            if (activeData == null) {
                continue;
            }
            Integer position1 = objectData.objectType().schema().getIndex(activeData.activeType());
            switch (activeData.role()) {
                case Value:
                    tokens.add(formatValue((ValueData) activeData, position1, generation + 1));
                    break;
                case Object:
                    tokens.addAll(formatObject((ObjectData) activeData, position1, generation + 1));
                    break;
                case Array:
                    tokens.addAll(formatArray((ArrayData) activeData, position1, generation + 1));
                    break;
                default:
                    break;
            }
        }
        return tokens;
    }

    protected List<String> formatObject(ObjectData objectData, int position, int generation) {
        String header = String.format("%s %d %s {}", indent(generation), position, objectData.name());
        List<String> tokens = new ArrayList<>(20);
        tokens.add(header);
        for (ActiveData activeData : objectData.nodes()) {
            if (activeData == null) {
                continue;
            }
            Integer position1 = objectData.objectType().schema().getIndex(activeData.activeType());
            switch (activeData.role()) {
                case Value:
                    tokens.add(formatValue((ValueData) activeData, position1, generation + 1));
                    break;
                case Object:
                    tokens.addAll(formatObject((ObjectData) activeData, position1, generation + 1));
                    break;
                case Array:
                    tokens.addAll(formatArray((ArrayData) activeData, position1, generation + 1));
                    break;
                default:
                    break;
            }
        }
        return tokens;
    }

    protected List<String> formatArray(ArrayData arrayData, int position, int generation) {
        String header = String.format("%s %d %s []", indent(generation), position, arrayData.name());
        List<String> tokens = new ArrayList<>(100);
        tokens.add(header);
        int row = 0;
        for (ActiveData activeData : arrayData.nodes()) {
            row += 1;
            switch (activeData.role()) {
                case Value:
                    tokens.add(formatValue((ValueData) activeData, row, generation + 1));
                    break;
                case Object:
                    tokens.addAll(formatObject((ObjectData) activeData, row, generation + 1));
                    break;
                case Array:
                    tokens.addAll(formatArray((ArrayData) activeData, row, generation + 1));
                    break;
                default:
                    break;
            }
        }
        return tokens;
    }
}
