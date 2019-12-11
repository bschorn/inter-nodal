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

import org.schorn.ella.node.BondType;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ObjectType.ObjectSchema;

/**
 *
 * @author schorn
 *
 */
abstract class ActiveTypeToString<T> implements CustomToString<T> {

    abstract public String format(T type);

    protected String formatValue(ValueType vtype, BondType bondType, int position, int generation) {
        return String.format("%s %d $%s (%s)", indent(generation), position, vtype.name(), bondType.name().toLowerCase());
    }

    protected List<String> formatObject(ObjectType otype, int position, int generation) {
        String header = String.format("%s {%d} #%s", indent(generation), otype.activeId(), otype.name());
        List<String> tokens = new ArrayList<>(20);
        tokens.add(header);
        ObjectSchema schema = otype.schema();
        for (MemberDef memberType : schema.memberDefs()) {
            switch (memberType.activeType().role()) {
                case Value:
                    tokens.add(formatValue((ValueType) memberType.activeType(), memberType.bondType(), memberType.index(), generation + 1));
                    break;
                case Object:
                    tokens.addAll(formatObject((ObjectType) memberType.activeType(), memberType.index(), generation + 1));
                    break;
                case Array:
                    tokens.addAll(formatArray((ArrayType) memberType.activeType(), memberType.index(), generation + 1));
                    break;
                default:
                    break;
            }
        }
        return tokens;
    }

    protected List<String> formatArray(ArrayType arrayType, int position, int generation) {
        String header = String.format("%s [%d] @%s", indent(generation), arrayType.activeId(), arrayType.name());
        List<String> tokens = new ArrayList<>(20);
        tokens.add(header);
        ActiveType activeType = arrayType.memberType();
        switch (activeType.role()) {
            case Value:
                tokens.add(formatValue((ValueType) activeType, BondType.OPTIONAL, 0, generation + 1));
                break;
            case Object:
                tokens.addAll(formatObject((ObjectType) activeType, generation + 1, generation + 1));
                break;
            case Array:
                tokens.addAll(formatArray((ArrayType) activeType, generation + 1, generation + 1));
                break;
            default:
                break;
        }
        return tokens;
    }
}
