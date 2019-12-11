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
package org.schorn.ella.model;

import java.util.List;
import org.schorn.ella.Provider;
import org.schorn.ella.context.AppContext;

/**
 *
 * @author bschorn
 */
public interface ModelProvider extends Provider {

    static public ModelProvider provider() {
        return Provider.Providers.MODEL.getInstance(ModelProvider.class);
    }

    public void addMember(ActiveModel.ModelObjectType objectType, ActiveModel.ModelMember member);

    public void addType(ActiveModel.ModelType type);

    public List<ActiveModel.ModelDataType> dataTypes(AppContext context);

    public List<ActiveModel.ModelFieldType> fieldTypes(AppContext Model);

    public List<ActiveModel.ModelValueType> valueTypes(AppContext Model);

    public List<ActiveModel.ModelObjectType> objectTypes(AppContext Model);

    public List<ActiveModel.ModelArrayType> arrayTypes(AppContext Model);

}
