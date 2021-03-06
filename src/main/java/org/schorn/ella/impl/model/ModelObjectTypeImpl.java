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

import java.util.ArrayList;
import java.util.List;
import org.schorn.ella.model.ActiveModel;

/**
 *
 * @author bschorn
 */
public class ModelObjectTypeImpl extends ModelResolvableImpl implements ActiveModel.ModelObjectType {

    private final String name;
    private final ActiveModel.DataCategory category;
    private final ActiveModel.DataPurpose purpose;
    private final List<ActiveModel.ModelMember> members;

    ModelObjectTypeImpl(String name, ActiveModel.DataCategory category, ActiveModel.DataPurpose purpose) {
        this.name = name;
        this.category = category;
        this.purpose = purpose;
        this.members = new ArrayList<>();
    }

    @Override
    public void addMember(ActiveModel.ModelMember member) {
        if (this.members.stream()
                .filter(m -> m.name().equals(member.name()))
                .findAny()
                .isPresent()) {
            // TODO
            // throw error
        } else {
            this.members.add(member);
        }

    }

    @Override
    public ActiveModel.DataCategory category() {
        return this.category;
    }

    @Override
    public ActiveModel.DataPurpose purpose() {
        return this.purpose;
    }

    @Override
    public List<ActiveModel.ModelMember> members() {
        return this.members;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public void resolve() throws ActiveModel.UnresolveModelDependency {

    }

    @Override
    public boolean isResolved() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long objectTypeFlags() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
