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

import org.schorn.ella.context.AppContext;
import org.schorn.ella.format.SupportString;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.BondType;

/**
 *
 * @author schorn
 *
 */
public class ArrayTypeImpl extends ActiveTypeImpl implements ArrayType {

    private MemberDef memberDef;
    private boolean dynamic;

    protected ArrayTypeImpl(AppContext context, String name, MemberDef memberDef, Short activeIdx) {
        super(context, name, activeIdx);
        if (memberDef == null) {
            this.dynamic = true;
            this.memberDef = null;
        } else {
            this.dynamic = false;
            this.memberDef = memberDef;
        }
    }

    @Override
    public boolean isDynamic() {
        return this.dynamic;
    }

    void setMemberType(ActiveType activeType) {
        if (this.memberDef == null) {
            try {
                this.memberDef = MemberDef.create(activeType, BondType.OPTIONAL, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ActiveType memberType() {
        if (this.memberDef == null) {
            return null;
        }
        return this.memberDef.activeType();
    }

    @Override
    public int bytes() {
        if (this.memberDef == null) {
            return 0;
        }
        return this.memberDef.activeType().bytes();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ArrayType) {
            return this.compareTo((ArrayType) object) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        return SupportString.format(this);
    }

    @Override
    public ArrayData create() {
        return new ArrayDataImpl(this);
    }

    @Override
    public MemberDef memberDef() {
        return this.memberDef;
    }

}
