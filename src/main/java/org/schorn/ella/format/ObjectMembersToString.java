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

import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ObjectType.ObjectSchema;

/**
 *
 * @author schorn
 *
 */
class ObjectMembersToString implements CustomToString<ObjectSchema> {

    public static final ObjectMembersToString INSTANCE = new ObjectMembersToString();

    @Override
    public String format(ObjectSchema objectMembers) {
        StringJoiner joiner = new StringJoiner("\n", "{\n", "\n}");
        for (MemberDef memberType : objectMembers.memberDefs()) {
            if (memberType != null) {
                joiner.add(String.format("%d %s.%s (%s)",
                        memberType.index(),
                        memberType.activeType().context().name(),
                        memberType.activeType().name(),
                        memberType.bondType().name().toLowerCase()
                ));
            } else {
                joiner.add("null");
            }
        }
        return joiner.toString();
    }

}
