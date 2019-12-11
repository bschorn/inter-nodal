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

import java.util.StringJoiner;

import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.Constraints;
import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintData;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType;
import org.schorn.ella.node.ActiveNode.ObjectType.ObjectSchema;
import org.schorn.ella.node.ActiveNode.ValueType.DataType;
import org.schorn.ella.node.ActiveNode.ValueType.FieldType;
import org.schorn.ella.repo.RepoSupport.FilteredObjectData;

/**
 *
 * Centralized the handling of the customized overriding of the
 * Object.toString() method.
 *
 * @author schorn
 *
 */
public interface SupportString {

    static String format(ActiveNode active) {
        if (active instanceof ValueData) {
            /*
			 * ValueData
             */
            return ValueDataToString.INSTANCE.format((ValueData) active);
        } else if (active instanceof FilteredObjectData) {
            /*
			 * ObjectData
             */
            return FilterableObjectDataToString.INSTANCE.format((FilteredObjectData) active);
        } else if (active instanceof ObjectData) {
            /*
			 * ObjectData
             */
            return ObjectDataToString.INSTANCE.format((ObjectData) active);
        } else if (active instanceof ArrayData) {
            /*
			 * ArrayData
             */
            return ArrayDataToString.INSTANCE.format((ArrayData) active);
        } else if (active instanceof ValueType) {
            /*
			 * ValueType
             */
            return ValueTypeToString.INSTANCE.format((ValueType) active);
        } else if (active instanceof ObjectType) {
            /*
			 *  ObjectType
             */
            return ObjectTypeToString.INSTANCE.format((ObjectType) active);
        } else if (active instanceof ArrayType) {
            /*
			 * ArrayType
             */
            return ArrayTypeToString.INSTANCE.format((ArrayType) active);
        } else if (active instanceof FieldType) {
            /*
			 * FieldType 
             */
            return FieldTypeToString.INSTANCE.format((FieldType) active);
        } else if (active instanceof DataType) {
            /*
			 * DataType 
             */
            DataType d = (DataType) active;
            return String.format("%s %s", d.name(), d.primitiveType().dataGroup().toString());
        } else if (active instanceof ObjectSchema) {
            /*
			 * ObjectMembers
             */
            return ObjectMembersToString.INSTANCE.format((ObjectSchema) active);
        } else if (active instanceof MemberDef) {
            /*
			 * MemberDef
             */
            MemberDef m = (MemberDef) active;
            return String.format("%s IS %s", m.activeType().name(), m.bondType().name().toLowerCase());
            /*		} else if (active instanceof MemberType) {
			MemberType m = (MemberType) active;
			return String.format("%s IS %s", m.activeType().name(), m.bondType().name().toLowerCase());
             */        } else if (active instanceof Constraints) {
            /*
			 * Constraints
             */
            return ConstraintsToString.INSTANCE.format((Constraints) active);
        }
        return "Error.toString()";
    }

    static String format(Constraints constraints) {
        StringJoiner joiner = new StringJoiner(", ", String.format("%s -> constraint: { ", constraints.dataGroup().name()), " }");
        if (constraints.constraintTypes().size() > 0) {
            for (ConstraintType<?> constraintType : constraints.constraintTypes()) {
                ConstraintData constraintData = constraints.constraint(constraintType);
                StringJoiner values = new StringJoiner(",", "", "");
                constraintData.constraintValues().stream().filter(v -> v != null).forEach(v -> values.add(v.toString()));
                joiner.add(String.format("%s: %s", constraintType.name(), values.toString()));
            }
        } else {
            joiner.add("-none-");
        }
        return joiner.toString();
    }

    static String format(MemberDef memberDef) {
        return String.format("%d %s %s", memberDef.index(), memberDef.activeType().toString(), memberDef.bondType().toString());
    }

}
