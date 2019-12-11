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
package org.schorn.ella.impl.model;

import org.schorn.ella.model.ActiveModel;
import org.schorn.ella.node.BondType;

/**
 *
 * @author bschorn
 */
public class ModelMemberImpl extends ModelResolvableImpl implements ActiveModel.ModelMember {

    private final ActiveModel.ModelContainerType containerType;
    private final ActiveModel.ModelMemberType memberType;
    private final BondType bondType;
    private final ActiveModel.ModelQualifiedName qualifiedName;

    ModelMemberImpl(ActiveModel.ModelContainerType containerType,
            ActiveModel.ModelMemberType memberType,
            BondType bondType) {
        this.containerType = containerType;
        this.memberType = memberType;
        this.bondType = bondType;
        this.containerType.addMember(this);
        this.qualifiedName = ActiveModel.ModelQualifiedName.create(containerType.name(), memberType.name());
    }

    @Override
    public BondType bondType() {
        return this.bondType;
    }

    @Override
    public String name() {
        return this.qualifiedName.name();
    }

    @Override
    public ActiveModel.ModelRole modelRole() {
        return this.memberType.modelRole();
    }

    @Override
    public void resolve() throws ActiveModel.UnresolveModeldDependency {

    }

    @Override
    public ActiveModel.ModelMemberType memberType() {
        return this.memberType;
    }

    @Override
    public boolean isResolved() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ActiveModel.ModelContainerType containerType() {
        return this.containerType;
    }

    @Override
    public long valueTypeFlags() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isFlag(ActiveModel.ValueTypeFlag valueTypeFlag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
