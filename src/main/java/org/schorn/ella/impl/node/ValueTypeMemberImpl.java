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

import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.MemberDef;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueTypeMember;

/**
 * ValueTypeMember is a convenience class to clearly represent an
 * ObjectType->ValueType in isoloation.
 *
 * @author schorn
 *
 */
public class ValueTypeMemberImpl implements ValueTypeMember {

    private final ObjectType memberOfType;
    private final MemberDef memberDef;

    ValueTypeMemberImpl(ObjectType memberOfType, MemberDef memberDef) {
        this.memberOfType = memberOfType;
        this.memberDef = memberDef;
    }

    ValueTypeMemberImpl(AppContext context, String valueTypeMemberStr) throws Exception {
        String[] parts = valueTypeMemberStr.split("\\.");
        if (parts.length == 2) {
            ObjectType memberOfType = ObjectType.get(context, parts[0]);
            ValueType memberType = ValueType.get(context, parts[1]);
            MemberDef memberDef = memberOfType.schema().get(memberType);
            this.memberOfType = memberOfType;
            this.memberDef = memberDef;
        } else {
            throw new Exception(
                    String.format("%s.ctor() - the parameter '%s' for ValueTypeMemberStr may be incomplete. Please specifiy ObjectType.ValueType",
                            this.getClass().getSimpleName(), valueTypeMemberStr));
        }
    }

    @Override
    public ObjectType memberOfType() {
        return this.memberOfType;
    }

    @Override
    public int index() {
        return this.memberDef.index();
    }

    @Override
    public ValueType memberType() {
        return (ValueType) this.memberDef.activeType();
    }

    @Override
    public String asValueTypeMemberStr() {
        return String.format("%s.%s", this.memberOfType.name(), this.memberDef.activeType().name());
    }

    @Override
    public String toString() {
        return String.format("%s[%d]->%s", this.memberOfType.name(),
                this.memberDef.index(), this.memberDef.activeType().name());
    }
}
