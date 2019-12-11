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

import org.schorn.ella.node.ActiveNode.Constraints;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType;

/**
 *
 * @author schorn
 *
 */
class ConstraintsToString implements CustomToString<Constraints> {

    public static final ConstraintsToString INSTANCE = new ConstraintsToString();

    @Override
    public String format(Constraints constraints) {
        StringJoiner joiner = new StringJoiner(" AND ", "", "");
        for (ConstraintType<?> constraintType : constraints.constraintTypes()) {
            StringJoiner innerJoiner = new StringJoiner(",", "[", "]");
            for (Object constraintValue : constraints.constraint(constraintType).constraintValues()) {
                innerJoiner.add(constraintValue.toString());
            }
            joiner.add(String.format("%s = %s", constraintType.name(), innerJoiner.toString()));
        }
        return joiner.toString();
    }

}
